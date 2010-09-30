package com.sun.j2ee.blueprints.opc.otwebservice;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

//@ExceptionHandler
public privileged aspect OtwebserviceHandler {

	pointcut ejbCreateHandler(): execution(public void OtEndpointBean.ejbCreate());
	pointcut internalGetOrderDetailsHandler(): execution(private void OtEndpointBean.internalGetOrderDetails(String, OrderDetails));

	declare soft: FinderException: internalGetOrderDetailsHandler();

	void around(String orderId) throws OrderNotFoundException  : internalGetOrderDetailsHandler() && args( orderId , *)	{
		try {
			proceed(orderId);
		} catch (FinderException fe) {
			throw new OrderNotFoundException("Unable to locate order with id "
					+ orderId
					+ "; Please ensure that you entered the correcr order Id");
		}
	}

	
	// ServiceLocatorException extende RuntimeException. Dessa forma o reuso de nivel III
	// não é apropriado, pois deveriamos criar uma nova classe...
	
	void around() throws CreateException  : ejbCreateHandler(){
		try {
			proceed();
		} catch (ServiceLocatorException se) {
			throw new CreateException(se.getMessage());
		}
	}

}
