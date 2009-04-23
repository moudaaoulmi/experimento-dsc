/*
 * Created on 24/09/2005
 */
package com.sun.j2ee.blueprints.asyncsender.ejb;

import javax.ejb.EJBException;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Raquel Maranhao
 */
public aspect AsyncsenderEjbHandler {
	
	//QueueConnection qConnect = null;
	private Map qConnect = new HashMap();
	
	// ---------------------------
    // Declare soft
    // ---------------------------
	declare soft : Exception : sendAMessage();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** AsyncSenderEJB ***/
	pointcut sendAMessage() : 
		execution(* AsyncSenderEJB.sendAMessage(..));
	
	pointcut createQueueConnectionHandler() : 
		call(* QueueConnectionFactory.createQueueConnection()) && 
		withincode(public void AsyncSenderEJB.sendAMessage(String));
	
	// ---------------------------
    // Advice's
    // ---------------------------
	
	QueueConnection around():createQueueConnectionHandler() {
		QueueConnection qc = proceed();
    	//Save inner method variable to local(multi-thread)
	    qConnect.put(Thread.currentThread().getName(), qc);
		//qConnect = qc; 
	    return qc;
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
