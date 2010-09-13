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

package org.maze.eimp.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

/**
 * @author loya
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ImageManager {

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
	public final static String IMG_VIEW_BUDDY = "eimp_img_view_buddy";
	public final static String IMG_VIEW_SESSION = "eimp_img_view_session";
	public final static String IMG_VIEW_TABLE = "eimp_img_view_table";
	public final static String IMG_SAVE_AS = "eimp_img_save_as";
	public final static String IMG_LOGO = "eimp_img_logo";
	public final static String IMG_LOGO2 = "eimp_img_logo2";

	private static ImageRegistry imageRegistry;

	public static ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}

	public static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();

			imageRegistry.put(
				IMG_LOGIN,
				ImageDescriptor.createFromURL(newURL("file:icons/login.gif")));

			imageRegistry.put(
				IMG_LOGOUT,
				ImageDescriptor.createFromURL(newURL("file:icons/logout.gif")));

			imageRegistry.put(
				IMG_SENDMSG,
				ImageDescriptor.createFromURL(
					newURL("file:icons/sendmsg.gif")));

			imageRegistry.put(
				IMG_ADDBUDDY,
				ImageDescriptor.createFromURL(
					newURL("file:icons/addbuddy.gif")));

			imageRegistry.put(
				IMG_REMOVEBUDDY,
				ImageDescriptor.createFromURL(
					newURL("file:icons/removebuddy.gif")));

			imageRegistry.put(
				IMG_REFRESH,
				ImageDescriptor.createFromURL(
					newURL("file:icons/refresh.gif")));

			imageRegistry.put(
				IMG_SAVE_AS,
				ImageDescriptor.createFromURL(
					newURL("file:icons/saveas_edit.gif")));

			imageRegistry.put(
				IMG_ACC_AWAY,
				ImageDescriptor.createFromURL(
					newURL("file:icons/account_away.gif")));

			imageRegistry.put(
				IMG_ACC_OFFLINE,
				ImageDescriptor.createFromURL(
					newURL("file:icons/account_fln.gif")));

			imageRegistry.put(
				IMG_ACC_ONLINE,
				ImageDescriptor.createFromURL(
					newURL("file:icons/account_nln.gif")));

			imageRegistry.put(
				IMG_BUDDY_OFFLINE,
				ImageDescriptor.createFromURL(
					newURL("file:icons/buddy_offline.gif")));

			imageRegistry.put(
				IMG_BUDDY_ONLINE,
				ImageDescriptor.createFromURL(
					newURL("file:icons/buddy_online.gif")));

			imageRegistry.put(
				IMG_VIEW_BUDDY,
				ImageDescriptor.createFromURL(
					newURL("file:icons/account.gif")));

			imageRegistry.put(
				IMG_VIEW_SESSION,
				ImageDescriptor.createFromURL(newURL("file:icons/v_conv.gif")));

			imageRegistry.put(
				IMG_VIEW_TABLE,
				ImageDescriptor.createFromURL(
					newURL("file:icons/v_table.gif")));

			imageRegistry.put(
				IMG_LOGO,
				ImageDescriptor.createFromURL(
					newURL("file:icons/eimp_logo4.gif")));
			imageRegistry.put(
							IMG_LOGO2,
							ImageDescriptor.createFromURL(
								newURL("file:icons/eimp_logo2.gif")));
			imageRegistry.put(
					IMG_HIDE_OFFLINE,
					ImageDescriptor.createFromURL(
						newURL("file:icons/hide_offline.gif")));
		}
		return imageRegistry;
	}

	public static URL newURL(String url_name) {
		try {
			return new URL(url_name);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL " + url_name+e);
		}
	}
}
