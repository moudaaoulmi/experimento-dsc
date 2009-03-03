package com.sun.j2ee.blueprints.signon.ejb;

import javax.ejb.FinderException;

public aspect SignonEjbHandler {

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