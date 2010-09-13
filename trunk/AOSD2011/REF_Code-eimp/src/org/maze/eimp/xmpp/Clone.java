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

package org.maze.eimp.xmpp;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.MimeMessage;
import org.maze.eimp.im.UserStatus;

public class Clone {

	public static Presence toJabberPresence(String s) {
		Presence p = null;
		if (s.equals(UserStatus.ONLINE)) {
			p = new Presence(Presence.Type.AVAILABLE);
			p.setStatus("Online");
		}
		if (s.equals(UserStatus.AWAY_FROM_COMPUTER)) {
			p = new Presence(Presence.Type.UNAVAILABLE);
			p.setStatus("Away");
		}
		if (s.equals(UserStatus.OFFLINE)) {
			p = new Presence(Presence.Type.UNAVAILABLE);
			p.setStatus("Offline");
		}
		return p;
	}

	public static MimeMessage toImMessage(Message m) {
		return new MimeMessage(m.getBody());
	}

	public static Buddy toBuddy(String name) {
		return new Buddy(name);
	}

	public static String toStatus(Presence p) {
		String r = null;
		if(p==null)return UserStatus.OFFLINE;
		
		if (p.getType() == Presence.Type.AVAILABLE) {
			r = UserStatus.ONLINE;
		} else {
			r = UserStatus.OFFLINE;
		}
		return r;
	}

}
