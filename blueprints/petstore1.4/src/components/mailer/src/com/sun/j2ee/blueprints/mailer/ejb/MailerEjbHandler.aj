/*
 * Created on 26/09/2005
 */
package com.sun.j2ee.blueprints.mailer.ejb;

import java.net.URL;
import java.util.Locale;
import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.util.aspect.XMLDocumentExceptionGenericAspect;
import com.sun.j2ee.blueprints.mailer.exceptions.MailerAppException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Raquel Maranhao
 */
public aspect MailerEjbHandler extends XMLDocumentExceptionGenericAspect {
	
	/*** ByteArrayDataSource ***/
	pointcut internalByteArrayDataSource() : 
		execution(private void ByteArrayDataSource.internalByteArrayDataSource(String));
	
	/*** Mail ***/
	public pointcut afterXMLDocumentExceptionHandler() : 
		execution(public String Mail.toXML(URL));
	public pointcut afterWithPrintXMLDocumentExceptionHandler() : 
		execution(Mail Mail.fromXML(String, URL, boolean));
	pointcut mainMailHandler() : 
		execution(public static void Mail.main(String[]));
	
	/*** MailerMDB ***/
	pointcut onMessageHandler() : 
		execution(public void MailerMDB.onMessage(Message));
	
	/*** MailHelper ***/
	pointcut createAndSendMailHandler() : 
		execution(public void MailHelper.createAndSendMail(String, String, String, Locale));
	
	
	
	declare soft : UnsupportedEncodingException : internalByteArrayDataSource();
	declare soft : IOException : mainMailHandler();
	declare soft : XMLDocumentException : mainMailHandler() ||
		onMessageHandler();
	declare soft : MailerAppException : onMessageHandler();
	declare soft : JMSException : onMessageHandler();
	declare soft : UnsupportedEncodingException : afterXMLDocumentExceptionHandler();
	declare soft : NamingException : createAndSendMailHandler();
	declare soft : MessagingException : createAndSendMailHandler();
	declare soft : AddressException : createAndSendMailHandler();
	 

	
	void around() :  internalByteArrayDataSource() {
		try {
			proceed();
		} catch(UnsupportedEncodingException uex) {
			//Do nothing
		}
	}
	

	void around() : mainMailHandler(){
		try{
			proceed();
		}catch(IOException exception){
			System.err.println(exception);
	        System.exit(2);	
		}catch(XMLDocumentException exception){
			System.err.println(exception.getRootCause());
	        System.exit(2);
		}
	}
	


	void around() : onMessageHandler() {
		try {
			proceed();		
	    } catch (MailerAppException me) {
	        //throw new EJBException("MailerMDB.onMessage" + me);
	        //ignore since user probably forgot to set up mail server
	    } catch (XMLDocumentException xde) {
	        throw new EJBException("MailerMDB.onMessage" + xde);
	    } catch(JMSException je) {
	        throw new EJBException("MailerMDB.onMessage" + je);
	    }
	}
	
	after() throwing(Exception e) throws MailerAppException : 
		createAndSendMailHandler() {
        System.err.print("createAndSendMail exception : " + e);
        throw new MailerAppException("Failure while sending mail");
    }

}
