/*
 * Created on 30/08/2005
 */
package com.sun.j2ee.blueprints.admin.web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.CreateException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import petstore.exception.ExceptionHandler;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect AdminWebHandler {
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : ServiceLocatorException : adminRequestBDHandler() || 
	 										 updateOrdersHandler();
	declare soft : CreateException : adminRequestBDHandler() || 
									 updateOrdersHandler();
	declare soft : RemoteException : adminRequestBDHandler() || 
									 getOrdersByStatusHandler() || 
									 getChartInfoHandler();
	declare soft : com.sun.j2ee.blueprints.opc.admin.ejb.OPCAdminFacadeException : 
									 getOrdersByStatusHandler() || 
									 getChartInfoHandler();
	declare soft : com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException : 
									 updateOrdersHandler();
	declare soft : ParserConfigurationException : processRequestHandler();
	declare soft : SAXException : processRequestHandler();
	declare soft : IOException : processRequestHandler();
	declare soft : AdminBDException : getOrdersHandler() ||
									  internalUpdateOrdersHandler() || 
									  getChartInfoARPHandler();

	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** Class AdminRequestBD ***/
	pointcut adminRequestBDHandler() : 
		execution(public AdminRequestBD.new(..));

	pointcut getOrdersByStatusHandler() : 
		execution(public com.sun.j2ee.blueprints.opc.admin.ejb.OrdersTO AdminRequestBD.getOrdersByStatus(..));

	pointcut updateOrdersHandler(): 
	  	execution(public void AdminRequestBD.updateOrders(..));

	pointcut getChartInfoHandler(): 
	  	execution(public Map AdminRequestBD.getChartInfo(..));

	/*** Class ApplRequestProcessor ***/
	pointcut processRequestHandler(): 
	  	execution(String ApplRequestProcessor.processRequest(..));

	pointcut getOrdersHandler(): 
	  	execution(String ApplRequestProcessor.getOrders(..));

	/*
	 * //Frist attempt - does not work with around advice(with proceed).
	 * //Because, it must run a return to the method, not to this join point
	 * captured within the method pointcut externalUpdateOrdersHandler():
	 * withincode(String ApplRequestProcessor.updateOrders(..)); pointcut
	 * newAdminRequestBD() : call(AdminRequestBD.new(..)) &&
	 * externalUpdateOrdersHandler(); pointcut callUpdateOrders() : call(
	 * AdminRequestBD.updateOrders(..)) && externalUpdateOrdersHandler();
	 */
	pointcut internalUpdateOrdersHandler(): 
	  	execution(String ApplRequestProcessor.internalUpdateOrders(..));

	pointcut getChartInfoARPHandler(): 
	  	execution(String ApplRequestProcessor.getChartInfo(..));

	// ---------------------------
    // Advice's
    // ---------------------------
	void around() throws AdminBDException : 
		  adminRequestBDHandler() || 
		  updateOrdersHandler(){
		try {
			proceed();
		} catch (ServiceLocatorException sle) {
			sle.printStackTrace();
			throw new AdminBDException(sle.getMessage());
		}
	}

	void around() throws AdminBDException: 
		  adminRequestBDHandler(){
		try {
			proceed();
		} catch (CreateException ce) {
			ce.printStackTrace();
			throw new AdminBDException(ce.getMessage());
		}
	}

	Object around() throws AdminBDException : 
		  adminRequestBDHandler() || 
		  getOrdersByStatusHandler() || 
		  getChartInfoHandler(){
		try {
			return proceed();
		} catch (RemoteException re) {
			re.printStackTrace();
			throw new AdminBDException(re.getMessage());
		}
	}

	Object around() throws AdminBDException :
		  getOrdersByStatusHandler() || getChartInfoHandler(){
		try {
			return proceed();
		} catch (com.sun.j2ee.blueprints.opc.admin.ejb.OPCAdminFacadeException oafee) {
			oafee.printStackTrace();
			throw new AdminBDException(oafee.getMessage());
		}
	}

	void around() throws AdminBDException :
		  updateOrdersHandler(){
		try {
			proceed();
		} catch (CreateException ce) {
			throw new AdminBDException(ce.getMessage());
		} catch (com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException xde) {
			xde.printStackTrace();
			throw new AdminBDException(xde.getMessage());
		}
	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	// Used same advice for many handlers because especific code can be
	// implemented into catch block and improves reuse
	String around(ApplRequestProcessor arp) : processRequestHandler() && target(arp) {
		try {
			return proceed(arp);
		} catch (ParserConfigurationException pe) {
			return arp.replyHeader + "<Error>Exception while processing :  "
					+ pe.getMessage()
					+ ". Please try again</Error>\n</Response>\n";
		} catch (SAXException se) {
			return arp.replyHeader + "<Error>Exception while processing :  "
					+ se.getMessage()
					+ ". Please try again</Error>\n</Response>\n";
		} catch (IOException ie) {
			return arp.replyHeader + "<Error>Exception while processing :  "
					+ ie.getMessage()
					+ ". Please try again</Error>\n</Response>\n";
		}

	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	String around() : getOrdersHandler() || 
	  	getChartInfoARPHandler() {
		try {
			return proceed();
		} catch (AdminBDException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	String around(ApplRequestProcessor arp) : internalUpdateOrdersHandler() && target(arp) {
		try {
			return proceed(arp);
		} catch (AdminBDException e) {
			return arp.replyHeader + "<Error>Exception while processing :  "
					+ e.getMessage()
					+ ". Please try again</Error>\n</Response>\n";
		}

	}

}
