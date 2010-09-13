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

package org.maze.eimp.views;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * A window to notice user a buddy send message.
 * @author hliu
 */
public class InfoNotifyWindow {

	private Shell shell = null;
	
	///the text to show
	private String text = "Tips";
	private boolean isAllowAutoClose = true;
	private Label lblText=null;
	private boolean clickToClose=false;
	private boolean created=false;
	private int autoHideDelaySecond=10;
	private boolean allowPopup=true;

	public InfoNotifyWindow(Shell parent) {
		shell = new Shell(parent.getDisplay(), SWT.ON_TOP);
	}
	
	public void setAutoHideDelaySecond(int n){
		autoHideDelaySecond=n;
	}
	
	/**
	 * if b=true allow auto close window.
	 * @param b
	 */
	public void setAllowAutoClose(boolean b){
		isAllowAutoClose=b;
	}
	
	
	private void createContent() {
		lblText = new Label(shell, SWT.NONE);
		lblText.setText(text);
		Color bg = new Color(shell.getDisplay(), 255, 255, 96);
		lblText.setBackground(bg);
		rePack();
		lblText.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent arg0) {}
			public void mouseDown(MouseEvent arg0) {}
			public void mouseUp(MouseEvent arg0) {
				hide();
			}
		});
	}
	
	private void rePack() {
		shell.pack();
		Rectangle rl = shell.getDisplay().getClientArea();
		Rectangle srl = shell.getBounds();
		shell.setLocation(rl.width - srl.width, rl.height - srl.height);
	}

	private void open() {
		shell.setLayout(new FillLayout());
		createContent();
		shell.open();
		created=true;
	}
	
	private void setAutoHide(){
		if(!isAllowAutoClose)return;
		Timer timer=new Timer();
		TimerTask task=new TimerTask() {
			public void run() {
				shell.getDisplay().syncExec(new Runnable(){
					public void run() {
						hide();
					}});
			}
		};
		timer.schedule(task,autoHideDelaySecond*1000);
	}
	
	/**
	 * hide the window
	 *
	 */
	public void hide(){
		if(shell.isVisible()){
			shell.setVisible(false);
		}
	}
	
	/**
	 * show the windows
	 */
	public void show(){
		
		if(!allowPopup)return;
		
		if(created==false){
			open();
		}
		if(lblText!=null)lblText.setText(text);
		if(!shell.isVisible()){
			shell.setVisible(true);
			rePack();
			shell.forceActive();
		}
		setAutoHide();
	}
	
	/**
	 * get the shell 
	 * @return
	 */
	public Shell getShell() {
		return shell;
	}
	
	/**
	 * close tips window
	 */
	public void close() {
		if(!shell.isDisposed())	shell.close();
	}

	/**
	 * set the information to show in the window
	 * @param string
	 */
	public void setText(String string) {
		text = string;
		shell.setText(string);
	}

	/**
	 * @return
	 */
	public boolean isAllowPopup() {
		return allowPopup;
	}

	/**
	 * @param allowPopup
	 */
	public void setAllowPopup(boolean allowPopup) {
		this.allowPopup = allowPopup;
	}

}
