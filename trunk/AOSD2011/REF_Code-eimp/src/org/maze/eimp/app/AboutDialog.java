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

import java.io.FileReader;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.maze.eimp.EimpConsts;
import org.maze.eimp.Environment;
import org.maze.eimp.util.ImageManager;

/**
 * About dialog
 */
public class AboutDialog extends Dialog {
	private final static String DESC = "This product includes the following software:\n" + //$NON-NLS-1$
			"Jaimlib(http://sourceforge.net/projects/jaimlib/)\n"
			+ //$NON-NLS-1$
			"JOscalLib(http://sourceforge.net/projects/ooimlib/)\n"
			+ //$NON-NLS-1$
			"ymsg9(http://sourceforge.net/projects/jymsg9/)\n"
			+ //$NON-NLS-1$
			"Java Rendezvous(http://www.strangeberry.com/java_rendevous.htm)\n"
			+ //$NON-NLS-1$
			"SysTray4j(http://sourceforge.net/projects/systray/)\n" + //$NON-NLS-1$
			"SWT/JFace(http://www.eclipse.org)"; //$NON-NLS-1$
	private static String descAboutme = null;

	/**
	 * @param parentShell
	 */
	public AboutDialog(Shell parentShell) {
		super(parentShell);
	}

	private void loadfile() {
		FileReader f = new FileReader("ABOUTME.TXT"); //$NON-NLS-1$
		char[] c;
		c = new char[20];
		StringBuffer buf = new StringBuffer();
		internalLoadFile(f, c, buf);
	}

	private void internalLoadFile(FileReader f, char[] c, StringBuffer buf) {
		int r = 0;
		do {
			r = f.read(c);
			if (r != -1 && r < 20)
				c[r] = '\0';
			buf.append(c);
		} while (r != -1);
		descAboutme = buf.toString();
		f.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		if (descAboutme == null)
			loadfile();
		// parent.setLayout(new FillLayout());
		this
				.getShell()
				.setText(
						Messages.getString("AboutDialog.About") + Environment.getInstance().getPrefStore().getString(EimpConsts.STO_APP_TITLE)); //$NON-NLS-1$

		Composite root = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		root.setLayout(gridLayout);
		// root.setLayout(new GridLayout(SWT.VERTICAL));
		root.setLayoutData(new GridData(GridData.FILL_BOTH));

		// lb_sep.setText("Author:Loya Liu");

		Label lb_logo = new Label(root, SWT.NONE);
		lb_logo.setImage(ImageManager.getImageRegistry().get(
				ImageManager.IMG_LOGO));

		Composite infoPanel = new Composite(root, SWT.NONE);
		infoPanel
				.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		infoPanel.setLayout(new RowLayout(SWT.VERTICAL));

		Label lb_appname = new Label(infoPanel, SWT.NONE);
		lb_appname.setText(Environment.getInstance().getPrefStore().getString(
				EimpConsts.STO_APP_TITLE));

		Label lb_emp = new Label(infoPanel, SWT.NONE);
		lb_emp.setText("\n"); //$NON-NLS-1$

		Label lb_version = new Label(infoPanel, SWT.NONE);
		lb_version.setText(Messages.getString("AboutDialog.Version")); //$NON-NLS-1$

		Label lb_author = new Label(infoPanel, SWT.NONE);
		lb_author.setText(Messages.getString("AboutDialog.Author")); //$NON-NLS-1$

		Label lb_sep = new Label(root, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sep_gd = new GridData(GridData.FILL_HORIZONTAL);
		sep_gd.horizontalSpan = 2;
		lb_sep.setLayoutData(sep_gd);

		Text tx_desc = new Text(root, SWT.MULTI | SWT.BORDER | SWT.WRAP);
		GridData desc_gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		desc_gd.horizontalSpan = 2;
		tx_desc.setLayoutData(desc_gd);
		tx_desc.setText(descAboutme);

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

}
