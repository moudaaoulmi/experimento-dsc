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
	
	/*** PetstoreComponentManager ***/
	pointcut getCustomerHandler() : 
		execution(public CustomerLocal PetstoreComponentManager.getCustomer(HttpSession));
	
	pointcut getShoppingControllerHandler() :
		execution(public ShoppingControllerLocal PetstoreComponentManager.getShoppingController(HttpSession));
	
	/*** ShoppingWebController ***/
	pointcut destroyHandler() : 
		execution(public void ShoppingWebController.destroy(HttpSession));
	
	/*** SignOnNotifier ***/
	pointcut internalHandleEventHandler() : 
		execution(private void SignOnNotifier.internalHandleEvent(WebController, SignOnEvent, HttpSession));
	
	/*** CartHTMLAction ***/
	pointcut internalGetQuantityHandler() : 
		execution(private Integer com.sun.j2ee.blueprints.petstore.controller.web.actions.CartHTMLAction.internalGetQuantity(String));
	
	
	
	declare soft : FinderException : getCustomerHandler();
	declare soft : CreateException : getShoppingControllerHandler();
	declare soft : ServiceLocatorException : getShoppingControllerHandler();
	declare soft : RemoveException : destroyHandler();
	declare soft : EventException : internalHandleEventHandler();
	//NumberFormatException is already a RuntimeException
	//declare soft : NumberFormatException : internalGetQuantityHandler();
	
	
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
	
	ShoppingControllerLocal around() throws GeneralFailureException : 
		getShoppingControllerHandler() {
		try{
			return proceed();
		}catch(CreateException ce){
			throw new GeneralFailureException(ce.getMessage());			
		}catch(ServiceLocatorException ne){
			throw new GeneralFailureException(ne.getMessage());			
		}
	}
	
//	after() throwing(CreateException ce) throws GeneralFailureException : 
//		getShoppingControllerHandler() {
//		throw new GeneralFailureException(ce.getMessage());
//	}
//
//	after() throwing(ServiceLocatorException ne) throws GeneralFailureException : 
//		getShoppingControllerHandler() {
//		throw new GeneralFailureException(ne.getMessage());
//	}
	
	void around() : 
		destroyHandler() {
	    try {
	        proceed();
        } catch(RemoveException re){
            // ignore, after all its only a remove() call!
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
        }
        catch (NumberFormatException nfe) {
            return new Integer(0);
        }
	}
	
}
