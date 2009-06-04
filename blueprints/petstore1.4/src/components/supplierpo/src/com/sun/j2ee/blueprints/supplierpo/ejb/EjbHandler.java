package com.sun.j2ee.blueprints.supplierpo.ejb;

import java.util.Date;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

import exception.ExceptionHandler;
import exception.GeneralException;
@ExceptionHandler


public class EjbHandler extends GeneralException{
	
//	public void toXMLHandler(Exception e) throws XMLDocumentException{
//		throw new XMLDocumentException(e);
//	}

	public void fromXMLHandler(XMLDocumentException exception) throws XMLDocumentException {
        System.err.println(exception.getRootCause().getMessage());
        throw new XMLDocumentException(exception);
	}
	
	public void fromDOMHandler(SupplierOrder supplierOrder, String XML_ORDERDATE, Exception e){
		supplierOrder.setOrderDate(new Date());
        System.err.println(XML_ORDERDATE + ": " + e.getMessage() + " reset to current date.");
	}
	
	public void ejbPostCreateHandler(Exception e) throws CreateException{
		throw new CreateException("ServiceLocator Ex while persisting PO CMR :" + e.getMessage());
	}
	
	public void ejbPostCreate2Handler(Exception e){
		 System.err.println("SupplierOrderEJB caught a null pointer");
	     e.printStackTrace();
	}
}
