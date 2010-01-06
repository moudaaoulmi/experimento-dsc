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
import java.util.Iterator;

import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import org.eclipse.ui.*;
import org.eclipse.ui.part.*;

import org.maze.eimp.EimpConsts;
import org.maze.eimp.Environment;
import org.maze.eimp.eimpPlugin;
import org.maze.eimp.app.BuddyFilter;
import org.maze.eimp.app.BuddyInfoDialog;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.AccountList;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.IMAdapter;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;
import org.maze.eimp.im.UserStatus;
import org.maze.eimp.sounds.EventSoundList;

/**
 * BuddyListView
 */
public class BuddyListView extends ViewPart {
	private TreeViewer viewer;
	// private DrillDownAdapter drillDownAdapter;
	private Action ac_login;
	private Action ac_loginInvisibly;
	private Action ac_logout;
	private Action ac_sendmsg;
	private Action ac_addbuddy;
	private Action ac_removebuddy;
	private Action ac_refresh;
	private Action doubleClickAction;
	private ViewContentProvider contentProvider;
	private BLVAdapter notifer;
	private InfoNotifyWindow notifyWindow = null;
	private ArrayList accountActionList = new ArrayList();
	private ArrayList statusActionList = new ArrayList();
	private EventSoundList eventSoundList;
	private Action ac_hide_offline;
	private boolean hideOfflineBuddies = false;
	private BuddyFilter buddyFilter;

	class BLVAdapter extends IMAdapter {
		public void loginComplete() {
			// viewer.refresh();
			viewer.getControl().getDisplay().syncExec(new Runnable() {
				public void run() {
					refreshBuddyView();
					eventSoundList.play(EventSoundList.Login_Complete);
				}

			});
		}

		public void buddyOnline(Buddy b) {
			viewer.getControl().getDisplay().syncExec(new Runnable() {
				public void run() {
					refreshBuddyView();
					eventSoundList.play(EventSoundList.BUDDY_ONLINE);
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.maze.eimp.im.IMListener#instantMessageReceived(org.maze.eimp.
		 * im.Session, org.maze.eimp.im.Buddy, org.maze.eimp.im.MimeMessage)
		 */
		public void instantMessageReceived(final Session ss,
				final Buddy friend, final MimeMessage mime) {
			if (notifyWindow != null) {
				viewer.getControl().getDisplay().syncExec(new Runnable() {
					public void run() {
						if (getSite().getWorkbenchWindow().getShell()
								.getDisplay().getActiveShell() == null) {
							notifyWindow.setText(friend.getFriendlyName()
									+ "\n " + mime.getMessageString());
							notifyWindow.show();
							internalRunnable();
						}
					}

					private void internalRunnable() {
						getSite().getPage().showView(
								"org.maze.eimp.views.MultiSessionBoardView");
					}
				});
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.maze.eimp.im.IMListener#buddyStatusChange(org.maze.eimp.im.Buddy)
		 */
		public void buddyStatusChange(final Buddy buddy) {
			viewer.getControl().getDisplay().syncExec(new Runnable() {
				public void run() {
					eventSoundList.play(buddy);
					refreshBuddyView();
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.maze.eimp.im.IMListener#logoutNotify()
		 */
		public void logoutNotify() {
			viewer.getControl().getDisplay().syncExec(new Runnable() {
				public void run() {
					eventSoundList.play(EventSoundList.LOGOUT_NOTIFY);
					refreshBuddyView();
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.maze.eimp.im.IMListener#loginError(java.lang.String)
		 */
		public void loginError(String cause) {
			eventSoundList.play(EventSoundList.LOGIN_ERROR);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.maze.eimp.im.IMListener#sessionEnded(org.maze.eimp.im.Session)
		 */
		public void sessionEnded(Session ss) {
			eventSoundList.play(EventSoundList.SESSION_ENDED);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.maze.eimp.im.IMListener#sessionStarted(org.maze.eimp.im.Session)
		 */
		public void sessionStarted(Session ss) {
			eventSoundList.play(EventSoundList.SESSION_STARTED);
		}
	}

	class StatusAction extends Action {
		String status = UserStatus.OFFLINE;

		public void setStatus(String st) {
			status = st;
		}

		public String getStatus() {
			return status;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.IAction#run()
		 */
		public void run() {
			Object obj = getFirstSelectObject();
			if (obj != null && (obj instanceof Account)) {
				Account acc = (Account) obj;
				acc.getConnection().setStatus(status);
				updateUI();
				refreshBuddyView();
			}
		}

	}

	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			return Environment.getInstance().getAccountList().toArray();
		}

		public Object getParent(Object child) {
			if (child instanceof Buddy) {
				return ((Buddy) child).getAccount();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof Account) {
				if (hasChildren(parent)) {
					return ((Account) parent).getConnection().getBuddyGroup()
							.getForwardList().toArray();
				}
			}

			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof Account) {
				if (((Account) parent).getConnection().getBuddyGroup()
						.getForwardList().size() > 0)
					return true;
				else
					return false;
			}
			return false;
		}

	}

	class ViewLabelProvider extends LabelProvider {
		public String getText(Object obj) {
			if (obj instanceof Buddy) {
				String lbl = ((Buddy) obj).getFriendlyName() + "("
						+ ((Buddy) obj).getStatus() + ")";
				return lbl;
			}
			if (obj instanceof Account) {
				Account acc = (Account) obj;
				return acc.getName() + "(" + acc.getConnection().getStatus()
						+ ")";
			}
			return "Error Node";
		}

		public Image getImage(Object obj) {
			if (obj instanceof Buddy) {
				if (((Buddy) obj).isOnline())
					return eimpPlugin.getDefault().getImageRegistry().get(
							eimpPlugin.IMG_BUDDY_ONLINE);
				else
					return eimpPlugin.getDefault().getImageRegistry().get(
							eimpPlugin.IMG_BUDDY_OFFLINE);
			}

			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;

			if (obj instanceof Account) {
				Account acc = (Account) obj;
				String st = acc.getConnection().getStatus();
				if (st.equals(UserStatus.ONLINE))
					return eimpPlugin.getDefault().getImageRegistry().get(
							eimpPlugin.IMG_ACC_ONLINE);
				if (st.equals(UserStatus.OFFLINE))
					return eimpPlugin.getDefault().getImageRegistry().get(
							eimpPlugin.IMG_ACC_OFFLINE);
				if (st.equals(UserStatus.AWAY_FROM_COMPUTER))
					return eimpPlugin.getDefault().getImageRegistry().get(
							eimpPlugin.IMG_ACC_AWAY);
				else
					return eimpPlugin.getDefault().getImageRegistry().get(
							eimpPlugin.IMG_ACC_OFFLINE);
			}

			return PlatformUI.getWorkbench().getSharedImages().getImage(
					imageKey);
		}
	}

	/**
	 * sort by the status of buddy
	 */
	class BuddySorter extends ViewerSorter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.
		 * viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public int compare(Viewer viewer, Object e1, Object e2) {
			if (e1 instanceof Buddy && e2 instanceof Buddy) {
				int e = getStatusPri(((Buddy) e2).getStatus())
						- getStatusPri(((Buddy) e1).getStatus());
				if (e != 0)
					return e;
			}
			return super.compare(viewer, e1, e2);
		}

		private int getStatusPri(String st) {
			if (st.equals(UserStatus.ONLINE))
				return 8;
			if (st.equals(UserStatus.OFFLINE))
				return 1;
			if (st.equals(UserStatus.INVISIBLE))
				return 0;
			if (st.equals(UserStatus.BUSY))
				return 6;
			if (st.equals(UserStatus.IDLE))
				return 6;
			if (st.equals(UserStatus.BE_RIGHT_BACK))
				return 4;
			if (st.equals(UserStatus.AWAY_FROM_COMPUTER))
				return 4;
			if (st.equals(UserStatus.ON_THE_PHONE))
				return 4;
			if (st.equals(UserStatus.ON_THE_LUNCH))
				return 4;
			return 0;
		}

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
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
			e.getConnection().removeIMListener(notifer);
		}
		notifyWindow.close();
		super.dispose();
	}

	public void refreshBuddyView() {
		// viewer.setInput(ResourcesPlugin.getWorkspace());
		viewer.refresh();
		// viewer.expandToLevel(2);
		updateAction();
		updateUI();
	}

	/**
	 * The constructor.
	 */
	public BuddyListView() {
		notifer = new BLVAdapter();
		eventSoundList = EventSoundList.getInstance();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		// drillDownAdapter = new DrillDownAdapter(viewer);
		contentProvider = new ViewContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new BuddySorter());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				// update all action status
				updateAction();
				updateUI();

			}
		});

		viewer.setInput(ResourcesPlugin.getWorkspace());

		buddyFilter = new BuddyFilter(hideOfflineBuddies);
		viewer.addFilter(buddyFilter);

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		AccountList al = Environment.getInstance().getAccountList();
		for (Iterator i = al.iterator(); i.hasNext();) {
			Account e = (Account) i.next();
			e.getConnection().addIMListener(notifer);
		}

		// create notify windows
		notifyWindow = new InfoNotifyWindow(parent.getShell());
		int t = eimpPlugin.getDefault().getPreferenceStore().getInt(
				EimpConsts.STO_NOTIFER_DELAY_TIME);
		if (t == 0) {
			notifyWindow.setAllowAutoClose(false);
		} else
			notifyWindow.setAllowAutoClose(true);
		notifyWindow.setAutoHideDelaySecond(t);

		registerAccountChangeListener();

	}

	/**
	 * response to the account list change
	 * 
	 */
	private void registerAccountChangeListener() {
		Environment.getInstance().getPrefStore().addPropertyChangeListener(
				new IPropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent e) {
						if (e.getProperty().equals(EimpConsts.STO_ACCOUNTS)) {
							viewer.getControl().getDisplay().syncExec(
									new Runnable() {
										public void run() {
											refreshBuddyView();
										}
									});
						}

					}
				});

	}

	/**
	 * create the context menu
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				// refreshBuddyView();
				BuddyListView.this.fillContextMenu(manager);
			}
		});

		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	/**
	 * DOCUMENT ME!
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param manager
	 *            DOCUMENT ME!
	 */
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(ac_login);
		manager.add(ac_logout);
		manager.add(ac_addbuddy);
		manager.add(ac_removebuddy);
		manager.add(new Separator());
		manager.add(ac_sendmsg);
		manager.add(new Separator());
		manager.add(ac_refresh);
		manager.add(ac_hide_offline);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param manager
	 *            DOCUMENT ME!
	 */
	private void fillContextMenu(IMenuManager manager) {
		manager.add(ac_sendmsg);
		manager.add(new Separator());
		for (Iterator i = accountActionList.iterator(); i.hasNext();) {
			Action ac = (Action) i.next();
			manager.add(ac);
		}
		manager.add(ac_addbuddy);
		manager.add(ac_removebuddy);
		manager.add(new Separator());
		manager.add(ac_refresh);
		// drillDownAdapter.addNavigationActions(manager);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator("Additions"));

		// MenuManager ms=new MenuManager();
		// ms.setParent(manager);
		for (Iterator i = statusActionList.iterator(); i.hasNext();) {
			Action ac = (Action) i.next();
			manager.add(ac);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param manager
	 *            DOCUMENT ME!
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(ac_login);
		manager.add(ac_logout);
		manager.add(ac_addbuddy);
		manager.add(ac_removebuddy);
		manager.add(new Separator());
		manager.add(ac_sendmsg);
		manager.add(new Separator());
		manager.add(ac_refresh);
		// manager.add(new Separator());
		// drillDownAdapter.addNavigationActions(manager);
	}

	/**
	 * DOCUMENT ME!
	 */
	private void makeActions() {
		createAccountAction();
		createYahooAccountAction();
		// createStatusAction();

		// add buddy action
		ac_addbuddy = new Action() {
			public void run() {
				Object obj = getFirstSelectObject();
				if (obj != null && (obj instanceof Account)) {
					Account acc = (Account) obj;
					// acc.getConnection().login(acc.getLoginid(),acc.getPassword());
					InputDialog ind = new InputDialog(viewer.getControl()
							.getShell(), "Add a buddy", "Login ID:", "",
							new IInputValidator() {
								public String isValid(String newText) {
									return null;
								}
							});
					ind.open();
					String id = ind.getValue();
					Connection conn = acc.getConnection();
					conn.addBuddy(new Buddy(id));
				}
				refreshBuddyView();
			}
		};
		ac_addbuddy.setText("Add a buddy");
		ac_addbuddy.setToolTipText("Add a buddy");
		ac_addbuddy.setImageDescriptor(eimpPlugin.getDefault()
				.getImageRegistry().getDescriptor(eimpPlugin.IMG_ADDBUDDY));

		// remove buddy action
		ac_removebuddy = new Action() {
			public void run() {
				ISelection sel = viewer.getSelection();
				if (sel instanceof IStructuredSelection) {
					IStructuredSelection ssel = (IStructuredSelection) sel;
					Object obj = ssel.getFirstElement();
					if (obj != null && obj instanceof Buddy) {
						Buddy bud = (Buddy) obj;
						bud.getAccount().getConnection().removeBuddy(
								(Buddy) obj);
					}
				}
				refreshBuddyView();
			}
		};
		ac_removebuddy.setText("Delete a buddy");
		ac_removebuddy.setToolTipText("Delete a buddy");
		ac_removebuddy.setImageDescriptor(eimpPlugin.getDefault()
				.getImageRegistry().getDescriptor(eimpPlugin.IMG_REMOVEBUDDY));

		// send message action
		ac_sendmsg = new Action() {
			public void run() {
				ISelection sel = viewer.getSelection();
				if (sel instanceof IStructuredSelection) {
					IStructuredSelection ssel = (IStructuredSelection) sel;
					Object obj = ssel.getFirstElement();
					if (obj != null && obj instanceof Buddy) {
						Buddy bud = (Buddy) obj;
						bud.getAccount().getConnection().callBuddy(bud);
					}
				}
				// refreshBuddyView();
			}
		};
		ac_sendmsg.setText("Send instant message");
		ac_sendmsg.setToolTipText("Send instant message");
		ac_sendmsg.setImageDescriptor(eimpPlugin.getDefault()
				.getImageRegistry().getDescriptor(eimpPlugin.IMG_SENDMSG));

		// refresh action
		ac_refresh = new Action() {
			public void run() {
				refreshBuddyView();
			}
		};
		ac_refresh.setText("refresh");
		ac_refresh.setToolTipText("refresh view");
		ac_refresh.setImageDescriptor(eimpPlugin.getDefault()
				.getImageRegistry().getDescriptor(eimpPlugin.IMG_REFRESH));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				if (obj instanceof Buddy) {
					BuddyInfoDialog pd = new BuddyInfoDialog(viewer.getTree()
							.getShell());
					pd.setBuddy(((Buddy) obj));
					pd.open();
				}
				// showMessage("Properties: " + ((Buddy)obj).toString());
			}
		};

		// hide offline action
		ac_hide_offline = new Action() {
			public void run() {
				hideOfflineBuddies = !hideOfflineBuddies;
				buddyFilter.setIgnoreOfflineBuddies(hideOfflineBuddies);
				setChecked(hideOfflineBuddies);
				refreshBuddyView();
			}
		};
		ac_hide_offline.setText("hide offline buddies");
		ac_hide_offline.setToolTipText("hide offline buddies");
		ac_hide_offline.setChecked(hideOfflineBuddies);
		ac_hide_offline.setImageDescriptor(eimpPlugin.getDefault()
				.getImageRegistry().getDescriptor(eimpPlugin.IMG_HIDE_OFFLINE));

		createStatusAction();
		createYahooStatusAction();

	}

	/**
	 * Creates actions that are associated with Account. Such as Login and
	 * Logout.
	 */
	private void createAccountAction() {
		accountActionList = new ArrayList();

		// login action
		ac_login = new Action() {
			public void run() {
				Object obj = getFirstSelectObject();
				if (obj != null && (obj instanceof Account)) {
					Account acc = (Account) obj;
					acc.getConnection().login();
					refreshBuddyView();
				}
			}
		};
		ac_login.setText("Login");
		ac_login.setToolTipText("Login to MSN");
		ac_login.setImageDescriptor(eimpPlugin.getDefault().getImageRegistry()
				.getDescriptor(eimpPlugin.IMG_LOGIN));
		accountActionList.add(ac_login);

		// logout action
		ac_logout = new Action() {
			public void run() {
				Object obj = getFirstSelectObject();
				if (obj != null && (obj instanceof Account)) {
					Account acc = (Account) obj;
					acc.getConnection().logout();
					refreshBuddyView();
				}
			}
		};
		ac_logout.setText("Logout");
		ac_logout.setToolTipText("Logout");
		ac_logout.setImageDescriptor(eimpPlugin.getDefault().getImageRegistry()
				.getDescriptor(eimpPlugin.IMG_LOGOUT));
		accountActionList.add(ac_logout);

	}

	/**
	 * Creates Yahoo specific actions that are associated with Account. Such as
	 * Login as Invisible.
	 */
	private void createYahooAccountAction() {
		// login invisibly action
		ac_loginInvisibly = new Action() {
			public void run() {
				Object obj = getFirstSelectObject();
				if (obj != null && (obj instanceof Account)) {

					Account acc = (Account) obj;
					acc.getConnection().setStatus(UserStatus.INVISIBLE);
					acc.getConnection().login();
					refreshBuddyView();
				}
			}
		};
		ac_loginInvisibly.setText("Login As Invisible");
		ac_loginInvisibly.setToolTipText("Login As Invisible");
		ac_loginInvisibly.setImageDescriptor(eimpPlugin.getDefault()
				.getImageRegistry().getDescriptor(eimpPlugin.IMG_LOGIN));
		accountActionList.add(1, ac_loginInvisibly);

	}

	/**
	 * Create actions that are associated with a user's status. Such as Online,
	 * Away, Busy, etc...
	 */
	private void createStatusAction() {
		statusActionList = new ArrayList();

		// Online
		StatusAction ac_st_online = new StatusAction();
		ac_st_online.setText("Online");
		ac_st_online.setStatus(UserStatus.ONLINE);
		statusActionList.add(ac_st_online);

		StatusAction ac_st_offline = new StatusAction();
		ac_st_offline.setText("Offline");
		ac_st_offline.setStatus(UserStatus.OFFLINE);
		statusActionList.add(ac_st_offline);

		StatusAction ac_st_away = new StatusAction();
		ac_st_away.setText("Away");
		ac_st_away.setStatus(UserStatus.AWAY_FROM_COMPUTER);
		statusActionList.add(ac_st_away);

	}

	/**
	 * Create Yahoo specific actions that are associated with a user's status.
	 * Such as Invisible.
	 */
	private void createYahooStatusAction() {
		StatusAction ac_st_invisible = new StatusAction();
		ac_st_invisible.setText("Invisible");
		ac_st_invisible.setStatus(UserStatus.INVISIBLE);
		statusActionList.add(ac_st_invisible);
	}

	private void updateUI() {
		Object obj = getFirstSelectObject();
		if (obj != null) {
			if (obj instanceof Account) {
				Account e = (Account) obj;
				for (Iterator j = statusActionList.iterator(); j.hasNext();) {
					StatusAction s = (StatusAction) j.next();
					if (s.getStatus().equals(e.getConnection().getStatus())) {
						s.setChecked(true);
					} else {
						s.setChecked(false);
					}
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				// refreshBuddyView();
				doubleClickAction.run();
			}
		});
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param message
	 *            DOCUMENT ME!
	 */
	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"BuddyListView", message);
	}

	private Object getFirstSelectObject() {
		ISelection sel = viewer.getSelection();
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;
			return ssel.getFirstElement();
		}
		return null;
	}

	private void updateAction() {
		// 1 -- Account Node selected
		// 2 -- Buddy Node selected
		int state = 0;
		boolean isOnline = false;
		Object obj = getFirstSelectObject();
		if (obj != null) {
			if (obj instanceof Buddy) {
				state = 2;
			}
			if (obj instanceof Account) {
				state = 1;
			}
		}
		// createAccountAction();
		// createStatusAction();
		switch (state) {
		case 1:
			boolean isYahoo = false;
			ac_login.setEnabled(true);
			ac_logout.setEnabled(true);
			ac_addbuddy.setEnabled(true);
			ac_removebuddy.setEnabled(false);
			ac_sendmsg.setEnabled(false);
			if (((Account) obj).getType().equals(EimpConsts.IMP_YAHOO)) {
				// createYahooAccountAction();
				// createYahooStatusAction();
				// ac_loginInvisibly.
				ac_loginInvisibly.setEnabled(true);
				isYahoo = true;
			} else {
				ac_loginInvisibly.setEnabled(false);
			}
			// TODO: why?
			if (!(((Account) obj).getConnection().getStatus()
					.equals(UserStatus.OFFLINE)))
				isOnline = true;
			for (Iterator i = statusActionList.iterator(); i.hasNext();) {
				Action ac = (Action) i.next();
				if (ac instanceof StatusAction) {
					StatusAction acc = (StatusAction) ac;
					ac.setEnabled(true);
					if (!isYahoo
							&& acc.getStatus().equals(UserStatus.INVISIBLE)) {
						ac.setEnabled(false);
					} else
						ac.setEnabled(true);
				}
			}
			break;
		case 2:

			ac_login.setEnabled(false);
			ac_logout.setEnabled(false);
			ac_addbuddy.setEnabled(false);
			ac_removebuddy.setEnabled(true);
			ac_sendmsg.setEnabled(true);
			ac_loginInvisibly.setEnabled(false);
			for (Iterator i = statusActionList.iterator(); i.hasNext();) {
				Action ac = (Action) i.next();
				ac.setEnabled(false);
			}
			break;

		default:
			ac_login.setEnabled(false);
			ac_loginInvisibly.setEnabled(false);
			ac_logout.setEnabled(false);
			ac_addbuddy.setEnabled(false);
			ac_removebuddy.setEnabled(false);
			ac_sendmsg.setEnabled(false);
			for (Iterator i = statusActionList.iterator(); i.hasNext();) {
				Action ac = (Action) i.next();
				ac.setEnabled(false);
			}
			break;
		}
	}

}