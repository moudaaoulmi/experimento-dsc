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
	
	
	void around() throws EJBException: onMessageHandler(){
		try{
			proceed();
		}catch(TransitionException te){
			throw new EJBException(te);
		}catch(CreateException ce){
			throw new EJBException(ce);
		}catch(XMLDocumentException xe){
			xe.printStackTrace();
		    throw new EJBException(xe);
		}catch(JMSException je){
			throw new EJBException(je);
		}
	}
	
}
