/*
 * Created on 01/10/2005
 */
package com.sun.j2ee.blueprints.waf.controller.ejb;

import com.sun.j2ee.blueprints.waf.controller.ejb.action.EJBAction;

/**
 * @author Raquel Maranhao
 */
public aspect WafControllerEjbHandler {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : ClassNotFoundException : internalGetActionHandler();
	declare soft : IllegalAccessException : internalGetActionHandler();
	declare soft : InstantiationException : internalGetActionHandler();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** StateMachine ***/
	pointcut internalGetActionHandler() : 
		execution(private EJBAction StateMachine.internalGetAction(String));
	
	// ---------------------------
    // Advice's
    // ---------------------------	
	EJBAction around(String actionName) : 
		internalGetActionHandler() && args(actionName)  {
		try {
			return proceed(actionName);
	    } catch (Exception ex) {
	    	System.err.println("StateMachine: error loading " + actionName + " :" + ex);
	    	return null;
	    }
	}

}
