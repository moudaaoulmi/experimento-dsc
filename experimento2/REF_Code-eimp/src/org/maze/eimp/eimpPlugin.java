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

package org.maze.eimp;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.AccountList;
import org.maze.eimp.script.ImCommandServer;

/**
 * The main plugin class to be used in the desktop.
 */
public class eimpPlugin extends AbstractUIPlugin {
	// The shared instance.
	private static eimpPlugin plugin;

	// public final static String PERF_USERID = "eimp.userid";
	// public final static String PERF_PASSWD = "eimp.passwd";

	public final static String IMG_LOGIN = "eimp_img_login";
	public final static String IMG_LOGOUT = "eimp_img_logout";
	public final static String IMG_SENDMSG = "eimp_img_sendmsg";
	public final static String IMG_REFRESH = "eimp_img_refresh";
	public final static String IMG_ADDBUDDY = "eimp_img_addbuddy";
	public final static String IMG_REMOVEBUDDY = "eimp_img_removebuddy";
	public final static String IMG_BUDDY_ONLINE = "eimp_img_buddy_online";
	public final static String IMG_BUDDY_OFFLINE = "eimp_img_buddy_offline";
	public final static String IMG_HIDE_OFFLINE = "eimp_img_hide_offline";
	public final static String IMG_ACC_ONLINE = "eimp_img_acc_online";
	public final static String IMG_ACC_OFFLINE = "eimp_img_acc_offline";
	public final static String IMG_ACC_AWAY = "eimp_img_acc_away";
	public final static String IMG_SAVE_AS = "eimp_img_save_as";

	// Resource bundle.
	private ResourceBundle resourceBundle;

	/**
	 * The constructor.
	 */
	public eimpPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;

		internalEimpPlugin();
	}

	private void internalEimpPlugin() {
		resourceBundle = ResourceBundle
				.getBundle("org.maze.eimp.eimpPluginResources");
	}

	/**
	 * Returns the shared instance.
	 */
	public static eimpPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = eimpPlugin.getDefault().getResourceBundle();

		return internalGetResourceString(key, bundle);
	}

	private static String internalGetResourceString(String key,
			ResourceBundle bundle) {
		return bundle.getString(key);
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPreferences(org
	 * .eclipse.jface.preference.IPreferenceStore)
	 */
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		// store.setDefault(eimpPlugin.PERF_USERID, "@hotmail.com");
		// store.setDefault(eimpPlugin.PERF_PASSWD, "");
		store.setDefault(EimpConsts.STO_NOTIFER_DELAY_TIME, 10);
		store.setDefault(EimpConsts.STO_CMDSERVER_ENABLE, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse
	 * .jface.resource.ImageRegistry)
	 */
	protected void initializeImageRegistry(ImageRegistry imgr) {
		ImageDescriptor image;
		URL url = null;

		url = internalInitializeImageRegistry(url, "icons/login.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_LOGIN, image);

		url = internalInitializeImageRegistry(url, "icons/logout.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_LOGOUT, image);

		url = internalInitializeImageRegistry(url, "icons/sendmsg.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_SENDMSG, image);

		url = internalInitializeImageRegistry(url, "icons/refresh.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_REFRESH, image);

		url = internalInitializeImageRegistry(url, "icons/addbuddy.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_ADDBUDDY, image);

		url = internalInitializeImageRegistry(url, "icons/removebuddy.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_REMOVEBUDDY, image);

		url = internalInitializeImageRegistry(url, "icons/buddy_online.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_BUDDY_ONLINE, image);

		url = internalInitializeImageRegistry(url, "icons/buddy_offline.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_BUDDY_OFFLINE, image);

		url = internalInitializeImageRegistry(url, "icons/account_nln.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_ACC_ONLINE, image);

		url = internalInitializeImageRegistry(url, "icons/account_fln.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_ACC_OFFLINE, image);

		url = internalInitializeImageRegistry(url, "icons/account_away.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_ACC_AWAY, image);

		url = internalInitializeImageRegistry(url, "icons/saveas_edit.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_SAVE_AS, image);

		url = internalInitializeImageRegistry(url, "icons/hide_offline.gif");

		image = ImageDescriptor.createFromURL(url);
		imgr.put(eimpPlugin.IMG_HIDE_OFFLINE, image);

		// super.initializeImageRegistry(arg0);
	}

	private URL internalInitializeImageRegistry(URL url, String path) {
		url = new URL(eimpPlugin.getDefault().getDescriptor().getInstallURL(),
				path);
		return url;
	}

	private ImCommandServer ics;

	private ProtocolManager protocolManager;

	public void startup() throws CoreException {

		this.protocolManager = new ProtocolManager();

		Environment.getInstance().setPrefStore(this.getPreferenceStore());
		Environment.getInstance().init();

		if (Environment.getInstance().getPrefStore().getBoolean(
				EimpConsts.STO_CMDSERVER_ENABLE)) {
			ics = new ImCommandServer();
			AccountList al = Environment.getInstance().getAccountList();
			for (Iterator i = al.iterator(); i.hasNext();) {
				Account e = (Account) i.next();
				ics.init(e.getConnection());
			}
		}

		createLogger();

		internalStartup();

		super.startup();

	}

	private void internalStartup() {
		Environment.getInstance().setPathHome(
				Platform
						.resolve(
								eimpPlugin.getDefault().getDescriptor()
										.getInstallURL()).getFile());
	}

	/**
	 * create a default logger
	 */
	private void createLogger() {

		ILogger log = new ILogger() {

			public void log(String info) {
				log(1000, info);
			}

			public void log(int number, String info) {
				Status ss = new Status(IStatus.ERROR, "org.maze.eimp", 1020,
						info, null);
				eimpPlugin.getDefault().getLog().log(ss);

			}
		};
		Environment.getInstance().setLogger(log);

	}

	public ProtocolManager getProtocolManager() {
		return this.protocolManager;
	}

}