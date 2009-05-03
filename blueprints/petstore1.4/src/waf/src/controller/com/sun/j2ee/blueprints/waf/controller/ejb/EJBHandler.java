package com.sun.j2ee.blueprints.waf.controller.ejb;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class EJBHandler extends GeneralException{

	public void processEventHandler(String actionName, Exception ex) {
		System.err.println("StateMachine: error loading " + actionName + " :" + ex);
	}
	
}
