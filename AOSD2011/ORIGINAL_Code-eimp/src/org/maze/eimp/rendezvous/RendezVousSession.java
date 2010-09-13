package org.maze.eimp.rendezvous;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.digester.Digester;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;
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
		try {
			String urlAsString = buddy.getAccount().getUrl();
			URL url = new URL(urlAsString);
			this.startSession(url);
		} catch (MalformedURLException e) {
		}
	}

	protected void startSession(URL buddyLocation) {
		try {
			chatConnection = new Socket(buddyLocation.getHost(), buddyLocation.getPort());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		streamParser.addRule(
			"stream",
			new SetNextRule("setXMLStream", XMLStream.class.getName()));
		streamParser.addSetProperties("stream");

		streamParser.setRuleNamespaceURI("jabber:client");
		streamParser.addObjectCreate("*/message", XMLMessage.class);
		streamParser.addSetProperties("*/message");
		streamParser.addSetNext(
			"*/message",
			"startMessage",
			XMLMessage.class.getName());
		streamParser.addCallMethod(
			"*/message/body",
			"setBody",
			1,
			new Class[] { String.class });
		streamParser.addCallParam("*/message/body", 0);
		return streamParser;
	}

	public void setXMLStream(XMLStream streamForThisSession) {
		streamForThisSession.setOutputWriter(output);
	}

	protected class MessageReaderThread extends Thread {

		public void run() {
			try {
				InputStreamReader input =
					new InputStreamReader(chatConnection.getInputStream());
				output = new OutputStreamWriter(chatConnection.getOutputStream());
				createConfiguredDigester().parse(input);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException se) {
			}
			System.out.println("Exiting ChatSession.run()");
		}
	}

}
