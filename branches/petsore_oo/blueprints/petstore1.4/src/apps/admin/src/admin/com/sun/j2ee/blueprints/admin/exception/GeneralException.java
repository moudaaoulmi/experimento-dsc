package com.sun.j2ee.blueprints.admin.exception;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

public class GeneralException {
	
	
	public void printStackTraceHandler(Exception e) {
		e.printStackTrace();
	}
	
	public void errPrintlnHandler(Exception e){
		System.err.println(e.toString());
	}
	
	public void throwXMLDocumentExceptiontHandler(Exception e) throws XMLDocumentException{
		throw new XMLDocumentException(e);
	}
	
	public void throwEJBExceptionHandler(Exception e){
		throw new EJBException(e);
	}
	
	public void throwsTransitionExceptionHandler(Exception e) throws TransitionException{
		throw new TransitionException(e);
	}
	
	public void ignoreHandler(Exception e) {
	}
	
	public void throwCreateExceptionHandler()throws CreateException {
		throw new CreateException("ContactInfoEJB error: ServiceLocator exception looking up address");
		
	}

}
