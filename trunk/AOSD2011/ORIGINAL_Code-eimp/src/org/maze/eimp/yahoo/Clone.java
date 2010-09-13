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

package org.maze.eimp.yahoo;

import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.UserStatus;

import ymsg.network.StatusConstants;
import ymsg.network.YahooUser;

/**
 * util class
 */
public class Clone {

	public static long statusToYahoo(String s) {
		if (s.equals(UserStatus.ONLINE))
			return StatusConstants.STATUS_AVAILABLE;
		if (s.equals(UserStatus.OFFLINE))
			return StatusConstants.STATUS_OFFLINE;
		if (s.equals(UserStatus.AWAY_FROM_COMPUTER))
			return StatusConstants.STATUS_IDLE;
		if (s.equals(UserStatus.INVISIBLE))
			return StatusConstants.STATUS_INVISIBLE;
		return StatusConstants.STATUS_OFFLINE;
	}

	public static String statusToIM(long s) {
		if (s == StatusConstants.STATUS_AVAILABLE)
			return UserStatus.ONLINE;
		if (s == StatusConstants.STATUS_OFFLINE)
			return UserStatus.OFFLINE;
		if (s == StatusConstants.STATUS_INVISIBLE)
			return UserStatus.INVISIBLE;

		return UserStatus.AWAY_FROM_COMPUTER;

	}
	
	public static Buddy buddyToIm(YahooUser y){
		Buddy b=new Buddy(y.getId());
		b.setStatus(statusToIM(y.getStatus()));
		return b;
	}

}
