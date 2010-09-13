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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.UserStatus;
import org.maze.eimp.util.ImageManager;

class BuddyListLabelProvider extends LabelProvider {
		public String getText(Object obj) {
			if (obj instanceof Buddy) {
				String lbl = ((Buddy) obj).getFriendlyName()+"("+((Buddy) obj).getStatus()+")";
				//				if (((Buddy) obj).isOnline()) {
				//					lbl = lbl + "(O)";
				//				}
				return lbl;
			}
			if (obj instanceof Account) {
				Account acc = (Account) obj;
				return acc.getName()
					+ "("
					+ acc.getConnection().getStatus()
					+ ")";
			}
			return "Error Node";
		}

		public Image getImage(Object obj) {
			if (obj instanceof Buddy) {
				if (((Buddy) obj).isOnline())
					return ImageManager.getImageRegistry().get(ImageManager.IMG_BUDDY_ONLINE);
				else
					return ImageManager.getImageRegistry().get(ImageManager.IMG_BUDDY_OFFLINE); 
			}

			if (obj instanceof Account) {
				Account acc = (Account) obj;
				String st = acc.getConnection().getStatus();
				if (st.equals(UserStatus.ONLINE))
					return ImageManager.getImageRegistry().get(ImageManager.IMG_ACC_ONLINE);
				if (st.equals(UserStatus.OFFLINE))
					return ImageManager.getImageRegistry().get(ImageManager.IMG_ACC_OFFLINE);
				if (st.equals(UserStatus.AWAY_FROM_COMPUTER))
					return ImageManager.getImageRegistry().get(ImageManager.IMG_ACC_AWAY);
				else
					return ImageManager.getImageRegistry().get(ImageManager.IMG_ACC_OFFLINE);
			}

			return ImageManager.getImageRegistry().get(ImageManager.IMG_ACC_OFFLINE);
		}
	}