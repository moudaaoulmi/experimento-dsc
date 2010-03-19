/*
 * Created on 19/09/2005
 */
package com.sun.j2ee.blueprints.supplier.orderfulfillment.ejb;

import javax.ejb.FinderException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import com.sun.j2ee.blueprints.lineitem.ejb.LineItemLocal;
import com.sun.j2ee.blueprints.supplierpo.ejb.SupplierOrderLocal;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.HashMap;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect SupplierOrderfulfillmentEjbHandler {

	
	declare soft : XMLDocumentException : internalCreateInvoiceHandler() || 
										  internalProcessAnOrderHandler();
	declare soft : IOException : internalLoadHandler();
//	declare soft : TransformerConfigurationException : internalGetTransformerHandler();

	pointcut internalCreateInvoiceHandler() : 
		execution(private String OrderFulfillmentFacadeEJB.internalCreateInvoice(SupplierOrderLocal, HashMap));

	pointcut internalProcessAnOrderHandler() : 
		execution(private String OrderFulfillmentFacadeEJB.internalProcessAnOrder(SupplierOrderLocal));

	/*** TPASupplierOrderXDE ***/
	pointcut internalLoadHandler() :  
		execution(private void TPASupplierOrderXDE.internalLoad(Properties, InputStream));

//	pointcut internalGetTransformerHandler() : 
//		execution(private Transformer TPASupplierOrderXDE.internalGetTransformer(InputStream));

//	pointcut internalCreateTransformerHandler() : 
//		execution(private Transformer TPASupplierOrderXDE.internalCreateTransformer());

	
	String around() : 
		internalCreateInvoiceHandler() || 
		internalProcessAnOrderHandler() {
		try {
			return proceed();
		} catch (XMLDocumentException xe) {
			System.out.println("OrderFulfillmentFacade**" + xe);
			return null;
		}
	}

	void around() :
		internalLoadHandler() {
		try {
			proceed();
		} catch (IOException exception) {
			System.err.println("Can't load from resource: "
					+ TPASupplierOrderXDE.STYLE_SHEET_CATALOG_PATH + ": "
					+ exception);
		}
	}

//	Object around() throws XMLDocumentException :
//		internalGetTransformerHandler() || 
//		internalCreateTransformerHandler(){
//		try {
//			return proceed();
//		} catch (Exception exception) {
//			throw new XMLDocumentException(exception);
//		}
//	
//	}

}
