package com.sun.j2ee.blueprints.asyncsender.ejb;

import javax.ejb.EJBException;
import javax.jms.QueueConnection;

public class EjbHandler {

	public void throwEJBExceptionHandler(String str, Exception e)
			throws EJBException {
		throw new EJBException(str, e);
	}

	public void sendAMessageHandler(Exception e) {
		e.printStackTrace();
	}

	public void sendAMessageFinallyHandler(QueueConnection qConnect) {
		try {
			if (qConnect != null) {
				qConnect.close();
			}
		} catch (Exception e) {
		}
	}

}
