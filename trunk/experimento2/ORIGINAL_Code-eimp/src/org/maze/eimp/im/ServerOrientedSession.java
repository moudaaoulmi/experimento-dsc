package org.maze.eimp.im;

/**
 * @author Ringo De Smet
 */
public abstract class ServerOrientedSession implements Session {

	Account account;
	
	public ServerOrientedSession(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return this.account;
	}
}
