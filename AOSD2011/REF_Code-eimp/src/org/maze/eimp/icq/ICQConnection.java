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

package org.maze.eimp.icq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.kano.joscar.rv.RvProcessor;
import net.kano.joscar.rvcmd.DefaultRvCommandFactory;
import net.kano.joscar.snac.ClientSnacProcessor;
import net.kano.joscar.snaccmd.CapabilityBlock;
import net.kano.joscar.snaccmd.FullUserInfo;
import net.kano.joustsim.Screenname;
import net.kano.joustsim.oscar.AimConnection;
import net.kano.joustsim.oscar.AimConnectionProperties;
import net.kano.joustsim.oscar.AimSession;
import net.kano.joustsim.oscar.AppSession;
import net.kano.joustsim.oscar.CapabilityHandler;
import net.kano.joustsim.oscar.CapabilityListener;
import net.kano.joustsim.oscar.CapabilityManager;
import net.kano.joustsim.oscar.DefaultAimSession;
import net.kano.joustsim.oscar.OpenedServiceListener;
import net.kano.joustsim.oscar.State;
import net.kano.joustsim.oscar.StateEvent;
import net.kano.joustsim.oscar.StateListener;
import net.kano.joustsim.oscar.oscar.service.Service;
import net.kano.joustsim.oscar.oscar.service.buddy.BuddyService;
import net.kano.joustsim.oscar.oscar.service.buddy.BuddyServiceListener;
import net.kano.joustsim.oscar.oscar.service.icbm.Conversation;
import net.kano.joustsim.oscar.oscar.service.icbm.ConversationAdapter;
import net.kano.joustsim.oscar.oscar.service.icbm.IcbmBuddyInfo;
import net.kano.joustsim.oscar.oscar.service.icbm.IcbmListener;
import net.kano.joustsim.oscar.oscar.service.icbm.IcbmService;
import net.kano.joustsim.oscar.oscar.service.icbm.Message;
import net.kano.joustsim.oscar.oscar.service.icbm.MessageInfo;
import net.kano.joustsim.oscar.oscar.service.icbm.SimpleMessage;
import net.kano.joustsim.oscar.oscar.service.icbm.TypingInfo;
import net.kano.joustsim.oscar.oscar.service.icbm.TypingListener;
import net.kano.joustsim.oscar.oscar.service.icbm.TypingState;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.FileTransfer;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.IncomingRvConnection;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.RvConnectionManager;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.RvConnectionManagerListener;
import net.kano.joustsim.oscar.oscar.service.ssi.BuddyListLayoutListener;
import net.kano.joustsim.oscar.oscar.service.ssi.Group;
import net.kano.joustsim.oscar.oscar.service.ssi.MutableGroup;
import net.kano.joustsim.oscar.oscar.service.ssi.SsiService;

import org.maze.eimp.Environment;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.BuddyGroup;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.Connection;
import org.maze.eimp.im.IMListener;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.Session;
import org.maze.eimp.im.UserStatus;
import org.maze.eimp.util.LocalStore;

/**
 * @author loya
 */
public class ICQConnection implements Connection {

	private Account acc;
	private ArrayList imListeners;
	private HashMap uidToSessionMap = null;
	private BuddyGroup buddyGroup = null;
	private LocalStore localStore = null;
	private String myStatus = UserStatus.OFFLINE;

	// TODO new
	private AppSession appSession;
	private AimConnectionProperties connectionProperties;
	private AimConnection connection;

	private void log(String s) {
		// code: 1010 is for ICQ
		Environment.getInstance().getLogger().log(1010, s);
	}

	private void fireLoginComplete() {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.loginComplete();
		}
	}

	private void fireBuddyOnline(Buddy buddy) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.buddyOnline(buddy);
		}
	}

	private void fireBuddyOffline(Buddy buddy) {
		// for (Iterator i = imListeners.iterator(); i.hasNext();) {
		// IMListener im = (IMListener) i.next();
		// im.(buddy);
		// }
	}

	private void fireBuddyStatusChange(Buddy buddy) {
		buddy.setAccount(getAccount());
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.buddyStatusChange(buddy);
		}
	}

	private void fireMessageReceived2(MessageInfo messageInfo) {
		ICQSession sess = null;
		if (uidToSessionMap.containsKey(messageInfo.getFrom().getNormal())) {
			sess = (ICQSession) uidToSessionMap.get(messageInfo.getFrom()
					.getNormal());
		} else {
			sess = new ICQSession(messageInfo.getFrom().getNormal(), this);
			sess.getBuddyList().add(
					Clone.toBuddy(messageInfo.getFrom().getNormal()));
			uidToSessionMap.put(messageInfo.getFrom().getNormal(), sess);
		}
		fireMessageReceived(new MimeMessage(messageInfo.getMessage()
				.getMessageBody()), Clone.toBuddy(messageInfo.getFrom()
				.getNormal()), sess);
	}

	protected void fireMessageReceived(MimeMessage m, Buddy b, ICQSession sess) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.instantMessageReceived(sess, b, m);
		}
	}

	protected void fireSessionStarted(Session s) {
		for (Iterator i = imListeners.iterator(); i.hasNext();) {
			IMListener im = (IMListener) i.next();
			im.sessionStarted(s);
		}
	}

	public ICQConnection() {
		imListeners = new ArrayList();
		uidToSessionMap = new HashMap();
		buddyGroup = BuddyGroup.getInstance();
		localStore = new LocalStore();
		this.appSession = new AppSession() {
			public AimSession openAimSession(Screenname sn) {
				return new DefaultAimSession(this, sn) {
				};
			}
		};
		this.connectionProperties = new AimConnectionProperties(null, null); // use
																				// to
																				// hold
																				// on
																				// connection
																				// settings
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#login()
	 */
	public boolean login() {
		createConnection(acc);
		setStatus(UserStatus.ONLINE);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#setAccount(org.maze.eimp.im.Account)
	 */
	public void setAccount(Account acc) {
		this.acc = acc;
	}

	private void createConnection(Account acc) {
		final Screenname screenName = new Screenname(acc.getLoginid());
		AimSession session = appSession.openAimSession(screenName);
		this.connectionProperties.setScreenname(screenName);
		this.connectionProperties.setPassword(acc.getPassword());
		this.connectionProperties.setLoginHost(System.getProperty("OSCAR_HOST",
				this.connectionProperties.getLoginHost()));
		this.connectionProperties.setLoginPort(Integer.getInteger("OSCAR_PORT",
				this.connectionProperties.getLoginPort()).intValue());
		this.connection = session.openConnection(this.connectionProperties);

		localStore.setLoginName(acc.getLoginid());
		localStore.loadInformation();
		localStore.loadBuddies(buddyGroup);
		BuddyList bl = buddyGroup.getForwardList();
		for (Iterator i = bl.iterator(); i.hasNext();) {
			Buddy b = (Buddy) i.next();
			b.setAccount(acc);
			// Contact ac = new Contact();
			// ac.setContactId(b.getLoginName());
			// ac.setNickName(b.getFriendlyName());
			// contactList.addToContactList(ac);
		}

		catchBuddyList();
		this.connection.addStateListener(new StateListener() {
			public void handleStateChange(StateEvent event) {
				if (State.ONLINE == event.getNewState()) {
					fireAlmostConnected(ICQConnection.this.connection);
					// notifyConnectionEstablished();
					fireLoginComplete();
				} else if (State.FAILED == event.getNewState()) {
					fireFailedToConnect();
				} else if (State.DISCONNECTED == event.getNewState()) {
					logout();
				}
			}
		});
		this.connection.getCapabilityManager().setCapabilityHandler(
				CapabilityBlock.BLOCK_FILE_SEND, new CapabilityHandler() {
					public boolean isEnabled() {
						return true;
					}

					public void handleAdded(CapabilityManager manager) {
					}

					public void handleRemoved(CapabilityManager manager) {
					}

					public void addCapabilityListener(
							CapabilityListener capabilityListener) {
					}

					public void removeCapabilityListener(
							CapabilityListener capabilityListener) {
					}
				});

		// this.connection.getSsiService().getAimConnection().getBuddyInfoManager().addGlobalBuddyInfoListener(
		// new GlobalBuddyInfoListener() {
		//
		// public void buddyInfoChanged(BuddyInfoManager arg0, Screenname arg1,
		// BuddyInfo arg2, PropertyChangeEvent arg3) {
		// // TODO Auto-generated method stub
		// System.out.println("Update status in buddyInfoChanged");
		// }
		//
		// public void newBuddyInfo(BuddyInfoManager arg0, Screenname arg1,
		// BuddyInfo arg2) {
		// // TODO Auto-generated method stub
		// System.out.println("Update status in newBuddyInfo");
		// }
		//
		// public void receivedStatusUpdate(BuddyInfoManager arg0, Screenname
		// arg1, BuddyInfo arg2) {
		// // TODO Auto-generated method stub
		// System.out.println("Update status in receivedStatusUpdate");
		// }
		//			
		// });

		this.connection.connect();
	}

	/**
	 * Manage a list of ppl.
	 */
	private void catchBuddyList() {
		connection.addOpenedServiceListener(new OpenedServiceListener() {

			public void closedServices(AimConnection arg0, Collection arg1) {
				// TODO Auto-generated method stub

			}

			public void openedServices(AimConnection conn, Collection services) {
				for (Iterator iter = services.iterator(); iter.hasNext();) {
					Service service = (Service) iter.next();
					if (service instanceof SsiService) {
						((SsiService) service).getBuddyList()
								.addRetroactiveLayoutListener(
										new BuddyListLayoutListener() {

											public void buddiesReordered(
													net.kano.joustsim.oscar.oscar.service.ssi.BuddyList arg0,
													Group arg1, List arg2,
													List arg3) {
												// TODO Auto-generated method
												// stub

											}

											public void buddyAdded(
													net.kano.joustsim.oscar.oscar.service.ssi.BuddyList arg0,
													Group arg1,
													List arg2,
													List arg3,
													net.kano.joustsim.oscar.oscar.service.ssi.Buddy arg4) {
												// TODO Auto-generated method
												// stub
												System.out
														.println("Buddy Added");
											}

											public void buddyRemoved(
													net.kano.joustsim.oscar.oscar.service.ssi.BuddyList arg0,
													Group arg1,
													List arg2,
													List arg3,
													net.kano.joustsim.oscar.oscar.service.ssi.Buddy arg4) {
												// TODO Auto-generated method
												// stub

											}

											public void groupAdded(
													net.kano.joustsim.oscar.oscar.service.ssi.BuddyList arg0,
													List arg1, List arg2,
													Group arg3, List arg4) {
												// TODO Auto-generated method
												// stub

											}

											public void groupRemoved(
													net.kano.joustsim.oscar.oscar.service.ssi.BuddyList arg0,
													List arg1, List arg2,
													Group arg3) {
												// TODO Auto-generated method
												// stub

											}

											public void groupsReordered(
													net.kano.joustsim.oscar.oscar.service.ssi.BuddyList arg0,
													List arg1, List arg2) {
												// TODO Auto-generated method
												// stub

											}
										});
					}
				}
			}

		});
	}

	private void fireAlmostConnected(AimConnection connection) {
		// get the list
		ClientSnacProcessor processor = connection.getBosService()
				.getOscarConnection().getSnacProcessor();

		// ////////////////////////////
		// setup the rvProcessor //
		// ///////////////////////////
		RvProcessor rvProcessor = new RvProcessor(processor);
		rvProcessor.registerRvCmdFactory(new DefaultRvCommandFactory());

		// ////////////////////////////////////
		// setup file transfer listener //
		// ///////////////////////////////////

		connection.getIcbmService().getRvConnectionManager()
				.addConnectionManagerListener(
						new RvConnectionManagerListener() {
							public void handleNewIncomingConnection(
									RvConnectionManager manager,
									IncomingRvConnection transfer) {
								if (transfer instanceof FileTransfer) {
									FileTransfer fileTransfer = (FileTransfer) transfer;

								} // if
							}
						});

		{ // setup conversation support
			IcbmService icbmService = connection.getIcbmService();
			// icbmService.removeIcbmListener(lastIcbmListener);
			IcbmListener lastIcbmListener = new IcbmListener() {
				public void newConversation(IcbmService service,
						Conversation conv) {
					// Adds a conversation listener that tells every listener
					// when a message has been received.
					conv.addConversationListener(new ConversationAdapter() {

						public void canSendMessageChanged(Conversation arg0,
								boolean arg1) {
							System.out.println("canSendMessageChanged = "
									+ arg0.getBuddy());
						}

						public void conversationOpened(Conversation arg0) {
							System.out.println("conversationOpened = "
									+ arg0.getBuddy());
						}

						public void gotMessage(Conversation arg0,
								MessageInfo messageInfo) {
							System.out.println("gotMessage = "
									+ messageInfo.getMessage());
							ICQConnection.this
									.fireMessageReceived2(messageInfo);
						}

						public void sentMessage(Conversation arg0,
								MessageInfo arg1) {
							System.out.println("sentMessage = "
									+ arg1.getMessage());
						}

					});
				}

				public void buddyInfoUpdated(IcbmService service,
						Screenname buddy, IcbmBuddyInfo info) {
					// don't care yet
					System.out.println("buddyInfoUpdated");
					// log.fine("Buddy Info Updated. - " + buddy.getNormal() +
					// " " + info);
				}

				public void sendAutomaticallyFailed(IcbmService arg0,
						Message arg1, Set arg2) {
					// TODO Auto-generated method stub

				}
			};
			icbmService.addIcbmListener(lastIcbmListener);
		} // setup conversation support

		// final DelayedThread updateStatus = new DelayedThread(1000, new
		// Runnable() {
		//
		// });

		connection.getBuddyService().addBuddyListener(
				new BuddyServiceListener() {
					public void gotBuddyStatus(BuddyService service,
							Screenname buddy, FullUserInfo info) {
						Buddy b = Clone.toBuddy(buddy.getNormal());
						b.setStatus(Clone.toStatus(info.getIcqStatus()));
						buddyGroup.getForwardList().add(b);
						fireBuddyStatusChange(b);
						if (UserStatus.ONLINE.equals(b.getStatus())) {
							fireBuddyOnline(b);
						}
					}

					public void buddyOffline(BuddyService service,
							Screenname buddy) {
						Buddy b = Clone.toBuddy(buddy.getNormal());
						b.setStatus(UserStatus.OFFLINE);
						buddyGroup.getForwardList().add(b);
						fireBuddyStatusChange(b);
						fireBuddyOffline(b);
					}
				});

		this.setStatus(UserStatus.ONLINE);
	}

	private static void fireFailedToConnect() {
		System.out.println("Failed to connect");
	}

	private static void fireDisconnect() {
		System.out.println("Disconnected");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getAccount()
	 */
	public Account getAccount() {
		return acc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#close()
	 */
	public void close() {

	}

	final class TypingAdapter extends ConversationAdapter implements
			TypingListener {
		public void gotMessage(Conversation c, final MessageInfo minfo) {
			System.out.println("Received message = " + minfo.getMessage());
		}

		public void gotTypingState(Conversation conversation,
				TypingInfo typingInfo) {
			if (typingInfo.getTypingState().equals(TypingState.TYPING)) {

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#logout()
	 */
	public void logout() {
		if (connection != null) {
			connection.disconnect(true); // intentionally
			setStatus(UserStatus.OFFLINE);
		}
		fireDisconnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getBuddyGroup()
	 */
	public BuddyGroup getBuddyGroup() {
		return buddyGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#addBuddy(org.maze.eimp.im.Buddy)
	 */
	public void addBuddy(Buddy buddy) {
		if (connection == null) {
			return;
		}
		buddy.setAccount(acc);
		// Contact c = Clone.toContact(buddy);
		// TODO used ?
		// contactList.addToContactList(c);
		Group group = this.findGroup();
		if (group instanceof MutableGroup) {
			((MutableGroup) group).addBuddy(buddy.getLoginName());
		}
		buddyGroup.getForwardList().add(buddy);
		localStore.storeBuddies(buddyGroup);
	}

	private net.kano.joustsim.oscar.oscar.service.ssi.Group findGroup() {
		List list = connection.getSsiService().getBuddyList().getGroups();
		if (list != null) {
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Group aimGroup = (Group) iter.next();
				if (aimGroup instanceof MutableGroup) {
					return aimGroup;
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#removeBuddy(org.maze.eimp.im.Buddy)
	 */
	public void removeBuddy(Buddy buddy) {
		if (connection == null) {
			return;
		}

		// contactList.removeFromContactList(Clone.toContact(buddy));
		// TODO used ?
		buddyGroup.getForwardList().remove(buddy);
		localStore.storeBuddies(buddyGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#findSession(org.maze.eimp.im.Buddy)
	 */
	public Session findSession(Buddy buddy) {
		if (uidToSessionMap.containsKey(buddy.getLoginName())) {
			return (Session) uidToSessionMap.get(buddy.getLoginName());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.maze.eimp.im.Connection#addIMListener(org.maze.eimp.im.IMListener)
	 */
	public void addIMListener(IMListener lsn) {
		imListeners.add(lsn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#setStatus(java.lang.String)
	 */
	public void setStatus(String st) {
		if (connection == null) {
			return;
		}

		if (State.ONLINE == connection.getState()) {
			if (UserStatus.AWAY_FROM_COMPUTER.equals(st)) {
				this.connection.getInfoService().setAwayMessage("sfdvgsdfg");
			} else if (UserStatus.ONLINE.equals(st)) {
				this.connection.getInfoService().setAwayMessage(null);
			}
			myStatus = st;
		} else {
			myStatus = UserStatus.OFFLINE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#getStatus()
	 */
	public String getStatus() {
		return myStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.im.Connection#callBuddy(org.maze.eimp.im.Buddy)
	 */
	public void callBuddy(Buddy buddy) {
		if (connection == null) {
			return;
		}

		fireSessionStarted(getSession(buddy));
	}

	private ICQSession getSession(Buddy buddy) {
		if (uidToSessionMap.containsKey(buddy.getLoginName())) {
			return (ICQSession) uidToSessionMap.get(buddy.getLoginName());
		} else {
			ICQSession s = new ICQSession(buddy.getLoginName(), this);
			s.getBuddyList().add(buddy);
			return s;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.maze.eimp.im.Connection#removeIMListener(org.maze.eimp.im.IMListener)
	 */
	public void removeIMListener(IMListener lsn) {
		imListeners.remove(lsn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.maze.eimp.im.Connection#sendInstantMessage(org.maze.eimp.im.MimeMessage
	 * , org.maze.eimp.im.Buddy)
	 */
	public void sendInstantMessage(MimeMessage msg, Buddy buddy) {
		if (connection == null) {
			return;
		}

		Conversation conversation = connection.getIcbmService()
				.getImConversation(new Screenname(buddy.getLoginName()));
		conversation.sendMessage(new SimpleMessage(msg.getMessageString()));
	}

}
