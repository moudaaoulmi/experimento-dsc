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
import java.util.Vector;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.maze.eimp.im.DressMessage;

/**
 * @author hliu
 *
 */
public class TableConvPanel	implements IMultiSessionBoardProvider {

	private ArrayList sendListenerList;
	private TableViewer convArea;
	//private Text convArea;
	private Text inputArea;
	private Button sendButton;
	private Vector msgList;
	private Table table;
	private Composite root;
	
	private String[] columnNames = new String[] { 
				"Name", 
				"Msg",
				"Time"
				};

	class IMTableProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return msgList.toArray();
		}
	}

	class MessageLabelProvider implements ITableLabelProvider {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			String r = "";

			if (element instanceof DressMessage) {
				DressMessage msg = (DressMessage) element;
				switch (columnIndex) {
					case 0 :
						if (msg.getBuddy() != null) {
							return msg.getBuddy().getFriendlyName();
						}
						break;
					case 1 :
						r = msg.getMessageString();
						break;
					case 2 :
						r = msg.getTime().toString();
						break;
					default :
						break;
				}

			}
			return r;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setFocus()
	 */
	public boolean setFocus() {
		return inputArea.setFocus();
	}

	/**
	 * @param parent
	 * @param style
	 */
	public TableConvPanel() {
		//super(parent, style);
		convArea = null;
		inputArea = null;
		sendButton = null;
		sendListenerList = null;
		msgList = new Vector();
		table = null;
		//root=new Composite();

	}

	private void createTableControl() {
		table = new Table(root, SWT.MULTI | SWT.V_SCROLL);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		GridData dc = new GridData();
		dc.horizontalSpan = 2;
		//dc.grabExcessHorizontalSpace = true;
		dc.grabExcessVerticalSpace = true;
		dc.horizontalAlignment = GridData.FILL;
		dc.verticalAlignment = GridData.FILL;
		table.setLayoutData(dc);

		TableColumn tcName = new TableColumn(table, SWT.LEFT, 0);
		tcName.setText("Name");
		tcName.setWidth(50);

		TableColumn tcMsg = new TableColumn(table, SWT.LEFT, 1);
		tcMsg.setText("Msg");
		tcMsg.setWidth(400);
		TableColumn tcTime = new TableColumn(table, SWT.LEFT, 2);
		tcTime.setText("Time");
		tcTime.setWidth(200);

	}

	public void create(Composite parent, int style) {
		root=new Composite(parent, style);
		GridLayout rl = new GridLayout();
		rl.numColumns = 2;
		rl.marginHeight = 0;
		rl.marginWidth = 0;
		rl.makeColumnsEqualWidth=false;
		//rl.type=SWT.VERTICAL;
		root.setLayout(rl);
		if (convArea == null) {
			//convArea = new TableViewer(this, SWT.V_SCROLL);
			createTableControl();

			convArea = new TableViewer(table);
			convArea.setUseHashlookup(true);
			convArea.setColumnProperties(columnNames);

			convArea.setContentProvider(new IMTableProvider());
			convArea.setLabelProvider(new MessageLabelProvider());
			convArea.setInput(null);

		}
		if (inputArea == null) {
			inputArea = new Text(root, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
			//inputArea.
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
					msgList.add(msg);
					convArea.add(msg);
					convArea.update(msg,null);
					convArea.reveal(msg);
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

	/* (non-Javadoc)
	 * @see org.maze.eimp.views.IMultiSessionBoardProvider#getControl()
	 */
	public Composite getControl() {
		return root;
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.views.IMultiSessionBoardProvider#registerTextGlobalAction(org.eclipse.ui.actions.TextActionHandler)
	 */
	public void registerTextGlobalAction(IGlobalTextActionManager tax) {
		tax.addText(inputArea);		
	}

	/* (non-Javadoc)
	 * @see org.maze.eimp.views.IMultiSessionBoardProvider#removeTextGlobalAction(org.eclipse.ui.actions.TextActionHandler)
	 */
	public void removeTextGlobalAction(IGlobalTextActionManager tax) {
		tax.removeText(inputArea);
	}

}
