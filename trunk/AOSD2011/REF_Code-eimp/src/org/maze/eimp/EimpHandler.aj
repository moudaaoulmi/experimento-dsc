package org.maze.eimp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public privileged aspect EimpHandler {

	pointcut internalEimpPluginHandler(): execution(private void eimpPlugin.internalEimpPlugin());

	pointcut internalGetResourceStringHandler(): execution(private static String eimpPlugin.internalGetResourceString(String ,ResourceBundle));

	pointcut internalInitializeImageRegistryHandler(): execution(private URL eimpPlugin.internalInitializeImageRegistry(URL, String));

	declare soft: MalformedURLException: internalInitializeImageRegistryHandler();

	void around(eimpPlugin eimp): internalEimpPluginHandler()&& this(eimp){
		try {
			proceed(eimp);
		} catch (MissingResourceException x) {
			eimp.resourceBundle = null;
		}
	}

	String around(String key):internalGetResourceStringHandler()&& args(key,*) {
		try {
			return proceed(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	URL around(URL url):internalInitializeImageRegistryHandler()&& args(url,*) {
		try {
			return proceed(url);
		} catch (MalformedURLException e) {
			return url;
		}
	}

}
