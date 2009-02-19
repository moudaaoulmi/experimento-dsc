/*
 * Created on 21/09/2005
 */
package com.sun.j2ee.blueprints.supplier.processpo.ejb;

import javax.jms.Message;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.jms.JMSException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;


/**
 * @author rmaranhao
 */
public aspect SupplierProcessPoEjbHandler {
	
	/*** SupplierOrderMDB ***/
	pointcut onMessageHandler() : 
		execution(public void SupplierOrderMDB.onMessage(Message));

	
	declare soft : TransitionException : onMessageHandler();
	declare soft : CreateException : onMessageHandler();
	declare soft : XMLDocumentException : onMessageHandler();
	declare soft : JMSException : onMessageHandler();
	
	
	after() throwing(TransitionException te) throws EJBException :
		onMessageHandler() {
		throw new EJBException(te);
	}
	
	after() throwing(CreateException ce) throws EJBException :
		onMessageHandler() {
		throw new EJBException(ce);
	}
	
	after() throwing(XMLDocumentException xe) throws EJBException :
		onMessageHandler() {
	    xe.printStackTrace();
	    throw new EJBException(xe);
	}

	after() throwing(JMSException je) throws EJBException :
		onMessageHandler() {
		throw new EJBException(je);
	}		
		
}
