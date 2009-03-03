package com.sun.j2ee.blueprints.waf.controller.ejb;

import com.sun.j2ee.blueprints.waf.controller.ejb.action.EJBAction;

public aspect WafControllerEjbHandler {
	
	pointcut internalGetActionHandler() : 
		execution(private EJBAction StateMachine.internalGetAction(String));	
	declare soft : ClassNotFoundException : internalGetActionHandler();
	declare soft : IllegalAccessException : internalGetActionHandler();
	declare soft : InstantiationException : internalGetActionHandler();	
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