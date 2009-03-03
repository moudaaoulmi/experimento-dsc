package com.sun.j2ee.blueprints.purchaseorder.ejb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.util.aspect.XMLDocumentExceptionGenericAspect;

public aspect PurchaseOrderEjbHandler extends XMLDocumentExceptionGenericAspect {
	
    public pointcut afterXMLDocumentExceptionHandler() : 
		execution(public String PurchaseOrder.toXML(URL));
    public pointcut afterWithPrintXMLDocumentExceptionHandler() : 
	    execution(PurchaseOrder PurchaseOrder.fromXML(String, URL, boolean));	
	pointcut internalSetOrderDateHandler() : 
		execution(private static Element PurchaseOrder.internalSetOrderDate(Element, PurchaseOrder));
	pointcut mainHandler() : 
		execution(public static void PurchaseOrder.main(String[]));
	declare soft : UnsupportedEncodingException : afterXMLDocumentExceptionHandler();
	declare soft : XMLDocumentException : internalSetOrderDateHandler() || 
		mainHandler();
	declare soft : ParseException : internalSetOrderDateHandler() ||
		mainHandler();
	declare soft : FileNotFoundException : mainHandler();
	declare soft : SAXException : mainHandler();
	declare soft : ParserConfigurationException : mainHandler();
	Element around(Element child, PurchaseOrder purchaseOrder) : 
		internalSetOrderDateHandler() && args(child, purchaseOrder) {
		try {
			return proceed(child, purchaseOrder);
	    } catch (Exception exception) {
	        purchaseOrder.setOrderDate(new Date());
	        System.err.println(PurchaseOrder.XML_ORDERDATE + ": " + exception.getMessage() + " reset to current date.");
	        return child;
	    }
	}
	void around() : mainHandler() {
		try {
			proceed();	    
	    } catch (XMLDocumentException exception) {
	        exception.printStackTrace(System.err);	        
	        System.err.println(exception.getRootCause());
	        System.exit(2);
	    } catch (Exception exception) {
	        exception.printStackTrace(System.err);
	        System.err.println(exception);
	        System.exit(2);
	    }
	}	
}