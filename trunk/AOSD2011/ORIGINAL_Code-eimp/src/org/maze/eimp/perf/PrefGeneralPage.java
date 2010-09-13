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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.maze.eimp.EimpConsts;

/**
 * for the general preferences
 */
public class PrefGeneralPage
	extends FieldEditorPreferencePage {

	public PrefGeneralPage() {
		super(FieldEditorPreferencePage.GRID);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {

		BooleanFieldEditor systryEnable =
			new BooleanFieldEditor(
				EimpConsts.STO_USE_SYSTRAY,
				PreferenceMessages.getString("PrefGeneralPage.enable_systray"), //$NON-NLS-1$
				getFieldEditorParent());
		addField(systryEnable);

		BooleanFieldEditor notiferEnable =
			new BooleanFieldEditor(
				EimpConsts.STO_NOTIFER_SHOW,
				PreferenceMessages.getString("PrefGeneralPage.enable_notice_window"), //$NON-NLS-1$
				getFieldEditorParent());
		addField(notiferEnable);

		IntegerFieldEditor notifDelayTime =
			new IntegerFieldEditor(
				EimpConsts.STO_NOTIFER_DELAY_TIME,
				PreferenceMessages.getString("PrefGeneralPage.notice_windows_show_time"), //$NON-NLS-1$
				getFieldEditorParent());
		addField(notifDelayTime);

		BooleanFieldEditor cmdServerEnable =
			new BooleanFieldEditor(
				EimpConsts.STO_CMDSERVER_ENABLE,
				PreferenceMessages.getString("PrefGeneralPage.enable_im_server"), //$NON-NLS-1$
				getFieldEditorParent());
		addField(cmdServerEnable);
		
		StringFieldEditor passwdCmdServer =
			new StringFieldEditor(
				EimpConsts.STO_CMDSERVER_PASSWD,
				PreferenceMessages.getString("PrefGeneralPage.im_server_password"), //$NON-NLS-1$
				getFieldEditorParent());
		passwdCmdServer.getTextControl(getFieldEditorParent()).setEchoChar('*');
		addField(passwdCmdServer);

	}

}
