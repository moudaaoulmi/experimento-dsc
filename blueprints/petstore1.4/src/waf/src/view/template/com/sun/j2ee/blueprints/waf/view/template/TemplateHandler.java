package com.sun.j2ee.blueprints.waf.view.template;

import org.xml.sax.SAXParseException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class TemplateHandler extends GeneralException{

	public void initScreensHandler(java.net.MalformedURLException ex) {
		System.err.println("TemplateServlet: malformed URL exception: " + ex);
	}
	

	public void loadDocument1Handler(SAXParseException err) {
		System.err.println ("ScreenFlowXmlDAO ** Parsing error" + ", line " +
		            err.getLineNumber () + ", uri " + err.getSystemId ());
		System.err.println("ScreenFlowXmlDAO error: " + err.getMessage ());
	}
	
	public void loadDocument2Handler(Exception err) {		
		System.err.println("ScreenFlowXmlDAO error: " + err);
	}
	
}
