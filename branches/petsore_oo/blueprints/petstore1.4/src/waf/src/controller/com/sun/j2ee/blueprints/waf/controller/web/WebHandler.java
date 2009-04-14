package com.sun.j2ee.blueprints.waf.controller.web;

import javax.ejb.RemoveException;

import org.xml.sax.SAXParseException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

public class WebHandler {

	public void destroyHandler(RemoveException re) {
		// ignore, after all its only a remove() call!
		Debug.print(re);
	}

	public void getWebControllerHandler(Exception exception) {
		throw new RuntimeException(
				"Cannot create bean of class WebController: " + exception);
	}

	public void getEJBControllerHandler(String message) {
		throw new GeneralFailureException(message);
	}

	public void initHandler(java.net.MalformedURLException ex) {
		System.err
				.println("MainServlet: initializing ScreenFlowManager malformed URL exception: "
						+ ex);
	}

	public void getActionHandler(Exception ex) {
		System.err.println("RequestProcessor caught loading action: " + ex);
	}

	public void loadDocument1Handler(SAXParseException err) {
		System.err.println("URLMappingsXmlDAO ** Parsing error" + ", line "
				+ err.getLineNumber() + ", uri " + err.getSystemId());
		System.err.println("URLMappingsXmlDAO error: " + err.getMessage());
	}
	
	public void loadDocument2Handler(Exception e){
		System.err.println("URLMappingsXmlDAO error: " + e);
	}
}
