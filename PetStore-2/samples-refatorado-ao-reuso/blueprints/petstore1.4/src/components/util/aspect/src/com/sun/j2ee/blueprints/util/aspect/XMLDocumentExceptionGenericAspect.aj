/*
 * Created on 09/11/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sun.j2ee.blueprints.util.aspect;

import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

/**
 * @author Raquel Maranhao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
