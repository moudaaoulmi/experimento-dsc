package com.sun.j2ee.blueprints.processmanager.transitions;


public class TransitionsHandler {
	
	public void getTransitionDelegateHandler(Exception e) throws TransitionException {
		throw new TransitionException(e);		
	}

}
