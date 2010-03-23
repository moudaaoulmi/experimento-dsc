package ish.ecletex.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.ScrolledFormText;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.editors.tex.TeXColorConstants;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage </samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class TeXPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public static final String WORD_WRAP = "wordwrapPreference";

	public static final String WORD_WRAP_2 = "wordwrap2Preference";

	public static final String SPELL_CHECK = "spellcheckPreference";

	public static final String CHARACTER_FILTER = "charFilterPreference";

	public static final String CHARACTER_FILTER_SAVE = "charFilterSavePreference";
	
	public static final String TRIGGER_TIME = "triggerTimePreference";

	public TeXPreferencePage() {
		super(GRID);
		setPreferenceStore(ecletexPlugin.getDefault().getPreferenceStore());
		setDescription("ecleTeX");

		//initDefaults(getPreferenceStore());
	}

	public static void initDefaults(IPreferenceStore store) {
		store.setDefault(TRIGGER_TIME,500);
		store.setDefault(WORD_WRAP,false);
		store.setDefault(WORD_WRAP_2,false);
		store.setDefault(SPELL_CHECK,true);
		store.setDefault(CHARACTER_FILTER,false);
		store.setDefault(CHARACTER_FILTER_SAVE,false);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	

	public void createFieldEditors() {

		addField(new BooleanFieldEditor(WORD_WRAP, "&Word Wrap",
				getFieldEditorParent())); // Stephan Dlugosz 19.11.2003
		addField(new BooleanFieldEditor(WORD_WRAP_2, "&Experimental Word Wrap",
				getFieldEditorParent()));
		createNoteComposite(null,getFieldEditorParent(),"Warning","Beta word wrap. For testing only.");
		addField(new BooleanFieldEditor(SPELL_CHECK, "&Inline Spell Checking",
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(CHARACTER_FILTER,
				"&Character Translation - Loading", getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				CHARACTER_FILTER_SAVE,
				"&Character Translation - Saving (if you use '\\usepackage[ansinew]{inputenc}')",
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(TRIGGER_TIME,"Auto complete trigger delay.",getFieldEditorParent()));
		
	}

	public void init(IWorkbench workbench) {
		
	}
}