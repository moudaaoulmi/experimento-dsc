package com.sun.j2ee.blueprints.processmanager.transitions;

public aspect TransitionsHandler {
	
	pointcut getTransitionDelegateHandler() :  
		execution(public TransitionDelegate TransitionDelegateFactory.getTransitionDelegate(String));	
	declare soft : ClassNotFoundException : getTransitionDelegateHandler();
	declare soft : IllegalAccessException : getTransitionDelegateHandler();
	declare soft : InstantiationException : getTransitionDelegateHandler();	 
	after() throwing(Exception e) throws TransitionException :
		getTransitionDelegateHandler() {
		throw new TransitionException(e);
    }
}