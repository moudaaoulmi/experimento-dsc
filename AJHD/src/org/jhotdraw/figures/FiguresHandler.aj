package org.jhotdraw.figures;


public privileged aspect FiguresHandler {
	
	pointcut FigureAttributes_clone() : execution(public Object FigureAttributes.clone());
	

    declare soft : CloneNotSupportedException : FigureAttributes_clone();
    
    
    Object around() throws InternalError : FigureAttributes_clone() {
    	try {
			return proceed();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
    }

}
