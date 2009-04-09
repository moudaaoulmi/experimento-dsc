package com.sun.j2ee.blueprints.opc.ejb;

import javax.ejb.EJBException;

public class EjbHandler {

	// private EjbHandler ejbHandler = new EjbHandler();

	public void throwEJBExceptionHandler(Exception e) throws EJBException {
		throw new EJBException(e);
	}

	public void errPrintlnHandler(Exception e) {
		System.err.println(e.toString());
	}

	public void getDocumentHandler(Exception e) {
		System.err
				.println("TPAInvoiceXDE::getDocument error loading XML Document "
						+ e);
	}

}
