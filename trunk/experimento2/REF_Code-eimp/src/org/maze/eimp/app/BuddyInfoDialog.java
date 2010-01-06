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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.util.ImageManager;

/**
 * show the detail infomation of a buddy
 */
public class BuddyInfoDialog extends Dialog {
	private Buddy buddy;

	/**
	 * @param parentShell
	 */
	public BuddyInfoDialog(Shell parentShell) {
		super(parentShell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		//parent.setLayout(new FillLayout());
		this.getShell().setText("Buddy Detail");

		Composite root = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		root.setLayout(gridLayout);
		//root.setLayout(new GridLayout(SWT.VERTICAL));
		root.setLayoutData(new GridData(GridData.FILL_BOTH));

		//lb_sep.setText("Author:Loya Liu");

		Label lb_logo = new Label(root, SWT.NONE);
		lb_logo.setImage(
			ImageManager.getImageRegistry().get(ImageManager.IMG_LOGO));

		Composite infoPanel = new Composite(root, SWT.NONE);
		infoPanel.setLayoutData(
			new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		infoPanel.setLayout(new RowLayout(SWT.VERTICAL));

		Label lb_id = new Label(infoPanel, SWT.NONE);
		lb_id.setText(buddy.getLoginName());

		Label lb_sep = new Label(root, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sep_gd = new GridData(GridData.FILL_HORIZONTAL);
		sep_gd.horizontalSpan = 2;
		lb_sep.setLayoutData(sep_gd);

		//detail info about buddy
		Label lb_fname_t = new Label(root, SWT.NONE);
		lb_fname_t.setText("Friendly name:");

		Label lb_fname = new Label(root, SWT.NONE);
		lb_fname.setText(buddy.getLoginName());

		Label lb_status_t = new Label(root, SWT.NONE);
		lb_status_t.setText("Status:");

		Label lb_status = new Label(root, SWT.NONE);
		lb_status.setText(buddy.getStatus());

		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#getInitialSize()
	 */
	protected Point getInitialSize() {
		return new Point(400, 350);
	}

	/**
	 * @return Returns the buddy.
	 */
	public Buddy getBuddy() {
		return buddy;
	}

	/**
	 * @param buddy
	 *            The buddy to set.
	 */
	public void setBuddy(Buddy buddy) {
		this.buddy = buddy;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(
			parent,
			IDialogConstants.OK_ID,
			IDialogConstants.OK_LABEL,
			true);
	}

}
