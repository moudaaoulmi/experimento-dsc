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
 * main listener
 *  @author hliu
 *
 */
public interface IMListener {
	
	/**
	 * a instant message received
	 * @param ss the session with this message
	 * @param friend a buddy
	 * @param msg the message
	 */
	public void instantMessageReceived(Session ss,Buddy friend,MimeMessage msg);
	
	/**
	 * login completed
	 *
	 */
	public void loginComplete();
	
	/**
	 * login failed
	 * 
	 * @param cause the cause of fail
	 */
	public void loginError(String cause);
	
	/**
	 * a session started by a buddy
	 * @param ss
	 */
	public void sessionStarted( Session ss );
	
	/**
	 * a session ended
	 * @param ss
	 */
	public void sessionEnded(Session ss );
	
	/**
	 * add buddy failed
	 * @param errorno
	 */
	public void addFailed(int errorno);
	
	/**
	 * a buddy online
	 * @param buddy
	 */
	public void buddyOnline(Buddy buddy);
	
	/**
	 * a buddy has changed his status
	 * @param buddy
	 */
	public void buddyStatusChange(Buddy buddy);
	
	/**
	 * notice im logout 
	 *
	 */
	public void logoutNotify();

}
