package com.sun.j2ee.blueprints.lodgingsupplier.pomessagebean;

import javax.ejb.CreateException;
import com.sun.j2ee.blueprints.lodgingsupplier.powebservice.LodgingOrder;
import com.sun.j2ee.blueprints.lodgingsupplier.powebservice.OrderSubmissionException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

//@ExceptionHandler
public privileged aspect PomessagebeanHandler {

	// Pointcuts
	// -------------------------------------------------------------------------------

	pointcut persistOrderHandler(): execution(public void LodgingMessageBean.persistOrder(LodgingOrder));	

	// Declare soft
	// ----------------------------------------------------------------------------

	declare soft: CreateException: persistOrderHandler();
	 

	// Advices
	// -----------------------------------------------------------------------------

	
	void around() throws OrderSubmissionException : persistOrderHandler() {
		try {
			proceed();
		} catch (ServiceLocatorException je) {
			throw new OrderSubmissionException("Error LODGE persisting order:"
					+ je.getMessage());
		} catch (CreateException ce) {
			throw new OrderSubmissionException("Error LODGE persisting order:"
					+ ce.getMessage());
		}
	}	

}
