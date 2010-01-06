package org.maze.eimp.rendezvous;

import org.maze.eimp.im.MimeMessage;

/**
 * @author Ringo De Smet
 */
public class XMLMessage {

	private String body;
	private String to;
	private String type;

	public XMLMessage() {
	}

	public XMLMessage(MimeMessage eimpMessage) {
		this.setBody(eimpMessage.getMessageString());
	}

	public void setBody(String bodyText) {
		this.body = bodyText;
	}

	public String getBody() {
		return this.body;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return "Message[to=" + to + ",type=" + type + ",body=" + body + "]";
	}

	public XMLMessage createReply() {
		XMLMessage reply = new XMLMessage();
		reply.setBody(this.body);
		reply.to = "localhost";
		reply.type = this.type;
		return reply;
	}

	public String toXML() {
		return "<message to='"+ to + "' type='" + type + "'><body>" + body + "</body></message>";
	}
}
