package ish.ecletex;

import java.io.File;
import java.net.MalformedURLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;

public privileged aspect EcletexHandler {
	
	pointcut internalEcletexPluginHandler(): execution(private void ecletexPlugin.internalEcletexPlugin());

	pointcut internalGetResourceStringHandler(): execution(private String ecletexPlugin.internalGetResourceString(String,ResourceBundle));

	pointcut getImageDescriptorHandler(): execution(public ImageDescriptor ecletexPlugin.getImageDescriptor(String));

	pointcut listDictionariesHandler(): execution(public String[] ecletexPlugin.listDictionaries());

	pointcut readTemplateHandler(): execution(private String templateProvider.readTemplate(File));
	
	pointcut internalInitHandler(): execution(private void WordNetProvider.internalInit());

	declare soft: MalformedURLException : getImageDescriptorHandler();
	declare soft: Exception : listDictionariesHandler()|| readTemplateHandler()|| internalInitHandler();
	
	void around(ecletexPlugin ep): internalEcletexPluginHandler() && this(ep) {
		try {
			proceed(ep);
		} catch (MissingResourceException x) {
			ep.resourceBundle = null;
		}
	}

	String around(String key): internalGetResourceStringHandler() && args(key,*) {
		try {
			return proceed(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	ImageDescriptor around(): getImageDescriptorHandler() {
		try {
			return proceed();
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	String[] around(): listDictionariesHandler() {
		try {
			return proceed();
		} catch (Exception ex) {
			return new String[0];
		}
	}

	String around(): readTemplateHandler(){
		try {
			return proceed();
		} catch (Exception ex) {
			return "";
		}
	}

	

	void around(): internalInitHandler() {
		try {
			proceed();
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
	}

}
