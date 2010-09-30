package com.sun.j2ee.blueprints.signon.web;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ExceptionHandler
public privileged aspect WebHandler {

	pointcut loadDocumentHandler(): execution(private  Element ConfigFileSignOnDAO.loadDocument(URL));
	pointcut internalInitHandler(): execution(private void SignOnFilter.internalInit(FilterConfig, URL));
	pointcut internalValidateSignOnHandler(): execution(private void SignOnFilter.internalValidateSignOn(HttpServletRequest ,HttpServletResponse, String, String));

	declare soft: SAXParseException : loadDocumentHandler();
	declare soft: SAXException : loadDocumentHandler();
	declare soft: MalformedURLException : loadDocumentHandler() || internalInitHandler() ;
	declare soft: IOException : loadDocumentHandler();
	declare soft: Exception : loadDocumentHandler() || internalValidateSignOnHandler();
	
	void around(): internalValidateSignOnHandler() {
		try {
			proceed();
		} catch (Exception e) {
			System.out.println("SignOnFilter signOnError:::exception to:" + e);
		}
	}

	void around() throws RuntimeException : internalInitHandler(){
		try {
			proceed();
		} catch (java.net.MalformedURLException ex) {
			System.err.println("SignonFilter: malformed URL exception: " + ex);
			throw new RuntimeException(ex);
		}
	}

	Element around(): loadDocumentHandler() {
		try {
			return proceed();
		} catch (SAXParseException err) {
			System.err.println("ConfigFileSignOnDAO  ** Parsing error"
					+ ", line " + err.getLineNumber() + ", uri "
					+ err.getSystemId());
			System.err.println("ConfigFileSignOnDAO  error: "
					+ err.getMessage());
		} catch (SAXException e) {
			System.err.println("ConfigFileSignOnDAO  error: " + e);
		} catch (java.net.MalformedURLException mfx) {
			System.err.println("ConfigFileSignOnDAO  error: " + mfx);
		} catch (java.io.IOException e) {
			System.err.println("ConfigFileSignOnDAO  error: " + e);
		} catch (Exception pce) {
			System.err.println("ConfigFileSignOnDAO  error: " + pce);
		}
		return null;
	}

}
