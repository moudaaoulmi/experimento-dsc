package ish.ecletex.properties;

import ish.ecletex.ecletexPlugin;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class ecletexProjectProperties extends PropertyPage {

	public static final String CHECKED = "checked";
	public static final String UNCHECKED = "unchecked";

	private static final String PATH_TITLE = "Path:";
	private static final String MAINFILE_TITLE = "&Main Tex File:";
	public static final String MAINFILE_PROPERTY = "OWNER";
	public static final String BIBTEX_COMPLIER_ACTION_PROPERTY = "BIBCOMPILER";
	public static final String PS_ACTION_PROPERTY = "PSACTION";
	public static final String PDF_ACTION_PROPERTY = "PDFACTION";
	public static final String DEFAULT_BIBTEX_COMPLIER_ACTION = CHECKED;
	public static final String DEFAULT_PS_ACTION = CHECKED;
	public static final String DEFAULT_PDF_ACTION = CHECKED;
	private static final String DEFAULT_FILE = "main.tex";

	public static final String PDF_TARGET_PROPERTY = "PDFTARGET";

	public static final String DICTIONARY_PROPERTY = "DICTIONARY";
	public static final String ALTERNATE_SUPPORT = "ALTERNATE";

	private static final int TEXT_FIELD_WIDTH = 50;

	private Text ownerText;
	private Button bibtexCheck;
	private Button psChecked;
	private Button pdfChecked;

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public ecletexProjectProperties() {
		super();
	}

	private void addFirstSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for path field
		Label pathLabel = new Label(composite, SWT.NONE);
		pathLabel.setText(PATH_TITLE);

		// Path text field
		Text pathValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
		pathValueText.setText(((IResource) getElement()).getFullPath()
				.toString());
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	private void addSecondSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText(MAINFILE_TITLE);

		// Owner text field
		ownerText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		ownerText.setLayoutData(gd);

		// Populate owner text field
		internalAddSecondSection();
	}

	private void internalAddSecondSection() {
		String owner = ((IResource) getElement())
				.getPersistentProperty(new QualifiedName("ish.ecletex",
						MAINFILE_PROPERTY));
		ownerText.setText((owner != null) ? owner : DEFAULT_FILE);
	}

	private void addBibtexOption(Composite parent) {
		bibtexCheck = new Button(parent, SWT.CHECK);
		bibtexCheck.setText("&Compile Bibtex");

		internalAddBibtexOption();
	}

	private void internalAddBibtexOption() {
		String checked = ((IResource) getElement())
				.getPersistentProperty(new QualifiedName("ish.ecletex",
						BIBTEX_COMPLIER_ACTION_PROPERTY));
		if (checked == null) {
			if (DEFAULT_BIBTEX_COMPLIER_ACTION.equals(CHECKED)) {
				bibtexCheck.setSelection(true);
			} else {
				bibtexCheck.setSelection(false);
			}
		} else if (checked.equals(CHECKED)) {
			bibtexCheck.setSelection(true);
		} else {
			bibtexCheck.setSelection(false);
		}
	}

	private void addPSOption(Composite parent) {
		psChecked = new Button(parent, SWT.CHECK);
		psChecked.setText("&Build Postscript");
		internalAddPSOption();
	}

	private void internalAddPSOption() {
		String checked = ((IResource) getElement())
				.getPersistentProperty(new QualifiedName("ish.ecletex",
						PS_ACTION_PROPERTY));
		if (checked == null) {
			if (DEFAULT_PS_ACTION.equals(CHECKED)) {
				psChecked.setSelection(true);
			} else {
				psChecked.setSelection(false);
			}
		} else if (checked.equals(CHECKED)) {
			psChecked.setSelection(true);
		} else {
			psChecked.setSelection(false);
		}
	}

	private void addPDFOption(Composite parent) {
		pdfChecked = new Button(parent, SWT.CHECK);
		pdfChecked.setText("&Build PDF");
		internalAddPDFOption();
	}

	private void internalAddPDFOption() {
		String checked = ((IResource) getElement())
				.getPersistentProperty(new QualifiedName("ish.ecletex",
						PDF_ACTION_PROPERTY));
		if (checked == null) {
			if (DEFAULT_PDF_ACTION.equals(CHECKED)) {
				pdfChecked.setSelection(true);
			} else {
				pdfChecked.setSelection(false);
			}
		} else if (checked.equals(CHECKED)) {
			pdfChecked.setSelection(true);
		} else {
			pdfChecked.setSelection(false);
		}
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		addFirstSection(composite);
		addSeparator(composite);
		addSecondSection(composite);
		addSeparator(composite);
		addBibtexOption(composite);
		addPSOption(composite);
		// addPDFOption(composite);
		addSeparator(composite);
		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected void performDefaults() {
		// Populate the owner text field with the default value
		ownerText.setText(DEFAULT_FILE);
	}

	public boolean performOk() {
		// store the value in the owner text field
		((IResource) getElement()).setPersistentProperty(new QualifiedName(
				"ish.ecletex", MAINFILE_PROPERTY), ownerText.getText());

		if (bibtexCheck.getSelection()) {
			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"ish.ecletex", BIBTEX_COMPLIER_ACTION_PROPERTY), CHECKED);
		} else {
			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"ish.ecletex", BIBTEX_COMPLIER_ACTION_PROPERTY), UNCHECKED);
		}

		if (psChecked.getSelection()) {
			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"ish.ecletex", PS_ACTION_PROPERTY), CHECKED);
		} else {
			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"ish.ecletex", PS_ACTION_PROPERTY), UNCHECKED);
		}
		return true;
	}

}