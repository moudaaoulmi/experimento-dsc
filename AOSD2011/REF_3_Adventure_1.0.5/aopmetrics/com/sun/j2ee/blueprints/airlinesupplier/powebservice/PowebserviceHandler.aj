package com.sun.j2ee.blueprints.airlinesupplier.powebservice;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

@ExceptionHandler
public privileged aspect PowebserviceHandler {

	Map jmsConnectionMap = new HashMap();
	Map jmsSessionMap = new HashMap();
	Map jmsSenderMap = new HashMap();

	pointcut fromXMLHandler(): execution(public static AirlineOrder AirlineOrder.fromXML(String));

	pointcut internalSubmitRequestHandler(): execution(private void AirlinePOEndpointBean.internalSubmitRequest(AirlineOrder,
			ConnectionFactory , Destination ,
			Connection , Session ,
			MessageProducer ));

	pointcut getConnectionHandler(): call(public Connection ConnectionFactory.createConnection()) && withincode(private void AirlinePOEndpointBean.internalSubmitRequest(AirlineOrder,
			ConnectionFactory , Destination ,
			Connection , Session ,
			MessageProducer ));

	pointcut getSessionHandler(): call(public Session createSession(boolean , int )) && withincode(private void AirlinePOEndpointBean.internalSubmitRequest(AirlineOrder,
			ConnectionFactory , Destination ,
			Connection , Session ,
			MessageProducer ));

	pointcut getSenderHandler(): call(public MessageProducer javax.jms.Session.createProducer(Destination)) && withincode(private void AirlinePOEndpointBean.internalSubmitRequest(AirlineOrder,
			ConnectionFactory , Destination ,
			Connection , Session ,
			MessageProducer ));

	declare soft: Exception : fromXMLHandler();
	declare soft: JMSException : internalSubmitRequestHandler();

	void around() throws OrderSubmissionException : internalSubmitRequestHandler()  {
		try {
			proceed();
		} catch (ServiceLocatorException se) {
			throw new OrderSubmissionException("Error while sending a message:"
					+ se.getMessage());
		} catch (JMSException e) {
			throw new OrderSubmissionException("Error while sending a message:"
					+ e.getMessage());
		} finally {
			// close all JMS resources

			Connection jmsConnection = (Connection)this.jmsConnectionMap.get(Thread
					.currentThread().getName());
			Session jmsSession = (Session)this.jmsSessionMap.get(Thread.currentThread()
					.getName());
			MessageProducer jmsSender = (MessageProducer)this.jmsSenderMap.get(Thread
					.currentThread().getName());

			if (jmsSender != null) {
				try {
					jmsSender.close();
				} catch (JMSException e) {
					throw new OrderSubmissionException("Error sender close");
				}
			}
			if (jmsSession != null) {
				try {
					jmsSession.close();
				} catch (JMSException e) {
					throw new OrderSubmissionException("Error session close");
				}
			}
			if (jmsConnection != null) {
				try {
					jmsConnection.close();
				} catch (JMSException e) {
					throw new OrderSubmissionException("Error Connection close");
				}
			}
		}
	}

	Connection around(): getConnectionHandler() {
		Connection con = proceed();
		this.jmsConnectionMap.put(Thread.currentThread().getName(), con);
		return con;
	}

	Session around(): getSessionHandler() {
		Session con = proceed();
		this.jmsSessionMap.put(Thread.currentThread().getName(), con);
		return con;
	}

	MessageProducer around(): getSenderHandler() {
		MessageProducer con = proceed();
		this.jmsSenderMap.put(Thread.currentThread().getName(), con);
		return con;
	}

	AirlineOrder around() throws InvalidOrderException : fromXMLHandler(){
		try {
			return proceed();
		} catch (Exception exe) {
			exe.printStackTrace(System.err);
			throw new InvalidOrderException("PO for Airline not valid : "
					+ exe.getMessage());
		}
	}

}
