package com.sun.j2ee.blueprints.petstore.controller.web;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.event.EventException;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;


public class WebHandler {
	
	public void SystemErrHandler(String msg,Exception e){
		System.err.println(msg + e);
	}
		
	public void getShoppingControllerHandler(Exception e){
		throw new GeneralFailureException(e.getMessage());
	}
	
	public void destroyHandler(RemoveException re){
		Debug.print(re);
	}
	
	
}
