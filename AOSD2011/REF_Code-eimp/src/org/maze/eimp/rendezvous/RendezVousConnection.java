package org.maze.eimp.rendezvous;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyGroup;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.IMListener;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;
import org.maze.eimp.im.UserStatus;

import com.strangeberry.rendezvous.Rendezvous;
import com.strangeberry.rendezvous.ServiceInfo;
import com.strangeberry.rendezvous.ServiceListener;

/**
 * @author Ringo De Smet
 */
public class RendezVousConnection implements Connection {

	private Account account = null;
	private BuddyGroup buddyGroup;
	private Rendezvous rendezVous;
	private ServiceInfo serviceInfo;
	private String status;
	private ArrayList imListeners;
	private HashMap uidToSessionMap = null;
	private ServiceListener rendezVousBuddyListener;

	public RendezVousConnection() {
		imListeners = new ArrayList();
		uidToSessionMap = new HashMap();
		buddyGroup = BuddyGroup.getInstance();
		this.setStatus(UserStatus.OFFLINE);
		internalRendezVousConnection();
	}

	private void internalRendezVousConnection() {
		rendezVous = new Rendezvous();
	}

	/**
	 * Login for RendezVous means to register the talk service. There is no
	 * central server, so other RendezVous listeners on the network will see us
	 * now.
	 */
	public boolean login() {
		Properties props = new Properties();
		props.put("port.p2pj", "5298");
		props.put("txtvers", "1");
		props.put("status", "avail");
		props.put("1st", "Freak");
		props.put("last", account.getName());
		props.put("version", "1");
		props.put("vc", "!");
		props.put("email", account.getLoginid());
		serviceInfo = new ServiceInfo("_presence._tcp.local.", "iChat",
				rendezVous.getInterface(), 5289, props);
		rendezVous.registerService(serviceInfo);
		rendezVousBuddyListener = new RendezVousBuddyListener(this);
		rendezVous.addServiceListener("_presence._tcp.local.",
				rendezVousBuddyListener);
		this.setStatus(UserStatus.ONLINE);
		return true;
	}

	private void fireLoginComplete() {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.loginComplete();
		}
	}

	public void setAccount(Account acc) {
		this.account = acc;
	}

	public Account getAccount() {
		return account;
	}

	/**
	 *  
	 */
	public void close() {
		// TODO Auto-generated method stub
	}

	/**
	 *  
	 */
	public void logout() {
		rendezVous.removeServiceListener(rendezVousBuddyListener);
		rendezVous.unregisterService(serviceInfo);
		this.setStatus(UserStatus.OFFLINE);
	}

	/**
	 *  
	 */
	public BuddyGroup getBuddyGroup() {
		return buddyGroup;
	}

	/**
	 *  
	 */
	public void addBuddy(Buddy buddy) {
		this.fireBuddyStatusChange(buddy);
	}

	/**
	 *  
	 */
	public void removeBuddy(Buddy buddy) {
		this.getBuddyGroup().getAllowList().remove(buddy);
	}

	/**
	 *  
	 */
	public void removeBuddy(String buddyLoginName) {
		this.getBuddyGroup().getAllowList().remove(buddyLoginName);
	}

	/**
	 *  
	 */
	public Session findSession(Buddy buddy) {
		return getSession(buddy);
	}

	private Session getSessionByUid(String uid) {
		if (uidToSessionMap.containsKey(uid)) {
			return (Session) uidToSessionMap.get(uid);
			// } else {
			// RendezVousSession rvs = new RendezVousSession(uid, this);
			// rvs.addBuddy(new Buddy(uid));
			// uidToSessionMap.put(uid, rvs);
			// return rvs;
		}
		return null;
	}

	/**
	 *  
	 */
	public void addIMListener(IMListener lsn) {
		imListeners.add(lsn);
	}

	/**
	 *  
	 */
	public void setStatus(String st) {
		this.status = st;
	}

	public String getStatus() {
		return status;
	}

	/**
	 *  
	 */
	public void callBuddy(Buddy buddy) {
		fireSessionStarted(this.getSession(buddy));
	}

	private RendezVousSession getSession(Buddy buddy) {
		if (uidToSessionMap.containsKey(buddy.getLoginName())) {
			return (RendezVousSession) uidToSessionMap
					.get(buddy.getLoginName());
		} else {
			RendezVousSession s = new RendezVousSession(buddy, this
					.getAccount());
			s.getBuddyList().add(buddy);
			return s;
		}
	}

	private void fireSessionStarted(Session s) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.sessionStarted(s);
		}
	}

	/**
	 *  
	 */
	public void removeIMListener(IMListener lsn) {
		imListeners.remove(lsn);
	}

	/**
	 *  
	 */

	public void sendInstantMessage(MimeMessage msg, Buddy buddy) {
		Session ss = findSession(buddy);
		Buddy b = new Buddy("System");
		b.setAccount(account);
		MimeMessage m = new MimeMessage("Send message not implemented!");
		// fire a local loop message
		fireInstantMessageReceived(ss, buddy, msg);
		// notice the send messge not implemented!
		fireInstantMessageReceived(ss, b, m);
	}

	private void fireInstantMessageReceived(Session ss, Buddy b, MimeMessage msg) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener lsn = (IMListener) i.next();
			lsn.instantMessageReceived(ss, b, msg);
		}
	}

	private void fireBuddyStatusChange(Buddy b) {
		b.setAccount(account);
		buddyGroup.getForwardList().add(b);
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.buddyStatusChange(b);
		}
	}

	private void fireBuddyOnline(Buddy b) {
		b.setAccount(account);
		buddyGroup.getAllowList().add(b);
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.buddyOnline(b);
		}
	}

}
