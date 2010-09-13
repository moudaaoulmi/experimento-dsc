/*******************************************************************************
 * Copyright (c) 2003, Loya Liu
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice, 
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice, 
 *     this list of conditions and the following disclaimer in the documentation 
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the MAZE.ORG nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 *******************************************************************************/

package org.maze.eimp.xmpp;

import org.jivesoftware.smack.packet.Message;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.ServerOrientedSession;

/**
 * A session for Jabber.
 * This class require invoke addBuddy() before other invoke!
 */
public class JabberSession extends ServerOrientedSession {
	
	JabberConnection connection;
	String id;
	BuddyList buddyList;
	
	public JabberSession(String sessid, JabberConnection connection){
		super(connection.getAccount());
		this.id=sessid;
		this.connection = connection;
		buddyList=new BuddyList("chat");
	}
	
	private String removeSlash(String ins){
		if(ins==null)return null;
		int i=ins.indexOf('/');
		if(i!=-1){
			return ins.substring(i);
		}else return ins;
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.Session#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.Session#addBuddy(org.maze.eimp.im.Buddy)
	 */
	public void addBuddy(Buddy buddy) {
		buddyList.add(buddy);
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.Session#removeBuddy(org.maze.eimp.im.Buddy)
	 */
	public void removeBuddy(Buddy buddy) {
		buddyList.remove(buddy);
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.Session#sendMessage(org.maze.eimp.im.MimeMessage)
	 */
	public void sendMessage(MimeMessage msg) {
		Message m = new Message();
		m.setType(Message.Type.CHAT);
		m.setFrom(this.getAccount().getLoginid());
		m.setBody(msg.getMessageString());
		m.setThread(id);
		m.setTo(buddyList.get(0).getLoginName());
		this.connection.sendInstantMessage(m);
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.Session#getBuddyList()
	 */
	public BuddyList getBuddyList() {
		return buddyList;
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.Session#getSessionShowName()
	 */
	public String getSessionShowName() {
		if(buddyList.size()>0)return buddyList.get(0).getFriendlyName();
		return id;
	}

}
