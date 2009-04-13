package com.sun.j2ee.blueprints.opc.ejb;

import javax.ejb.EJBException;

public class EjbHandler {

	public void throwEJBExceptionHandler(Exception e) throws EJBException {
		throw new EJBException(e);
	}

	public void getDocument1Handler(Exception e) {
		System.err.println(e.toString());
	}

	public void getDocument2Handler(Exception e) {
		System.err
				.println("TPAInvoiceXDE::getDocument error loading XML Document "
						+ e);
	}

}
