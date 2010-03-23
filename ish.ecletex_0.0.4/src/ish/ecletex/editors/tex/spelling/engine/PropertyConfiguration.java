package ish.ecletex.editors.tex.spelling.engine;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @author aim4min
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class PropertyConfiguration extends Configuration {

	public Properties prop;
	public URL filename;

	public PropertyConfiguration() {
		prop = new Properties();
		internalPropertyConfiguration();
	}

	private void internalPropertyConfiguration() {
		filename = getClass()
				.getClassLoader()
				.getResource(
						"ish/ecletex/editors/tex/spelling/engine/configuration.properties");
		InputStream in = filename.openStream();
		prop.load(in);
	}

	/**
	 * @see com.swabunga.spell.engine.Configuration#getBoolean(String)
	 */
	public boolean getBoolean(String key) {
		return new Boolean(prop.getProperty(key)).booleanValue();
	}

	/**
	 * @see com.swabunga.spell.engine.Configuration#getInteger(String)
	 */
	public int getInteger(String key) {
		return new Integer(prop.getProperty(key)).intValue();
	}

	/**
	 * @see com.swabunga.spell.engine.Configuration#setBoolean(String, boolean)
	 */
	public void setBoolean(String key, boolean value) {
		String string = null;
		if (value)
			string = "true";
		else
			string = "false";

		prop.setProperty(key, string);
		save();
	}

	/**
	 * @see com.swabunga.spell.engine.Configuration#setInteger(String, int)
	 */
	public void setInteger(String key, int value) {
		prop.setProperty(key, Integer.toString(value));
		save();
	}

	public void save() {
		File file = new File(filename.getFile());
		FileOutputStream fout = new FileOutputStream(file);
		prop.store(fout, "HEADER");
	}

}
