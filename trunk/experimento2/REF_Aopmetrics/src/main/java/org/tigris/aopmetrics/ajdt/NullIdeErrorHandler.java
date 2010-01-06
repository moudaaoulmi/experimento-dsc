/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 * ******************************************************************/

package org.tigris.aopmetrics.ajdt;

import org.aspectj.ajde.ErrorHandler;

/**
 * @author Mik Kersten
 */
public class NullIdeErrorHandler implements ErrorHandler {

	public void handleWarning(String message) {
		System.out.println("warning: " + message);
	}

	public void handleError(String message) {
		System.out.println("error: " + message);
	}

	public void handleError(String message, Throwable t) {
		System.out.println("error: " + message);
		t.printStackTrace(System.out);
	}
}
