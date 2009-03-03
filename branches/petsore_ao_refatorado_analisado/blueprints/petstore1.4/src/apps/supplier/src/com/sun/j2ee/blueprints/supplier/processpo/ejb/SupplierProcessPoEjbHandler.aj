package com.sun.j2ee.blueprints.supplier.processpo.ejb;

import javax.jms.Message;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.jms.JMSException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

public aspect SupplierProcessPoEjbHandler {

	pointcut onMessageHandler() : 
		execution(public void SupplierOrderMDB.onMessage(Message));
	declare soft : TransitionException : onMessageHandler();
	declare soft : CreateException : onMessageHandler();
	declare soft : XMLDocumentException : onMessageHandler();
	declare soft : JMSException : onMessageHandler();
	void around() throws EJBException : onMessageHandler(){
		try {
			proceed();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}
}