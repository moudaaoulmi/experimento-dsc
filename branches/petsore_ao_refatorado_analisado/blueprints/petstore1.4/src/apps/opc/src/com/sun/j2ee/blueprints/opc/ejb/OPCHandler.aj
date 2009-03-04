package com.sun.j2ee.blueprints.opc.ejb;

import java.io.IOException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.util.aspect.EJBExceptionGenericAspect;

public aspect OPCHandler extends EJBExceptionGenericAspect {

	pointcut onMessageHandler() : 
		execution(public void InvoiceMDB.onMessage(Message));
	pointcut orderApprovalMDBOnMessageHandler() : 
		execution(public void OrderApprovalMDB.onMessage(Message));
	pointcut purchaseOrderMDBOnMessageHandler() : 
		execution(public void PurchaseOrderMDB.onMessage(Message));	
	pointcut getDocumentHandler() : 
		execution(private Document TPAInvoiceXDE.getDocument(InputSource));	
	pointcut getDocumentBuilderHandler() : 
		execution(private DocumentBuilder TPAInvoiceXDE.getDocumentBuilder());
	public pointcut afterEJBExceptionHandler() : 
	    onMessageHandler() || 
		orderApprovalMDBOnMessageHandler() ||
		purchaseOrderMDBOnMessageHandler();		
	declare soft : TransitionException : onMessageHandler() || 
		orderApprovalMDBOnMessageHandler() || 
		purchaseOrderMDBOnMessageHandler();
	declare soft : XMLDocumentException : onMessageHandler() || 
		orderApprovalMDBOnMessageHandler() || 
		purchaseOrderMDBOnMessageHandler();
	declare soft : JMSException : onMessageHandler() || 
		orderApprovalMDBOnMessageHandler() || 
		purchaseOrderMDBOnMessageHandler();
	declare soft : FinderException : onMessageHandler() || 
		orderApprovalMDBOnMessageHandler();
	declare soft : CreateException : purchaseOrderMDBOnMessageHandler();
	declare soft : IOException : getDocumentHandler();
	declare soft : SAXException : getDocumentHandler();
	declare soft : ParserConfigurationException : getDocumentBuilderHandler();		
	Document around() : getDocumentHandler() {
		try {
			return proceed();
        } catch (Exception e) {
            System.err.println("TPAInvoiceXDE::getDocument error loading XML Document " + e);
            return null;
        }		
	}
	DocumentBuilder around() : getDocumentBuilderHandler() {
		try {
			return proceed();
	    } catch (javax.xml.parsers.ParserConfigurationException pce) {
	        System.err.println(pce);
	        return null;
	    }		
	}		
}