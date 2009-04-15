package com.sun.j2ee.blueprints.xmldocuments.tpa;

import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

public class TPAHandler {

	public void printStackTraceThrowXMLDocumentExceptionHandler(Exception exception)
			throws XMLDocumentException {
		exception.printStackTrace(System.err);
		throw new XMLDocumentException(exception);
	}

	public void throwXMLDocumentExceptionHandler(Exception exception)
			throws XMLDocumentException {
		throw new XMLDocumentException(exception);
	}	

}
