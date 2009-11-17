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

import net.kano.joscar.snaccmd.FullUserInfo;

import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.UserStatus;

import JOscarLib.Management.Contact;
import JOscarLib.Setting.Enum.StatusModeEnum;

public class Clone {

	public static Buddy toBuddy(String uid) {
		Buddy r = new Buddy(uid);
		return r;
	}
	
	public static int toIcqStatus(String s){
		if(s.equals(UserStatus.ONLINE))return StatusModeEnum.ONLINE;
		if(s.equals(UserStatus.AWAY_FROM_COMPUTER))return StatusModeEnum.AWAY;
		if(s.equals(UserStatus.OFFLINE))return StatusModeEnum.OFFLINE;
		return StatusModeEnum.OFFLINE;
	}

	public static String toStatus(long icqStatus) {
		String r = null;
		if (FullUserInfo.ICQSTATUS_AWAY == icqStatus) {
			r = UserStatus.AWAY_FROM_COMPUTER;
		} else if (FullUserInfo.ICQSTATUS_INVISIBLE == icqStatus) {
			r = UserStatus.OFFLINE;
		} else if (FullUserInfo.ICQSTATUS_OCCUPIED == icqStatus) {
			r = UserStatus.BUSY;
		} else if (FullUserInfo.ICQSTATUS_DEFAULT == icqStatus) {
			r = UserStatus.ONLINE;
		} else if (FullUserInfo.ICQSTATUS_DND == icqStatus) {
			r = UserStatus.BUSY;
		} else if (FullUserInfo.ICQSTATUS_FFC == icqStatus) {
			r = UserStatus.ONLINE;
		} else if (FullUserInfo.ICQSTATUS_NA == icqStatus) {
			r = UserStatus.OFFLINE;
		} else {
			r = UserStatus.ONLINE;
			// TODO detect
		}
		return r;
	}
	
	public static Contact toContact(Buddy b){
		Contact c=new Contact();
		c.setContactId(b.getLoginName());
		c.setNickName(b.getFriendlyName());
		c.setCurrentStatus(new StatusModeEnum(toIcqStatus(b.getStatus())));
		//c.setIsInVisibleList(true);
		return c;
	}

}
