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

package org.maze.eimp.msn;

import org.maze.eimp.im.Buddy;
import org.maze.eimp.util.MessageUtil;

import rath.msnm.entity.MsnFriend;
import rath.msnm.msg.MimeMessage;

/**
 * @author hliu
 *
 * 
 */
public class Clone {

	public static MimeMessage MsgToMsn(org.maze.eimp.im.MimeMessage msg) {
		MimeMessage m = new MimeMessage();
		m.setMessage(msg.getMessageString());
		m.setKind(MimeMessage.KIND_MESSAGE);
		return m;
	}

	public static org.maze.eimp.im.MimeMessage MsgToIM(MimeMessage msg) {
		org.maze.eimp.im.MimeMessage m =
			new org.maze.eimp.im.MimeMessage(msg.getMessage());
		return m;

	}

	public static Buddy buddyToIM(MsnFriend f) {
		Buddy b = new Buddy(f.getLoginName());
		b.setFriendlyName(MessageUtil.fixBlank(f.getFriendlyName()));
		b.setStatus(f.getStatus());
		b.setOldStatus(f.getOldStatus());
		return b;
	}

	public static MsnFriend buddyToMSN(Buddy b) {
		MsnFriend f = new MsnFriend(b.getLoginName());
		f.setFriendlyName(b.getFriendlyName());
		f.setStatus(b.getStatus());
		return f;
	}
	


}
