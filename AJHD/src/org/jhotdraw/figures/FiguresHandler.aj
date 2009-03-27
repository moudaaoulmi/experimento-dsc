package org.jhotdraw.figures;


public privileged aspect FiguresHandler {
	
	pointcut cloneHandler() : execution(public Object FigureAttributes.clone());
	

    declare soft : CloneNotSupportedException : cloneHandler();
    
    
    Object around() throws InternalError : cloneHandler() {
    	try {
			return proceed();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
    }

}
