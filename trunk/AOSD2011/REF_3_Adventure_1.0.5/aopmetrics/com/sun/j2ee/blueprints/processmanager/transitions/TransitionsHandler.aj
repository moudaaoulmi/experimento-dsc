package com.sun.j2ee.blueprints.processmanager.transitions;

@ExceptionHandler
public privileged aspect TransitionsHandler {

	pointcut getTransitionDelegateHandler(): execution(public TransitionDelegate TransitionDelegateFactory.getTransitionDelegate(String));

	declare soft: Exception :getTransitionDelegateHandler();

	TransitionDelegate around() throws TransitionException : getTransitionDelegateHandler()	 {
		try {
			return proceed();
		} catch (Exception e) {
			throw new TransitionException(e);
		}
	}

}
