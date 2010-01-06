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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyGroup;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.IMListener;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;
import org.maze.eimp.im.UserStatus;

/**
 * This class is an adapter for Smack API from JiveSortware.
 * 
 */
public class JabberConnection implements Connection {

	protected XMPPConnection jabber = null;
	private String hostServer = "jabber.org";
	private int portServer = 5222;
	private Account acc = null;
	private BuddyGroup buddyGroup = null;
	private String myStatus = UserStatus.OFFLINE;

	// session id to Session map
	private HashMap sessMap = null;

	// IMListener list
	private ArrayList imListeners = null;

	// to generate the thread id
	private static long threadID = 0;
	private static String threadIdPrefix = StringUtils.randomString(5);

	public JabberConnection() {
		buddyGroup = BuddyGroup.getInstance();
		imListeners = new ArrayList();
		sessMap = new HashMap();
	}

	private void createConnection() throws XMPPException {

		jabber = new XMPPConnection(StringUtils.parseServer(acc.getLoginid()),
				portServer);
		jabber.addConnectionListener(new ConnectionListener() {
			public void connectionClosed() {
				myStatus = UserStatus.OFFLINE;
			}

			public void connectionClosedOnError(Exception e) {
				myStatus = UserStatus.OFFLINE;
				e.printStackTrace();
			}
		});

		// listen to message(now not use Chat)
		jabber.addPacketListener(new PacketListener() {

			public void processPacket(Packet p) {
				Message m = (Message) p;
				fireMessageReceived(m);
			}
		}, new PacketTypeFilter(Message.class));

		// listen to Precent(now not use Chat)
		jabber.addPacketListener(new PacketListener() {
			public void processPacket(Packet p) {
				Presence m = (Presence) p;
				if (m.getType() == Presence.Type.SUBSCRIBE) {
					Presence r = new Presence(Presence.Type.SUBSCRIBED);
					r.setTo(m.getFrom());
					r.setFrom(acc.getLoginid());
					jabber.sendPacket(r);

					Presence s = new Presence(Presence.Type.SUBSCRIBE);
					s.setTo(m.getFrom());
					s.setFrom(acc.getLoginid());
					// m.set
					jabber.sendPacket(s);
				}
			}
		}, new PacketTypeFilter(Presence.class));
	}

	private void fireMessageReceived(Message m) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener lsn = (IMListener) i.next();
			Buddy b = Clone.toBuddy(m.getFrom());
			JabberSession s = getSessionByID(m.getThread(), b);
			lsn.instantMessageReceived(s, b, Clone.toImMessage(m));
		}
	}

	private void fireSessionStarted(JabberSession ss) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener lsn = (IMListener) i.next();
			lsn.sessionStarted(ss);
		}
	}

	private void fireStatusChanged(Buddy b) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener lsn = (IMListener) i.next();
			lsn.buddyStatusChange(b);
		}
	}

	private void fireLoginCompleted() {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener lsn = (IMListener) i.next();
			lsn.loginComplete();
		}
	}

	private JabberSession getSessionByID(String id, Buddy b) {
		if (sessMap.containsKey(id)) {
			return (JabberSession) sessMap.get(id);
		} else {
			JabberSession s = new JabberSession(id, this);
			s.addBuddy(b);
			sessMap.put(id, s);
			fireSessionStarted(s);
			return s;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#login()
	 */
	public boolean login() {
		Thread t = new Thread() {
			public void run() {
				if (jabber == null || !jabber.isConnected()) {
					createConnection();
				}
				jabber.login(StringUtils.parseName(acc.getLoginid()), acc
						.getPassword());
				myStatus = UserStatus.ONLINE;
				jabber.getRoster().setSubscriptionMode(
						Roster.SUBSCRIPTION_MANUAL);
				loadRosterFromServer();
				fireLoginCompleted();
			}
		};
		t.start();
		return true;
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#logout()
	 */
	public void logout() {
		jabber.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getBuddyGroup()
	 */
	public BuddyGroup getBuddyGroup() {
		refreshRoster();
		return buddyGroup;
	}

	private void loadRosterFromServer() {
		Roster r = jabber.getRoster();
		r.reload();
		r.addRosterListener(new RosterListener() {
			public void rosterModified() {
				refreshRoster();
			}

			public void presenceChanged(String user) {
				refreshRoster();
				fireStatusChanged(Clone.toBuddy(user));
			}
		});
	}

	private void refreshRoster() {
		if (jabber == null || !jabber.isAuthenticated())
			return;
		Roster r = jabber.getRoster();

		for (Iterator i = r.getEntries(); i.hasNext();) {
			RosterEntry e = (RosterEntry) i.next();
			Buddy b = new Buddy(e.getUser());
			b.setFriendlyName(e.getName());
			Presence p = r.getPresence(e.getUser());
			b.setStatus(Clone.toStatus(p));
			b.setAccount(acc);
			buddyGroup.getForwardList().add(b);
			// System.out.println(e.toString());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#addBuddy(org.maze.eimp.im.Buddy)
	 */
	public void addBuddy(Buddy buddy) {
		if (jabber == null || !jabber.isAuthenticated())
			return;

		Roster r = jabber.getRoster();
		internalAddBuddy(buddy, r);

	}

	private void internalAddBuddy(Buddy buddy, Roster r) {
		r.createEntry(buddy.getLoginName(), buddy.getFriendlyName(), null);
		refreshRoster();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#removeBuddy(org.maze.eimp.im.Buddy)
	 */
	public void removeBuddy(Buddy buddy) {
		if (jabber == null || !jabber.isAuthenticated())
			return;

		Roster r = jabber.getRoster();
		if (r.contains(buddy.getLoginName())) {
			r.removeEntry(r.getEntry(buddy.getLoginName()));
			buddyGroup.getForwardList().remove(buddy);
			fireStatusChanged(buddy);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#findSession(org.maze.eimp.im.Buddy)
	 */
	public Session findSession(Buddy buddy) {
		for (Iterator i = sessMap.values().iterator(); i.hasNext();) {
			JabberSession ss = (JabberSession) i.next();
			for (Iterator j = ss.getBuddyList().iterator(); j.hasNext();) {
				if (buddy.getLoginName().equalsIgnoreCase(
						((Buddy) j.next()).getLoginName())) {
					return ss;
				}
			}
		}
		return null;
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
		if (jabber != null && jabber.isAuthenticated()) {
			jabber.sendPacket(Clone.toJabberPresence(st));
			myStatus = st;
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
		JabberSession ss = getSessionByID(nextThreadID(), buddy);
		ss.addBuddy(buddy);
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
		Session ss = findSession(buddy);
		if (ss == null)
			return;
		ss.sendMessage(msg);

	}

	public void sendInstantMessage(Message m) {
		jabber.sendPacket(m);
		fireMessageReceived(m);
	}

	/**
	 * Returns the next unique id. Each id made up of a short alphanumeric
	 * prefix along with a unique numeric value. copied from Chat.
	 * 
	 * @return the next id.
	 */
	private static synchronized String nextThreadID() {
		return threadIdPrefix + Long.toString(threadID++);
	}

}
