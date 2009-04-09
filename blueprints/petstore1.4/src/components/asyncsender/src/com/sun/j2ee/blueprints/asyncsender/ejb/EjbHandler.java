package com.sun.j2ee.blueprints.asyncsender.ejb;

import javax.ejb.EJBException;
import javax.jms.QueueConnection;

public class EjbHandler {

	// private EjbHandler ejbHandler = new EjbHandler();

	public void throwEJBExceptionHandler(String str, Exception e)
			throws EJBException {
		throw new EJBException(str, e);
	}

	public void printStackTraceHandler(Exception e) {
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
