/*
 * Created on 09-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.wizards.template;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.templateProvider;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class templateWizardPage extends WizardPage {
	private IStatus fCurrStatus;
	private boolean fPageVisible;
	
	Text nameBox;
	Combo categoryChooser;
	Text templateText;
	
	public String getName(){
		return nameBox.getText();
	}
	
	public String getCategory(){
		return  categoryChooser.getText();
	}
	
	public String getTemplate(){
		return templateText.getText();
	}

	
	
	public templateWizardPage(int pageNumber) {
		super("page" + pageNumber); //$NON-NLS-1$
		fCurrStatus= createStatus(IStatus.OK, ""); //$NON-NLS-1$
		setTitle("New Template Wizard"); //$NON-NLS-1$
		setDescription("Create a new Template"); //$NON-NLS-1$		
	}
	
	private static IStatus createStatus(int severity, String message) {
		return new Status(severity, ecletexPlugin.getPluginId(), severity, message, null);
	}

	public void createControl(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		GridLayout gd= new GridLayout();
		gd.numColumns= 4;
		composite.setLayout(gd);
		
		GridData threeWide = new GridData();
		threeWide.horizontalSpan =3;
		threeWide.grabExcessHorizontalSpace = true;
		
		GridData fourWide = new GridData();
		threeWide.horizontalSpan =4;

			
		Label namelabel= new Label(composite, SWT.LEFT);
		namelabel.setText("&Name: (no spaces)");
		namelabel.setLayoutData(new GridData());
		
		nameBox = new Text(composite,SWT.BORDER);

		nameBox.setLayoutData(threeWide);
		
		Label categoryLabel = new Label(composite,SWT.LEFT);
		categoryLabel.setText("&Choose Category (or add one)");
		categoryLabel.setLayoutData(new GridData());
		
		categoryChooser = new Combo(composite,SWT.DROP_DOWN);
		String[] categories = templateProvider.GetCategories();
		for(int i=0;i<categories.length;i++)categoryChooser.add(categories[i]);
		
		Text newCategoryBox = new Text(composite,SWT.BORDER);
		newCategoryBox.setLayoutData(new GridData(SWT.FILL));
		
		Button addCategoryButton = new Button(composite,SWT.FLAT);
		addCategoryButton.setText("Add");
		addCategoryButton.addSelectionListener(new AddSelectionListener(categoryChooser,newCategoryBox));
		
		
		Label templateTextLabel = new Label(composite,SWT.LEFT|SWT.FILL);
		templateTextLabel.setText("&Template");
		templateTextLabel.setLayoutData(fourWide);
		
		templateText = new Text(composite,SWT.MULTI| SWT.BORDER);
		GridData textData = new GridData(SWT.FILL,SWT.FILL,true,true);
		textData.horizontalSpan=4;
		templateText.setLayoutData(textData);
		
		
		

		setControl(composite);

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
	
	
	private class AddSelectionListener implements SelectionListener{		

		private Combo categoryCombo;
		private Text categoryText;
		
		public AddSelectionListener(Combo categoryCombo,Text categoryText){
			this.categoryCombo = categoryCombo;
			this.categoryText = categoryText;
		}
		
		
		public void widgetDefaultSelected(SelectionEvent e) {
			

		}

		public void widgetSelected(SelectionEvent e) {
			if(!templateProvider.TemplateCategoryExists(categoryText.getText().toLowerCase())){
				categoryCombo.add(categoryText.getText().toLowerCase());
				categoryCombo.select(categoryCombo.getItemCount()-1);
				categoryCombo.redraw();
			}else{
				System.out.println("Category: "+categoryText.getText().toLowerCase()+" Exists");
			}
		}
}

}




