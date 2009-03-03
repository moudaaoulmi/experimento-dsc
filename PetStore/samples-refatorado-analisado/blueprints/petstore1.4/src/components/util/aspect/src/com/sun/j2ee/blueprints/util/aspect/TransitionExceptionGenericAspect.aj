package com.sun.j2ee.blueprints.util.aspect;

import javax.jms.JMSException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

public abstract aspect TransitionExceptionGenericAspect {

    public abstract pointcut afterServiceLocatorExceptionHandler();
    public abstract pointcut afterJMSExceptionHandler();    
	Object around() throws TransitionException : afterServiceLocatorExceptionHandler(){
		try{
			return proceed();
		}catch(ServiceLocatorException e){
			throw new TransitionException(e);
		}
	}	
	Object around() throws TransitionException : afterJMSExceptionHandler(){
		try{
			return proceed();
		}catch(JMSException e){
			throw new TransitionException(e);
		}
	}
}