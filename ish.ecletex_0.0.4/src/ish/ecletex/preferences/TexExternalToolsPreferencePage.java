/*
 * Created on 10-Nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.preferences;

import ish.ecletex.ecletexPlugin;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Ian Hartney
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TexExternalToolsPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public static final String TEX_BIN_DIR = "texBinDir";
	public static final String GS_BIN_DIR = "gsBinDir";
	public static final String WN_DICT_DIR = "wnDictDir";

	
	
	public TexExternalToolsPreferencePage() {
		super(GRID);
		setPreferenceStore(ecletexPlugin.getDefault().getPreferenceStore());
		setDescription("External Tools");
		
		//initDefaults(getPreferenceStore());
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor(TEX_BIN_DIR,"&Tex Bin Directory:",getFieldEditorParent()));
		addField(new DirectoryFieldEditor(GS_BIN_DIR,"&Ghostscript bin Directory"+System.getProperty("line.separator")+"(or wherever 'gs' / 'gswin32c.exe' lives):",getFieldEditorParent()));
		addField(new DirectoryFieldEditor(WN_DICT_DIR,"&WordNet 2.0 dictionary files directory:",getFieldEditorParent()));		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}
	
	public static void initDefaults(IPreferenceStore store){
		
		
	}
	

}
