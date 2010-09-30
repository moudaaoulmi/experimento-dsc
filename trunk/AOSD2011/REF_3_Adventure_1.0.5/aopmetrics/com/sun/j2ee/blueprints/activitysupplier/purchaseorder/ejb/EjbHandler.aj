package com.sun.j2ee.blueprints.activitysupplier.purchaseorder.ejb;


import javax.ejb.CreateException;
import com.sun.j2ee.blueprints.activitysupplier.powebservice.ActivityOrder;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

@ExceptionHandler
public privileged aspect EjbHandler {

	pointcut ejbPostCreateHandler(): execution(public void ActivityPurchaseOrderBean.ejbPostCreate(ActivityOrder));

	void around() throws CreateException : ejbPostCreateHandler()  {
		try {
			proceed();
		} catch (ServiceLocatorException se) {
			throw new CreateException("Exception saving Activity PO:"
					+ se.getMessage());
		}
	}

}
