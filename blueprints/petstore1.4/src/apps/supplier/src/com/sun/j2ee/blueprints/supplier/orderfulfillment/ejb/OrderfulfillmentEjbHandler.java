package com.sun.j2ee.blueprints.supplier.orderfulfillment.ejb;

import java.io.IOException;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler
public class OrderfulfillmentEjbHandler extends GeneralException {

	public boolean checkInventoryHandler() {
		return false;
	}

	public String processAnOrderHandler(Exception e) {
		System.out.println("OrderFulfillmentFacade**" + e);
		return null;
	}

	public void processPendingPOHandler(Exception e) {
		System.out.println("OrderFulfillmentFacade:" + e);
	}

	public void TPASupplierOrderXDE1Handler(Exception e, String s) {
		System.err.println("Can't load from resource: " + s + ": " + e);
	}
}
