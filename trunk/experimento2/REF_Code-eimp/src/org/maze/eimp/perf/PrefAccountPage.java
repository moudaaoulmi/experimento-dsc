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

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.maze.eimp.Environment;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.AccountList;

/**
 * Account preference page
 * 
 */
public class PrefAccountPage extends PreferencePage {
	private List accountList = null;
	private HashMap accMap = new HashMap();

	private void addAccount(Account acc) {
		if (!accMap.containsKey(acc.getName())) {
			accMap.put(acc.getName(), acc);
			accountList.add(acc.getName());
		} else {
			accMap.remove(acc.getName());
			accMap.put(acc.getName(), acc);
		}

	}

	private void removeAccount(Account acc) {
		if (accMap.containsKey(acc.getName())) {
			accMap.remove(acc.getName());
			accountList.remove(acc.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite top = new Composite(parent, SWT.LEFT);
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		top.setLayout(gridLayout);

		accountList = new List(top, SWT.BORDER);
		GridData t =
			new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		t.grabExcessHorizontalSpace = true;
		t.grabExcessVerticalSpace = true;
		t.verticalSpan = 3;
		accountList.setLayoutData(t);
		accountList.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent arg0) {
				editSelectAccount();
			}
		});

		Button addBtn = new Button(top, SWT.PUSH);
		addBtn.setText(PreferenceMessages.getString("PrefAccountPage.Add")); //$NON-NLS-1$
		addBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				AccountEditDialog dialog = new AccountEditDialog(getShell());
				int r = dialog.open();
				if (r == AccountEditDialog.OK) {
					Account acc = dialog.getAccount();
					addAccount(acc);
				}
			}
		});
		GridData atl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		atl.heightHint = 25;
		atl.widthHint = 70;
		addBtn.setLayoutData(atl);

		Button editBtn = new Button(top, SWT.PUSH);
		editBtn.setText(PreferenceMessages.getString("PrefAccountPage.Edit")); //$NON-NLS-1$
		editBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				editSelectAccount();
			}
		});
		GridData etl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		etl.heightHint = 25;
		etl.widthHint = 70;
		editBtn.setLayoutData(etl);

		Button removeBtn = new Button(top, SWT.PUSH);
		removeBtn.setText(PreferenceMessages.getString("PrefAccountPage.Remove")); //$NON-NLS-1$
		GridData rtl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		rtl.verticalAlignment = GridData.BEGINNING;
		rtl.heightHint = 25;
		rtl.widthHint = 70;
		removeBtn.setLayoutData(rtl);
		removeBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				if (accountList.getSelectionCount() > 0) {
					String list[] = accountList.getSelection();
					for (int i = 0; i < list.length; i++) {
						removeAccount((Account) accMap.get(list[i]));
					}
				}
			}
		});

		loadPerf();

		return top;
	}

	private void loadPerf() {
		AccountList al = Environment.getInstance().loadAccounts();
		for (Iterator i = al.iterator(); i.hasNext();) {
			Account e = (Account) i.next();
			addAccount(e);
		}
	}

	private void editSelectAccount() {
		if (accountList.getSelectionCount() > 0);
		Account accIn = (Account) accMap.get(accountList.getSelection()[0]);
		AccountEditDialog dialog = new AccountEditDialog(getShell());
		dialog.setAccount(accIn);
		int r = dialog.open();
		if (r == AccountEditDialog.OK) {
			Account acc = dialog.getAccount();
			addAccount(acc);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		AccountList al = new AccountList();
		for (Iterator i = accMap.values().iterator(); i.hasNext();) {
			Account element = (Account) i.next();
			al.add(element);
		}
		Environment.getInstance().saveAccounts(al);
		return super.performOk();
	}

}
