package com.sun.j2ee.blueprints.xmldocuments.tpa;

import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class TPAHandler extends GeneralException{

	public void printStackTraceThrowXMLDocumentExceptionHandler(Exception exception)
			throws XMLDocumentException {
		exception.printStackTrace(System.err);
		throw new XMLDocumentException(exception);
	}

//	public void throwXMLDocumentExceptionHandler(Exception exception)
//			throws XMLDocumentException {
//		throw new XMLDocumentException(exception);
//	}	

}
