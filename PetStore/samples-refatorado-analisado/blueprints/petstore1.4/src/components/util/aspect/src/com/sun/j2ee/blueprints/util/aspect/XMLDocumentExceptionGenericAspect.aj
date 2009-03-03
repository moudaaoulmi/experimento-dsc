package com.sun.j2ee.blueprints.util.aspect;

import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

public abstract aspect XMLDocumentExceptionGenericAspect {

    public abstract pointcut afterXMLDocumentExceptionHandler();
    public abstract pointcut afterWithPrintXMLDocumentExceptionHandler();    
	after() throwing(Exception exception) throws XMLDocumentException : 
	    afterXMLDocumentExceptionHandler() {
		throw new XMLDocumentException(exception);
	}    
	after() throwing(XMLDocumentException exception) throws XMLDocumentException : 
	    afterWithPrintXMLDocumentExceptionHandler() {
        System.err.println(exception.getRootCause().getMessage());
        throw new XMLDocumentException(exception);		
	}	
}