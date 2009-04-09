package com.sun.j2ee.blueprints.opc.customerrelations.ejb;

import java.util.Locale;

import javax.ejb.EJBException;
import com.sun.j2ee.blueprints.opc.customerrelations.ejb.MailContentXDE.FormatterException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;


public class EjbHandler {
	
	// private EjbHandler ejbHandler = new EjbHandler();

	
	public void throwEJBExceptionHandler(Exception e)throws EJBException{
		throw new EJBException(e);
	}
	
	public void throwFormatterException(Exception e)throws MailContentXDE.FormatterException{
		throw new MailContentXDE.FormatterException(e);
	}
	
	public void errPrintlnHandler(Exception e){
		System.err.println(e.toString());
	}
	
	public void getTransformerHandler(Locale locale) throws FormatterException{
		throw new FormatterException("No style sheet found for locale: " + locale);
	}
	
	public void throwXMLDocumentExceptiontHandler(Exception e) throws XMLDocumentException{
		throw new XMLDocumentException(e);
	}
}
