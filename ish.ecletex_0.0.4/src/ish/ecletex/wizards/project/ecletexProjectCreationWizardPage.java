/*
 * Created on 14-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.wizards.project;

import ish.ecletex.ecletexPlugin;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;



/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ecletexProjectCreationWizardPage extends WizardPage {
		private IStatus fCurrStatus;
	
		private boolean fPageVisible;
	
		private String fNameLabel;
		private String fProjectName;
		private String fMainName;
	
		private Text fTextControl;
		private Text fMainControl;
		private Combo language_combo;
	
	public ecletexProjectCreationWizardPage(int pageNumber) {
		super("page" + pageNumber); //$NON-NLS-1$
		fCurrStatus= createStatus(IStatus.OK, ""); //$NON-NLS-1$
		setTitle("Latex (ecleTeX) Project"); //$NON-NLS-1$
		setDescription("Setup the Latex Project"); //$NON-NLS-1$
		fNameLabel= "Project Name: "; //$NON-NLS-1$
		fProjectName= "LatexProject";		 //$NON-NLS-1$
		
	}
	
	private static IStatus createStatus(int severity, String message) {
		return new Status(severity, ecletexPlugin.getPluginId(), severity, message, null);
	}

	public String getName(){
		return fProjectName;
	}

	public String getMain(){
		return fMainName;
	}
	
	public String getLanguage(){
		return language_combo.getItem(language_combo.getSelectionIndex());
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
	Composite composite= new Composite(parent, SWT.NONE);
	GridLayout gd= new GridLayout();
	gd.numColumns= 2;
	composite.setLayout(gd);
		
	Label label= new Label(composite, SWT.LEFT);
	label.setText(fNameLabel);
	label.setLayoutData(new GridData());
		
	fTextControl= new Text(composite, SWT.SINGLE | SWT.BORDER);
	fTextControl.setText(fProjectName);
	fTextControl.addModifyListener(new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			if (!fTextControl.isDisposed()) {
				validateText(fTextControl.getText());
			}
		}
	});
	fTextControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	Label mainLabel = new Label(composite,SWT.LEFT);
	mainLabel.setText("Main .tex File: ");
	mainLabel.setLayoutData(new GridData());

	fMainControl= new Text(composite, SWT.SINGLE | SWT.BORDER);
	fMainControl.setText("main.tex");
	fMainControl.addModifyListener(new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			if (!fMainControl.isDisposed()) {
				validateMain(fMainControl.getText());
			}
		}
	});
	fMainControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));	
	addLanguageOption(composite);
		
	setControl(composite);
}
	
	private void addLanguageOption(Composite parent){
		Label languageLabel = new Label(parent, SWT.NONE);
		languageLabel.setText("&Language for main file:");
			
		language_combo = new Combo(parent,SWT.DROP_DOWN | SWT.READ_ONLY); 
		GridData gd = new GridData();
		initializeDialogUnits(languageLabel);
		gd.widthHint = convertWidthInCharsToPixels(25);
		language_combo.setLayoutData(gd);
		String[] dictionaries = ecletexPlugin.listDictionaries();
		String selected_dictionary="english";
		int selected_index=-1;
		
		for(int i=0;i<dictionaries.length;i++){
			if(dictionaries[i].equals(selected_dictionary)){
				selected_index = i;
				System.out.println("Found Default Dictionaty: "+selected_dictionary);
				break;
			}
		}
		
		language_combo.setItems(dictionaries);
		language_combo.select(selected_index);
	
	}

private void validateText(String text) {
	IWorkspace workspace= ResourcesPlugin.getWorkspace();
	IStatus status= workspace.validateName(text, IResource.PROJECT);
	if (status.isOK()) {
		if (workspace.getRoot().getProject(text).exists()) {
			status= createStatus(IStatus.ERROR,"A Project of that name already exists."); //$NON-NLS-1$
		}
	}	
	updateStatus(status);
		
	fProjectName= text;
}	

private void validateMain(String text) {
	IStatus status = null;
	if(!text.toLowerCase().endsWith(".tex")){
		status= createStatus(IStatus.ERROR,"A Project of that name already exists.");
	}else{
		createStatus(IStatus.OK,"File Name Okay");
	}

	updateStatus(status);
		
	fMainName= text;
}

	private void updateStatus(IStatus status) {
		fCurrStatus= status;
		setPageComplete(!status.matches(IStatus.ERROR));
		if (fPageVisible) {
			applyToStatusLine(this, status);
		}
	}

	private static void applyToStatusLine(DialogPage page, IStatus status) {
		String errorMessage= null;
		String warningMessage= null;
		String statusMessage= status.getMessage();
		if (statusMessage.length() > 0) {
			if (status.matches(IStatus.ERROR)) {
				errorMessage= statusMessage;
			} else if (!status.isOK()) {
				warningMessage= statusMessage;
			}
		}
		page.setErrorMessage(errorMessage);
		page.setMessage(warningMessage);
	}
	
	public String getMainFile(){
		
		return fMainControl.getText();
	}

	}
