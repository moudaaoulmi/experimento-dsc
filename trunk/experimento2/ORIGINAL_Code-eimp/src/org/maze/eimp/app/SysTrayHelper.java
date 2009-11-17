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

import org.maze.eimp.EimpConsts;
import org.maze.eimp.Environment;

import snoozesoft.systray4j.SysTrayMenu;
import snoozesoft.systray4j.SysTrayMenuAdapter;
import snoozesoft.systray4j.SysTrayMenuEvent;
import snoozesoft.systray4j.SysTrayMenuIcon;
import snoozesoft.systray4j.SysTrayMenuItem;

/**
 * Create a systay (win32 only now)
 */
public class SysTrayHelper {

	SimpleIMWindow w = null;
	SysTrayMenu menu = null;

	boolean isWindow = System.getProperty("os.name").startsWith("Windows");

	public SysTrayHelper() {
		create();
	}

	private boolean canSysTray() {
		if (Environment.getInstance().getPrefStore().getBoolean(EimpConsts.STO_USE_SYSTRAY))
			return true;
		else
			return false;
	}

	private void create() {
		if (!canSysTray())
			return;

		createMainMenu();
		createSubMenu();
	}

	private void createMainMenu() {
		String ti =
			Environment.getInstance().getPrefStore().getString(
				EimpConsts.STO_APP_TITLE);
		SysTrayMenuIcon iconMenu = new SysTrayMenuIcon("icons/eimp_logo");
		iconMenu.addSysTrayMenuListener(new SysTrayMenuAdapter() {
			public void menuItemSelected(SysTrayMenuEvent e) {
			}
			public void iconLeftDoubleClicked(SysTrayMenuEvent e){
				showWindow();
			}
		});
		menu = new SysTrayMenu(iconMenu, ti);

	}
	
	

	private void createSubMenu() {
		SysTrayMenuItem exit = new SysTrayMenuItem("&Exit");
		exit.addSysTrayMenuListener(new SysTrayMenuAdapter() {
			public void menuItemSelected(SysTrayMenuEvent e) {
				w.getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						w.close();
					}
				});

			}
		});
		menu.addItem(exit);

		SysTrayMenuItem t = new SysTrayMenuItem("&Show main window");
		t.addSysTrayMenuListener(new SysTrayMenuAdapter() {
			public void menuItemSelected(SysTrayMenuEvent e) {
				showWindow();
			}


		});
		menu.addItem(t);
	}

	private void showWindow() {
		w.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				w.getShell().setVisible(true);
				w.getShell().setMinimized(false);
				w.getShell().forceActive();
			}
		});
	}
	
	public void close() {
		if (!canSysTray())
			return;
		if(menu!=null)SysTrayMenu.dispose();
	}

	public void setSimpleIMWindow(SimpleIMWindow w) {
		this.w = w;
	}

}
