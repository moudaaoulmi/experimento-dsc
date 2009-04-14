/*
 * Created on 24/09/2005
 */
package com.sun.j2ee.blueprints.asyncsender.ejb;

import javax.ejb.EJBException;
import javax.jms.QueueSession;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;

/**
 * @author Raquel Maranhao
 */
public aspect AsyncsenderEjbHandler {

	declare soft : Exception : sendAMessage();

	/*** AsyncSenderEJB ***/
	pointcut sendAMessage() : 
		execution(* AsyncSenderEJB.internalSendAMessage(..));

	void around(String msg, QueueConnection qConnect, QueueSession session,
			QueueSender qSender): sendAMessage()
		&& args(msg, qConnect, session,qSender){
		try {
			proceed(msg, qConnect, session, qSender);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException("askMDBToSendAMessage: Error!", e);
		} finally {
			try {
				if (qConnect != null) {
					qConnect.close();
				}
			} catch (Exception e) {
			}
		}
	}
}
