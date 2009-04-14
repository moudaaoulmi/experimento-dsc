package com.sun.j2ee.blueprints.waf.controller.ejb;

public class EJBHandler {

	public void processEventHandler(String actionName, Exception ex) {
		System.err.println("StateMachine: error loading " + actionName + " :" + ex);
	}
	
}
