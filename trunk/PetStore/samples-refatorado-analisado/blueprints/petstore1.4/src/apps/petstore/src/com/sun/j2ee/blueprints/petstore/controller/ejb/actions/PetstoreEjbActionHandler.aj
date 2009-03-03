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


public aspect PetstoreEjbActionHandler {

	pointcut performHandler() :
		execution(public EventResponse CreateUserEJBAction.perform(Event));
	pointcut createUserHandler() : 
		execution(private void CreateUserEJBAction.createUser(String, String, SignOnLocal));
	pointcut internalGetCustomerHandler() : 
		execution(private CustomerLocal CustomerEJBAction.internalGetCustomer());
	pointcut internalSendAMessageHandler() : 
		execution(private void OrderEJBAction.internalSendAMessage(PurchaseOrder));
	pointcut performSignOnHandler() : 
		execution(public EventResponse SignOnEJBAction.perform(Event));
	declare soft : ServiceLocatorException : performHandler() || 
											 internalSendAMessageHandler();
	declare soft : CreateException : performHandler() || 
									 createUserHandler() || 
									 internalSendAMessageHandler();
	declare soft : FinderException : internalGetCustomerHandler() || 
									 performSignOnHandler();
	declare soft : XMLDocumentException : internalSendAMessageHandler();
	EventResponse around() throws EventException: performHandler(){
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
	void around()throws EventException: createUserHandler(){
		try{
			proceed();
		}catch(CreateException ce){
			throw new DuplicateAccountException("Bad UserName or password");
		}
	}	
	Object around() : 
		internalGetCustomerHandler() || 
		performSignOnHandler() {
		try {
			return proceed();
		} catch (FinderException fe) {
			return null;
		}
	}	
	void around() : 
		internalSendAMessageHandler() {
		try {
			proceed();
		} catch (ServiceLocatorException sle) {
			sle.printStackTrace();	
		} catch (XMLDocumentException xde) {
			xde.printStackTrace();
			System.err.println(xde.getRootCause().getMessage());
		} catch (CreateException ce) {
		}
	}
	
	
}