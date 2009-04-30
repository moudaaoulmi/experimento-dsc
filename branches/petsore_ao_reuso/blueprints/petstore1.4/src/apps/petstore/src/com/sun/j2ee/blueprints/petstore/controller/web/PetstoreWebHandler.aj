/*
 * Created on 18/09/2005
 */
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

/**
 * @author Raquel Maranhao
 */
public aspect PetstoreWebHandler {
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : Exception : getCustomerHandler();
	
//	declare soft : RemoveException : destroyHandler();
	
	declare soft : EventException : internalHandleEventHandler();

	// ---------------------------
    // Pointcut's
    // ---------------------------
	//NumberFormatException is already a RuntimeException
	//declare soft : NumberFormatException : internalGetQuantityHandler();
	/*** PetstoreComponentManager ***/
	pointcut getCustomerHandler() : 
		execution(public CustomerLocal PetstoreComponentManager.getCustomer(HttpSession));
	
	/*** ShoppingWebController ***/
//	pointcut destroyHandler() : 
//		execution(public void ShoppingWebController.destroy(HttpSession));
	
	/*** SignOnNotifier ***/
	pointcut internalHandleEventHandler() : 
		execution(private void SignOnNotifier.internalHandleEvent(WebController, SignOnEvent, HttpSession));
	
	/*** CartHTMLAction ***/
	pointcut internalGetQuantityHandler() : 
		execution(private Integer com.sun.j2ee.blueprints.petstore.controller.web.actions.CartHTMLAction.internalGetQuantity(String));
	
	// ---------------------------
    // Advice's
    // ---------------------------
	CustomerLocal around() : 
		getCustomerHandler() {
	    try {
	        return proceed();
        } catch (Exception e) {
            System.err.println("PetstoreComponentManager error: " + e);
            return null;
        }
	}
	
//	void around() : 
//		destroyHandler() {
//	    try {
//	        proceed();
//        } catch(RemoveException re){
//            // ignore, after all its only a remove() call!
//            Debug.print(re);
//        }
//	}

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
        }
        catch (NumberFormatException nfe) {
            return new Integer(0);
        }
	}
	
}
