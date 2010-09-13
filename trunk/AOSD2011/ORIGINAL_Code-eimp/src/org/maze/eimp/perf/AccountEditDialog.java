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

package org.maze.eimp.perf;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.maze.eimp.EimpConsts;
import org.maze.eimp.im.Account;

/**
 * @author loya
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AccountEditDialog extends Dialog {
	private Text textAccountName = null;
	private Account acc = null;
	private String oldName = null;
	private Text textLoginID = null;
	private Text textPasswd = null;
	private Combo comboProtocol = null;

	/**
	 * @param parentShell
	 */
	protected AccountEditDialog(Shell parentShell) {
		super(parentShell);
	}

	public Account getAccount() {
		return acc;
		//acc.
	}

	public void setAccount(Account accIn) {
		acc = accIn;
	}

	private void initValues() {
		if (acc != null) {
			textAccountName.setText(acc.getName());
			oldName = acc.getName();
			textLoginID.setText(acc.getLoginid());
			textPasswd.setText(acc.getPassword());
			if (acc.getType().equals(EimpConsts.IMP_ICQ)) {
				comboProtocol.select(1);
			}
			if (acc.getType().equals(EimpConsts.IMP_YAHOO)) {
				comboProtocol.select(2);
			}
			if (acc.getType().equals(EimpConsts.IMP_AIM)) {
				comboProtocol.select(3);
			}
			if (acc.getType().equals(EimpConsts.IMP_RENDEZVOUS)) {
				comboProtocol.select(4);
			}
			if (acc.getType().equals(EimpConsts.IMP_JABBER)) {
							comboProtocol.select(5);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite top = new Composite(parent, SWT.LEFT);
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		top.setLayout(gridLayout);

		//Account Name
		Label labelAccountName = new Label(top, SWT.NONE);
		labelAccountName.setText(PreferenceMessages.getString("AccountEditDialog.AccountShowName")); //$NON-NLS-1$

		textAccountName = new Text(top, SWT.SINGLE | SWT.BORDER);
		GridData tl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		tl.grabExcessHorizontalSpace = true;
		tl.widthHint = 200;
		textAccountName.setLayoutData(tl);

		Label labelSep = new Label(top, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		sl.grabExcessHorizontalSpace = true;
		sl.horizontalSpan = 2;
		labelSep.setLayoutData(sl);

		//LoginID
		Label labelLoginId = new Label(top, SWT.NONE);
		labelLoginId.setText(PreferenceMessages.getString("AccountEditDialog.LoginID")); //$NON-NLS-1$

		textLoginID = new Text(top, SWT.SINGLE | SWT.BORDER);
		GridData ll = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		ll.grabExcessHorizontalSpace = true;
		textLoginID.setLayoutData(ll);

		//Password
		Label labelPasswd = new Label(top, SWT.NONE);
		labelPasswd.setText(PreferenceMessages.getString("AccountEditDialog.Password")); //$NON-NLS-1$

		textPasswd = new Text(top, SWT.SINGLE | SWT.BORDER);
		GridData pl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		pl.grabExcessHorizontalSpace = true;
		textPasswd.setLayoutData(pl);
		textPasswd.setEchoChar('*');

		//Protocol
		Label labelProtocol = new Label(top, SWT.NONE);
		labelProtocol.setText(PreferenceMessages.getString("AccountEditDialog.Protocol")); //$NON-NLS-1$

		comboProtocol = new Combo(top, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData cl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		cl.grabExcessHorizontalSpace = true;
		comboProtocol.setLayoutData(cl);
		comboProtocol.add(EimpConsts.IMP_MSN, 0);
		comboProtocol.add(EimpConsts.IMP_ICQ, 1);
		comboProtocol.add(EimpConsts.IMP_YAHOO, 2);
		comboProtocol.add(EimpConsts.IMP_AIM, 3);
		comboProtocol.add(EimpConsts.IMP_RENDEZVOUS, 4);
		comboProtocol.add(EimpConsts.IMP_JABBER, 5);
		comboProtocol.select(0);

		initValues();

		return top;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		if (acc == null)
			acc = new Account(textAccountName.getText());
		else {
			if (!acc.getName().equals(textAccountName.getText())) {
				acc = null;
				acc = new Account(textAccountName.getText());
			}
		}
		acc.setLoginid(textLoginID.getText());
		acc.setPassword(textPasswd.getText());
		acc.setType(comboProtocol.getText());
		super.okPressed();
	}

}
