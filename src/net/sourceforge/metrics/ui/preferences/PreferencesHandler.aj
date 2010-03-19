package net.sourceforge.metrics.ui.preferences;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect PreferencesHandler {

	

	pointcut internalHandler(): execution(private void RangePage.RangeCellModifier.internal(String[], String, int));

	void around(String[] values, int i): internalHandler() && args(values,*,i) {
		try {
			proceed(values, i);
		} catch (NumberFormatException e) {
			values[i] = "";
		}
	}

	
}
