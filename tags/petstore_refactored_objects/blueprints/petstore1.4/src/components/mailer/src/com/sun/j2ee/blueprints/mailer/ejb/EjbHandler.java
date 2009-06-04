package com.sun.j2ee.blueprints.mailer.ejb;

import java.io.UnsupportedEncodingException;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.mailer.exceptions.MailerAppException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class EjbHandler extends GeneralException{

//	public void byteArrayDataSourceHandler(UnsupportedEncodingException uex){
//		
//	}
	
//	public void toXMLHandler(Exception e) throws XMLDocumentException{
//		throw new XMLDocumentException(e);
//	}
	
	public void fromXMLHandler(XMLDocumentException e) throws XMLDocumentException{
		 System.err.println(e.getRootCause().getMessage());
	     throw new XMLDocumentException(e);
	}
	
	public void mainHandler(Exception e){
		System.err.println(e);
        System.exit(2);
	}
	
//	public void onMessageHandler(MailerAppException me){
//		
//	}
	
	public void onMessage2Handler(Exception e){
		throw new EJBException("MailerMDB.onMessage" + e);
	}
	
	public void createAndSendMailHandler(Exception e) throws MailerAppException{
		System.err.print("createAndSendMail exception : " + e);
        throw new MailerAppException("Failure while sending mail");
	}
}
