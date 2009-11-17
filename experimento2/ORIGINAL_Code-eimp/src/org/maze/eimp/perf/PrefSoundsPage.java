/*******************************************************************************
 * Copyright (c) 2004, Loya Liu
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
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.maze.eimp.Environment;
import org.maze.eimp.sounds.EventSoundList;
import org.maze.eimp.sounds.Sound;
/**
 * @author Vilmar
 * 
 * Sounds preference page
 *  
 */
public class PrefSoundsPage extends PreferencePage {
	private Button apply;
	private Button browse;
	private Button enableSound;
	private Text fileName;
	private Composite parent;
	private Button restore;
	private List soundList;
	private Sound[] sounds;
	private void clickApply() {
		int i = soundList.getSelectionIndex();
		if (i >= 0 && i < sounds.length) {
			Sound s = sounds[i];
			s.setEnabled(enableSound.getSelection());
			s.setFileSoundTitle(fileName.getText());
			s.play();
			refreshSelectedSound();
		}
	}
	private void clickBrowse() {
		FileDialog fd = new FileDialog(parent.getShell(), SWT.OPEN);
		fd.setText(PreferenceMessages
				.getString("PrefSoundsPage.ChooseASoundFile")); //$NON-NLS-1$
				fd.setFileName(fileName.getText());
		String path = fd.open();
		if (path != null) {
			fileName.setText(path);
		}
	}
	private void clickRestore() {
		int i = soundList.getSelectionIndex();
		if (i >= 0 && i < sounds.length) {
			Sound s = EventSoundList.getInstance().getList()[i].copy(); // the
			// original
			// sound
			sounds[i] = s;
			refreshSelectedSound();
		}
	}
	private void createApply(Composite top) {
		apply = new Button(top, SWT.PUSH);
		apply.setEnabled(false);
		apply.setText(PreferenceMessages.getString("PrefSoundsPage.Apply")); //$NON-NLS-1$
		apply.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				clickApply();
			}
		});
	}
	private void createBrowse(Composite top) {
		browse = new Button(top, SWT.PUSH);
		browse.setEnabled(false);
		browse.setText(PreferenceMessages.getString("PrefSoundsPage.Browse")); //$NON-NLS-1$
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				clickBrowse();
			}
		});
	}
	private void createEnableSound(Composite top) {
		//Label lb1 = new Label(top, SWT.SEPARATOR);
		enableSound = new Button(top, SWT.CHECK);
		GridData tl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		tl.horizontalSpan=2;
		enableSound.setLayoutData(tl);
		
		enableSound.setEnabled(false);
		enableSound.setText(PreferenceMessages
				.getString("PrefSoundsPage.Enabled")); //$NON-NLS-1$
		enableSound.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}
	private void createFileName(Composite top) {
		Label lb1 = new Label(top, SWT.NONE);
		lb1.setText(PreferenceMessages
				.getString("PrefSoundsPage.FileSoundTitle")); //$NON-NLS-1$
		GridData tl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		tl.horizontalSpan=2;
		lb1.setLayoutData(tl);
				//Label lb2= new Label(top, SWT.SEPARATOR);
		fileName = new Text(top, SWT.SINGLE | SWT.BORDER);
		GridData tl2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.GRAB_HORIZONTAL);
		fileName.setLayoutData(tl2);
		fileName.setEnabled(false);
	}
	private void createRestore(Composite top) {
		restore = new Button(top, SWT.PUSH);
		GridData tl = new GridData(GridData.HORIZONTAL_ALIGN_END);
		restore.setLayoutData(tl);
		restore.setText(PreferenceMessages.getString("PrefSoundsPage.Restore")); //$NON-NLS-1$
		restore.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				clickRestore();
			}
		});
	}
	private void createSoundList(Composite top) {
		Label lb1 = new Label(top, SWT.NONE);
		lb1.setText(PreferenceMessages.getString("PrefSoundsPage.Events")); //$NON-NLS-1$
		GridData tl = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		tl.horizontalSpan=2;
		lb1.setLayoutData(tl);
		
		soundList = new List(top, SWT.BORDER);
		GridData t = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL);
		t.horizontalSpan=2;
		//t.grabExcessHorizontalSpace = true;
		t.grabExcessVerticalSpace = true;
		t.verticalSpan = 3;
		soundList.setLayoutData(t);
		soundList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				refreshSelectedSound();
			}
		});
	}
	private void loadSounds() {
		sounds = EventSoundList.getInstance().getCopyEventSoundList();
		for (int i = 0; i < sounds.length; i++) {
			Sound s = sounds[i];
			soundList.add(s.getEventDescription());
		}
	}
	private void refreshSelectedSound() {
		int i = soundList.getSelectionIndex();
		if (i >= 0) {
			Sound s = sounds[i];
			enableSound.setEnabled(true);
			apply.setEnabled(true);
			fileName.setEnabled(true);
			browse.setEnabled(true);
			enableSound.setSelection(s.isEnabled());
			fileName.setText(s.getFileSoundTitle());
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		EventSoundList.getInstance().setList(sounds);
		Environment.getInstance().saveSoundsByEvents();
		return super.performOk();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		this.parent = parent;
		noDefaultAndApplyButton();
		Composite top = new Composite(parent, SWT.LEFT);
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		top.setLayout(gridLayout);
		createSoundList(top);
		createEnableSound(top);
		createFileName(top);
		createBrowse(top);
		
		createRestore(top);
		createApply(top);
		loadSounds();
		return top;
	}
}