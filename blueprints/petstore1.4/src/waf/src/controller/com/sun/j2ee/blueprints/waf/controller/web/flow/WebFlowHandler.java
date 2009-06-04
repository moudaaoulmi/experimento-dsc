package com.sun.j2ee.blueprints.waf.controller.web.flow;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class WebFlowHandler extends GeneralException{

	public void initHandler(java.net.MalformedURLException ex) {
		System.err.println("ScreenFlowManager: initializing ScreenFlowManager malformed URL exception: " + ex);
	}
	
	public void forwardToNextScreenHandler(Exception ex) {
		System.err.println("ScreenFlowManager caught loading handler: " + ex);
	}
	
	public void getExceptionScreenHandler(String exceptionName) {
		System.err.println("ScreenFlowManager: Could not load exception " + exceptionName);
	}
	
}
