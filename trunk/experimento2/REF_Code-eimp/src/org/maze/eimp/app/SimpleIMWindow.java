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

package org.maze.eimp.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.maze.eimp.EimpConsts;
import org.maze.eimp.Environment;
import org.maze.eimp.app.action.AboutDialogAction;
import org.maze.eimp.app.action.AccountLoginAction;
import org.maze.eimp.app.action.AccountLogoutAction;
import org.maze.eimp.app.action.AddBuddyAction;
import org.maze.eimp.app.action.BuddyInfoDialogAction;
import org.maze.eimp.app.action.ExitAppAction;
import org.maze.eimp.app.action.ExportToTxtAction;
import org.maze.eimp.app.action.ToggleHideOfflineBuddiesAction;
import org.maze.eimp.app.action.PerferenceAction;
import org.maze.eimp.app.action.RefreshBuddyListAction;
import org.maze.eimp.app.action.RemoveBuddyAction;
import org.maze.eimp.app.action.SendMessageAction;
import org.maze.eimp.app.action.ShellControlAction;
import org.maze.eimp.app.action.StatusAction;
import org.maze.eimp.app.action.ToggleBuddyListViewAction;
import org.maze.eimp.app.action.ToggleSessionViewAction;
import org.maze.eimp.app.action.ToggleSessionViewTypeAction;
import org.maze.eimp.im.*;
import org.maze.eimp.sounds.EventSoundList;
import org.maze.eimp.util.AllwaysOnlineHelper;
import org.maze.eimp.util.ImageManager;
import org.maze.eimp.util.MessageArchiver;
import org.maze.eimp.views.IConvSession;
import org.maze.eimp.views.IGlobalTextActionManager;
import org.maze.eimp.views.InfoNotifyWindow;
import org.maze.eimp.views.MultiSessionPanel;

/**
 * A simple standalone window for IM
 *  
 */
public class SimpleIMWindow extends ApplicationWindow implements IConvSession {

    public static final String STO_ST_SW_WIDTH = "eimp_STO_ST_SW_WIDTH"; //$NON-NLS-1$
    public static final String STO_ST_SW_HEIGHT = "eimp_STO_ST_SW_HEIGHT"; //$NON-NLS-1$
    public static final String STO_ST_SW_SHOW_BUDDYLISTVIEW = "eimp_STO_ST_SW_SHOW_BUDDYLISTVIEW"; //$NON-NLS-1$
    public static final String STO_ST_SW_SHOW_SESSIONVIEW = "eimp_STO_ST_SW_SHOW_SESSIONVIEW"; //$NON-NLS-1$
    public static final String STO_TOGGLE_HIDE_OFFLINE_BUDDIES = "eimp_STO_TOGGLE_HIDE_OFFLINE_BUDDIES"; //$NON-NLS-1$    

    String windowTitle = null;
    SashForm sashForm = null;
    TreeViewer buddyListViewer = null;
    MultiSessionPanel sessionView;

    //Enable AllwaysOnline
    AllwaysOnlineHelper allwaysOnlineHelper;

    HashMap sessMap = null;

    AccountLoginAction ac_login;
    AccountLogoutAction ac_logout;
    RefreshBuddyListAction ac_refresh;
    ToggleBuddyListViewAction ac_view_buddy;
    ToggleSessionViewAction ac_view_session;
    ToggleSessionViewTypeAction ac_view_sess_type;
    ToggleHideOfflineBuddiesAction ac_view_offline;
    SendMessageAction ac_sendmsg;
    AddBuddyAction ac_add_buddy;
    RemoveBuddyAction ac_rm_buddy;
    BuddyInfoDialogAction ac_buddy_info;
    ExportToTxtAction ac_save_session_txt;
    PerferenceAction ac_file_perf;
    AboutDialogAction ac_file_about;
    //hold StatusAction
    ArrayList ac_st_list;
    ExitAppAction ac_file_exit;

    //A small window to notice some information
    InfoNotifyWindow infoNotifyWindow;

    MessageArchiver messageArchiver;
    ShellControlAction shellControlAction;
    EventSoundList eventSoundList;

    //
    boolean st_view_buddy_visible = true;
    boolean st_view_session_visible = true;
    private boolean st_toggle_hide_offline_buddies  = true;
	private BuddyFilter buddyFilter;

    /**
     * @param parentShell
     */
    public SimpleIMWindow(Shell parentShell) {
        super(parentShell);

        initDefaultStatus();

        windowTitle =
            Environment.getInstance().getPrefStore().getString(
                EimpConsts.STO_APP_TITLE);

        ac_st_list = new ArrayList();
        sessMap = new HashMap();

        messageArchiver = new MessageArchiver();
        eventSoundList = EventSoundList.getInstance();

        allwaysOnlineHelper = new AllwaysOnlineHelper();
        allwaysOnlineHelper.setEnable(
            Environment.getInstance().getPrefStore().getBoolean(
                EimpConsts.STO_ALLWAYSONLINE_ENABLE));
        allwaysOnlineHelper.start();

        createAction();
        registerImListner();

        addStatusLine();
        addMenuBar();
        addToolBar(SWT.FLAT | SWT.WRAP);

        Environment.getInstance().getSysTrayHelper().setSimpleIMWindow(this);
    }

    private void registerAccountChangeListener() {
        Environment
            .getInstance()
            .getPrefStore()
            .addPropertyChangeListener(new IPropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                if (e.getProperty().equals(EimpConsts.STO_ACCOUNTS)) {
                    getShell().getDisplay().syncExec(new Runnable() {
                        public void run() {
                            refreshBuddyView();
                            unRegisterImListner();
                            registerImListner();
                        }
                    });
                }

            }
        });

    }

    private void initDefaultStatus() {

        IPreferenceStore p = Environment.getInstance().getPrefStore();
        p.setDefault(STO_ST_SW_HEIGHT, 300);
        p.setDefault(STO_ST_SW_WIDTH, 400);
        p.setDefault(STO_ST_SW_SHOW_BUDDYLISTVIEW, true);
        p.setDefault(STO_ST_SW_SHOW_SESSIONVIEW, true);
        p.setDefault(STO_TOGGLE_HIDE_OFFLINE_BUDDIES, true); 
    }

    private void restoreStatus() {
        IPreferenceStore p = Environment.getInstance().getPrefStore();
        st_view_buddy_visible = p.getBoolean(STO_ST_SW_SHOW_BUDDYLISTVIEW);
        st_view_session_visible = p.getBoolean(STO_ST_SW_SHOW_SESSIONVIEW);
        st_toggle_hide_offline_buddies = p.getBoolean(STO_TOGGLE_HIDE_OFFLINE_BUDDIES);  
    }

    public void createAction() {
        ac_login = new AccountLoginAction(this);
        ac_logout = new AccountLogoutAction(this);
        ac_refresh = new RefreshBuddyListAction(this);
        ac_view_buddy = new ToggleBuddyListViewAction(this);
        ac_view_session = new ToggleSessionViewAction(this);
        ac_sendmsg = new SendMessageAction(this);
        ac_add_buddy = new AddBuddyAction(this);
        ac_rm_buddy = new RemoveBuddyAction(this);
        ac_save_session_txt = new ExportToTxtAction(this);
        ac_file_perf = new PerferenceAction(this);
        ac_file_about = new AboutDialogAction(this);
        ac_view_sess_type = new ToggleSessionViewTypeAction(this);
        ac_view_offline = new ToggleHideOfflineBuddiesAction(this);
        ac_file_exit = new ExitAppAction(this);
        shellControlAction = new ShellControlAction(this);
        ac_buddy_info = new BuddyInfoDialogAction(this);

        createStatusAction();
    }

    public void createStatusAction() {
        StatusAction ac_st_online = new StatusAction(this, UserStatus.ONLINE, Messages.getString("SimpleIMWindow.Online")); //$NON-NLS-1$
        ac_st_list.add(ac_st_online);

        StatusAction ac_st_offline = new StatusAction(this, UserStatus.OFFLINE, Messages.getString("SimpleIMWindow.Offline")); //$NON-NLS-1$
        ac_st_list.add(ac_st_offline);

        StatusAction ac_st_away = new StatusAction(this, UserStatus.AWAY_FROM_COMPUTER, Messages.getString("SimpleIMWindow.Away")); //$NON-NLS-1$
        ac_st_list.add(ac_st_away);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent) {
    	restoreStatus();
        parent.getShell().setText(windowTitle);
        getShell().setImage(
            ImageManager.getImageRegistry().get(ImageManager.IMG_LOGO2));

        sashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.NULL);

        buddyListViewer =
            new TreeViewer(
                sashForm,
                SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

        buddyListViewer.setContentProvider(new BuddyListProvider());
        buddyFilter = new BuddyFilter(st_toggle_hide_offline_buddies );
        ac_view_offline.setChecked(buddyFilter.isIgnoreOfflineBuddies());
        buddyListViewer.addFilter(buddyFilter);
        buddyListViewer.setLabelProvider(new BuddyListLabelProvider());
        buddyListViewer.setSorter(new BuddySorter());
        buddyListViewer.setInput(new Object());
        buddyListViewer.refresh();
        buddyListViewer
            .addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                UpdateUI();
            }
        });

        sessionView = new MultiSessionPanel();
        sessionView.setGlobalTextActionAdapter(new IGlobalTextActionManager() {
            public void addText(Text textControl) {
            }
            public void removeText(Text textControl) {
            }
        });
        sessionView.create(sashForm, SWT.NONE);

        initFormLayout();

        //init infoNotifyWindow
        infoNotifyWindow = new InfoNotifyWindow(getShell());
        infoNotifyWindow.setAllowPopup(
            Environment.getInstance().getPrefStore().getBoolean(
                EimpConsts.STO_NOTIFER_SHOW));
        infoNotifyWindow.setAutoHideDelaySecond(
            Environment.getInstance().getPrefStore().getInt(
                EimpConsts.STO_NOTIFER_DELAY_TIME));
        //infoNotifyWindow.set

        hookContextMenu();

        buddyListViewer.getControl().setFocus();

        this.getShell().addShellListener(shellControlAction);
        
        registerAccountChangeListener();

        return sashForm;

    }

    private void initFormLayout() {
        IPreferenceStore p = Environment.getInstance().getPrefStore();
        getShell().setSize(
            p.getInt(STO_ST_SW_WIDTH),
            p.getInt(STO_ST_SW_HEIGHT));
        int[] wl = sashForm.getWeights();
        int total = 0;
        for (int i = 0; i < wl.length; i++) {
            total = total + wl[i];
        }
        wl[0] = total * 2 / 7;
        sashForm.setWeights(wl);
    }

    /**
     * get the current selection of the buddy list view
     * 
     * @return ISelection
     *  
     */
    public ISelection getBuddyListViewSelections() {
        return buddyListViewer.getSelection();
    }

    /**
     * create the menu
     */
    protected MenuManager createMenuManager() {
        MenuManager bar_menu = new MenuManager(""); //$NON-NLS-1$

        MenuManager file_menu = new MenuManager(Messages.getString("SimpleIMWindow.M_File")); //$NON-NLS-1$
        MenuManager acc_menu = new MenuManager(Messages.getString("SimpleIMWindow.M_Account")); //$NON-NLS-1$
        MenuManager view_menu = new MenuManager(Messages.getString("SimpleIMWindow.M_View")); //$NON-NLS-1$

        bar_menu.add(file_menu);
        bar_menu.add(acc_menu);
        bar_menu.add(view_menu);

        acc_menu.add(ac_login);
        acc_menu.add(ac_logout);
        acc_menu.add(ac_sendmsg);
        acc_menu.add(ac_add_buddy);
        acc_menu.add(ac_rm_buddy);
        acc_menu.add(ac_buddy_info);
        acc_menu.add(new Separator("status")); //$NON-NLS-1$
        for (Iterator i = ac_st_list.iterator(); i.hasNext();) {
            StatusAction ac = (StatusAction) i.next();
            acc_menu.add(ac);
        }
        view_menu.add(ac_refresh);
        view_menu.add(ac_view_sess_type);
        view_menu.add(ac_view_buddy);
        view_menu.add(ac_view_session);
        view_menu.add(ac_view_offline);

        file_menu.add(ac_save_session_txt);
        file_menu.add(ac_file_perf);
        file_menu.add(new Separator("Exit")); //$NON-NLS-1$
        file_menu.add(ac_file_about);
        file_menu.add(ac_file_exit);

        return bar_menu;
    }

    /**
     * create toolbar
     */
    protected ToolBarManager createToolBarManager(int style) {

        ToolBarManager tool_bar_manager = new ToolBarManager(style);

        tool_bar_manager.add(ac_login);
        tool_bar_manager.add(ac_logout);
        tool_bar_manager.add(ac_add_buddy);
        tool_bar_manager.add(ac_rm_buddy);
        tool_bar_manager.add(new Separator("account")); //$NON-NLS-1$
        tool_bar_manager.add(ac_save_session_txt);
        tool_bar_manager.add(ac_sendmsg);
        tool_bar_manager.add(new Separator("view")); //$NON-NLS-1$
        tool_bar_manager.add(ac_refresh);
        tool_bar_manager.add(ac_view_buddy);
        tool_bar_manager.add(ac_view_session);
        tool_bar_manager.add(ac_view_sess_type);
        tool_bar_manager.add(ac_view_offline);

        return tool_bar_manager;
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        fillContextMenu(menuMgr);
        //menuMgr.setRemoveAllWhenShown(true);
        //			menuMgr.addMenuListener(new IMenuListener() {
        //				public void menuAboutToShow(IMenuManager manager) {
        //					//refreshBuddyView();
        //					fillContextMenu(manager);
        //				}
        //
        //			});

        Menu menu = menuMgr.createContextMenu(buddyListViewer.getControl());
        buddyListViewer.getControl().setMenu(menu);
        //getSite().registerContextMenu(menuMgr, viewer);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(ac_sendmsg);
        manager.add(new Separator("account")); //$NON-NLS-1$
        //			for (Iterator i = accountActionList.iterator(); i.hasNext();) {
        //				Action ac = (Action) i.next();
        //				manager.add(ac);
        //			}
        manager.add(ac_login);
        manager.add(ac_logout);
        manager.add(new Separator("budy")); //$NON-NLS-1$
        manager.add(ac_add_buddy);
        manager.add(ac_rm_buddy);
        manager.add(new Separator());
        manager.add(ac_refresh);
        //drillDownAdapter.addNavigationActions(manager);

        // Other plug-ins can contribute there actions here
        manager.add(new Separator("Additions")); //$NON-NLS-1$
        for (Iterator i = ac_st_list.iterator(); i.hasNext();) {
            StatusAction ac = (StatusAction) i.next();
            manager.add(ac);
        }
        manager.add(new Separator("userdetail")); //$NON-NLS-1$
        manager.add(ac_buddy_info);

        //MenuManager ms=new MenuManager();
        //ms.setParent(manager);
        //			for (Iterator i = statusActionList.iterator(); i.hasNext();) {
        //				Action ac = (Action) i.next();
        //				manager.add(ac);
        //			}
    }

    public void registerImListner() {

        SwIMListener lsn = new SwIMListener();
        AccountList l = Environment.getInstance().getAccountList();
        for (Iterator i = l.iterator(); i.hasNext();) {
            Account acc = (Account) i.next();
            acc.getConnection().addIMListener(lsn);
            acc.getConnection().addIMListener(messageArchiver);
        }
    }
    
    public void unRegisterImListner(){
        SwIMListener lsn = new SwIMListener();
        AccountList l = Environment.getInstance().getAccountList();
        for (Iterator i = l.iterator(); i.hasNext();) {
            Account acc = (Account) i.next();
            acc.getConnection().removeIMListener(lsn);
            acc.getConnection().removeIMListener(messageArchiver);
        }
    }

    public void refreshBuddyView() {
        //buddyListViewer.setInput(ResourcesPlugin.getWorkspace());
        buddyListViewer.refresh();
        //buddyListViewer.expandToLevel(2);
        //			updateAction();
        			UpdateUI();
    }

    public void UpdateUI() {
        sessionView.getControl().setVisible(st_view_session_visible);
        buddyListViewer.getControl().setVisible(st_view_buddy_visible);

        if (st_view_buddy_visible && st_view_session_visible)
            sashForm.setMaximizedControl(null);
        if (st_view_buddy_visible && !st_view_session_visible)
            sashForm.setMaximizedControl(buddyListViewer.getControl());
        if (!st_view_buddy_visible && st_view_session_visible)
            sashForm.setMaximizedControl(sessionView.getControl());

        ac_view_buddy.setChecked(st_view_buddy_visible);

        ac_view_session.setChecked(st_view_session_visible);
                
        IStructuredSelection sl =
            (IStructuredSelection) buddyListViewer.getSelection();
        Object e = sl.getFirstElement();
        if (e != null && e instanceof Account) {
            Account acc = (Account) e;
            boolean offC = acc.getConnection().getStatus().equals(UserStatus.OFFLINE);
            ac_login.setEnabled(offC);
            ac_logout.setEnabled(!offC);
            ac_add_buddy.setEnabled(!offC);
            ac_rm_buddy.setEnabled(false);
            ac_sendmsg.setEnabled(false);
            ac_buddy_info.setEnabled(false);
            for (Iterator i = ac_st_list.iterator(); i.hasNext();) {
                StatusAction ac = (StatusAction) i.next();
                ac.setEnabled(!offC);
                if (!offC && acc.getConnection().getStatus().equals(ac.getStatus()))
                    ac.setChecked(true);
                else
                    ac.setChecked(false);
            }

        }
        if (e != null && e instanceof Buddy) {
            Buddy b = (Buddy) e;
            boolean offB = b.getStatus().equals(UserStatus.OFFLINE);
            Account acc = b.getAccount();
            boolean offC = acc.getConnection().getStatus().equals(UserStatus.OFFLINE);             
            ac_login.setEnabled(false);
            ac_logout.setEnabled(false);
            ac_add_buddy.setEnabled(false);
            ac_rm_buddy.setEnabled(!offC);
            ac_sendmsg.setEnabled(!offB);
            ac_buddy_info.setEnabled(!offC);
            for (Iterator i = ac_st_list.iterator(); i.hasNext();) {
                StatusAction ac = (StatusAction) i.next();
                ac.setEnabled(false);
            }
        }
    }

    public void toggleBuddyListViewVisible() {
        if (st_view_buddy_visible == true)
            st_view_buddy_visible = false;
        else
            st_view_buddy_visible = true;
        UpdateUI();
    }
    public void toggleSessionViewVisible() {
        if (st_view_session_visible == true)
            st_view_session_visible = false;
        else
            st_view_session_visible = true;
        UpdateUI();
    }
    public void toggleHideOfflineBuddies(boolean value) {
    	st_toggle_hide_offline_buddies = value;
    	    	buddyFilter.setIgnoreOfflineBuddies(st_toggle_hide_offline_buddies);
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.maze.eimp.views.IConvSession#SessionClosed(org.maze.eimp.im.Session)
     */
    public void SessionClosed(Session sess) {
        messageArchiver.remove(sess.getId());
        sessMap.remove(sess.getId());
    }

    public void showMessage(String message) {
        MessageDialog.openInformation(getShell(), "Eclipse Instant Messenger", //$NON-NLS-1$
        message);
    }

    class SwIMListener extends IMAdapter {
        public void loginComplete() {
            buddyListViewer.getControl().getDisplay().syncExec(new Runnable() {
                public void run() {
                eventSoundList.play(EventSoundList.Login_Complete);
                    refreshBuddyView();
                }

            });
        }

        public void buddyOnline(final Buddy b) {
                                    buddyListViewer.getControl().getDisplay().syncExec(new Runnable() {
                    	public void run() {
                    		eventSoundList.play(b);
                    		refreshBuddyView();
                    	}
						});
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.maze.eimp.im.IMListener#buddyStatusChange(org.maze.eimp.im.Buddy)
         */
        public void buddyStatusChange(final Buddy b) {
            buddyListViewer.getControl().getDisplay().syncExec(new Runnable() {
                public void run() {
                	eventSoundList.play(b);
                	refreshBuddyView();
                }
            });
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.maze.eimp.im.IMListener#instantMessageReceived(org.maze.eimp.im.Session,
         *      org.maze.eimp.im.Buddy, org.maze.eimp.im.MimeMessage)
         */
        public void instantMessageReceived(
            final Session ss,
            final Buddy friend,
            final MimeMessage mime) {
            buddyListViewer.getControl().getDisplay().syncExec(new Runnable() {
                public void run() {
                eventSoundList.play(EventSoundList.INSTANT_MESSAGE_RECEIVED);
                    sessionView.addSession(ss);
                    if (!sessMap.containsKey(ss.getId()))
                        sessMap.put(ss.getId(), ss);
                    DressMessage m = new DressMessage(mime.getMessageString());
                    m.setTime(mime.getTime());
                    m.setBuddy(friend);
                    sessionView.append(m, ss);

                    //notice a message
                    if (infoNotifyWindow != null) {
                        getShell().getDisplay().syncExec(new Runnable() {
                            public void run() {
                                if (Display.getCurrent().getActiveShell()
                                    == null) {
                                    infoNotifyWindow.setText(friend.getFriendlyName() + "\n " //$NON-NLS-1$
                                    +mime.getMessageString());
                                    infoNotifyWindow.show();
                                }
                            }
                        });
                    }
                }
            });

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.maze.eimp.im.IMListener#sessionEnded(org.maze.eimp.im.Session)
         */
        public void sessionEnded(final Session ss) {
            buddyListViewer.getControl().getDisplay().syncExec(new Runnable() {
                public void run() {
                eventSoundList.play(EventSoundList.SESSION_ENDED);
                    sessionView.closeSession(ss);
                }
            });
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.maze.eimp.im.IMListener#sessionStarted(org.maze.eimp.im.Session)
         */
        public void sessionStarted(final Session ss) {
            buddyListViewer.getControl().getDisplay().syncExec(new Runnable() {
                public void run() {
                eventSoundList.play(EventSoundList.SESSION_STARTED);
                    sessionView.addSession(ss);
                    if (!sessMap.containsKey(ss.getId()))
                        sessMap.put(ss.getId(), ss);
                }
            });

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.maze.eimp.im.IMListener#loginError(java.lang.String)
         */
        public void loginError(final String cause) {
            buddyListViewer.getControl().getDisplay().syncExec(new Runnable() {
                public void run() {
                eventSoundList.play(EventSoundList.LOGIN_ERROR);
                    infoNotifyWindow.setText(cause);
                    infoNotifyWindow.show();
                }
            });
        }
        /* (non-Javadoc)
         * @see org.maze.eimp.im.IMListener#logoutNotify()
         */
        public void logoutNotify() {
            buddyListViewer.getControl().getDisplay().syncExec(new Runnable() {
                public void run() {
                eventSoundList.play(EventSoundList.LOGOUT_NOTIFY);
                    refreshBuddyView();
                }

            });
        }
    }
    
    /**
     * get the global message archiver
     * 
     * @return the global message archiver
     */
    public MessageArchiver getMessageArchiver() {
        return messageArchiver;
    }

    /**
     * @return
     */
    public TreeViewer getBuddyListViewer() {
        return buddyListViewer;
    }

    /**
     * @return
     */
    public MultiSessionPanel getSessionView() {
        return sessionView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#close()
     */
    public boolean close() {
        IPreferenceStore p = Environment.getInstance().getPrefStore();
        //p.setValue(EimpConsts.STO_APP_TITLE,windowTitle);
        p.setValue(STO_ST_SW_HEIGHT, getShell().getSize().y);
        p.setValue(STO_ST_SW_WIDTH, getShell().getSize().x);
        p.setValue(STO_ST_SW_SHOW_BUDDYLISTVIEW, st_view_buddy_visible);
        p.setValue(STO_ST_SW_SHOW_SESSIONVIEW, st_view_session_visible);
        p.setValue(STO_TOGGLE_HIDE_OFFLINE_BUDDIES, st_toggle_hide_offline_buddies);

        Environment.getInstance().getSysTrayHelper().close();

        allwaysOnlineHelper.stop();

        return super.close();
    }

    /**
     * @return
     */
    public HashMap getSessMap() {
        return sessMap;
    }
    /**
     * @return Returns the allwaysOnlineHelper.
     */
    public AllwaysOnlineHelper getAllwaysOnlineHelper() {
        return allwaysOnlineHelper;
    }

}
