package com.sun.j2ee.blueprints.waf.controller.web.flow;

public class WebFlowHandler {

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
