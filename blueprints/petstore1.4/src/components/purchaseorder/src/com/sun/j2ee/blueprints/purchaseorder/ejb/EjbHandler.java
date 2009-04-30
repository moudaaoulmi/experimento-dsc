package com.sun.j2ee.blueprints.purchaseorder.ejb;

import java.util.Date;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class EjbHandler extends GeneralException {
	
		
	public PurchaseOrder fromXMLHandler(XMLDocumentException e) throws XMLDocumentException{ 
		System.err.println(e.getRootCause().getMessage());	       
		throw new XMLDocumentException(e);
	}

	public PurchaseOrder fromDOMHandler(PurchaseOrder pO,Exception exception) { 
		pO.setOrderDate(new Date());
        System.err.println(pO.XML_ORDERDATE + ": " + exception.getMessage() + " reset to current date.");
		return pO;      
	}
	
	public void mainHandler(Exception e, Exception msg){
		e.printStackTrace(System.err);
        System.err.println(msg);
        System.exit(2);	
	}
	
	
	public void ejbPostCreateHandler(ServiceLocatorException ne) throws CreateException{
		throw new CreateException("ServiceLocator Ex while persisting PO CMR :" + ne.getMessage());
	}

	

}
