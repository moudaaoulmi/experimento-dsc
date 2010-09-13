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

package org.maze.eimp.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.maze.eimp.app.SimpleIMWindow;
import org.maze.eimp.im.Account;
import org.maze.eimp.util.ImageManager;

/**
 * logout action
 */
public class AccountLogoutAction extends Action {
	
	SimpleIMWindow w;
	public AccountLogoutAction(SimpleIMWindow w){
		this.w=w;
		setText(Messages.getString("AccountLogoutAction.M_Logout")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AccountLogoutAction.D_Logout")); //$NON-NLS-1$
		setImageDescriptor(ImageManager.getImageDescriptor(ImageManager.IMG_LOGOUT));
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		ISelection ss = w.getBuddyListViewSelections();
		if(ss!=null && !ss.isEmpty()){
			if (ss instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) ss;
			Object o=ssel.getFirstElement();
			if(o instanceof Account){
				((Account)o).getConnection().logout();
				w.getAllwaysOnlineHelper().logout((Account)o);
			}
		}
		}
	}

}
