package com.sun.j2ee.blueprints.petstore.controller.ejb;


import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

public class EjbHandler {
	
	public void getCustomerHandler(ServiceLocatorException slx) {
		throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of customer: caught " + slx);
	}
	
	public void createCustomerHandler(javax.ejb.CreateException ce) {
		throw new GeneralFailureException("ShoppingClientFacade: failed to create customer: caught " + ce);
	}
	
	public void getShoppingCart1Handler(javax.ejb.CreateException ce) {
		throw new GeneralFailureException("ShoppingClientFacade: failed to create cart: caught " + ce);
	}

	public void getShoppingCart2Handler(ServiceLocatorException slx) {
		throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of cart: caught " + slx);
	}
	
	public void shoppingClientFacadeLocal1Handler(Exception e) {
		throw new GeneralFailureException("ShoppingControllerEJB: Failed to Create ShoppingClientFacade: caught " + e);
	}
	
	

	

	
}
