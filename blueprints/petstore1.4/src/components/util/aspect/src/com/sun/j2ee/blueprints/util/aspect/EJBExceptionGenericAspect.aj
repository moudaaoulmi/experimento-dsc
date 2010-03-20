/*
 * Created on 10/11/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sun.j2ee.blueprints.util.aspect;

import javax.ejb.EJBException;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@ExceptionHandler
public abstract aspect EJBExceptionGenericAspect {

    public abstract pointcut afterEJBExceptionHandler();
    
    Object around() throws EJBException : afterEJBExceptionHandler(){
    	try{
    		return proceed();
    	}catch(Exception e){
    		throw new EJBException(e);	
    	}
    }
    
}