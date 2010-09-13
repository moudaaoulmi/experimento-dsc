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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.DressMessage;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;

/**
 */
public class MultiSessionPanel {

	private CTabFolder tabFolder;
	private Composite root;
	//private CTabItem sysTabItem;
	//private ConvPanel convPanel;
	///session to session provider
	private ArrayList convSessList;

	//session.id to sp
	private HashMap sessionList = new HashMap();

	private IGlobalTextActionManager globalTextActionAdapter;

	public static final int TEXT_CONV_PANEL = 0;
	public static final int TABLE_CONV_PANEL = 1;

	//panelType
	//  0 - ConvPanel
	//  1 - TableConvPanel
	private int panelType = TEXT_CONV_PANEL;

	class SessionProvider {
		private Session session;
		private CTabItem tab;
		private IMultiSessionBoardProvider cPanel;
		//private ConvPanel cPanel;
		private SendAdapter sendListener;
		private int panelType = TEXT_CONV_PANEL;

		class SendAdapter implements SendListener {
			public void sendMessage(String msg) {
				session.sendMessage(new MimeMessage(msg));
			}
		}

		private void setPanelType(int panelType) {
			if (this.panelType != panelType) {
				this.panelType = panelType;
				if (cPanel != null) {
					cPanel.removeSendListener(sendListener);
					cPanel.getControl().dispose();
				}
				cPanel = createPanel();
				tab.setControl(cPanel.getControl());
				cPanel.addSendListener(sendListener);
			}
		}

		public SessionProvider(Session ss) {
			session = ss;
			create();
		}
		private void create() {
			if (tabFolder != null) {
				cPanel = createPanel();
				tab = new CTabItem(tabFolder, SWT.NONE);
				tab.setText(session.getSessionShowName());
				tab.setControl(cPanel.getControl());
				sendListener = new SendAdapter();
				cPanel.addSendListener(sendListener);
			}
		}
		public SessionProvider(Session ss, int type) {
			session = ss;
			panelType = type;
			create();
		}

		private IMultiSessionBoardProvider createPanel() {
			IMultiSessionBoardProvider n = null;
			switch (panelType) {
				case TABLE_CONV_PANEL :
					n = new TableConvPanel();
					n.create(tabFolder, SWT.NONE);
					//n.registerTextGlobalAction(globalTextActionAdapter);
					break;
				case TEXT_CONV_PANEL :
					n = new ConvPanel();
					n.create(tabFolder, SWT.NONE);
					//n.registerTextGlobalAction(globalTextActionAdapter);
					break;
			}
			return n;
		}

		public void dispose() {
			cPanel.removeSendListener(sendListener);
		}
		/**
		 * @return
		 */
		public Session getSession() {
			return session;
		}

		/**
		 * @return
		 */
		public CTabItem getTab() {
			return tab;
		}

		public IMultiSessionBoardProvider getConvPanel() {
			return cPanel;
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setFocus()
	 */
	public boolean setFocus() {
		CTabItem t = tabFolder.getSelection();
		if (t == null)
			return true;
		for (Iterator i = sessionList.values().iterator(); i.hasNext();) {
			SessionProvider sp = (SessionProvider) i.next();
			if (t.equals(sp.getTab())) {
				return sp.cPanel.getControl().setFocus();
			}
		}
		return t.getControl().setFocus();
	}

	/**
	 * @return
	 */
	public int getPanelType() {
		return panelType;
	}

	/**
	 * @param panelType
	 */
	public void setPanelType(int type) {
		panelType = type;
		for (Iterator i = sessionList.values().iterator(); i.hasNext();) {
			SessionProvider sp = (SessionProvider) i.next();
			sp.setPanelType(panelType);
		}
	}

	/**
	 * @param parent
	 * @param style
	 */
	public MultiSessionPanel() {
		//super(parent, style);
		tabFolder = null;
		convSessList = new ArrayList();
		//sysTabItem = null;
		//convPanel = null;
	}

	public void create(Composite p, int style) {
		root = new Composite(p, style);
		root.setLayout(new FillLayout());
		if (tabFolder == null) {
			tabFolder = new CTabFolder(root, SWT.TOP);

			tabFolder.addCTabFolderListener(new CTabFolderAdapter() {
				public void itemClosed(CTabFolderEvent event) {
					//(CTabItem)event.item
					Session ss = null;
					for (Iterator i = sessionList.values().iterator();
						i.hasNext();
						) {
						SessionProvider sp = (SessionProvider) i.next();
						if (sp.getTab().equals((CTabItem) event.item)) {
							ss = sp.getSession();
							sp.getConvPanel().removeTextGlobalAction(
							globalTextActionAdapter);
						}
					}
					if (ss != null) {
						sessionList.remove(ss.getId());
						//notice session closed
						fireSessionClosed(ss);
					}
				}
			});

		}
	}

	private boolean findSession(Session ss) {
		if (sessionList.containsKey(ss.getId())) {
			return true;
		} else
			return false;
	}

	/**
	 * add a session to this panal
	 * @param ss
	 */
	public void addSession(final Session ss) {
		if (!findSession(ss) && ss != null) {
			root.getDisplay().syncExec(new Runnable() {
				public void run() {

					SessionProvider sp = new SessionProvider(ss, panelType);
					sessionList.put(ss.getId(), sp);
					tabFolder.setSelection(sp.getTab());
					//tabFolder.showItem(sp.getTab());
				}
			});
		};

	}

	public void closeSession(final Session ss) {
		root.getDisplay().syncExec(new Runnable() {
			public void run() {
				if (findSession(ss) && ss != null) {
					SessionProvider sp =
						(SessionProvider) sessionList.get(ss.getId());
					DressMessage m = new DressMessage("session closed.");
					Buddy b = new Buddy("System");
					m.setBuddy(b);
					((IMultiSessionBoardProvider) sp.getConvPanel()).append(m);
					//					sp.getConvPanel().append(
					//						"SYSTEM MESSAGE: session closed.\n");
					((IMultiSessionBoardProvider) sp.getConvPanel())
						.disableInput();
					sp.dispose();
					//sessionList.remove(ss.getId());
				}
			}

		});
	}

	public void append(final DressMessage msg, final Session ss) {
		root.getDisplay().asyncExec(new Runnable(){

			public void run() {
				if (sessionList.containsKey(ss.getId())) {
					SessionProvider sc = (SessionProvider) sessionList.get(ss.getId());
					sc.getConvPanel().append(msg);
					sc.getTab().setText(ss.getSessionShowName());
				}

			}});

	}

	/**
	 * Get the id of the current active session
	 * if no active session, return null.
	 * @return id of active session
	 */
	public String getCurrentSessionID() {
		CTabItem t = tabFolder.getSelection();
		if (t != null) {
			for (Iterator i = sessionList.values().iterator(); i.hasNext();) {
				SessionProvider sp = (SessionProvider) i.next();
				if (t.equals(sp.getTab())) {
					return sp.getSession().getId();
				}
			}
		}
		return null;
	}

	private void fireSessionClosed(Session ss) {
		for (Iterator i = convSessList.iterator(); i.hasNext();) {
			IConvSession lsn = (IConvSession) i.next();
			lsn.SessionClosed(ss);
		}
	}

	public void addConvSessionListener(IConvSession lsn) {
		convSessList.add(lsn);
	}
	public void removeConvSessionListener(IConvSession lsn) {
		convSessList.remove(lsn);
	}
	public Composite getControl() {
		return root;
	}


	/**
	 * @return
	 */
	public IGlobalTextActionManager getGlobalTextActionAdapter() {
		return globalTextActionAdapter;
	}

	/**
	 * @param globalTextActionAdapter
	 */
	public void setGlobalTextActionAdapter(IGlobalTextActionManager globalTextActionAdapter) {
		this.globalTextActionAdapter = globalTextActionAdapter;
	}

}
