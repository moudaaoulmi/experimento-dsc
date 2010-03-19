package net.sourceforge.metrics.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sourceforge.metrics.calculators.Calculator;
import org.eclipse.core.runtime.CoreException;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect CoreHandler {

	pointcut internalSetRangeHandler(): execution(private void MetricDescriptor.internalSetRange(String));

	pointcut internalSetRangeMaxHandler(): execution(private void MetricDescriptor.internalSetRangeMax(String));

	pointcut internalMetricsPluginHandler(): execution(private void MetricsPlugin.internalMetricsPlugin());

	pointcut internalMetricsPlugin2Handler(): call(private void MetricsPlugin.installExtensions()) && withincode(public MetricsPlugin.new(..));

	pointcut getStringHandler(): call(public String ResourceBundle.getString(String)) && withincode(public static String MetricsPlugin.getResourceString(String));

	String around(String key): getStringHandler() && args(key) {
		try {
			return proceed(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	void around(): internalMetricsPlugin2Handler() {
		try {
			proceed();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	void around(MetricsPlugin mp): internalMetricsPluginHandler() && this(mp) {
		try {
			proceed(mp);
		} catch (MissingResourceException x) {
			mp.resourceBundle = null;
		}
	}

	void around(): internalSetRangeMaxHandler() {
		try {
			proceed();
		} catch (NumberFormatException x) {
			//XXX LOG - não generalizado
			Log.logError(
					"Non-numeric maximum specified by a metrics extension", x);
		}
	}

	void around(): internalSetRangeHandler() {
		try {
			proceed();
		} catch (NumberFormatException x) {
			//XXX LOG - não generalizado
			Log.logError(
					"Non-numeric minimum specified by a metrics extension", x);
		}
	}
	
}
