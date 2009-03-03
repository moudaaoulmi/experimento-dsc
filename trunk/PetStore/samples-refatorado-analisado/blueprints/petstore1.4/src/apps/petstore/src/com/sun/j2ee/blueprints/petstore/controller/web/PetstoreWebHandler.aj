package com.sun.j2ee.blueprints.petstore.controller.web;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.servlet.http.HttpSession;

import com.sun.j2ee.blueprints.customer.ejb.CustomerLocal;
import com.sun.j2ee.blueprints.petstore.controller.ejb.ShoppingControllerLocal;
import com.sun.j2ee.blueprints.petstore.controller.events.SignOnEvent;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.controller.web.WebController;
import com.sun.j2ee.blueprints.waf.event.EventException;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

public aspect PetstoreWebHandler {	

	pointcut getCustomerHandler() : 
		execution(public CustomerLocal PetstoreComponentManager.getCustomer(HttpSession));	
	pointcut getShoppingControllerHandler() :
		execution(public ShoppingControllerLocal PetstoreComponentManager.getShoppingController(HttpSession));
	pointcut destroyHandler() : 
		execution(public void ShoppingWebController.destroy(HttpSession));	
	pointcut internalHandleEventHandler() : 
		execution(private void SignOnNotifier.internalHandleEvent(WebController, SignOnEvent, HttpSession));	
	pointcut internalGetQuantityHandler() : 
		execution(private Integer com.sun.j2ee.blueprints.petstore.controller.web.actions.CartHTMLAction.internalGetQuantity(String));
	declare soft : FinderException : getCustomerHandler();
	declare soft : CreateException : getShoppingControllerHandler();
	declare soft : ServiceLocatorException : getShoppingControllerHandler();
	declare soft : RemoveException : destroyHandler();
	declare soft : EventException : internalHandleEventHandler();
	CustomerLocal around() : 
		getCustomerHandler() {
	    try {
	        return proceed();
        } catch (FinderException e) {
            System.err.println("PetstoreComponentManager finder error: " + e);
            return null;
        } catch (Exception e) {
            System.err.println("PetstoreComponentManager error: " + e);
            return null;
        }
	}	
	Object around() throws GeneralFailureException: getShoppingControllerHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			if(e instanceof RuntimeException){
				throw (RuntimeException)e;
			}
			throw new GeneralFailureException(e.getMessage());
		}
	}	
	void around() : 
		destroyHandler() {
	    try {
	        proceed();
        } catch(RemoveException re){
            Debug.print(re);
        }
	}
	void around() : 
		internalHandleEventHandler() {
	    try {
	        proceed();
        } catch (EventException e) {
            System.err.println("SignOnNotifier Error handling event " + e);
        }    	
	}
	Integer around() : 
		internalGetQuantityHandler() {
	    try {
	        return proceed();
        } catch (NumberFormatException nfe) {
            return new Integer(0);
        }
	}
	
}
