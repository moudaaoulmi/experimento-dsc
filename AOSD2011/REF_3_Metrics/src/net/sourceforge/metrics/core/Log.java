/*
 * Created on May 13, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.metrics.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import br.upe.dsc.reusable.exception.ILogObject;

/**
 * Separating this from the MetricsPlugin class breaks numerous cyclic dependencies
 * 
 * @author Frank Sauer
 */
//REFLOG
public class Log implements ILogObject {
	
	public final static String pluginId = "net.sourceforge.metrics";
	private static Log logInstance;
	
	public Log(){
		logInstance = this;
	}
	
	//A usada no log de exceções
	public static void logError(String message, Throwable t) {
		Platform.getPlugin(pluginId).getLog().log(new Status(IStatus.ERROR, pluginId,
				IStatus.ERROR, message, t));
	}
	
	public static void logMessage(String message) {
		Platform.getPlugin(pluginId).getLog().log(
			new Status(IStatus.INFO, pluginId, IStatus.INFO, message, null));
	}

	public void logGeneral(String msg, Throwable t) {
		Log.logError(msg, t);
	}
	
	public static Log getInstance() {
		return logInstance;
	}

}
