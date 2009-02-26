/*
 * Created on 10/11/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sun.j2ee.blueprints.util.aspect;

import javax.jms.JMSException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

/**
 * @author Raquel Maranhao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract aspect TransitionExceptionGenericAspect {

    public abstract pointcut afterServiceLocatorExceptionHandler();
    public abstract pointcut afterJMSExceptionHandler();
    
	after() throwing(ServiceLocatorException se) throws TransitionException : 
	    afterServiceLocatorExceptionHandler() {
        throw new TransitionException(se);
	}
	
	after() throwing(JMSException je) throws TransitionException : 
	    afterJMSExceptionHandler() {
        throw new TransitionException(je);		
	}
    
    
}
