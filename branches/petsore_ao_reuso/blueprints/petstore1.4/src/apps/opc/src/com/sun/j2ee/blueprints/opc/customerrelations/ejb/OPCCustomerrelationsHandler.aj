/*
 * Created on 01/09/2005
 */
package com.sun.j2ee.blueprints.opc.customerrelations.ejb;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import com.sun.j2ee.blueprints.opc.customerrelations.ejb.MailContentXDE.FormatterException;
import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.util.aspect.EJBExceptionGenericAspect;

/**
 * @author Raquel Maranhao
 */
public aspect OPCCustomerrelationsHandler extends EJBExceptionGenericAspect {
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft: XMLDocumentException : onMessageHandler() || 
										 mailInvoiceMDBOnMessageHandler() || 
										 mailOrderApprovalMDBOnMessageHandler();
	declare soft: TransitionException : onMessageHandler() || 
										mailInvoiceMDBOnMessageHandler() || 
										mailOrderApprovalMDBOnMessageHandler();
	declare soft: JMSException : onMessageHandler() || 
								 mailInvoiceMDBOnMessageHandler() || 
								 mailOrderApprovalMDBOnMessageHandler();
	declare soft: MailContentXDE.FormatterException : onMessageHandler() || 
													  mailInvoiceMDBOnMessageHandler() || 
													  mailOrderApprovalMDBOnMessageHandler();
	declare soft: FinderException : onMessageHandler() || 
									mailInvoiceMDBOnMessageHandler() || 
									mailOrderApprovalMDBOnMessageHandler();
	declare soft : TransformerConfigurationException : getTransformerHandler();
	declare soft : TransformerException : formatHandler();
	declare soft : UnsupportedEncodingException : formatHandler();
	declare soft : FormatterException : getDocumentHandler() || 
										getDocumentAsStringHandler();

	// ---------------------------
    // Pointcut's
    // ---------------------------

	/*** MailCompletedOrderMDB ***/
	pointcut onMessageHandler() : 
		execution(public void MailCompletedOrderMDB.onMessage(Message));
	
	/*** MailContentXDE ***/
	pointcut newMailContentXDEHandler() : 
		execution(public MailContentXDE.new(String));
	
	pointcut getTransformerHandler() : 
		execution(private Transformer MailContentXDE.getTransformer(Locale));
	
	pointcut getDocumentHandler() : 
		execution(public Source MailContentXDE.getDocument());
	
	pointcut getDocumentAsStringHandler() :
		execution(public String MailContentXDE.getDocumentAsString());		

	pointcut formatHandler() : 
		execution(private String MailContentXDE.format(Source, Locale));
	
	/*** MailInvoiceMDB ***/
	pointcut mailInvoiceMDBOnMessageHandler() : 
		execution(public void MailInvoiceMDB.onMessage(Message));
	
	/*** MailOrderApprovalMDB ***/
	pointcut mailOrderApprovalMDBOnMessageHandler() : 
		execution(public void MailOrderApprovalMDB.onMessage(Message));
	
	public pointcut afterEJBExceptionHandler() :	
	    mailInvoiceMDBOnMessageHandler() || 
	    mailOrderApprovalMDBOnMessageHandler();
	    
	// ---------------------------
    // Advice's
    // ---------------------------
	void around() throws EJBException : onMessageHandler(){
		try{
			proceed();
		}catch(Exception e){
			if (e instanceof MailContentXDE.FormatterException) {		
				System.err.println(e.toString());
			}
			throw new EJBException(e);
		}
	}
	Object around() throws FormatterException : 
		newMailContentXDEHandler() || getTransformerHandler() || formatHandler(){
		try{
			return proceed();
		}catch(Exception exception){
			throw new FormatterException(exception);
		}
	}
//	after() throwing(Exception exception) throws FormatterException : 
//		newMailContentXDEHandler() || getTransformerHandler() || formatHandler() {
//        throw new FormatterException(exception);
//	}

	Object around() throws XMLDocumentException : 
		getDocumentHandler() || getDocumentAsStringHandler(){
		try{
			return proceed();
		}catch(Exception exception){
			throw new XMLDocumentException(exception);
		}
	}
	
	
}
