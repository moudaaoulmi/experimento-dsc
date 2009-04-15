package com.sun.j2ee.blueprints.waf.view.template;

import org.xml.sax.SAXParseException;

public class TemplateHandler {

	public void initScreensHandler(java.net.MalformedURLException ex) {
		System.err.println("TemplateServlet: malformed URL exception: " + ex);
	}
	
	public void printStackTraceHandler(Exception e){
		e.printStackTrace();
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
