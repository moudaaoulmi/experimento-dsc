package com.sun.j2ee.blueprints.waf.controller.web.flow.handlers;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class HandlersHandler extends GeneralException{
	
	public void processFlowHandler(Exception e){
		System.err.println("ClientCacheLinkFlowHandler caught: " + e);
	}

}
