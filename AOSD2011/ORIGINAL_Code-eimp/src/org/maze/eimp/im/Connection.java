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

package org.maze.eimp.im;

/**
 * A abstract layer for all protocal
 * 
 * @author hliu
 *
 * $Id: Connection.java,v 1.4 2003/06/20 07:15:07 loya Exp $
 */
public interface Connection {

	/**
	 * login to a service of a provider
	 *  
	 * @return true if success,else false
	 */
	public boolean login();

	/**
	 * pass the account to connection
	 * @param acc
	 */
	public void setAccount(Account acc);

	/**
	 * get the account of this connection
	 * @return
	 */
	public Account getAccount();

	/**
	 * close the connection. If you do this, you shouldn't 
	 * reuse this connection.
	 *
	 */
	public void close();

	/**
	 * logout a service. If you not login, it will do nothing. 
	 *
	 */
	public void logout();

	/**
	 * get a buddygroup whitch is from the server, and not store
	 * in the local.
	 *  
	 * @return BuddyGroup,always not null.
	 */
	public BuddyGroup getBuddyGroup();

	/**
	 * add a buddy to the buddygroup
	 * 
	 * @param buddy to be added
	 */
	public void addBuddy(Buddy buddy);

	/**
	 * remove a buddy from buddygroup
	 * @param buddy
	 */
	public void removeBuddy(Buddy buddy);

	public Session findSession(Buddy buddy);

	/**
	 * register a IMListener to run some operations.
	 * 
	 * @param lsn a instance of IMListener. It must be not null!
	 */
	public void addIMListener(IMListener lsn);

	/**
	 * set the status of this connection
	 * @param st defined in UserStatus
	 */
	public void setStatus(String st);

	/**
	 * get the status
	 * @return
	 */
	public String getStatus();

	/**
	 * make a new session with buddy
	 * @param buddy
	 */
	public void callBuddy(Buddy buddy);

	/**
	 * remove a IMListener from this connection.
	 * 
	 * @param lsn a instance of IMListener. It must be not null!
	 */
	public void removeIMListener(IMListener lsn);

	/**
	 * Send a instantant message to a buddy without a session.
	 * 
	 * @param msg the message to sent
	 * @param buddy the buddy who will receive the message
	 */
	public void sendInstantMessage(MimeMessage msg, Buddy buddy);

}
