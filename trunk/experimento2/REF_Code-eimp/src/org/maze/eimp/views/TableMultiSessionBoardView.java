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

package org.maze.eimp.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.maze.eimp.Environment;
import org.maze.eimp.eimpPlugin;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.AccountList;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.DressMessage;
import org.maze.eimp.im.IMAdapter;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;
import org.maze.eimp.util.MessageArchiver;

/**
 * @deprecated this view is deprecated
 */

public class TableMultiSessionBoardView extends ViewPart implements
		IConvSession {
	// private Action ac_login;
	// private Action ac_logout;
	private MultiSessionPanel te;
	private SCIMListener imListener;
	private HashMap sessionList = new HashMap();
	private Action ac_save_file;
	private Action ac_view_text;
	private Action ac_view_table;
	private int panelType = MultiSessionPanel.TEXT_CONV_PANEL;
	private MessageArchiver messageArchiver;
	private IMemento memento;

	class SCIMListener extends IMAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.maze.eimp.IMListener#instantMessageReceived(org.maze.eimp.Session
		 * , org.maze.eimp.Buddy, org.maze.eimp.MimeMessage)
		 */
		public void instantMessageReceived(Session ss, Buddy friend,
				MimeMessage msg) {
			if (!sessionList.containsKey(ss.getId()))
				sessionList.put(ss.getId(), ss);
			DressMessage m = new DressMessage(msg.getMessageString());
			m.setBuddy(friend);
			te.addSession(ss);
			te.append(m, ss);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.maze.eimp.IMListener#sessionStarted(org.maze.eimp.Session)
		 */
		public void sessionStarted(Session ss) {
			te.addSession(ss);
			if (!sessionList.containsKey(ss.getId()))
				sessionList.put(ss.getId(), ss);
			StringBuffer buf = new StringBuffer();

			buf.append(DateFormat.getDateTimeInstance(DateFormat.LONG,
					DateFormat.LONG).format(new Date()));
			// buf.append()
			// te.append(DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG).format(new
			// Date()), ss);
			// te.append("\n-\n", ss);

			BuddyList bl = ss.getBuddyList();
			if (bl.size() > 0) {
				for (Iterator i = bl.iterator(); i.hasNext();) {
					Buddy e = (Buddy) i.next();
					buf.append(e.getFriendlyName());
					buf.append("(");
					buf.append(e.getLoginName());
					buf.append(")");
					buf.append(" ");
				}
			}
			buf.append("\n---------\n");
			DressMessage m = new DressMessage(buf.toString());
			Buddy b = new Buddy("System");
			m.setBuddy(b);
			te.append(m, ss);

		}

		public void sessionEnded(Session ss) {
			te.closeSession(ss);
		}

	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(final Composite parent) {
		te = new MultiSessionPanel();

		// te.setPanelType(MultiSessionPanel.TABLE_CONV_PANEL);
		te.create(parent, SWT.NONE);
		te.addConvSessionListener(this);
		initConnection();

		makeActions();
		makeViewActions();
		// hookContextMenu();
		// hookDoubleClickAction();
		contributeToActionBars();
		if (memento != null)
			restoreState(this.memento);
		te.setPanelType(panelType);
		updateUI();
	}

	void restoreState(IMemento memento) {
		Integer p = memento.getInteger("eimp.msbv.panaltype");
		if (p != null)
			panelType = p.intValue();
	}

	private void initConnection() {
		AccountList al = Environment.getInstance().getAccountList();
		// Connection conn = ConnectionPool.getConnection();
		messageArchiver = new MessageArchiver();
		if (imListener == null)
			imListener = new SCIMListener();
		for (Iterator i = al.iterator(); i.hasNext();) {
			Account e = (Account) i.next();
			e.getConnection().addIMListener(imListener);
			e.getConnection().addIMListener(messageArchiver);
		}
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(ac_save_file);
		manager.add(new Separator());
		manager.add(ac_view_table);
		manager.add(ac_view_text);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(ac_save_file);
		// drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		ac_save_file = new Action() {
			public void run() {
				String ss = te.getCurrentSessionID();
				if (ss != null) {
					FileDialog fd = new FileDialog(te.getControl().getShell());
					fd.setFileName(ss + ".txt");
					String path = fd.open();
					if (path != null) {
						internalRun(ss, path);
					}
				}
			}
		};
		ac_save_file.setText("Save to file");
		ac_save_file.setToolTipText("Save to file");
		ac_save_file.setImageDescriptor(eimpPlugin.getDefault()
				.getImageRegistry().getDescriptor(eimpPlugin.IMG_SAVE_AS));
	}

	private void internalRun(String ss, String path) {
		messageArchiver.saveTXT(ss, path);
	}

	private void makeViewActions() {
		ac_view_table = new Action() {
			public void run() {
				ChangeView(MultiSessionPanel.TABLE_CONV_PANEL);
			}
		};
		ac_view_table.setText("Table View");
		ac_view_table.setToolTipText("Table View");

		ac_view_text = new Action() {
			public void run() {
				ChangeView(MultiSessionPanel.TEXT_CONV_PANEL);
			}
		};
		ac_view_text.setText("Text View");
		ac_view_text.setToolTipText("Text View");
	}

	private void ChangeView(int type) {
		if (type == te.getPanelType())
			return;
		panelType = type;
		te.setPanelType(type);
		for (Iterator i = sessionList.values().iterator(); i.hasNext();) {
			Session ss = (Session) i.next();
			ArrayList l = messageArchiver.getMessageList(ss.getId());
			if (l != null) {
				for (Iterator ii = l.iterator(); ii.hasNext();) {
					DressMessage dm = (DressMessage) ii.next();
					te.append(dm, ss);
				}
			}
		}
		updateUI();
	}

	private void updateUI() {
		switch (panelType) {
		case MultiSessionPanel.TABLE_CONV_PANEL:
			ac_view_table.setChecked(true);
			ac_view_text.setChecked(false);
			break;
		case MultiSessionPanel.TEXT_CONV_PANEL:
			ac_view_table.setChecked(false);
			ac_view_text.setChecked(true);
			break;
		}

	}

	private void showMessage(String message) {
		MessageDialog.openInformation(te.getControl().getShell(),
				"BuddyListView", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		te.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		AccountList al = Environment.getInstance().getAccountList();
		for (Iterator i = al.iterator(); i.hasNext();) {
			Account e = (Account) i.next();
			e.getConnection().removeIMListener(imListener);
		}
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.maze.eimp.views.IConvSession#SessionClosed(org.maze.eimp.im.Session)
	 */
	public void SessionClosed(Session sess) {
		messageArchiver.remove(sess.getId());
		sessionList.remove(sess.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewPart#init(org.eclipse.ui.IViewSite,
	 * org.eclipse.ui.IMemento)
	 */
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		this.memento = memento;
		super.init(site, memento);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewPart#saveState(org.eclipse.ui.IMemento)
	 */
	public void saveState(IMemento memento) {
		// panelType=memento.getInteger("eimp.msbv.panaltype").intValue();
		memento.putInteger("eimp.msbv.panaltype", panelType);
		super.saveState(memento);
	}

}