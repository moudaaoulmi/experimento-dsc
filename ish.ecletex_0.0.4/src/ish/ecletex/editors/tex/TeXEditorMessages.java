/*
 * Created on 23-Sep-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class TeXEditorMessages {
	private static final String RESOURCE_BUNDLE = "ish.ecletex.editors.tex.TeXEditorMessages";

	private static ResourceBundle resource = ResourceBundle
			.getBundle(RESOURCE_BUNDLE);

	public static String getString(String key) {
		return resource.getString(key);
	}

	public static ResourceBundle getResourceBundle() {
		return resource;
	}

}
