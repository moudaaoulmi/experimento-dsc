package com.sun.j2ee.blueprints.util.aspect;

import javax.ejb.EJBException;

public abstract aspect EJBExceptionGenericAspect {

    public abstract pointcut afterEJBExceptionHandler();    
	Object around() throws EJBException: afterEJBExceptionHandler(){
		try{
			return proceed();
		}catch(Exception e){
			throw new EJBException(e);
		}
	}    
}
