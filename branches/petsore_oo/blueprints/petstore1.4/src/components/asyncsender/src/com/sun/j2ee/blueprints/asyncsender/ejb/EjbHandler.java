package com.sun.j2ee.blueprints.asyncsender.ejb;

import javax.ejb.EJBException;
import javax.jms.QueueConnection;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler
public class EjbHandler extends GeneralException {

	public void throwEJBExceptionHandler(String str, Exception e)
			throws EJBException {
		e.printStackTrace();
		throw new EJBException(str, e);
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