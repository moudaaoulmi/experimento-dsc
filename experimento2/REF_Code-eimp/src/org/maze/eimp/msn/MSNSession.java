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

package org.maze.eimp.msn;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.ServerOrientedSession;
import org.maze.eimp.util.MessageUtil;

import rath.msnm.SwitchboardSession;
import rath.msnm.entity.MsnFriend;

class MSNSession extends ServerOrientedSession {
	private final MSNConnection connection;
	private SwitchboardSession msnSession;
	private HashMap budMap;
	private BuddyList buddyList;
	private boolean closed = false;

	public MSNSession(MSNConnection connection, SwitchboardSession ss) {
		super(connection.getAccount());
		this.connection = connection;
		msnSession = ss;
		buddyList = new BuddyList("Session");
		budMap = new HashMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Session#getAccount()
	 */
	public Account getAccount() {
		return this.connection.account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Session#addBuddy(org.maze.eimp.Buddy)
	 */
	public void addBuddy(Buddy buddy) {
		msnSession.inviteFriend(buddy.getLoginName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Session#removeBuddy(org.maze.eimp.Buddy)
	 */
	public void removeBuddy(Buddy buddy) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Session#sendMessage(org.maze.eimp.MimeMessage)
	 */
	public void sendMessage(org.maze.eimp.im.MimeMessage msg) {
		// /TODO must check the status
		if (isClosed())
			return;
		internalSendMessage(msg);
	}

	private void internalSendMessage(org.maze.eimp.im.MimeMessage msg) {
			Buddy me = new Buddy(connection.getAccount().getLoginid());
			// me.setFriendlyName("")
			connection.fireInstantMessageReceived(me, msg, this);
			msnSession.sendMessage(Clone.MsgToMsn(msg));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Session#getBuddyList()
	 */
	public BuddyList getBuddyList() {
		// BuddyList bul=new BuddyList("Session");
		Collection l = msnSession.getMsnFriends();
		for (Iterator i = l.iterator(); i.hasNext();) {
			MsnFriend e = (MsnFriend) i.next();
			if (!budMap.containsKey(e.getLoginName())) {
				buddyList.add(Clone.buddyToIM(e));
			}
		}
		return buddyList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Session#getId()
	 */
	public String getId() {
		return msnSession.getSessionId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Session#getSessionShowName()
	 */
	public String getSessionShowName() {
		MsnFriend fr = msnSession.getMsnFriend();
		if (fr != null)
			return MessageUtil.fixBlank(fr.getFriendlyName());
		return msnSession.msn.getLoginName();
	}

	/**
	 * @return
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

}