/*
 * Created on 24/09/2005
 */
package com.sun.j2ee.blueprints.asyncsender.ejb;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

/**
 * @author Raquel Maranhao
 */
public aspect AsyncsenderEjbHandler {
	
	//QueueConnection qConnect = null;
	Map qConnect = new HashMap();
	
	/*** AsyncSenderEJB ***/
	pointcut sendAMessage() : 
		execution(public void AsyncSenderEJB.sendAMessage(String));
	pointcut createQueueConnectionHandler() : 
		call(* QueueConnectionFactory.createQueueConnection()) && 
		withincode(public void AsyncSenderEJB.sendAMessage(String));
	
	
	declare soft : JMSException : sendAMessage();
	
	
	after() returning(QueueConnection qc) : createQueueConnectionHandler() {
    	//Save inner method variable to local(multi-thread)
	    qConnect.put(Thread.currentThread().getName(), qc);
		//qConnect = qc; 
	}
	
	void around() throws EJBException : sendAMessage() {
		try {
			proceed();
		} catch(Exception e) {
			e.printStackTrace();
			throw new EJBException("askMDBToSendAMessage: Error!",e);
		} finally {
			try {
			    QueueConnection qConnectAux = (QueueConnection)qConnect.get(Thread.currentThread().getName()); 
                  if( qConnectAux != null ) {
                      qConnectAux.close();
                  }
			} catch(Exception e) {}
         }		
	}

}
