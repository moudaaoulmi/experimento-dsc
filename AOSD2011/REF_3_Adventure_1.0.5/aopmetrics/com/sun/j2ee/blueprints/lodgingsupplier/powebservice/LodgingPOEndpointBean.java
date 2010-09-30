/* Copyright 2004 Sun Microsystems, Inc.  All rights reserved.  You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: 
 http://adventurebuilder.dev.java.net/LICENSE.txt
 $Id: LodgingPOEndpointBean.java,v 1.8 2004/07/30 23:59:20 inder Exp $ */

package com.sun.j2ee.blueprints.lodgingsupplier.powebservice;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.sun.j2ee.blueprints.lodgingsupplier.JNDINames;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator;

/**
 * This class is the entry point for purchase orders submitted by OPC to the
 * lodging suppliers;
 */
public class LodgingPOEndpointBean implements SessionBean {

	private SessionContext sc;

	public LodgingPOEndpointBean() {
	}

	public void ejbCreate() throws CreateException {
	}

	/**
	 * Receive an order, preprocess the request, submit request to workflow and
	 * return the order id so that the caller can have a correlation id for the
	 * order
	 */
	public String submitLodgingReservationDetails(String xmlPO)
			throws InvalidOrderException, OrderSubmissionException,
			RemoteException {

		// Do Interaction layer processing
		LodgingOrder lodgeObj = preProcessInput(xmlPO);

		// Submit request to JMS Queue
		submitRequest(lodgeObj);

		// Return co-relation ID - dummy ID for now
		return ("LODGE1234");
	}

	private LodgingOrder preProcessInput(String po)
			throws InvalidOrderException {
		// XML doc should be ideally validated against its schema here;
		// Similar scenario shown in OPC module;
		// Here it is skipped in this sample - we will convert doc to obj
		return (LodgingOrder.fromXML(po));
	}

	private void submitRequest(LodgingOrder lodge)
			throws OrderSubmissionException {

		ConnectionFactory jmsConnectionFactory = null;
		Destination jmsDest = null;
		Connection jmsConnection = null;
		Session jmsSession = null;
		MessageProducer jmsSender = null;

		internalSubmitRequest(lodge, jmsConnectionFactory, jmsDest,
				jmsConnection, jmsSession, jmsSender);
	}

	private void internalSubmitRequest(LodgingOrder lodge,
			ConnectionFactory jmsConnectionFactory, Destination jmsDest,
			Connection jmsConnection, Session jmsSession,
			MessageProducer jmsSender) throws OrderSubmissionException {
		ServiceLocator sl = new ServiceLocator();
		jmsConnectionFactory = (ConnectionFactory) sl
				.getJMSConnectionFactory(JNDINames.LODGE_QUEUECONNECTIONFACTORY);
		jmsDest = sl.getJMSDestination(JNDINames.LODGE_QUEUE);
		jmsConnection = jmsConnectionFactory.createConnection();
		jmsSession = jmsConnection
				.createSession(true, Session.AUTO_ACKNOWLEDGE);
		jmsSender = jmsSession.createProducer(jmsDest);

		ObjectMessage message = jmsSession.createObjectMessage();
		message.setObject(lodge);
		jmsSender.send(message);
	}

	public void setSessionContext(SessionContext sc) {
		this.sc = sc;
	}

	public void ejbRemove() throws RemoteException {
	}

	// empty for Stateless EJBs
	public void ejbActivate() {
	}

	// empty for Stateless EJBs
	public void ejbPassivate() {
	}
}
