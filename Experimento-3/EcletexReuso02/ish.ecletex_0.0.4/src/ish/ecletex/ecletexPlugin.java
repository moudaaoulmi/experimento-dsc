package ish.ecletex;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;

//import com.sun.org.apache.bcel.internal.generic.GETFIELD;

import ish.ecletex.editors.tex.ColorManager;
import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexColorsPreferencePage;
import ish.ecletex.preferences.TexExternalToolsPreferencePage;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class ecletexPlugin extends AbstractUIPlugin {
	// The shared instance.
	private static ecletexPlugin plugin;
	// Resource bundle.
	private ResourceBundle resourceBundle;

	private ColorManager colorManager;

	/**
	 * The constructor.
	 */
	public ecletexPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		internalEcletexPlugin();
		templateProvider.init(this);
	}

	private void internalEcletexPlugin() {
		resourceBundle = ResourceBundle
				.getBundle("ish.ecletex.ecletexPluginResources");
	}

	/**
	 * Returns the shared instance.
	 */
	public static ecletexPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ecletexPlugin.getDefault().getResourceBundle();
		return internalGetResourceString(key, bundle);
	}

	private static String internalGetResourceString(String key,
			ResourceBundle bundle) {
		return (bundle != null) ? bundle.getString(key) : key;
	}

	public ColorManager getColorManager() {
		if (colorManager == null) {
			colorManager = new ColorManager(getPreferenceStore());
		}
		return colorManager;
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	protected void initializeDefaultPluginPreferences() {
		TexColorsPreferencePage.initDefaults(getPreferenceStore());
		TeXPreferencePage.initDefaults(getPreferenceStore());
		TexExternalToolsPreferencePage.initDefaults(getPreferenceStore());
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public ImageDescriptor getImageDescriptor(String name) {
		URL url = new URL(getDescriptor().getInstallURL(), name);
		return ImageDescriptor.createFromURL(url);
	}

	public static String getPluginId() {
		return getDefault().getDescriptor().getUniqueIdentifier();
	}

	public static String[] listDictionaries() {
		String DictionaryDir = Platform.resolve(
				ecletexPlugin.getDefault().getDescriptor().getInstallURL())
				.getFile();
		DictionaryDir += "dictionary/";

		File rootFolder = new File(DictionaryDir);

		File[] children = rootFolder.listFiles();
		ArrayList dicts = new ArrayList();
		for (int i = 0; i < children.length; i++) {
			if (children[i].isDirectory()) {
				if (!children[i].getName().startsWith("CVS")) {
					System.out.println("Found Dictionary: "
							+ children[i].getName());
					dicts.add(children[i].getName());
				}
			}
		}
		String[] dicts_names = new String[dicts.size()];
		dicts.toArray(dicts_names);
		return dicts_names;
	}
}
