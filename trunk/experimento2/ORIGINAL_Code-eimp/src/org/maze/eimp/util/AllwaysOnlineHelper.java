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

import java.util.ArrayList;
import java.util.Iterator;

import org.maze.eimp.im.Account;
import org.maze.eimp.im.UserStatus;

/**
 * help a connection allways online 
 *
 **/
public class AllwaysOnlineHelper {
	
	private int sleepTime=30;
	private int waitConnectTime=120;
	private boolean stop=true;
	private int maxTryTimes=100;
	private ArrayList accountList;
	private boolean enable=false;
	
	public AllwaysOnlineHelper(){
		accountList=new ArrayList();
	}
	
	public void start(){
		if(!enable)return;
		stop=false;
		Thread t = new Thread(){
			public final void run(){
				while(!stop){
					try {
						try {
							sleep(sleepTime*1000);
						} catch (InterruptedException e) {}
						for(Iterator i=accountList.iterator();i.hasNext();){
							Account acc=(Account)i.next();
							if(acc.getConnection().getStatus().equals(UserStatus.OFFLINE)){
								acc.getConnection().logout();
								acc.getConnection().login();
								sleep(waitConnectTime*1000);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}			
			}
		};
		
		t.start();
		
	}
	
	public void stop(){
		stop=true;
	}
	
	public void logout(Account acc){
		if(accountList.contains(acc))accountList.remove(acc);
	}
	
	public void login(Account acc){
		if(accountList.contains(acc))return;
		accountList.add(acc);
	}

	/**
	 * @return Returns the enable.
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable The enable to set.
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
