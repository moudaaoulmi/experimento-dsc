/*
 * Created on 09/11/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sun.j2ee.blueprints.util.aspect;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@ExceptionHandler
public abstract aspect ExceptionGenericAspect {

    public abstract pointcut aroundExceptionDoNothingHandler();
    
	void around() : 
	    aroundExceptionDoNothingHandler() {
		try {
			proceed();
		} catch(Exception exception) {
			//Do nothing
		}
    }	
    
}
