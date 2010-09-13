package org.maze.eimp.rendezvous;

import org.maze.eimp.EimpConsts;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;

import com.strangeberry.rendezvous.Rendezvous;
import com.strangeberry.rendezvous.ServiceInfo;
import com.strangeberry.rendezvous.ServiceListener;

/**
 * @author Ringo De Smet
 */
class RendezVousBuddyListener implements ServiceListener {

	RendezVousConnection connection;

	public RendezVousBuddyListener(RendezVousConnection connection) {
		this.connection = connection;
	}

	/**
	 *  
	 */
	public void addService(Rendezvous rendezvous, String type, String name) {
		ServiceInfo info = rendezvous.getServiceInfo(type, name);
		String firstName = info.getPropertyString("1st");
		String lastName = info.getPropertyString("last");
		String email = info.getPropertyString("email");
		Account rendezVousAccount = new Account(email);
		rendezVousAccount.setName(firstName + " " + lastName);
		rendezVousAccount.setType(EimpConsts.IMP_RENDEZVOUS);
		rendezVousAccount.setUrl("xmpp://" + info.getAddress() + ":" + info.getPort());
		if (!this.connection.getAccount().getLoginid().equals(email)) {
			Buddy newBuddy = new Buddy(firstName + " " + lastName);
			newBuddy.setAccount(rendezVousAccount);
			this.connection.addBuddy(newBuddy);
		}
	}

	/**
	 *  
	 */
	public void removeService(
		Rendezvous rendezvous,
		String type,
		String name) {
		ServiceInfo info = rendezvous.getServiceInfo(type, name);
		String userName = info.getPropertyString("user");
		if (!this.connection.getAccount().getLoginid().equals(userName)) {
			this.connection.removeBuddy(userName);
		}
	}

	/**
	 *  
	 */
	public void resolveService(
		Rendezvous rendezvous,
		String type,
		String name,
		ServiceInfo info) {
	}

}
