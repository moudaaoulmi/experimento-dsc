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
 * A adapter class for IMListener
 */
public class IMAdapter implements IMListener {

	/* (non-Javadoc)
	 * @see org.maze.eimp.IMListener#instantMessageReceived(org.maze.eimp.Session, org.maze.eimp.Buddy, org.maze.eimp.MimeMessage)
	 */
	public void instantMessageReceived(
		Session ss,
		Buddy friend,
		MimeMessage mime) {

	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.IMListener#loginComplete()
	 */
	public void loginComplete() {
		
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.IMListener#sessionStarted(org.maze.eimp.Session)
	 */
	public void sessionStarted(Session ss) {
		
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.IMListener#sessionEnded(org.maze.eimp.Session)
	 */
	public void sessionEnded(Session ss) {
		
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.IMListener#addFailed(int)
	 */
	public void addFailed(int errorno) {
		
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.IMListener#buddyOnline(org.maze.eimp.Buddy)
	 */
	public void buddyOnline(Buddy buddy) {
	
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.IMListener#buddyStatusChange(org.maze.eimp.im.Buddy)
	 */
	public void buddyStatusChange(Buddy buddy) {

	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.im.IMListener#loginError(java.lang.String)
	 */
	public void loginError(String cause) {
		// TODO Auto-generated method stub
		
	}

    /* (non-Javadoc)
     * @see org.maze.eimp.im.IMListener#logoutNotify()
     */
    public void logoutNotify() {
        // TODO Auto-generated method stub
        
    }

}
