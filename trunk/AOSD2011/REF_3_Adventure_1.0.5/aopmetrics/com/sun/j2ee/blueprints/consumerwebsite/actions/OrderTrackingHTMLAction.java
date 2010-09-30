/* @@copyright $Id: OrderTrackingHTMLAction.java,v 1.3 2005/03/08 00:20:14 smitha Exp $ */

package com.sun.j2ee.blueprints.consumerwebsite.actions;

import javax.servlet.http.*;
import java.util.Collection;
import javax.naming.*;
import javax.xml.rpc.*;

// waf imports
//import com.sun.j2ee.blueprints.opc.otwebservice.OrderDetails;
//import com.sun.j2ee.blueprints.opc.otwebservice.OrderTrackingIntf;
import com.sun.j2ee.blueprints.waf.controller.Event;
import com.sun.j2ee.blueprints.waf.controller.web.html.*;

//adventure imports
import com.sun.j2ee.blueprints.consumerwebsite.*;
import com.sun.j2ee.blueprints.consumerwebsite.exceptions.OrderNotFoundException;

// Catalog imports
import com.sun.j2ee.blueprints.catalog.*;

/**
 * Handles responsibilities related to getting HTTP request info and making the
 * calls to process the action
 */
public class OrderTrackingHTMLAction extends HTMLActionSupport {

	/**
	 * Handles the http request and provides an appropriate response.
	 * 
	 * Post-condition: Set the bean with info to populate response.
	 */
	public Event perform(HttpServletRequest request) throws HTMLActionException {

		String orderId = null;
		OrderDetails result = null;
		orderId = request.getParameter("orderId");
		// put the orderId in the request to display in the screen
		request.setAttribute("orderTrackingId", orderId);
		internalPerform(request, orderId, result);
		return null;
	}

	private void internalPerform(HttpServletRequest request, String orderId,
			OrderDetails result) throws OrderNotFoundException {
		result = this.getOrderDetails(orderId, request);
		if (result.getPO() == null) {
			throw new OrderNotFoundException("Order Not Found: " + orderId);
		}
		// places result bean data in the response.
		request.setAttribute("orderDetails", result);
	}

	/**
	 * Accesses OrderTracking Web Service endpoint using Jax-rpc
	 */
	private OrderDetails getOrderDetails(String orderId,
			HttpServletRequest request) throws Exception {

		Context ic = new InitialContext();
		OpcOrderTrackingService opcOrderTrackingSvc = (OpcOrderTrackingService) ic
				.lookup("java:comp/env/service/OpcOrderTrackingService");
		OrderTrackingIntf port = (OrderTrackingIntf) opcOrderTrackingSvc
				.getOrderTrackingIntfPort();
		return port.getOrderDetails(orderId);
	}
}
