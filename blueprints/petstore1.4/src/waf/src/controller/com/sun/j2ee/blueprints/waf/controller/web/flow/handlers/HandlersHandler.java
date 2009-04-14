package com.sun.j2ee.blueprints.waf.controller.web.flow.handlers;

public class HandlersHandler {
	
	public void processFlowHandler(Exception e){
		System.err.println("ClientCacheLinkFlowHandler caught: " + e);
	}

}
