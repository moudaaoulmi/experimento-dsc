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

package org.maze.eimp.aim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.maze.eimp.Environment;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyGroup;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.IMListener;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;
import org.maze.eimp.im.UserStatus;
import org.maze.eimp.util.LocalStore;
import org.maze.eimp.yahoo.Util;

import com.wilko.jaim.BuddyUpdateTocResponse;
import com.wilko.jaim.ConfigTocResponse;
import com.wilko.jaim.ConnectionLostTocResponse;
import com.wilko.jaim.ErrorTocResponse;
import com.wilko.jaim.Group;
import com.wilko.jaim.IMTocResponse;
import com.wilko.jaim.JaimConnection;
import com.wilko.jaim.JaimEvent;
import com.wilko.jaim.JaimEventListener;
import com.wilko.jaim.LoginCompleteTocResponse;
import com.wilko.jaim.TocResponse;

/**
 * A adapter to JAIMbot lib
 * 
 */
public class AIMConnection implements Connection {

	private Account acc = null;
	private BuddyGroup buddyGroup;
	private JaimConnection aim = null;
	private MyAIMListener aimListener;
	private ArrayList imListeners;
	private HashMap uidToSessionMap = null;
	private String myStatus;
	private LocalStore localStore = null;

	public AIMConnection() {
		buddyGroup = BuddyGroup.getInstance();
		localStore = new LocalStore("eimp_aim");
		imListeners = new ArrayList();
		// createAIMConnection();

		uidToSessionMap = new HashMap();
		myStatus = UserStatus.OFFLINE;

	}

	private void createAIMConnection() {
		aimListener = new MyAIMListener();
		aim = new JaimConnection("toc.oscar.aol.com", 9898);
		// aim.setDebug(true);
		aim.addEventListener(aimListener);
		internalCreateAIMConnection();
		BuddyList bl = buddyGroup.getForwardList();
		if (aim != null) {
			for (Iterator i = bl.iterator(); i.hasNext();) {
				Buddy e = (Buddy) i.next();
				// e.setAccount(acc);
				internalCreateAIMConnection2(e);
			}
		}
	}

	private void internalCreateAIMConnection2(Buddy e) {
		aim.watchBuddy(e.getLoginName());
	}

	private void internalCreateAIMConnection() {
		aim.watchBuddy("asdfasdfasd23432");
	}

	class MyAIMListener implements JaimEventListener {

		// /* (non-Javadoc)
		// * @see
		// com.levelonelabs.aim.AIMListener#handleMessage(com.levelonelabs.aim.AIMBuddy,
		// java.lang.String)
		// */
		// public void handleMessage(AIMBuddy ab, String msg) {
		// fireMessageReceived(ab.getName(), msg);
		// }
		//
		// /* (non-Javadoc)
		// * @see
		// com.levelonelabs.aim.AIMListener#handleWarning(com.levelonelabs.aim.AIMBuddy,
		// int)
		// */
		// public void handleWarning(AIMBuddy arg0, int arg1) {
		//			
		//
		// }
		//
		// /* (non-Javadoc)
		// * @see
		// com.levelonelabs.aim.AIMListener#handleBuddySignOn(com.levelonelabs.aim.AIMBuddy,
		// java.lang.String)
		// */
		// public void handleBuddySignOn(AIMBuddy ab, String info) {
		// BuddyList bl = buddyGroup.getForwardList();
		// Buddy b = Clone.buddyToIm(ab);
		// b.setStatus(UserStatus.ONLINE);
		// bl.add(b);
		// fireBuddyStatusChange();
		// }
		//
		// /* (non-Javadoc)
		// * @see
		// com.levelonelabs.aim.AIMListener#handleBuddySignOff(com.levelonelabs.aim.AIMBuddy,
		// java.lang.String)
		// */
		// public void handleBuddySignOff(AIMBuddy ab, String arg1) {
		// BuddyList bl = buddyGroup.getForwardList();
		// Buddy b = Clone.buddyToIm(ab);
		// b.setStatus(UserStatus.OFFLINE);
		// bl.add(b);
		// fireBuddyStatusChange();
		// }

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.wilko.jaim.JaimEventListener#receiveEvent(com.wilko.jaim.JaimEvent
		 * )
		 */
		public void receiveEvent(JaimEvent ev) {
			TocResponse r = ev.getTocResponse();
			if (r instanceof IMTocResponse) {
				IMTocResponse ir = (IMTocResponse) r;
				fireMessageReceived(new Buddy(ir.getFrom()), new MimeMessage(
						Util.trimHtmlTag(ir.getMsg())), getSessionByUid(ir
						.getFrom()));
			}
			if (r instanceof LoginCompleteTocResponse) {
				fireLoginComplete();
			}
			if (r instanceof ErrorTocResponse) {
				ErrorTocResponse eer = (ErrorTocResponse) r;
				System.out.println(eer.getErrorDescription());
			}

			if (r instanceof BuddyUpdateTocResponse) {
				BuddyUpdateTocResponse br = (BuddyUpdateTocResponse) r;
				Buddy b = new Buddy(br.getBuddy());
				if (br.isAway())
					b.setStatus(UserStatus.AWAY_FROM_COMPUTER);
				else if (br.isOnline())
					b.setStatus(UserStatus.ONLINE);
				else
					b.setStatus(UserStatus.OFFLINE);
				// if(br.)b.setStatus(UserStatus.AWAY_FROM_COMPUTER);
				fireBuddyStatusChange(b);
			}
			if (r instanceof ConfigTocResponse) {
				updateServerSideBuddies();
				if (buddyGroup.getForwardList().size() > 0) {
					fireBuddyStatusChange((Buddy) buddyGroup.getForwardList()
							.get(0));
				}
			}

			if (r instanceof ConnectionLostTocResponse) {
				System.out.println("Connection lost!");

			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#login()
	 */
	public boolean login() {
		if (aim == null) {
			createAIMConnection();
		}
		if (aim != null) {
			internalLogin();
		}

		return true;
	}

	private void internalLogin() {
		aim.connect();
		aim.logIn(acc.getLoginid(), acc.getPassword(), 60000);
		aim.addBlock("");
		aim.setInfo(acc.getLoginid() + " ,powered by eimp.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#setAccount(org.maze.eimp.im.Account)
	 */
	public void setAccount(Account acc) {
		this.acc = acc;
		localStore.setLoginName(acc.getLoginid());
		localStore.loadInformation();
		localStore.loadBuddies(buddyGroup);
		BuddyList bl = buddyGroup.getForwardList();
		if (aim != null) {
			for (Iterator i = bl.iterator(); i.hasNext();) {
				Buddy e = (Buddy) i.next();
				e.setAccount(acc);
			}
		}
		// aim = new AIMClient(acc.getLoginid(), acc.getPassword(), "eimp",
		// true);
		// aim.addEventListener(aimListener);
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#logout()
	 */
	public void logout() {
		if (aim != null) {
			if (aim.isLoginComplete()) {
				aim.logOut();
				internalLogout();
				myStatus = UserStatus.OFFLINE;
				for (Iterator i = buddyGroup.getForwardList().iterator(); i
						.hasNext();) {
					Buddy b = (Buddy) i.next();
					b.setStatus(UserStatus.OFFLINE);
				}
				fireLogout();
			}
		}
	}

	private void internalLogout() {
		aim.disconnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getBuddyGroup()
	 */
	public BuddyGroup getBuddyGroup() {
		return buddyGroup;
	}

	private void updateServerSideBuddies() {
		Collection gl = aim.getGroups();
		for (Iterator gi = gl.iterator(); gi.hasNext();) {
			Group ge = (Group) gi.next();
			Collection bl = ge.getBuddies();
			for (Iterator bi = bl.iterator(); bi.hasNext();) {
				com.wilko.jaim.Buddy be = (com.wilko.jaim.Buddy) bi.next();
				Buddy b = new Buddy(be.getName());
				b.setAccount(acc);
				buddyGroup.getForwardList().add(b);
				internalUpdateServerSideBuddies(b);
			}

		}

	}

	private void internalUpdateServerSideBuddies(Buddy b) {
		aim.watchBuddy(b.getLoginName());
	}

	private void fireBuddyStatusChange(Buddy b) {
		b.setAccount(acc);
		b = buddyGroup.getForwardList().add(b);
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.buddyStatusChange(b);
		}
	}

	private void fireLoginComplete() {
		myStatus = UserStatus.ONLINE;
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();

			im.loginComplete();
		}
	}

	private void fireLogout() {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.logoutNotify();
		}
	}

	protected void fireMessageReceived(Buddy b, MimeMessage msg, Session ss) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();

			im.instantMessageReceived(ss, b, msg);
		}
	}

	private Session getSessionByUid(String uid) {
		if (uidToSessionMap.containsKey(uid)) {
			return (Session) uidToSessionMap.get(uid);
		} else {
			AIMSession ys = new AIMSession(this, uid);
			ys.addBuddy(new Buddy(uid));
			uidToSessionMap.put(uid, ys);
			return ys;
		}
	}

	private void fireSessionStarted(Session ss) {

		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();

			im.sessionStarted(ss);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#addBuddy(org.maze.eimp.im.Buddy)
	 */
	public void addBuddy(Buddy buddy) {
		// com.wilko.jaim.Buddy b = new
		// com.wilko.jaim.Buddy(buddy.getLoginName());
		if (!(aim != null && aim.isLoginComplete()))
			return;

		internalCreateAIMConnection2(buddy);
		buddy.setStatus(UserStatus.OFFLINE);
		buddy.setAccount(acc);
		buddyGroup.getForwardList().add(buddy);
		internalAddBuddy();
		localStore.storeBuddies(buddyGroup);

	}

	private void internalAddBuddy() {
		aim.saveConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#removeBuddy(org.maze.eimp.im.Buddy)
	 */
	public void removeBuddy(Buddy buddy) {
		buddyGroup.getForwardList().remove(buddy);
		localStore.storeBuddies(buddyGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#findSession(org.maze.eimp.im.Buddy)
	 */
	public Session findSession(Buddy buddy) {
		return getSessionByUid(buddy.getFriendlyName());

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
		if (aim == null)
			return;
		if (st.equals(myStatus))
			return;
		internalSetStatus(st);
		myStatus = st;

	}

	private void internalSetStatus(String st) {
		if (st.equals(UserStatus.ONLINE)) {
			login();
		}
		if (st.equals(UserStatus.OFFLINE)) {
			aim.logOut();
		}
		if (st.equals(UserStatus.AWAY_FROM_COMPUTER)) {
			aim.setAway("Away");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getStatus()
	 */
	public String getStatus() {
		return myStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#callBuddy(org.maze.eimp.im.Buddy)
	 */
	public void callBuddy(Buddy buddy) {
		if (aim != null && aim.isLoginComplete()) {
			Session ss = getSessionByUid(buddy.getLoginName());
			fireSessionStarted(ss);
		}
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
		if (aim != null && aim.isLoginComplete()) {
			internalSendInstantMessage(msg, buddy);
		}
	}

	private void internalSendInstantMessage(MimeMessage msg, Buddy buddy) {
		aim.sendIM(buddy.getLoginName(), msg.getMessageString());
	}

	private void log(String s) {
		Environment.getInstance().getLogger().log(1010, s);
	}

	private void log(Exception e) {
		log(e.getMessage());
	}

}
