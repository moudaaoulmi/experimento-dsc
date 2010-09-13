package net.sourceforge.texlipse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;

public privileged aspect TexlipseHandler {

	pointcut getEnvHandler(): execution(public static Properties PathUtils.getEnv());

	pointcut internalTexlipsePluginHandler(): execution(private void TexlipsePlugin.internalTexlipsePlugin());

	pointcut internalGetResourceStringHandler(): execution(private static String TexlipsePlugin.internalGetResourceString(String,ResourceBundle));

	pointcut getImageDescriptorHandler(): execution(public static ImageDescriptor TexlipsePlugin.getImageDescriptor(String));

	pointcut internalGetTexTemplateStoreHandler(): execution(private void TexlipsePlugin.internalGetTexTemplateStore());

	pointcut internalGetBibTemplateStoreHandler(): execution(private void TexlipsePlugin.internalGetBibTemplateStore() );

	declare soft: Exception :getEnvHandler();
	declare soft: MalformedURLException :getImageDescriptorHandler();
	declare soft: IOException :internalGetTexTemplateStoreHandler()||internalGetBibTemplateStoreHandler();

	void around():internalGetBibTemplateStoreHandler() {
		try {
			proceed();
		} catch (IOException e) {
			// e.printStackTrace();
			TexlipsePlugin.log("Loading BibTeX template store", e);
			throw new RuntimeException(e);
		}
	}

	void around(): internalGetTexTemplateStoreHandler() {
		try {
			proceed();
		} catch (IOException e) {
			// e.printStackTrace();
			TexlipsePlugin.log("Loading TeX template store", e);
			throw new RuntimeException(e);
		}
	}

	ImageDescriptor around(): getImageDescriptorHandler() {
		try {
			return proceed();
		} catch (MalformedURLException e) {
			// should not happen
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	String around(String key): internalGetResourceStringHandler() && args(key, *) {
		try {
			return proceed(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	void around(TexlipsePlugin tex): internalTexlipsePluginHandler() && this(tex){
		try {
			proceed(tex);
		} catch (MissingResourceException x) {
			tex.resourceBundle = null;
		}
	}

	Properties around(): getEnvHandler() {
		try {
			return proceed();
		} catch (Exception e) {
		}
		return new Properties();
	}

}
