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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.maze.eimp.EimpConsts;
import org.maze.eimp.Environment;
import org.maze.eimp.im.DressMessage;

/**
 * @author hliu
 *  
 */
public class ConvPanel implements IMultiSessionBoardProvider {

	private ArrayList sendListenerList;
	private Text convArea;
	private Text inputArea;
	private Button sendButton;
	private Composite root;

	/**
	 * @param parent
	 * @param style
	 */
	public ConvPanel() {
		//super(parent, style);
		convArea = null;
		inputArea = null;
		sendButton = null;
		sendListenerList = null;

	}

	public void create(Composite parent, int style) {
		root = new Composite(parent, style);
		GridLayout rl = new GridLayout();
		rl.numColumns = 2;
		rl.marginHeight = 0;
		rl.marginWidth = 0;
		//rl.type=SWT.VERTICAL;
		root.setLayout(rl);
		if (convArea == null) {
			convArea = new Text(root, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
			GridData dc = new GridData();
			dc.horizontalSpan = 2;
			//dc.grabExcessHorizontalSpace = true;
			dc.grabExcessVerticalSpace = true;
			dc.horizontalAlignment = GridData.FILL;
			dc.verticalAlignment = GridData.FILL;
			convArea.setLayoutData(dc);
			//convArea.
		}
		if (inputArea == null) {
			inputArea = new Text(root, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
			GridData di = new GridData(GridData.FILL_HORIZONTAL);
			//di.horizontalAlignment = GridData.FILL;
			di.grabExcessHorizontalSpace = true;
			inputArea.setLayoutData(di);
			inputArea.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == SWT.CR) {
						sendButtonClick();
					}
				}
			});
		}

		if (sendButton == null) {
			sendButton = new Button(root, SWT.PUSH | SWT.FLAT);
			sendButton.setText("Send");
			GridData ds = new GridData();
			ds.horizontalAlignment = GridData.END;
			sendButton.setLayoutData(ds);
			sendButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					sendButtonClick();
				}
			});

			//sendButton.
			//sendButton.setLayoutData(new RowData(40,100));
		}

		setTheAppearance();
		registerDisposedListener();
		registerPrefListener();
		
	}

	private void registerDisposedListener() {
		convArea.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				Color b = convArea.getBackground();
				if (!b.isDisposed())
					b.dispose();

				b = convArea.getForeground();
				if (!b.isDisposed())
					b.dispose();

			}
		});
	}

	private void registerPrefListener() {
		IPreferenceStore p = Environment.getInstance().getPrefStore();
		p.addPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(final PropertyChangeEvent e) {
				if (e.getProperty().equals(EimpConsts.STO_TEXT_VIEW_BG)
					|| e.getProperty().equals(EimpConsts.STO_TEXT_VIEW_FG)) {
					root.getShell().getDisplay().syncExec(new Runnable() {

						public void run() {
							setTheAppearance();
						}
					});
				}

			}
		});
	}

	private void setTheAppearance() {
		IPreferenceStore p = Environment.getInstance().getPrefStore();	
		
		RGB b = PreferenceConverter.getColor(p, EimpConsts.STO_TEXT_VIEW_BG);
		
		setConvAreaBG(b);

		RGB f = PreferenceConverter.getColor(p, EimpConsts.STO_TEXT_VIEW_FG);
		setConvAreaFG(f);

	}

	private void setConvAreaFG(RGB f) {
		Color cf = convArea.getForeground();
		Color ncf = new Color(root.getDisplay(), f);
		convArea.setForeground(ncf);
		if (!cf.isDisposed())
			cf.dispose();
	}

	private void setConvAreaBG(RGB b) {
		Color cb = convArea.getBackground();
		Color ncb = new Color(root.getDisplay(), b);
		convArea.setBackground(ncb);
		if (!cb.isDisposed())
			cb.dispose();
	}

	private void sendButtonClick() {
		//convArea.append();
		fireSend(inputArea.getText());
		inputArea.setText("");
		inputArea.setFocus();
	}
	private void fireSend(String msg) {
		for (Iterator i = sendListenerList.iterator(); i.hasNext();) {
			SendListener lsn = (SendListener) i.next();
			lsn.sendMessage(msg);
		}
	}
	public void addSendListener(SendListener lsn) {
		if (sendListenerList == null)
			sendListenerList = new ArrayList();
		sendListenerList.add(lsn);
	}

	public void removeSendListener(SendListener lsn) {
		if (sendListenerList != null)
			sendListenerList.remove(lsn);
	}

	public void append(final DressMessage msg) {
		if (convArea != null) {
			root.getDisplay().syncExec(new Runnable() {
				public void run() {
					if (msg.getBuddy() != null)
						convArea.append(
							msg.getBuddy().getFriendlyName() + ":\n");
					convArea.append(msg.getMessageString() + "\n");
				}
			});

		}
	}

	/**
	 * disable to send message;
	 *  
	 */
	public void disableInput() {
		inputArea.setEnabled(false);
		sendButton.setEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.views.IMultiSessionBoardProvider#getControl()
	 */
	public Composite getControl() {
		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.views.IMultiSessionBoardProvider#registerTextGlobalAction(org.maze.eimp.views.IGlobalTextActionManager)
	 */
	public void registerTextGlobalAction(IGlobalTextActionManager tax) {
		tax.addText(convArea);
		tax.addText(inputArea);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.maze.eimp.views.IMultiSessionBoardProvider#removeTextGlobalAction(org.maze.eimp.views.IGlobalTextActionManager)
	 */
	public void removeTextGlobalAction(IGlobalTextActionManager tax) {
		tax.removeText(convArea);
		tax.removeText(inputArea);

	}

}
