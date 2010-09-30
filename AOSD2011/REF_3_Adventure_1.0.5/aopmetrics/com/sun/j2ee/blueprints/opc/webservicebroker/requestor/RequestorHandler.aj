package com.sun.j2ee.blueprints.opc.webservicebroker.requestor;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.NamingException;

//@ExceptionHandler
public privileged aspect RequestorHandler {	

	pointcut onMessageHandler(): execution(public void BrokerRequestorBean.onMessage(Message));
	pointcut getWSClientHandler(): execution(public WSClient WSClientFactory.getWSClient(String));

	declare soft: JMSException : onMessageHandler();
	declare soft: NamingException : getWSClientHandler();
	declare soft: Exception: getWSClientHandler();

	WSClient around(): getWSClientHandler() {
		try {			
			return proceed();
		} catch (NamingException ne) {
			System.err.println(ne);
		} catch (Exception se) {
			System.err.println(se);
		}
		return null;
	}

	void around(): onMessageHandler() {
		try {
			proceed();
		} catch (JMSException jex) {
			throw new EJBException(jex);
		}
	}

	

}
