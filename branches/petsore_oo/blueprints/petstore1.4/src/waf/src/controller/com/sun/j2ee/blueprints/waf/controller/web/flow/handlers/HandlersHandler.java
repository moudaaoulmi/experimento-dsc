package com.sun.j2ee.blueprints.waf.controller.web.flow.handlers;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class HandlersHandler extends GeneralException{
	
	public void processFlowHandler(Exception e){
		System.err.println("ClientCacheLinkFlowHandler caught: " + e);
	}

}
