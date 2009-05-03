package com.sun.j2ee.blueprints.opc.ejb;


import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler
public class EjbHandler extends GeneralException {

	public void getDocument2Handler(Exception e) {
		System.err
				.println("TPAInvoiceXDE::getDocument error loading XML Document "
						+ e);
	}

}
