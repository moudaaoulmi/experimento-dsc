package org.maze.eimp.rendezvous;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.digester.Digester;
import org.maze.eimp.im.*;
import org.xml.sax.SAXException;

/**
 * @author Ringo De Smet
 */
public class RendezVousSession implements Session {

	String sessID;
	Buddy buddy;
	BuddyList buddies;
	Socket chatConnection;
	OutputStreamWriter output;
	Account rendezVousAccount;

	public RendezVousSession(Buddy buddy, Account myAccount) {
		this.rendezVousAccount = myAccount;
		sessID = buddy.getLoginName();
		buddies = new BuddyList("RendezVousSession");
		internalRendezVousSession(buddy);
	}

	private void internalRendezVousSession(Buddy buddy) {
		String urlAsString = buddy.getAccount().getUrl();
		URL url = new URL(urlAsString);
		this.startSession(url);
	}

	protected void startSession(URL buddyLocation) {
		chatConnection = new Socket(buddyLocation.getHost(), buddyLocation
				.getPort());
		// TODO Initialize an XML Chat connection starting from the host and
		// port in the URL.
	}

	public Account getAccount() {
		return this.rendezVousAccount;
	}

	/**
	 *  
	 */
	public String getId() {
		return sessID;
	}

	/**
	 *  
	 */
	public String getSessionShowName() {
		return sessID;
	}

	/**
	 *  
	 */
	public void addBuddy(Buddy buddy) {
		this.buddies.add(buddy);
	}

	/**
	 *  
	 */
	public void removeBuddy(Buddy buddy) {
		this.buddies.remove(buddy);
	}

	/**
	 *  
	 */
	public void sendMessage(MimeMessage msg) {
		// Send over direct link.
	}

	/**
	 *  
	 */
	public BuddyList getBuddyList() {
		return buddies;
	}

	protected Digester createConfiguredDigester() {
		Digester streamParser = new Digester();
		streamParser.setNamespaceAware(true);
		streamParser.push(this);

		streamParser.setRuleNamespaceURI("http://etherx.jabber.org/streams");
		streamParser.addObjectCreate("stream", XMLStream.class);
		streamParser.addRule("stream", new SetNextRule("setXMLStream",
				XMLStream.class.getName()));
		streamParser.addSetProperties("stream");

		streamParser.setRuleNamespaceURI("jabber:client");
		streamParser.addObjectCreate("*/message", XMLMessage.class);
		streamParser.addSetProperties("*/message");
		streamParser.addSetNext("*/message", "startMessage", XMLMessage.class
				.getName());
		streamParser.addCallMethod("*/message/body", "setBody", 1,
				new Class[] { String.class });
		streamParser.addCallParam("*/message/body", 0);
		return streamParser;
	}

	public void setXMLStream(XMLStream streamForThisSession) {
		streamForThisSession.setOutputWriter(output);
	}

	protected class MessageReaderThread extends Thread {

		public void run() {
			internalRun();
			System.out.println("Exiting ChatSession.run()");
		}

		private void internalRun() {
			InputStreamReader input = new InputStreamReader(chatConnection
					.getInputStream());
			output = new OutputStreamWriter(chatConnection.getOutputStream());
			createConfiguredDigester().parse(input);
		}
	}

}
