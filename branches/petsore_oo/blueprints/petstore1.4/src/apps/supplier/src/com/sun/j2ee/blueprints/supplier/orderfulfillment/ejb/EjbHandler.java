package com.sun.j2ee.blueprints.supplier.orderfulfillment.ejb;

import java.io.IOException;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;


public class EjbHandler {
	
	public void ejbCreateHandler(Exception e) {
		throw new EJBException(e);	
	}

	public boolean checkInventoryHandler(){
		return(false);
	}
	
	public String processAnOrderHandler(Exception e ){
		System.out.println("OrderFulfillmentFacade**" + e);
		return null;
	}
	
	public void processPendingPOHandler(Exception e){
		System.out.println("OrderFulfillmentFacade:" + e);
	}
	
	public void TPASupplierOrderXDE1Handler(Exception e, String s) {
	    System.err.println("Can't load from resource: " + s + ": " + e);      
	}
	
	public void TPASupplierOrderXDE2Handler(Exception e) throws XMLDocumentException {
		throw new XMLDocumentException(e);
	}
}
