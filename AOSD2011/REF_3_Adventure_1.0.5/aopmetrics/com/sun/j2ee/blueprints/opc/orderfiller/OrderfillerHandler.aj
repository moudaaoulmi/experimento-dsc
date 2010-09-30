package com.sun.j2ee.blueprints.opc.orderfiller;


import javax.ejb.EJBException;
import javax.jms.Message;

@ExceptionHandler
public privileged aspect OrderfillerHandler {

	pointcut onMessageHandler() : execution(public void OrderFillerBean.onMessage(Message));
	
	declare soft: Exception: onMessageHandler();

	void around(): onMessageHandler() {
		try {
			proceed();
		} catch (Exception exe) {
			System.err.println(exe);
			throw new EJBException(exe);
		}
	}

}
