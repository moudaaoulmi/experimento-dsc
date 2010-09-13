package org.maze.eimp.rendezvous;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Container for the chat stream.
 * 
 * @author Ringo De Smet
 */
public class XMLStream {

	protected OutputStreamWriter output;

	public XMLStream() {
		System.out.println("XMLStream object created!");
	}

	public void setTo(String toIPAddress) {
		System.out.println("To: " + toIPAddress);
		try {
			String response =
				"<?xml version='1.0'?>"
					+ "<stream:stream from='"
					+ toIPAddress
					+ "' to='10.0.0.52' "
					+ "xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>\n";
			System.out.println("Response: " + response);
			output.write(response);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns the output.
	 */
	OutputStreamWriter getOutputWriter() {
		return output;
	}

	/**
	 * @param output The output to set.
	 */
	void setOutputWriter(OutputStreamWriter output) {
		this.output = output;
	}

	public void startMessage(XMLMessage message) {
		try {
			output.write(message.createReply().toXML());
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
