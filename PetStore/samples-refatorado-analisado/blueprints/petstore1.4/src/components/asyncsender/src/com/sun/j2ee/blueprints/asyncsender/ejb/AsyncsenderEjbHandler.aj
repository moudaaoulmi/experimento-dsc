package com.sun.j2ee.blueprints.asyncsender.ejb;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

public aspect AsyncsenderEjbHandler {

	Map qConnect = new HashMap();
	pointcut sendAMessage() : 
		execution(public void AsyncSenderEJB.sendAMessage(String));
	pointcut createQueueConnectionHandler() : 
		call(* QueueConnectionFactory.createQueueConnection()) && 
		withincode(public void AsyncSenderEJB.sendAMessage(String));
	declare soft : JMSException : sendAMessage();
	after() returning(QueueConnection qc) : createQueueConnectionHandler() {
		qConnect.put(Thread.currentThread().getName(), qc);
	}
	void around() throws EJBException : sendAMessage() {
		try {
			proceed();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException("askMDBToSendAMessage: Error!", e);
		} finally {
			try {
				QueueConnection qConnectAux = (QueueConnection) qConnect
						.get(Thread.currentThread().getName());
				if (qConnectAux != null) {
					qConnectAux.close();
				}
			} catch (Exception e) {
			}
		}
	}
}