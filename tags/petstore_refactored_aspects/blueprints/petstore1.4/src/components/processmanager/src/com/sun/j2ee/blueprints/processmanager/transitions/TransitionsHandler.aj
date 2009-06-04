/*
 * Created on 26/09/2005
 */
package com.sun.j2ee.blueprints.processmanager.transitions;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect TransitionsHandler {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : ClassNotFoundException : transitionDelegateFactory_getTransitionDelegateHandler();
	declare soft : IllegalAccessException : transitionDelegateFactory_getTransitionDelegateHandler();
	declare soft : InstantiationException : transitionDelegateFactory_getTransitionDelegateHandler();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** TransitionDelegateFactory ***/
	pointcut transitionDelegateFactory_getTransitionDelegateHandler() :  
		execution(public TransitionDelegate TransitionDelegateFactory.getTransitionDelegate(String));
	
	// ---------------------------
    // Advice's
    // ---------------------------
	TransitionDelegate around(String className) throws TransitionException :
		transitionDelegateFactory_getTransitionDelegateHandler() &&
		args(className){
		TransitionDelegate td = null;
		try{
			td = proceed(className);
		} catch(Exception e) {
			throw new TransitionException(e);
		}
		return td;
	}

}
