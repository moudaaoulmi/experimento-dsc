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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jface.preference.PreferenceStore;
import org.maze.eimp.EimpConsts;
import org.maze.eimp.Environment;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.AccountList;
import org.maze.eimp.script.ImCommandServer;

/**
 * Main class for IM
 */
public class Main {

	public static void main(String[] args) {
		// init env
		Environment.getInstance().setAppType(Environment.APP_STANDALONE);
		Environment.getInstance().setLogger(new SimpleLogger());
		Environment.getInstance().setPathHome(".");

		
		//PreferenceStore prefStore=new PreferenceStore(System.getProperty("user.home")+"/.eimp/appsetting.prop");
		File f = new File(System.getProperty("user.home")+"/.eimp/appsetting.prop");
		if(!f.exists())
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PreferenceStore prefStore=new PreferenceStore(f.getAbsolutePath());
		Environment.getInstance().setPrefStore(prefStore);
		loadPref(prefStore);
		
		Environment.getInstance().init();
		
		
		ImCommandServer svr = null;
		if (prefStore.getBoolean(EimpConsts.STO_CMDSERVER_ENABLE)) {
			svr = new ImCommandServer();
			AccountList al = Environment.getInstance().getAccountList();
			for (Iterator i = al.iterator(); i.hasNext();) {
				Account ac = (Account) i.next();
				svr.init(ac.getConnection());
			}
		}

		//Display d = new Display();
		SimpleIMWindow w = new SimpleIMWindow(null);
		w.setBlockOnOpen(true);
		w.open();
		//Display.getCurrent().dispose();
		
		savePref(prefStore);
		System.exit(0);
	}
	
	public static void loadPref(PreferenceStore p){
		try {
			p.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void savePref(PreferenceStore p){
			try {
				p.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	
	

}
