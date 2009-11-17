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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.maze.eimp.app.SimpleIMWindow;
import org.maze.eimp.util.ImageManager;
import org.maze.eimp.util.MessageArchiver;
import org.maze.eimp.views.MultiSessionPanel;

/**
 * export to a txt file action
 */
public class ExportToTxtAction extends Action {

	SimpleIMWindow w;
	SimpleDateFormat dateFormater=null;
	public ExportToTxtAction(SimpleIMWindow w) {
		this.w = w;
		setText(Messages.getString("ExportToTxtAction.M_ExportChatLog")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExportToTxtAction.D_ExportChatLog")); //$NON-NLS-1$
		setImageDescriptor(
			ImageManager.getImageDescriptor(ImageManager.IMG_SAVE_AS));

	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		MultiSessionPanel sv = w.getSessionView();
		String ss = sv.getCurrentSessionID();
		MessageArchiver ma = w.getMessageArchiver();
		if (ss != null) {
			FileDialog fd = new FileDialog(w.getShell(), SWT.SAVE);
			fd.setFileName(getTimeString() + ".txt"); //$NON-NLS-1$
			String path = fd.open();
			if (path != null) {
				try {
					ma.saveTXT(ss, path);
				} catch (IOException e) {
					w.showMessage(Messages.getString("ExportToTxtAction.E_FailedWriteFile")); //$NON-NLS-1$
				}
			}
		}
	}
	
	private String getTimeString(){
		if(dateFormater==null)dateFormater=new SimpleDateFormat("yyyyMMdd-HHmmss"); //$NON-NLS-1$
		return dateFormater.format(new Date());
	}


}
