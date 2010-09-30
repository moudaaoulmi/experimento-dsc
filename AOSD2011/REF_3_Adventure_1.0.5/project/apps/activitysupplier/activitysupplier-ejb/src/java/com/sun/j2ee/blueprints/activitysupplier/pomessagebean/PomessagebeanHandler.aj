package com.sun.j2ee.blueprints.activitysupplier.pomessagebean;

import javax.ejb.CreateException;
import com.sun.j2ee.blueprints.activitysupplier.powebservice.ActivityOrder;
import com.sun.j2ee.blueprints.activitysupplier.powebservice.OrderSubmissionException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

//@ExceptionHandler
public privileged aspect PomessagebeanHandler {

		
	pointcut persistOrderHandler(): execution(public void ActivityMessageBean.persistOrder(ActivityOrder));
	
	declare soft: CreateException : persistOrderHandler();	

	void around() throws OrderSubmissionException : persistOrderHandler() {
		try {
			proceed();
		} catch (ServiceLocatorException je) {
			throw new OrderSubmissionException("Error while persisting order:"
					+ je.getMessage());
		} catch (CreateException ce) {
			throw new OrderSubmissionException("Error while persisting order:"
					+ ce.getMessage());
		}
	}

	

}
