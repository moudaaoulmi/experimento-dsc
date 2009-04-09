/*
 * Created on 11/11/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sun.j2ee.blueprints.signon.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

/**
 * @author Raquel Maranhao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public aspect SignonEjbHandler {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : FinderException : authenticateHandler();
	declare soft : NamingException : signOnEJB_ejbCreateHandler();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** SignOnEJB ***/
	pointcut authenticateHandler() : 
		execution(public boolean SignOnEJB.authenticate(String, String));
	
	pointcut signOnEJB_ejbCreateHandler() : 
		execution(void SignOnEJB.ejbCreate());
	
	// ---------------------------
    // Advice's
    // ---------------------------		
	boolean around() : authenticateHandler() {
		try {
			return proceed();
		} catch (FinderException fe) {
			return false; 
		}
	}
	void around() throws CreateException : signOnEJB_ejbCreateHandler() {
		try {
			proceed();
		} catch (NamingException ne) {
			throw new EJBException("SignOnEJB Got naming exception! " + ne.getMessage());
		}
	}

}
