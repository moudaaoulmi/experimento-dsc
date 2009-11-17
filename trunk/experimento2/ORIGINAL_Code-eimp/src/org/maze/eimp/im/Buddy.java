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

package org.maze.eimp.im;


/**
 * @author hliu
 */
public class Buddy  implements UserStatus
	{
		private Integer groupIndex = new Integer(0);
		private String status = null;
		private String oldStatus = null;
		private String loginName = null;
		private String friendlyName = null;
		private String formatFriendlyName = null;
		
		private Account account=null;

		public Buddy( String loginName )
		{
			this( loginName, loginName );
		}

		public Buddy( String loginName, String friendlyName )
		{
			if(loginName==null)throw new IllegalArgumentException("loginName can't be null.");
			this.loginName = loginName;
			this.friendlyName = friendlyName;

			setStatus( UserStatus.OFFLINE );
		}

		public void setGroupIndex( int index )
		{
			this.groupIndex = new Integer(index);
		}

		public void setGroupIndex( Integer index )
		{
			this.groupIndex = index;
		}

		public Integer getGroupIndex()
		{
			return this.groupIndex;
		}

		public void setLoginName( String loginName )
		{
			this.loginName = loginName;
		}

		public String getLoginName()
		{
			return this.loginName;
		}

		public void setFriendlyName( String frName )
		{
			if(frName==null)return;
			this.friendlyName = frName;
			this.formatFriendlyName = null;
		}

		public String getFriendlyName()
		{
			return this.friendlyName;
		}
		
		public boolean isOnline(){
			if(status.equals(UserStatus.ONLINE))return true;
			else return false;
		}


		public void setStatus( String st )
		{
			if( st==null )
				st = UserStatus.OFFLINE;

			oldStatus = this.status;
			this.status = st;
		}

		/**
		 * get old status.
		 */
		public String getOldStatus()
		{
			return this.oldStatus;
		}

		public String getStatus()
		{
			return this.status;
		}

		public String toString()
		{
			return loginName + ":" + friendlyName + " (" + status + ")";
		}

		public boolean equals( Object o )
		{
			if( this==o )
				return true;
			if( o!=null && o instanceof Buddy )
				return loginName.equals( ((Buddy)o).loginName );
			return false;
		}

		public int hashCode()
		{
			return loginName.hashCode();
		}

		/**
		 * @return
		 */
		public Account getAccount() {
			return account;
		}

		/**
		 * @param account
		 */
		public void setAccount(Account account) {
			this.account = account;
		}

		/**
		 * @param oldStatus
		 */
		public void setOldStatus(String oldStatus) {
			this.oldStatus = oldStatus;			
		}

};
