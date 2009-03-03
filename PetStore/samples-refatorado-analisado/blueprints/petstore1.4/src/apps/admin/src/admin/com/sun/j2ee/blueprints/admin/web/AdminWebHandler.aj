package com.sun.j2ee.blueprints.admin.web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import javax.ejb.CreateException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.opc.admin.ejb.OPCAdminFacadeException;
import com.sun.j2ee.blueprints.uidgen.ejb.UniqueIdGeneratorLocal;
import com.sun.j2ee.blueprints.petstore.controller.ejb.actions.OrderEJBAction;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

public aspect AdminWebHandler {

	pointcut getUniqueIdGeneratorHandler() : 
		execution(private UniqueIdGeneratorLocal OrderEJBAction.getUniqueIdGenerator());
	pointcut adminRequestBDHandler() : 
		execution(public AdminRequestBD.new(..));
	pointcut getOrdersByStatusHandler() : 
		execution(public com.sun.j2ee.blueprints.opc.admin.ejb.OrdersTO AdminRequestBD.getOrdersByStatus(..));
	pointcut updateOrdersHandler(): 
	  	execution(public void AdminRequestBD.updateOrders(..));
	pointcut getChartInfoHandler(): 
	  	execution(public Map AdminRequestBD.getChartInfo(..));
	pointcut processRequestHandler(): 
	  	execution(String ApplRequestProcessor.processRequest(..));
	pointcut getOrdersHandler(): 
	  	execution(String ApplRequestProcessor.getOrders(..));
	pointcut internalUpdateOrdersHandler(): 
	  	execution(String ApplRequestProcessor.internalUpdateOrders(..));
	pointcut getChartInfoARPHandler(): 
	  	execution(String ApplRequestProcessor.getChartInfo(..));
	declare soft : ServiceLocatorException : adminRequestBDHandler() || updateOrdersHandler() || getUniqueIdGeneratorHandler();
	declare soft : CreateException : adminRequestBDHandler() || updateOrdersHandler() || getUniqueIdGeneratorHandler();
	declare soft : RemoteException : adminRequestBDHandler() || getOrdersByStatusHandler() || getChartInfoHandler();
	declare soft : com.sun.j2ee.blueprints.opc.admin.ejb.OPCAdminFacadeException : 
	  	getOrdersByStatusHandler() || getChartInfoHandler();
	declare soft : com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException : 
	  	updateOrdersHandler();
	declare soft : ParserConfigurationException : processRequestHandler();
	declare soft : SAXException : processRequestHandler();
	declare soft : IOException : processRequestHandler();
	declare soft : AdminBDException : getOrdersHandler() || internalUpdateOrdersHandler() || getChartInfoARPHandler();
	
	Object around() throws AdminBDException : 
		  					adminRequestBDHandler() || updateOrdersHandler()
		  				 || getOrdersByStatusHandler() || getChartInfoHandler() {
		try {
			return proceed();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdminBDException(e.getMessage());
		}
	}
	String around(ApplRequestProcessor arp) : (internalUpdateOrdersHandler() || processRequestHandler()) && target(arp){
		try {
			return proceed(arp);
		} catch (Exception e) {
			return arp.replyHeader + "<Error>Exception while processing :  "
					+ e.getMessage()
					+ ". Please try again</Error>\n</Response>\n";
		}
	}
	Object around() : getOrdersHandler() || 
	  	getChartInfoARPHandler() || getUniqueIdGeneratorHandler(){
		try {
			return proceed();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}