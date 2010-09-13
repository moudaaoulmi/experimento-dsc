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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.maze.eimp.app.SimpleIMWindow;
import org.maze.eimp.im.DressMessage;
import org.maze.eimp.im.Session;
import org.maze.eimp.util.ImageManager;
import org.maze.eimp.util.MessageArchiver;
import org.maze.eimp.views.MultiSessionPanel;

/**
 * switch view type action
 */
public class ToggleSessionViewTypeAction extends Action {
	private final static String TT_TEXT = Messages.getString("ToggleSessionViewTypeAction.M_switch_to_textview"); //$NON-NLS-1$
	private final static String TT_TABEL = Messages.getString("ToggleSessionViewTypeAction.M_switch_to_tableview"); //$NON-NLS-1$

	SimpleIMWindow w;
	public ToggleSessionViewTypeAction(SimpleIMWindow w) {
		this.w = w;
		setText(TT_TABEL);
		setToolTipText(TT_TABEL);
		setImageDescriptor(
			ImageManager.getImageDescriptor(ImageManager.IMG_VIEW_TABLE));

		//setChecked(true);
	}

	private void setTitle() {
		if (w.getSessionView().getPanelType()
			== MultiSessionPanel.TABLE_CONV_PANEL) {
			setText(TT_TEXT);
			setToolTipText(TT_TEXT);
		} else {
			setText(TT_TABEL);
			setToolTipText(TT_TABEL);
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		MultiSessionPanel v = w.getSessionView();
		if (v.getPanelType() == MultiSessionPanel.TABLE_CONV_PANEL)
			v.setPanelType(MultiSessionPanel.TEXT_CONV_PANEL);
		else
			v.setPanelType(MultiSessionPanel.TABLE_CONV_PANEL);
			
		
		appendData();
		
		setTitle();
	}

	private void appendData() {
		MessageArchiver ma = w.getMessageArchiver();
		MultiSessionPanel v = w.getSessionView();
		Set sl = ma.getSessionIdList();
		HashMap sm = w.getSessMap();
		for (Iterator i = sl.iterator(); i.hasNext();) {
			String ssid = (String) i.next();
			for (Iterator j = (ma.getMessageList(ssid)).iterator();
				j.hasNext();
				) {
				DressMessage m = (DressMessage) j.next();
				v.append(m, (Session) sm.get(ssid));
			}
		}
	}

}
