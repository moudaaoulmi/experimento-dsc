/*
 * Created on 19/09/2005
 */
package com.sun.j2ee.blueprints.supplier.orderfulfillment.ejb;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import petstore.exception.ExceptionHandler;

import com.sun.j2ee.blueprints.supplierpo.ejb.SupplierOrderLocal;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
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
