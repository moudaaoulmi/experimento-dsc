/*
 * Created on 10/11/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sun.j2ee.blueprints.util.aspect;

import javax.ejb.EJBException;
import javax.jms.JMSException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@ExceptionHandler
public abstract aspect TransitionExceptionGenericAspect {

    public abstract pointcut afterServiceLocatorExceptionHandler();
    public abstract pointcut afterJMSExceptionHandler();
    
    Object around() throws TransitionException : afterServiceLocatorExceptionHandler(){
    	try{
    		return proceed();
    	}catch(ServiceLocatorException se){
    		throw new TransitionException(se);	
    	}
    }
    
	Object around() throws TransitionException : afterJMSExceptionHandler(){
    	try{
    		return proceed();
    	}catch(JMSException je){
    		throw new TransitionException(je);	
    	}
    }
    
    
}
