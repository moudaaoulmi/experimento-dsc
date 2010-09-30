package com.sun.j2ee.blueprints.opc.invoice;


//@ExceptionHandler
public privileged aspect InvoiceHandler {

	pointcut fromXMLHandler(): execution(public static Invoice Invoice.fromXML(String));

	declare soft: Exception: fromXMLHandler();

	Invoice around() throws XMLException  : fromXMLHandler() {
		try {
			return proceed();
		} catch (Exception exe) {
			throw new XMLException("Invoice XML Exception");
		}
	}

}
