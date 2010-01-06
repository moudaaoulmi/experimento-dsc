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

package org.maze.eimp.yahoo;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import org.maze.eimp.Environment;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyGroup;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.IMListener;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;
import org.maze.eimp.im.UserStatus;
import ymsg.network.StatusConstants;
import ymsg.network.YahooUser;
import ymsg.network.event.SessionChatEvent;
import ymsg.network.event.SessionConferenceEvent;
import ymsg.network.event.SessionErrorEvent;
import ymsg.network.event.SessionEvent;
import ymsg.network.event.SessionExceptionEvent;
import ymsg.network.event.SessionFileTransferEvent;
import ymsg.network.event.SessionFriendEvent;
import ymsg.network.event.SessionListener;
import ymsg.network.event.SessionNewMailEvent;
import ymsg.network.event.SessionNotifyEvent;

/**
 * @author loya
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class YahooConnection implements Connection {

	private Account acc = null;
	private BuddyGroup buddyGroup;
	private ymsg.network.Session yahoo = null;
	private YahooListener yahooListener;
	private ArrayList imListeners;
	private HashMap uidToSessionMap = null;

	private Thread connectionThread = null;

	class YahooListener implements SessionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#conferenceInviteDeclinedReceived
		 * (ymsg.network.event.SessionConferenceEvent)
		 */
		public void conferenceInviteDeclinedReceived(SessionConferenceEvent arg0) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#fileTransferReceived(ymsg.network
		 * .event.SessionFileTransferEvent)
		 */
		public void fileTransferReceived(SessionFileTransferEvent sv) {
			//

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#logoffReceived(ymsg.network.event
		 * .SessionEvent)
		 */
		public void logoffReceived(SessionEvent sv) {
			System.out.println(sv.getFrom() + sv.getMessage());
			refeshBuddyGroup();

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#listReceived(ymsg.network.event
		 * .SessionEvent)
		 */
		public void listReceived(SessionEvent sv) {
			refeshBuddyGroup();

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#messageReceived(ymsg.network.event
		 * .SessionEvent)
		 */
		public void messageReceived(SessionEvent sv) {
			fireMessageReceived(new Buddy(sv.getFrom()), new MimeMessage(Util
					.trimHtmlTag(sv.getMessage())), getSessionByUid(sv
					.getFrom()));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#offlineMessageReceived(ymsg.network
		 * .event.SessionEvent)
		 */
		public void offlineMessageReceived(SessionEvent sv) {
			reqRefreshYahooUsers();

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#errorPacketReceived(ymsg.network
		 * .event.SessionErrorEvent)
		 */
		public void errorPacketReceived(SessionErrorEvent sv) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#inputExceptionThrown(ymsg.network
		 * .event.SessionExceptionEvent)
		 */
		public void inputExceptionThrown(SessionExceptionEvent sv) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#newMailReceived(ymsg.network.event
		 * .SessionNewMailEvent)
		 */
		public void newMailReceived(SessionNewMailEvent sv) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#notifyReceived(ymsg.network.event
		 * .SessionNotifyEvent)
		 */
		public void notifyReceived(SessionNotifyEvent sv) {
			// fireMessageReceived(sv.getFrom(), sv.getMessage());

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#contactRequestReceived(ymsg.network
		 * .event.SessionEvent)
		 */
		public void contactRequestReceived(SessionEvent sv) {
			// yahoo.

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#contactRejectionReceived(ymsg.
		 * network.event.SessionEvent)
		 */
		public void contactRejectionReceived(SessionEvent sv) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#conferenceInviteReceived(ymsg.
		 * network.event.SessionConferenceEvent)
		 */
		public void conferenceInviteReceived(SessionConferenceEvent sv) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#conferenceLogonReceived(ymsg.network
		 * .event.SessionConferenceEvent)
		 */
		public void conferenceLogonReceived(SessionConferenceEvent sv) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#conferenceLogoffReceived(ymsg.
		 * network.event.SessionConferenceEvent)
		 */
		public void conferenceLogoffReceived(SessionConferenceEvent sv) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#conferenceMessageReceived(ymsg
		 * .network.event.SessionConferenceEvent)
		 */
		public void conferenceMessageReceived(SessionConferenceEvent sv) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#friendsUpdateReceived(ymsg.network
		 * .event.SessionFriendEvent)
		 */
		public void friendsUpdateReceived(SessionFriendEvent sv) {
			// reqRefreshYahooUsers();
			refeshBuddyGroup();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#friendAddedReceived(ymsg.network
		 * .event.SessionFriendEvent)
		 */
		public void friendAddedReceived(SessionFriendEvent sv) {
			// reqRefreshYahooUsers();
			refeshBuddyGroup();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#friendRemovedReceived(ymsg.network
		 * .event.SessionFriendEvent)
		 */
		public void friendRemovedReceived(SessionFriendEvent arg0) {
			// reqRefreshYahooUsers();
			refeshBuddyGroup();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#buzzReceived(ymsg.network.event
		 * .SessionEvent)
		 */
		public void buzzReceived(SessionEvent arg0) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#chatLogonReceived(ymsg.network
		 * .event.SessionChatEvent)
		 */
		public void chatLogonReceived(SessionChatEvent arg0) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#chatLogoffReceived(ymsg.network
		 * .event.SessionChatEvent)
		 */
		public void chatLogoffReceived(SessionChatEvent arg0) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#chatMessageReceived(ymsg.network
		 * .event.SessionChatEvent)
		 */
		public void chatMessageReceived(SessionChatEvent arg0) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#connectionClosed(ymsg.network.
		 * event.SessionEvent)
		 */
		public void connectionClosed(SessionEvent arg0) {
			fireLogout();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#chatUserUpdateReceived(ymsg.network
		 * .event.SessionChatEvent)
		 */
		public void chatUserUpdateReceived(SessionChatEvent arg0) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * ymsg.network.event.SessionListener#chatConnectionClosed(ymsg.network
		 * .event.SessionEvent)
		 */
		public void chatConnectionClosed(SessionEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	public YahooConnection() {
		buddyGroup = BuddyGroup.getInstance();
		imListeners = new ArrayList();
		yahoo = new ymsg.network.Session();
		yahooListener = new YahooListener();
		yahoo.addSessionListener(yahooListener);
		uidToSessionMap = new HashMap();
	}

	private Session getSessionByUid(String uid) {
		if (uidToSessionMap.containsKey(uid)) {
			return (Session) uidToSessionMap.get(uid);
		} else {
			YahooSession ys = new YahooSession(this, uid);
			ys.addBuddy(new Buddy(uid));
			uidToSessionMap.put(uid, ys);
			return ys;
		}
	}

	private void reqRefreshYahooUsers() {
		if (yahoo.getSessionStatus() == StatusConstants.MESSAGING) {
			// try {
			// yahoo.refreshFriends();
			// } catch (IOException e) {
			// log(e.getMessage());
			// }

		}
	}

	protected void fireMessageReceived(Buddy b, MimeMessage msg, Session ss) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();

			im.instantMessageReceived(ss, b, msg);
		}
	}

	private void refeshBuddyGroup() {
		Hashtable h = yahoo.getUsers();
		buddyGroup.clear();
		Enumeration e = h.elements();
		while (e.hasMoreElements()) {
			YahooUser u = (YahooUser) e.nextElement();
			if (u.isFriend()) {
				Buddy b = Clone.buddyToIm(u);
				b.setAccount(acc);
				buddyGroup.getForwardList().add(b);
			}
		}
		fireBuddyStatusUpdate();
	}

	private void fireSessionStarted(Session ss) {

		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();

			im.sessionStarted(ss);
		}
	}

	private void fireLogout() {

		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();

			im.logoutNotify();
		}
	}

	private void fireLoginComleted() {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.loginComplete();
		}
	}

	private void fireLoginError(String cause) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.loginError(cause);
		}
	}

	private void fireBuddyStatusUpdate() {
		Buddy b = null;
		if (buddyGroup.getForwardList().size() > 1) {
			b = buddyGroup.getForwardList().get(0);
		} else {
			b = new Buddy("System");
		}

		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.buddyStatusChange(b);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#login()
	 */
	public boolean login() {

		// yahoo.setStatus(StatusConstants.STATUS_INVISIBLE);
		// need to create the session inside the login method
		// in case of relogin the session should be reinitialized
		yahoo = new ymsg.network.Session();
		yahoo.addSessionListener(yahooListener);
		this.connectionThread = new Thread(new Runnable() {
			public void run() {
				boolean succ = true;
				succ = internalRunnable(succ);
				if (succ) {
					fireLoginComleted();
				}
			}
		});
		this.connectionThread.start();

		return true;
	}

	private boolean internalRunnable(boolean succ) {
		// yahoo.setStatus(StatusConstants.STATUS_INVISIBLE);
		yahoo.login(acc.getLoginid(), acc.getPassword());
		return succ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#setAccount(org.maze.eimp.im.Account)
	 */
	public void setAccount(Account acc) {
		this.acc = acc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getAccount()
	 */
	public Account getAccount() {
		return acc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#close()
	 */
	public void close() {
		//
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#logout()
	 */
	public void logout() {
		yahoo.logout();
		this.connectionThread.interrupt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getBuddyGroup()
	 */
	public BuddyGroup getBuddyGroup() {
		return buddyGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#addBuddy(org.maze.eimp.im.Buddy)
	 */
	public void addBuddy(Buddy buddy) {
		yahoo.addFriend(buddy.getLoginName(), "MyFriends");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#removeBuddy(org.maze.eimp.im.Buddy)
	 */
	public void removeBuddy(Buddy buddy) {
		yahoo.removeFriend(buddy.getLoginName(), "MyFriends");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#findSession(org.maze.eimp.im.Buddy)
	 */
	public Session findSession(Buddy buddy) {
		return getSessionByUid(buddy.getLoginName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.maze.eimp.im.Connection#addIMListener(org.maze.eimp.im.IMListener)
	 */
	public void addIMListener(IMListener lsn) {
		imListeners.add(lsn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#setStatus(java.lang.String)
	 */
	public void setStatus(String st) {
		yahoo.setStatus(Clone.statusToYahoo(st));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getStatus()
	 */
	public String getStatus() {
		if (yahoo.getSessionStatus() == StatusConstants.MESSAGING) {
			return Clone.statusToIM(yahoo.getStatus());
		} else
			return UserStatus.OFFLINE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#callBuddy(org.maze.eimp.im.Buddy)
	 */
	public void callBuddy(Buddy buddy) {
		fireSessionStarted(getSessionByUid(buddy.getLoginName()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.maze.eimp.im.Connection#removeIMListener(org.maze.eimp.im.IMListener)
	 */
	public void removeIMListener(IMListener lsn) {
		imListeners.remove(lsn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.maze.eimp.im.Connection#sendInstantMessage(org.maze.eimp.im.MimeMessage
	 * , org.maze.eimp.im.Buddy)
	 */
	public void sendInstantMessage(MimeMessage msg, Buddy buddy) {
		yahoo.sendMessage(buddy.getLoginName(), msg.getMessageString());
	}

	private void log(String s) {
		// code: 1020 is for Yahoo
		Environment.getInstance().getLogger().log(1020, s);
	}

}
