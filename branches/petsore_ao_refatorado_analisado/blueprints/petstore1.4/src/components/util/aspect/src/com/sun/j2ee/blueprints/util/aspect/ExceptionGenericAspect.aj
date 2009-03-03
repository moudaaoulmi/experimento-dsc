package com.sun.j2ee.blueprints.util.aspect;

public abstract aspect ExceptionGenericAspect {

    public abstract pointcut aroundExceptionDoNothingHandler();    
	Object around() : 
	    aroundExceptionDoNothingHandler() {
		Object retorno = null;
		try {
			retorno = proceed();
		} catch(Exception exception) {
			// Do nothing
		}
		return retorno;
    }	    
}
