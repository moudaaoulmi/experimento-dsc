package com.sun.j2ee.blueprints.petstore.controller.ejb;

import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.cart.ejb.ShoppingCartLocal;
import com.sun.j2ee.blueprints.customer.ejb.CustomerLocal;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.aspect.AspectUtil;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

public aspect PetstoreEjbHandler {
	
	pointcut CustomerAndShoppingHandler() : execution(public CustomerLocal ShoppingClientFacadeLocalEJB.getCustomer())
										|| execution(public CustomerLocal ShoppingClientFacadeLocalEJB.createCustomer(String))
										|| execution(public ShoppingCartLocal ShoppingClientFacadeLocalEJB.getShoppingCart())
										|| execution(public ShoppingClientFacadeLocal ShoppingControllerEJB.getShoppingClientFacade());	
	declare soft : javax.ejb.CreateException : CustomerAndShoppingHandler();
	declare soft : ServiceLocatorException : CustomerAndShoppingHandler();	
	Object around(): CustomerAndShoppingHandler(){
		try{
			return proceed();
		}catch(ServiceLocatorException slx){
			if(AspectUtil.verifyJointPoint(thisJoinPoint,"public CustomerLocal ShoppingClientFacadeLocalEJB.getCustomer()")){
				throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of customer: caught " + slx);
			}else if(AspectUtil.verifyJointPoint(thisJoinPoint,"public CustomerLocal ShoppingClientFacadeLocalEJB.createCustomer(String)")){
				throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of customer: caught " + slx);
			}else if(AspectUtil.verifyJointPoint(thisJoinPoint,"public ShoppingCartLocal ShoppingClientFacadeLocalEJB.getShoppingCart()")){
				throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of cart: caught " + slx);
			}else{
				throw new GeneralFailureException("ShoppingControllerEJB: Failed to Create ShoppingClientFacade: caught " + slx);
			}				
		}catch(javax.ejb.CreateException ce){			
			if(AspectUtil.verifyJointPoint(thisJoinPoint,"public CustomerLocal ShoppingClientFacadeLocalEJB.createCustomer(String)")){
				throw new GeneralFailureException("ShoppingClientFacade: failed to create customer: caught " + ce);
			}else if(AspectUtil.verifyJointPoint(thisJoinPoint,"public ShoppingCartLocal ShoppingClientFacadeLocalEJB.getShoppingCart()")){
				throw new GeneralFailureException("ShoppingClientFacade: failed to create cart: caught " + ce);
			}else{
				throw new GeneralFailureException("ShoppingControllerEJB: Failed to Create ShoppingClientFacade: caught " + ce);
			}			
		}			
	}	
}