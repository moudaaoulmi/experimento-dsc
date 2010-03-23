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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.graphics.RGB;
import org.maze.eimp.app.SysTrayHelper;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.AccountList;
import org.maze.eimp.sounds.EventSoundList;
import org.maze.eimp.sounds.Sound;
import org.maze.eimp.util.MessageArchiver;
import org.maze.eimp.util.StringEx;

/**
 * must set the following: pathHome pathLib logger perfStore
 *  
 */
public class Environment {

	private AccountList accountList;

	private static Environment instance;

	private MessageArchiver messageArchiver = null;

	private IPreferenceStore prefStore = null;

	public static final int APP_ECLIPSE = 0;
	public static final int APP_STANDALONE = 1;

	private int appType;

	private ILogger logger = null;

	private String pathHome = null;
	private String pathLib = null;

	private SysTrayHelper sysTrayHelper = null;

	/**
	 * @return
	 */
	public MessageArchiver getMessageArchiver() {
		if (messageArchiver == null)
			messageArchiver = new MessageArchiver();
		return messageArchiver;
	}

	/**
	 * @param messageArchiver
	 */
	public void setMessageArchiver(MessageArchiver messageArchiver) {
		this.messageArchiver = messageArchiver;
	}

	private Environment() {
		instance = null;
		accountList = null;
		appType = APP_ECLIPSE;
		if (messageArchiver == null)
			messageArchiver = new MessageArchiver();
	}

	public void init() {
		IPreferenceStore p = getPrefStore();
		p.setDefault(EimpConsts.STO_ACCOUNTS, "");
		p.setDefault(EimpConsts.STO_SOUNDS_BY_EVENTS, "");
		p.setDefault(EimpConsts.STO_CMDSERVER_ENABLE, false);
		p.setDefault(EimpConsts.STO_NOTIFER_DELAY_TIME, 10);
		p.setDefault(EimpConsts.STO_USE_SYSTRAY, false);
		p.setDefault(EimpConsts.STO_APP_TITLE, EimpConsts.DEF_STO_APP_TITLE);
		p.setDefault(EimpConsts.STO_NOTIFER_SHOW, false);
		p.setDefault(EimpConsts.STO_ALLWAYSONLINE_ENABLE, false);
		p.setDefault(EimpConsts.STO_CMDSERVER_PASSWD, "");
		PreferenceConverter.setDefault(p,EimpConsts.STO_TEXT_VIEW_BG,new RGB(0,0,0));
		PreferenceConverter.setDefault(p,EimpConsts.STO_TEXT_VIEW_FG,new RGB(255,255,255));
		
	}

	public AccountList getAccountList() {
		if (accountList == null) {
			accountList = loadAccounts();
		}
		//		if (accountList.size() > 0)
		//			accountList.clear();
		//
		//		Account ac = new Account("Default");
		//		ac.setLoginid(
		//			eimpPlugin.getDefault().getPreferenceStore().getString(
		//				eimpPlugin.PERF_USERID));
		//		ac.setPassword(
		//			eimpPlugin.getDefault().getPreferenceStore().getString(
		//				eimpPlugin.PERF_PASSWD));
		//		accountList.add(ac);
		//
		return accountList;
	}

	public void setAccountList(AccountList accountList) {
		this.accountList = accountList;
	}

	public static Environment getInstance() {
		if (instance == null) {
			instance = new Environment();
		}
		return instance;
	}

	/**
	 * save the accout list to prefStrore and sync the accountList
	 * @param al AccountList
	 */
	public void saveAccounts(AccountList al) {
		syncAccountList(al);
				getPrefStore().setValue(EimpConsts.STO_ACCOUNTS,accountlistToString(al));
	}

	/**
	 * Synchronize the account list with al. This method enable the changes to
	 * account immediately apply. But remove a account is not take effect until
	 * restart.
	 * 
	 * @param al
	 */
	public void syncAccountList(AccountList al) {
		AccountList acclist = getAccountList();
		for (Iterator i = al.iterator(); i.hasNext();) {
			Account acc = (Account) i.next();
			boolean found = false;
			for (Iterator j = acclist.iterator(); j.hasNext();) {
				Account sacc = (Account) j.next();
				if (sacc.getName().equals(acc.getName())) {
					//synchronize
					sacc.setLoginid(acc.getLoginid());
					sacc.setPassword(acc.getPassword());
					found = true;
				}
			}
			if (!found) {
				acclist.add(acc);
			}
		}

	}

	public AccountList loadAccounts() {
		AccountList accountList = new AccountList();
		//		String al =
		//			eimpPlugin.getDefault().getPreferenceStore().getString(
		//				EimpConsts.STO_ACCOUNTS);
		String al = prefStore.getString(EimpConsts.STO_ACCOUNTS);
		//StringReader a=new StringReader(al);
		ArrayList ala = StringEx.split(al, '|', '\\');
		if (ala.size() < 1)
			return accountList;
		for (Iterator i = ala.iterator(); i.hasNext();) {
			String e = (String) i.next();
			if (e.length() < 1)
				break;
			ArrayList ele = StringEx.split(e, ':', '\\');
			Account acc = new Account((String) ele.get(0));
			acc.setLoginid((String) ele.get(1));
			acc.setPassword((String) ele.get(2));
			acc.setType((String) ele.get(3));
			accountList.add(acc);
		}
		return accountList;

	}

	private String accountlistToString(AccountList al) {
		StringBuffer buf = new StringBuffer();
		for (Iterator i = al.iterator(); i.hasNext();) {
			Account e = (Account) i.next();
			buf.append(e.getName());
			buf.append(":");
			buf.append(e.getLoginid());
			buf.append(":");
			buf.append(e.getPassword());
			buf.append(":");
			buf.append(e.getType());
			if (i.hasNext())
				buf.append("|");
		}
		return buf.toString();
	}

	/**
	 * @return
	 */
	public int getAppType() {
		return appType;
	}

	/**
	 * @param appType
	 */
	public void setAppType(int appType) {
		this.appType = appType;
	}

	/**
	 * @return
	 */
	public IPreferenceStore getPrefStore() {
		if (prefStore == null)
			prefStore = new PreferenceStore(System.getProperty("user.home")+"/.eimp/appsetting.prop");
		return prefStore;
	}
	
	/**
		 * @return
		 */
		public void setPrefStore(IPreferenceStore store) {
			prefStore=store;
		}



	/**
	 * @return
	 */
	public ILogger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 */
	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	/**
	 * @return
	 */
	public String getPathHome() {
		return pathHome;
	}

	/**
	 * @param pathHome
	 */
	public void setPathHome(String pathHome) {
		this.pathHome = pathHome;
		if (pathLib == null)
			pathLib = pathHome + "/lib";
	}

	/**
	 * @return
	 */
	public String getPathLib() {
		return pathLib;
	}

	/**
	 * @param pathLib
	 */
	public void setPathLib(String pathLib) {
		this.pathLib = pathLib;
	}

	/**
	 * @return
	 */
	public SysTrayHelper getSysTrayHelper() {
		if (sysTrayHelper == null)
			sysTrayHelper = new SysTrayHelper();
		return sysTrayHelper;
	}
	public void saveSoundsByEvents() {
	getPrefStore().setValue(EimpConsts.STO_SOUNDS_BY_EVENTS, EventSoundList.getInstance().asString());
	}
	public void loadSoundsByEvents() {
		String sbe = prefStore.getString(EimpConsts.STO_SOUNDS_BY_EVENTS);
				ArrayList ala = StringEx.split(sbe, '|','\0');
		if (ala.size() < 1)
			return;
		for (Iterator i = ala.iterator(); i.hasNext();) {
			String e = (String) i.next();
						if (e.length() < 1)
				continue;
			ArrayList ele = StringEx.split(e, ';', '\0');
			Sound s = EventSoundList.getInstance().FindSound((String) ele.get(0));
			if (s == null)
				continue;
			if (ele.size() >= 1 )
				s.setFileSoundTitle((String) ele.get(1));
			if (ele.size() >= 2)
				s.setEnabled(ele.size() >= 2 );
		}
	}
}
