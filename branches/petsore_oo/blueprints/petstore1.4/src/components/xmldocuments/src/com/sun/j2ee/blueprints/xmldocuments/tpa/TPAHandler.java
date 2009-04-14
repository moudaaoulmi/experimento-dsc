package com.sun.j2ee.blueprints.xmldocuments.tpa;

import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

public class TPAHandler {

	public void tpaXDEHandler(Exception exception)
			throws XMLDocumentException {
		exception.printStackTrace(System.err);
		throw new XMLDocumentException(exception);
	}

	public void getDocumentAsStringHandler(Exception exception)
			throws XMLDocumentException {
		throw new XMLDocumentException(exception);
	}	

}
