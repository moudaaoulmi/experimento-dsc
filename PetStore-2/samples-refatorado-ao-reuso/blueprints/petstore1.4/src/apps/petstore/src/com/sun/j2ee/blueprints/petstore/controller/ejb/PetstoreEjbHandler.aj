/*
 * Created on 02/09/2005
 */
package com.sun.j2ee.blueprints.petstore.controller.ejb;

import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.cart.ejb.ShoppingCartLocal;
import com.sun.j2ee.blueprints.customer.ejb.CustomerLocal;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

/**
 * @author Raquel Maranhao
 */
public aspect PetstoreEjbHandler {
	
	/*** ShoppingClientFacadeLocalEJB ***/
	pointcut getCustomerHandler() : 
		execution(public CustomerLocal ShoppingClientFacadeLocalEJB.getCustomer());
	
	pointcut createCustomerHandler() : 
		execution(public CustomerLocal ShoppingClientFacadeLocalEJB.createCustomer(String));
	
	pointcut getShoppingCartHandler() : 
		execution(public ShoppingCartLocal ShoppingClientFacadeLocalEJB.getShoppingCart());
	
	/*** ShoppingControllerEJB ***/
	pointcut getShoppingClientFacadeHandler() :  
		execution(public ShoppingClientFacadeLocal ShoppingControllerEJB.getShoppingClientFacade());
	
	
	
	declare soft : ServiceLocatorException : getCustomerHandler() || 
		createCustomerHandler() || 
		getShoppingCartHandler() || 
		getShoppingClientFacadeHandler();
	declare soft : javax.ejb.CreateException : createCustomerHandler() || 
		getShoppingCartHandler() || 
		getShoppingClientFacadeHandler();
	
	
	
	
	CustomerLocal around() throws GeneralFailureException : getCustomerHandler()||createCustomerHandler(){
		try{
			return proceed();
		}catch(ServiceLocatorException slx){
			throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of customer: caught " + slx);
		}
	}

	CustomerLocal around() throws GeneralFailureException : createCustomerHandler(){
		try{
			return proceed();
		}catch(javax.ejb.CreateException ce){
			throw new GeneralFailureException("ShoppingClientFacade: failed to create customer: caught " + ce);
		}
	}

	ShoppingCartLocal around() throws GeneralFailureException : getShoppingCartHandler(){
		try{
			return proceed();
		}catch(ServiceLocatorException slx){
			throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of cart: caught " + slx);
		}catch(javax.ejb.CreateException cx){
			throw new GeneralFailureException("ShoppingClientFacade: failed to create cart: caught " + cx);
		}
	}
	
	ShoppingClientFacadeLocal around() throws GeneralFailureException : getShoppingClientFacadeHandler(){
		try{
			return proceed();
		}catch(ServiceLocatorException slx){
			throw new GeneralFailureException("ShoppingControllerEJB: Failed to Create ShoppingClientFacade: caught " + slx);
		}catch(javax.ejb.CreateException cx){
			throw new GeneralFailureException("ShoppingControllerEJB: Failed to Create ShoppingClientFacade: caught " + cx);
		}
	}
}
