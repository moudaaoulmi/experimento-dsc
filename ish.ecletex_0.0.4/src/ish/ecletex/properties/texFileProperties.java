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

public class texFileProperties extends PropertyPage {

	public static final String CHECKED = "checked";
	public static final String UNCHECKED = "unchecked";

	public static final String DEFAULT_ALTERNATE_SUPPORT = UNCHECKED;

	public static final String DEFAULT_DICTIONARY = "english";
	public static final String DICTIONARY_PROPERTY = "DICTIONARY";
	public static final String ALTERNATE_SUPPORT = "ALTERNATE";

	private Combo language_combo;

	private static final int TEXT_FIELD_WIDTH = 50;

	private Text ownerText;
	private Button alternateChecked;

	public texFileProperties() {
		super();
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
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
		addLanguageOption(composite);
		addSeparator(composite);
		addAlternateOption(composite);

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
	}

	public boolean performOk() {
		// store the value in the owner text field
		if (alternateChecked.getSelection()) {
			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"ish.ecletex", ALTERNATE_SUPPORT), CHECKED);
		} else {
			((IResource) getElement()).setPersistentProperty(new QualifiedName(
					"ish.ecletex", ALTERNATE_SUPPORT), UNCHECKED);
		}

		((IResource) getElement()).setPersistentProperty(new QualifiedName(
				"ish.ecletex", DICTIONARY_PROPERTY), language_combo
				.getItem(language_combo.getSelectionIndex()));

		return true;
	}

	private void addLanguageOption(Composite parent) {
		Composite composite = createDefaultComposite(parent);
		Label languageLabel = new Label(composite, SWT.NONE);
		languageLabel.setText("&Language for Spell Checker:");

		language_combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(25);
		language_combo.setLayoutData(gd);
		String[] dictionaries = ecletexPlugin.listDictionaries();
		String selected_dictionary = "";
		selected_dictionary = internalAddLanguageOption(selected_dictionary);
		System.out.println("Selected Dictionary is: " + selected_dictionary);
		int selected_index = -1;

		for (int i = 0; i < dictionaries.length; i++) {
			if (dictionaries[i].equals(selected_dictionary)) {
				selected_index = i;
				System.out.println("Found Default Dictionaty: "
						+ selected_dictionary);
				break;
			}
		}

		language_combo.setItems(dictionaries);
		language_combo.select(selected_index);

	}

	private String internalAddLanguageOption(String selected_dictionary) {
		selected_dictionary = ((IResource) getElement())
				.getPersistentProperty(new QualifiedName("ish.ecletex",
						DICTIONARY_PROPERTY));

		if (selected_dictionary == null)
			selected_dictionary = DEFAULT_DICTIONARY;

		return selected_dictionary;
	}

	private void addAlternateOption(Composite parent) {
		alternateChecked = new Button(parent, SWT.CHECK);
		alternateChecked.setText("&Enable Alternate Language Support");
		internalAddAlternateOption();
	}

	private void internalAddAlternateOption() {
		String checked = ((IResource) getElement())
				.getPersistentProperty(new QualifiedName("ish.ecletex",
						ALTERNATE_SUPPORT));
		if (checked == null) {
			if (DEFAULT_ALTERNATE_SUPPORT.equals(CHECKED)) {
				alternateChecked.setSelection(true);
			} else {
				alternateChecked.setSelection(false);
			}
		} else if (checked.equals(CHECKED)) {
			alternateChecked.setSelection(true);
		} else {
			alternateChecked.setSelection(false);
		}
	}

}