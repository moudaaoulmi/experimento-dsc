package com.sun.j2ee.blueprints.waf.controller.ejb;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class EJBHandler extends GeneralException{

	public void processEventHandler(String actionName, Exception ex) {
		System.err.println("StateMachine: error loading " + actionName + " :" + ex);
	}
	
}
