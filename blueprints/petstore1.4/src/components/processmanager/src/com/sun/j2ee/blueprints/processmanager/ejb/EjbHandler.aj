package com.sun.j2ee.blueprints.processmanager.ejb;

import javax.ejb.EJBException;
import javax.naming.NamingException;
import petstore.exception.ExceptionHandler;

@ExceptionHandler
public aspect EjbHandler {

	// ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: NamingException : processManagerEjb_ejbCreate();
    
    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut processManagerEjb_ejbCreate(): 
        execution (* ProcessManagerEJB.ejbCreate(..)) ;
    
    // ---------------------------
    // Advice's
    // ---------------------------
    void around(): processManagerEjb_ejbCreate(){
        try
        {
            proceed();
        }
        catch (NamingException ne)
        {
        	throw new EJBException("Got naming exception! " + ne.getMessage());
        }
    }
}
