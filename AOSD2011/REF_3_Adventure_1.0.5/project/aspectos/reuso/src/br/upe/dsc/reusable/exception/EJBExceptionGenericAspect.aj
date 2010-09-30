
package br.upe.dsc.reusable.exception;

import javax.ejb.EJBException;

//@ExceptionHandler
public abstract privileged aspect EJBExceptionGenericAspect {

    public abstract pointcut afterEJBExceptionHandler();
    
    Object around() throws EJBException : afterEJBExceptionHandler(){
    	try{
    		return proceed();
    	}catch(RuntimeException e){
    		throw e;
    	}catch(Exception e){
    		throw new EJBException(e);	
    	}
    }
    
}
