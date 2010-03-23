/*
 * Created on 20-May-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class templateProvider {
	private static ecletexPlugin plugin;

	private static HashMap templates;
	private static String[] templateCategories;

	public static void init(ecletexPlugin parentplugin) {
		plugin = parentplugin;
		templates = new HashMap();
		templateCategories = new String[0];
		LoadTemplates();
	}

	public static void AddTemplateType(String Category) {
		if (!TemplateCategoryExists(Category)) {
			String[] newCategories = new String[templateCategories.length + 1];
			for (int i = 0; i < templateCategories.length; i++) {
				newCategories[i] = templateCategories[i];
			}
			newCategories[templateCategories.length] = Category;
			templateCategories = newCategories;
		}

	}

	public static void AddUserTemplate(String Category, String Name,
			String Template) throws IOException {
		IPath stateLocation = ecletexPlugin.getDefault().getStateLocation();
		IPath templateDirectory = stateLocation.append(new Path("/templates/"
				+ Category + "/"));
		IPath template;
		String templateName = "";
		if (!templateDirectory.toFile().exists()) {
			templateDirectory.toFile().mkdirs();
			template = templateDirectory.append("default.template");
			FileWriter fw = new FileWriter(template.toFile());
			fw.write(Template);
			fw.flush();
			fw.close();
			templateName = "-" + Category + "-";
			AddTemplateType(Category);
		} else {
			template = templateDirectory.append(Name + ".template");
			FileWriter fw = new FileWriter(template.toFile());
			fw.write(Template);
			fw.flush();
			fw.close();
			templateName = "-" + Category + "_" + Name + "-";
		}
		templates.put(templateName, Template);

	}

	public static boolean TemplateCategoryExists(String Category) {
		for (int i = 0; i < templateCategories.length; i++) {
			if (templateCategories[i].toLowerCase().equals(
					Category.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static String[] GetCategories() {
		return templateCategories;
	}

	public static boolean isWholeTemplate(String template) {
		if (templates.containsKey(template)) {
			return true;
		} else
			return false;
	}

	public static String getTemplate(String template) {
		return (String) templates.get(template);
	}

	public static String[] getPossibleTemplates(String filter) {
		String[] sTemplates = new String[templates.keySet().size()];
		sTemplates = (String[]) templates.keySet().toArray(sTemplates);
		ArrayList matches = new ArrayList();
		for (int i = 0; i < sTemplates.length; i++) {
			if (sTemplates[i].startsWith(filter))
				matches.add(sTemplates[i]);
		}
		String[] sMatches = new String[matches.size()];
		sMatches = (String[]) matches.toArray(sMatches);
		return sMatches;

	}

	private static String readTemplate(File template) {
		FileReader fr = new FileReader(template);
		BufferedReader br = new BufferedReader(fr);
		String temp = "";
		String line = "";
		while ((line = br.readLine()) != null) {
			temp += line + "\n";
		}
		br.close();
		fr.close();
		return temp;
	}

	private static void LoadTemplates() {
		System.out.println("Loading Template Library...");
		String templateDir = Platform.resolve(
				plugin.getDescriptor().getInstallURL()).getFile();
		templateDir += "templates/";
		loadTemplates(templateDir);
		System.out.println("Loading User Template Library...");
		IPath stateLocation = ecletexPlugin.getDefault().getStateLocation();
		IPath userTemplates = stateLocation.append(new Path("/templates/"));

		File userTemplateDir = new File(userTemplates.toString());
		if (!userTemplateDir.exists())
			userTemplateDir.mkdirs();
		loadTemplates(userTemplateDir.toString());
	}

	private static void loadTemplates(String templateDir) {

		File rootFolder = new File(templateDir);
		File[] types = rootFolder.listFiles();
		for (int i = 0; i < types.length; i++) {
			if (!types[i].getName().startsWith("CVS")) {
				if (types[i].isDirectory()) {
					String name = types[i].getName();
					System.out
							.println("Found Template Category [" + name + "]");
					AddTemplateType(name);
					File[] entries = types[i].listFiles();
					for (int j = 0; j < entries.length; j++) {
						if (entries[j].isFile()) {
							if (entries[j].getName().toLowerCase().endsWith(
									".template")) {
								String template = readTemplate(entries[j]);
								String entryName = entries[j]
										.getName()
										.substring(
												0,
												entries[j].getName().length() - 9);
								if (entryName.toLowerCase().equals("default")) {
									System.out
											.println("Found Default Template");
									templates.put("-" + name + "-", template);
								} else {
									System.out.println("Found " + entryName
											+ " template");
									templates.put("-" + name + "_" + entryName
											+ "-", template);
								}
							}

						}
					}

				}
			}
		}

	}

}
