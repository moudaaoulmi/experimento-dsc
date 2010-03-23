/*
 * Created on 10-Nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.preferences;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.editors.tex.TeXColorConstants;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Ian Hartney
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TexColorsPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public static final String COMMAND_COLOR = "commandColorPreference";
	public static final String COMMENT_COLOR = "commentColorPreference";
	public static final String ARGUMENT_COLOR = "argumentColorPreference";
	public static final String OPTIONAL_COLOR = "optionalColorPreference";
	public static final String TEXT_COLOR = "textColorPreference";
	public static final String INCORRECT_COLOR = "incorrectColorPreference";
	public static final String BIBTEX_ENTRY_COLOR = "bibtexEntryColor";
	public static final String MATH_COLOR = "mathColorPreference"; 	
	
	public TexColorsPreferencePage() {
		super(GRID);
		setPreferenceStore(ecletexPlugin.getDefault().getPreferenceStore());
		setDescription("Editor Colors");
		
		//initDefaults(getPreferenceStore());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {

		addField(new ColorFieldEditor(COMMAND_COLOR,"&Command Color:",getFieldEditorParent()));
		addField(new ColorFieldEditor(COMMENT_COLOR,"&Comment Color:",getFieldEditorParent()));
		addField(new ColorFieldEditor(ARGUMENT_COLOR,"&Argument Color:",getFieldEditorParent()));
		addField(new ColorFieldEditor(OPTIONAL_COLOR,"&Optional Argument Color:",getFieldEditorParent()));
		addField(new ColorFieldEditor(TEXT_COLOR,"&Normal Text Color:",getFieldEditorParent()));
		addField(new ColorFieldEditor(INCORRECT_COLOR,"&Incorrect Spelling Color:",getFieldEditorParent()));
		addField(new ColorFieldEditor(MATH_COLOR,"&Math Environment Color:",getFieldEditorParent())); // Stephan Dlugosz 19.11.2003
		addField(new ColorFieldEditor(BIBTEX_ENTRY_COLOR,"&BibTeX Entry Color:",getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}
	
	public static void initDefaults(IPreferenceStore store){
		PreferenceConverter.setDefault(store,COMMAND_COLOR,TeXColorConstants.DEFAULT_COMMAND_COLOR);
		PreferenceConverter.setDefault(store,COMMENT_COLOR,TeXColorConstants.DEFAULT_COMMENT_COLOR);
		PreferenceConverter.setDefault(store,ARGUMENT_COLOR,TeXColorConstants.DEFAULT_ARGUMENT_COLOR);
		PreferenceConverter.setDefault(store,OPTIONAL_COLOR,TeXColorConstants.DEFAULT_OPTIONAL_COLOR);
		PreferenceConverter.setDefault(store,TEXT_COLOR,TeXColorConstants.DEFAULT_TEXT_COLOR);
		PreferenceConverter.setDefault(store,INCORRECT_COLOR,TeXColorConstants.DEFAULT_INCORRECT_COLOR);
		PreferenceConverter.setDefault(store,MATH_COLOR,TeXColorConstants.DEFAULT_MATH_COLOR);
		PreferenceConverter.setDefault(store,BIBTEX_ENTRY_COLOR,TeXColorConstants.DEFAULT_BIBTEX_ENTRY_COLOR);
		
		
	}


}
