/*
 * Created on 11/11/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sun.j2ee.blueprints.signon.ejb;

import javax.ejb.FinderException;

/**
 * @author Raquel Maranhao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public aspect SignonEjbHandler {
	/*** SignOnEJB ***/
	pointcut authenticateHandler() : 
		execution(public boolean SignOnEJB.authenticate(String, String));
	
	
	declare soft : FinderException : authenticateHandler();
	
	
	boolean around() : authenticateHandler() {
		try {
			return proceed();
		} catch (FinderException fe) {
			return false; 
		}
	}

}
