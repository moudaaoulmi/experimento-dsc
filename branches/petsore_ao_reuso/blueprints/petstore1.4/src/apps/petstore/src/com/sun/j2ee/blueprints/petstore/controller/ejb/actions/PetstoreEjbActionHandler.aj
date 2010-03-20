/*
 * Created on 02/09/2005
 */
package com.sun.j2ee.blueprints.petstore.controller.ejb.actions;

import javax.ejb.CreateException;

import petstore.exception.ExceptionHandler;

import com.sun.j2ee.blueprints.petstore.controller.exceptions.DuplicateAccountException;
import com.sun.j2ee.blueprints.purchaseorder.ejb.PurchaseOrder;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.signon.ejb.SignOnLocal;
import com.sun.j2ee.blueprints.waf.event.Event;
import com.sun.j2ee.blueprints.waf.event.EventException;
import com.sun.j2ee.blueprints.waf.event.EventResponse;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect PetstoreEjbActionHandler {
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : ServiceLocatorException : performHandler();
	
	declare soft : CreateException : performHandler() || 
									 createUserHandler() || 
									 internalSendAMessageHandler();

	declare soft : XMLDocumentException : internalSendAMessageHandler();

	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** CreateUserEJBAction ***/
	pointcut performHandler() :
		execution(public EventResponse CreateUserEJBAction.perform(Event));

	pointcut createUserHandler() : 
		execution(private void CreateUserEJBAction.createUser(String, String, SignOnLocal));

	/*** OrderEJBAction ***/
	pointcut internalSendAMessageHandler() : 
		execution(private void OrderEJBAction.internalSendAMessage(PurchaseOrder));

	// ---------------------------
    // Advice's
    // ---------------------------
	EventResponse around() throws EventException : performHandler(){
		try {
			return proceed();
		} catch (ServiceLocatorException slx) {
			throw new DuplicateAccountException(
					"Failed to Create SignOn EJB: caught " + slx);
		} catch (CreateException cx) {
			throw new DuplicateAccountException(
					"Failed to Create SignOn EJB: caught " + cx);
		}
	}

	void around() throws EventException : createUserHandler(){
		try {
			proceed();
		} catch (CreateException e) {
			throw new DuplicateAccountException("Bad UserName or password");
		}
	}

	void around() : 
		internalSendAMessageHandler() {
		try {
			proceed();
		} catch (XMLDocumentException xde) {
			xde.printStackTrace();
			System.err.println(xde.getRootCause().getMessage());
			// throw new EventResponse or whatever
		} catch (CreateException ce) {
			// throw new AdminBDException(ce.getMessage());
		}
	}
}
