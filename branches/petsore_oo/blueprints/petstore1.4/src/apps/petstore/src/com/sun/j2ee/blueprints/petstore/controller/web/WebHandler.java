package com.sun.j2ee.blueprints.petstore.controller.web;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.event.EventException;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;


public class WebHandler {
	
	public void getCustomer1Handler(FinderException e){
		System.err.println("PetstoreComponentManager finder error: " + e);
	}
	
	public void getCustomer2Handler(Exception e){
		System.err.println("PetstoreComponentManager error: " + e);
	}

	public void getShoppingControllerHandler(Exception e){
		throw new GeneralFailureException(e.getMessage());
	}
	
	public void destroyHandler(RemoveException re){
		Debug.print(re);
	}
	
	public void processEventHandler(EventException e){
		System.err.println("SignOnNotifier Error handling event " + e);
	}

}
