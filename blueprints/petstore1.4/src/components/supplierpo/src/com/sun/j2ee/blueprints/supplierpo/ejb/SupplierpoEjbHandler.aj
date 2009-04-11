/*
 * Created on 28/09/2005
 */
package com.sun.j2ee.blueprints.supplierpo.ejb;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import javax.ejb.CreateException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

import com.sun.j2ee.blueprints.util.aspect.XMLDocumentExceptionGenericAspect;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import org.w3c.dom.Element;

/**
 * @author Raquel Maranhao
 */
public aspect SupplierpoEjbHandler extends XMLDocumentExceptionGenericAspect {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : UnsupportedEncodingException : afterXMLDocumentExceptionHandler();
	declare soft : ParseException: supplierOrder_internalSetOrderDateHandler(); 
	declare soft : XMLDocumentException: supplierOrder_internalSetOrderDateHandler();
	declare soft : ServiceLocatorException: supplierOrderEJB_ejbPostCreateHandler();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** SupplierOrder ***/
	public pointcut afterXMLDocumentExceptionHandler() : 
		execution(public String SupplierOrder.toXML(URL));
	public pointcut afterWithPrintXMLDocumentExceptionHandler() : 
			execution(public static SupplierOrder SupplierOrder.fromXML(String, URL, boolean));
	pointcut supplierOrder_internalSetOrderDateHandler() : 
		execution(private static Element SupplierOrder.internalSetOrderDate(Element, SupplierOrder));
	pointcut supplierOrderEJB_ejbPostCreateHandler() : 
		execution(public void SupplierOrderEJB.ejbPostCreate(SupplierOrder));
	
	// ---------------------------
    // Advice's
    // ---------------------------		
	/*
	after() throwing(Exception exception) throws XMLDocumentException : 
		toXMLHandler() {
		throw new XMLDocumentException(exception);
	}
	*/
	/*
	after() throwing(XMLDocumentException exception) throws XMLDocumentException : 
		fromXMLHandler() {
        System.err.println(exception.getRootCause().getMessage());
        throw new XMLDocumentException(exception);		
	}
	*/
	Element around(Element child, SupplierOrder supplierOrder) : 
		supplierOrder_internalSetOrderDateHandler() && 
		args(child, supplierOrder) {
		try {
			return proceed(child, supplierOrder);
	    } catch (Exception exception) {
	        supplierOrder.setOrderDate(new Date());
	        System.err.println(SupplierOrder.XML_ORDERDATE + ": " + exception.getMessage() + " reset to current date.");
	        return child;
	    }		
	}
	void around() throws CreateException : supplierOrderEJB_ejbPostCreateHandler(){
		try{
			proceed();
		} catch (ServiceLocatorException ne) {
		      throw new CreateException("ServiceLocator Ex while persisting PO CMR :" +
		                                ne.getMessage());
		} catch (NullPointerException nex) {
		        System.err.println("SupplierOrderEJB caught a null pointer");
		        nex.printStackTrace();
		}
	}

}
