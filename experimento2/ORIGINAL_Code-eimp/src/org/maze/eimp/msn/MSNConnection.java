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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyGroup;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.IMListener;
import org.maze.eimp.im.Session;

import rath.msnm.MSNMessenger;
import rath.msnm.SwitchboardSession;
import rath.msnm.UserStatus;
import rath.msnm.entity.MsnFriend;
import rath.msnm.event.MsnAdapter;
import rath.msnm.msg.MimeMessage;

/**
 * This class is a adapter to MSNMessager from the msnm lib.
 * 
 * @author loya
 *  
 */
public class MSNConnection implements Connection {

	private ArrayList imListenerList;
	private MSNMessenger msn;
	Account account;
	private HashMap sessToMSN = new HashMap();
	private BuddyGroup buddyGroup;

	class MyEnvProc extends MsnAdapter {

		public void instantMessageReceived(
			SwitchboardSession ss,
			MsnFriend friend,
			MimeMessage mime) {
			Buddy b = Clone.buddyToIM(friend);
			fireInstantMessageReceived(
				b,
				Clone.MsgToIM(mime),
				getMSNSession(ss));
		}

		public void switchboardSessionStarted(SwitchboardSession ss) {
			fireSessionStarted(ss);
		}

		public void switchboardSessionEnded(SwitchboardSession ss) {
			fireSessionEnded(ss);
		}

		public void switchboardSessionAbandon(SwitchboardSession ss) {
			fireSessionEnded(ss);
		}

		public void loginComplete(MsnFriend own) {
			fireLoginCompleted();
		}

		public void addFailed(int errorCode) {
			fireAddFailed(errorCode);
		}

		public void userOnline(MsnFriend friend) {
		fireUserOnline(Clone.buddyToIM(friend));
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see rath.msnm.event.MsnListener#buddyListModified()
		 */
		public void buddyListModified() {
			rath.msnm.BuddyList bl = msn.getBuddyGroup().getForwardList();
			for (Iterator i = bl.iterator(); i.hasNext();) {
				MsnFriend b = (MsnFriend) i.next();
				if (!(b.getStatus().equals(b.getOldStatus()))) {
					fireBuddyStatusChanged(Clone.buddyToIM(b));
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see rath.msnm.event.MsnListener#listOnline(rath.msnm.entity.MsnFriend)
		 */
		public void listOnline(MsnFriend friend) {
					fireBuddyStatusChanged(Clone.buddyToIM(friend));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see rath.msnm.event.MsnListener#logoutNotify()
		 */
		public void logoutNotify() {
		//			try {
//				msn.setMyStatus(UserStatus.OFFLINE);
//			} catch (IOException e) {
//			}

			for (Iterator i = msn.getBuddyGroup().getForwardList().iterator();
				i.hasNext();
				) {
				MsnFriend f = (MsnFriend) i.next();
				if (!f.getStatus().equals(UserStatus.OFFLINE)) {
					f.setStatus(UserStatus.OFFLINE);
					msn.getBuddyGroup().getForwardList().add(f);
					fireBuddyStatusChanged(Clone.buddyToIM(f));
				}
			}
			
			fireLogout();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see rath.msnm.event.MsnListener#userOffline(java.lang.String)
		 */
		public void userOffline(String loginName) {
			Buddy b = new Buddy(loginName);
			b.setStatus(org.maze.eimp.im.UserStatus.OFFLINE);
			fireBuddyStatusChanged(b);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see rath.msnm.event.MsnListener#loginError(java.lang.String)
		 */
		public void loginError(String header) {
			fireLoginError(header);
		}

	}

	public MSNConnection() {
		msn = null;
		imListenerList = new ArrayList();
		buddyGroup = BuddyGroup.getInstance();
		if (msn == null) {
			msn = new MSNMessenger();
			msn.setInitialStatus(UserStatus.OFFLINE);
			msn.addMsnListener(new MyEnvProc());
		}
	}

	protected MSNSession getMSNSession(SwitchboardSession ss) {
		MSNSession sess = null;
		if (sessToMSN.containsKey(ss.getSessionId()))
			sess = (MSNSession) sessToMSN.get(ss.getSessionId());
		else {
			sess = new MSNSession(this, ss);
			sessToMSN.put(ss.getSessionId(), sess);
		}
		return sess;
	}

	private void fireUserOnline(Buddy buddy) {
	buddy.setAccount(getAccount());
		for (Iterator i = imListenerList.iterator(); i.hasNext();) {
			IMListener s = (IMListener) i.next();
			s.buddyOnline(buddy);
		}
	}

	private void fireBuddyStatusChanged(Buddy buddy) {
	buddy.setAccount(getAccount());
		for (Iterator i = imListenerList.iterator(); i.hasNext();) {
			IMListener s = (IMListener) i.next();
			s.buddyOnline(buddy);
		}
	}

	private void fireLoginCompleted() {
		for (Iterator i = imListenerList.iterator(); i.hasNext();) {
			IMListener s = (IMListener) i.next();
			s.loginComplete();
		}
	}

	private void fireLoginError(String header) {
		for (Iterator i = imListenerList.iterator(); i.hasNext();) {
			IMListener s = (IMListener) i.next();
			s.loginError(header);
		}
	}

	private void fireAddFailed(int errerno) {
		for (Iterator i = imListenerList.iterator(); i.hasNext();) {
			IMListener s = (IMListener) i.next();
			s.addFailed(errerno);
		}
	}

	private void fireSessionStarted(SwitchboardSession ss) {
		MSNSession sess = null;
		if (hasSession(ss.getSessionId())) {
			sess = (MSNSession) sessToMSN.get(ss.getSessionId());
		} else {
			sess = new MSNSession(this, ss);
			sessToMSN.put(ss.getSessionId(), sess);
		}
		for (Iterator i = imListenerList.iterator(); i.hasNext();) {
			IMListener s = (IMListener) i.next();
			s.sessionStarted(sess);
		}
	}

	private void fireSessionEnded(SwitchboardSession ss) {
		if (hasSession(ss.getSessionId())) {
			for (Iterator i = imListenerList.iterator(); i.hasNext();) {
				IMListener s = (IMListener) i.next();
				MSNSession mss = (MSNSession) sessToMSN.get(ss.getSessionId());
				mss.setClosed(true);
				s.sessionEnded(mss);
			}
			sessToMSN.remove(ss.getSessionId());
		} else {
			//TODO process exception
		}
	}

	private boolean hasSession(String sessionid) {
		if (sessToMSN.containsKey(sessionid))
			return true;
		else
			return false;
	}

	protected void fireInstantMessageReceived(
		org.maze.eimp.im.Buddy friend,
		org.maze.eimp.im.MimeMessage msg,
		MSNSession ss) {
		for (Iterator i = imListenerList.iterator(); i.hasNext();) {
			IMListener s = (IMListener) i.next();
			ss.setClosed(false);
			s.instantMessageReceived(ss, friend, msg);
		}
	}
	
	
	protected void fireLogout() {
			for (Iterator i = imListenerList.iterator(); i.hasNext();) {
				IMListener s = (IMListener) i.next();
				s.logoutNotify();
			}
		}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#login(java.lang.String, java.lang.String)
	 */
	public boolean login() {
		if (msn == null) {
			msn = new MSNMessenger(account.getLoginid(), account.getPassword());

			msn.addMsnListener(new MyEnvProc());
			//msn.login();
		}
		if (!msn.isLoggedIn()) {
			msn.setInitialStatus(UserStatus.ONLINE);
			msn.login(account.getLoginid(), account.getPassword());
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#close()
	 */
	public void close() {
		// TODO Auto-generated method stub
		//msn.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#getBuddyGroup()
	 */
	public BuddyGroup getBuddyGroup() {
		if (msn != null) {
			buddyGroup.getForwardList().clear();
			rath.msnm.BuddyList bl = msn.getBuddyGroup().getForwardList();
			BuddyList imbl = buddyGroup.getForwardList();
			for (Iterator i = bl.iterator(); i.hasNext();) {
				Buddy bud = Clone.buddyToIM((MsnFriend) i.next());
				bud.setAccount(account);
				imbl.add(bud);
			}
		}
		return buddyGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#findSession(org.maze.eimp.Buddy)
	 */
	public Session findSession(Buddy buddy) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#logout()
	 */
	public void logout() {
		if (msn != null) {
			try {
				msn.setMyStatus(UserStatus.OFFLINE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			msn.logout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#addIMListener(org.maze.eimp.IMListener)
	 */
	public void addIMListener(IMListener lsn) {
		imListenerList.add(lsn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#sendInstantMessage(org.maze.eimp.MimeMessage,
	 *      org.maze.eimp.Buddy)
	 */
	public void sendInstantMessage(
		org.maze.eimp.im.MimeMessage msg,
		Buddy buddy) {

		if (msn != null) {
			MimeMessage m = Clone.MsgToMsn(msg);
			m.setKind(MimeMessage.KIND_MESSAGE);
			try {
				msn.sendMessage(buddy.getLoginName(), m);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#removeIMListener(org.maze.eimp.IMListener)
	 */
	public void removeIMListener(IMListener lsn) {
		imListenerList.remove(lsn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#addBuddy(org.maze.eimp.Buddy)
	 */
	public void addBuddy(Buddy buddy) {

		try {
			msn.addFriend(buddy.getLoginName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.Connection#callBuddy(org.maze.eimp.Buddy)
	 */
	public void callBuddy(Buddy buddy) {
		try {
			msn.doCall(buddy.getLoginName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#removeBuddy(org.maze.eimp.im.Buddy)
	 */
	public void removeBuddy(Buddy buddy) {
		try {
			msn.removeFriend(buddy.getLoginName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#setAccount(org.maze.eimp.Account)
	 */
	public void setAccount(Account acc) {
		account = acc;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getAccount()
	 */
	public Account getAccount() {
		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#setStatus(java.lang.String)
	 */
	public void setStatus(String st) {
		if (msn.isLoggedIn()) {
			try {
				msn.setMyStatus(st);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getStatus()
	 */
	public String getStatus() {
		if (msn.isLoggedIn()) {
			String st = msn.getMyStatus();
			if (st != null)
				return st;
		}
		return org.maze.eimp.im.UserStatus.OFFLINE;
	}

}
