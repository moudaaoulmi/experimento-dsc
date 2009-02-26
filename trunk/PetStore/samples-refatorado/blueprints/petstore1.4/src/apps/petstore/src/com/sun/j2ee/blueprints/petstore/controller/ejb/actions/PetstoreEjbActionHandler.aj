/*
 * Created on 02/09/2005
 */
package com.sun.j2ee.blueprints.petstore.controller.ejb.actions;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.customer.ejb.CustomerLocal;
import com.sun.j2ee.blueprints.petstore.controller.exceptions.DuplicateAccountException;
import com.sun.j2ee.blueprints.purchaseorder.ejb.PurchaseOrder;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.signon.ejb.SignOnLocal;
import com.sun.j2ee.blueprints.uidgen.ejb.UniqueIdGeneratorLocal;
import com.sun.j2ee.blueprints.waf.event.Event;
import com.sun.j2ee.blueprints.waf.event.EventException;
import com.sun.j2ee.blueprints.waf.event.EventResponse;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

/**
 * @author Raquel Maranhao
 */
public aspect PetstoreEjbActionHandler {

	/*** CreateUserEJBAction ***/
	pointcut performHandler() :
		execution(public EventResponse CreateUserEJBAction.perform(Event));
	
	pointcut createUserHandler() : 
		execution(private void CreateUserEJBAction.createUser(String, String, SignOnLocal));

	/*** CustomerEJBAction ***/
	pointcut internalGetCustomerHandler() : 
		execution(private CustomerLocal CustomerEJBAction.internalGetCustomer());
	
	/*** OrderEJBAction ***/
	pointcut getUniqueIdGeneratorHandler() : 
		execution(private UniqueIdGeneratorLocal OrderEJBAction.getUniqueIdGenerator());
	
	pointcut internalSendAMessageHandler() : 
		execution(private void OrderEJBAction.internalSendAMessage(PurchaseOrder));
	
	/*** SignOnEJBAction ***/
	pointcut performSignOnHandler() : 
		execution(public EventResponse SignOnEJBAction.perform(Event));
		
	
	declare soft : ServiceLocatorException : performHandler() || 
		getUniqueIdGeneratorHandler() || 
		internalSendAMessageHandler();
	declare soft : CreateException : performHandler() || 
		createUserHandler() || 
		getUniqueIdGeneratorHandler() || 
		internalSendAMessageHandler();
	declare soft : FinderException : internalGetCustomerHandler() || 
		performSignOnHandler();
	declare soft : XMLDocumentException : internalSendAMessageHandler();
	
	
	EventResponse around() throws EventException : performHandler(){
		try{
			return proceed();			
		}catch(ServiceLocatorException slx){
			throw new DuplicateAccountException("Failed to Create SignOn EJB: caught " + slx);
		}catch(CreateException cx){
			throw new DuplicateAccountException("Failed to Create SignOn EJB: caught " + cx);
		}
	}
	
//	after() throwing(ServiceLocatorException slx) throws EventException : 
//		performHandler() {
//		throw new DuplicateAccountException("Failed to Create SignOn EJB: caught " + slx);
//	}
//	
//	after() throwing(CreateException cx) throws EventException : 
//		performHandler() {
//		throw new DuplicateAccountException("Failed to Create SignOn EJB: caught " + cx);
//	}

	void around() throws EventException : createUserHandler(){
		try{
			proceed();
		}catch(CreateException e){
			throw new DuplicateAccountException("Bad UserName or password");	
		}
	}
	
	//Two separate blocks try-catch into a method handling same exception CreateException in different ways.
//	after() throwing(CreateException ce) throws EventException : 
//		createUserHandler() {
//		throw new DuplicateAccountException("Bad UserName or password");
//	}
	
	Object around() : 
		internalGetCustomerHandler() || 
		performSignOnHandler() {
	    try {
	        return proceed();
	    } catch (FinderException fe) {
	    	return null;
	    }
	}
	
	
	/*
	CustomerLocal around() : 
		internalGetCustomerHandler() {
	    try {
	        return proceed();
	    } catch (FinderException fe) {
	    	return null;
	    }
	}
	
	EventResponse around() : 
		performSignOnHandler() {
	    try {
	        return proceed();
        } catch ( FinderException fe) {
            // in the process of creating a user for the first time
            // so the locale will be se when creating the user
        	return null;
        }
	}
	*/
	
	UniqueIdGeneratorLocal around() : 
		getUniqueIdGeneratorHandler() {
	    try {
	        return proceed();
	      } catch (CreateException cx) {
	        cx.printStackTrace();
	        return null;
	      } catch (ServiceLocatorException slx) {
	        slx.printStackTrace();
	        return null;
	      }  	
	}

	void around() : 
		internalSendAMessageHandler() {
	    try {
	        proceed();
	    } catch (ServiceLocatorException sle) {
	      sle.printStackTrace();
	      // throw new AdminBDException(sle.getMessage());
	    } catch (XMLDocumentException xde) {
	      xde.printStackTrace();
	      System.err.println(xde.getRootCause().getMessage());        
	      // throw new EventResponse    or whatever
	    }  catch (CreateException ce) {
	      //throw new AdminBDException(ce.getMessage());
	    }  	
	}
	
	
	
}
