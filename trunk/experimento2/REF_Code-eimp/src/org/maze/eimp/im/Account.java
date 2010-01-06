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

import org.maze.eimp.EimpConsts;
import org.maze.eimp.Environment;
import org.maze.eimp.ProtocolManager;
import org.maze.eimp.eimpPlugin;

/**
 * @author hliu
 * @author Ringo De Smet
 */
public class Account {
	private String loginid;
	private String password;
	private String url;
	private String type;
	private String name;
	private Connection connection = null;

	public Account(String name) {
		this.name = name;
		loginid = null;
		password = null;
		type = EimpConsts.IMP_MSN;
	}

	public Connection getConnection() {
		if (connection == null) {
			if (Environment.getInstance().getAppType() == Environment.APP_STANDALONE) {
				internalGetConnection();
			} else {
				ProtocolManager protocolManager = eimpPlugin.getDefault()
						.getProtocolManager();
				connection = protocolManager.createConnectionFor(this);
			}
		}
		return connection;
	}

	private void internalGetConnection() {
		if (type.equals(EimpConsts.IMP_MSN)) {
			connection = (Connection) Class.forName(
					"org.maze.eimp.msn.MSNConnection").newInstance();
			connection.setAccount(this);
		}
		if (type.equals(EimpConsts.IMP_ICQ)) {
			connection = (Connection) Class.forName(
					"org.maze.eimp.icq.ICQConnection").newInstance();
			connection.setAccount(this);
		}
		if (type.equals(EimpConsts.IMP_YAHOO)) {
			connection = (Connection) Class.forName(
					"org.maze.eimp.yahoo.YahooConnection").newInstance();
			connection.setAccount(this);
		}
		if (type.equals(EimpConsts.IMP_AIM)) {
			connection = (Connection) Class.forName(
					"org.maze.eimp.aim.AIMConnection").newInstance();
			connection.setAccount(this);
		}
		if (type.equals(EimpConsts.IMP_RENDEZVOUS)) {
			connection = (Connection) Class.forName(
					"org.maze.eimp.rendezvous.RendezVousConnection")
					.newInstance();
			connection.setAccount(this);
		}
		if (type.equals(EimpConsts.IMP_JABBER)) {
			connection = (Connection) Class.forName(
					"org.maze.eimp.xmpp.JabberConnection").newInstance();
			connection.setAccount(this);
		}
	}

	public Account(String name, String id, String passwd) {
		this.name = name;
		loginid = id;
		password = passwd;
		type = EimpConsts.IMP_MSN;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @param string
	 */
	public void setUrl(String string) {
		url = string;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}
}