/*
 * Created on 20-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.wizards.export.pdf;

import ish.ecletex.properties.ecletexProjectProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PDFWizardPageOne extends WizardPage {

	private IProject project;
	private IPath mainfile;

	protected Text targetText;
	protected Composite composite;
	protected Combo methodCombo;

	public IPath GetTarget() {
		return new Path(targetText.getText());
	}

	public IPath GetMainFile() {
		return mainfile;
	}

	public String GetExportMethod() {
		switch (methodCombo.getSelectionIndex()) {
		case 0:
			return PDFExportMethods.GS;
		case 1:
			return PDFExportMethods.PDFTEX;
		case 2:
			return PDFExportMethods.PDFLATEX;
		case 3:
			return PDFExportMethods.PDFETEX;
		default:
			return PDFExportMethods.PDFETEX;
		}
	}

	public PDFWizardPageOne(IProject project, IPath mainfile) {
		super("page1");
		this.project = project;
		this.mainfile = mainfile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {

		GridLayout compositeLayout = new GridLayout(4, true);
		GridData twowide = new GridData();
		twowide.horizontalSpan = 1;

		GridData twowide1 = new GridData();
		twowide1.horizontalSpan = 3;
		twowide1.horizontalAlignment = SWT.FILL;

		GridData twowide2 = new GridData();
		twowide2.horizontalSpan = 1;

		GridData twowide3 = new GridData();
		twowide3.horizontalSpan = 3;
		twowide3.horizontalAlignment = SWT.FILL;

		GridData twowide4 = new GridData();
		twowide4.horizontalSpan = 1;

		GridData twowide5 = new GridData();
		twowide5.horizontalSpan = 2;
		twowide5.horizontalAlignment = SWT.FILL;

		GridData twowide6 = new GridData();
		twowide6.horizontalSpan = 1;

		GridData twowide7 = new GridData();
		twowide7.horizontalSpan = 1;

		GridData twowide8 = new GridData();
		twowide8.horizontalSpan = 3;
		twowide8.horizontalAlignment = SWT.FILL;

		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(compositeLayout);

		Label projectLabel = new Label(composite, SWT.LEFT);
		projectLabel.setText("&Selected Project:");
		projectLabel.setLayoutData(twowide);

		Text projectText = new Text(composite, SWT.BORDER);
		projectText.setText(project.getName());
		projectText.setEditable(false);
		projectText.setLayoutData(twowide1);

		Label mainfileLabel = new Label(composite, SWT.LEFT);
		mainfileLabel.setText("&Projects Main file:");
		mainfileLabel.setLayoutData(twowide2);

		Text mainfileText = new Text(composite, SWT.BORDER);
		mainfileText.setText(mainfile.toOSString());
		mainfileText.setLayoutData(twowide3);
		mainfileText.setEditable(true);

		addSeparator(composite);

		Label targetLabel = new Label(composite, SWT.LEFT);
		targetLabel.setText("&Target PDF:");
		targetLabel.setLayoutData(twowide4);
		String target = null;

		target = internalCreateControl(target);
		if (target == null)
			target = "";

		targetText = new Text(composite, SWT.BORDER);
		targetText.setText(target);
		targetText.setLayoutData(twowide5);

		Button browseButton = new Button(composite, SWT.FLAT);
		browseButton.setLayoutData(twowide6);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new BrowseSelectionListener());
		addSeparator(composite);
		Label methodLabel = new Label(composite, SWT.LEFT);
		methodLabel.setText("&Export Method:");
		methodLabel.setLayoutData(twowide7);

		methodCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		methodCombo.setLayoutData(twowide8);
		methodCombo.add("GhostScript (PS -> PDF)");
		methodCombo.add("PDFLatex");
		methodCombo.select(1);
		addSeparator(composite);
		setControl(composite);

	}

	private String internalCreateControl(String target) {
		target = project.getPersistentProperty(new QualifiedName("ish.ecletex",
				ecletexProjectProperties.PDF_TARGET_PROPERTY));
		return target;
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 4;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	private class BrowseSelectionListener implements SelectionListener {

		FileDialog fileChooser;

		public BrowseSelectionListener() {
			fileChooser = new FileDialog(composite.getShell(), SWT.SAVE);
			fileChooser.setFileName(targetText.getText());
			fileChooser.setFilterExtensions(new String[] { "pdf" });
		}

		public void widgetDefaultSelected(SelectionEvent e) {

		}

		public void widgetSelected(SelectionEvent e) {
			fileChooser.open();
			IPath path = new Path(fileChooser.getFilterPath());
			path = path.addTrailingSeparator();
			path = path.append(new Path(fileChooser.getFileName()));
			targetText.setText(path.toOSString());
			internalWidgetSelected(path);
		}

		private void internalWidgetSelected(IPath path) {
			project.setPersistentProperty(new QualifiedName("ish.ecletex",
					ecletexProjectProperties.PDF_TARGET_PROPERTY), path
					.toOSString());
		}
	}

}
