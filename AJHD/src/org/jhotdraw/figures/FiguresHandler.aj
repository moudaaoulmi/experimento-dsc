package org.jhotdraw.figures;

import org.jhotdraw.Exception.ExceptionHandler;


@ExceptionHandler
public privileged aspect FiguresHandler {
	
	declare soft : CloneNotSupportedException : FigureAttributes_cloneHandler();
	//declare soft : NumberFormatException : NumberTextFigure_getValueHandler();
	
	
	pointcut FigureAttributes_cloneHandler() : execution(Object FigureAttributes.clone(..));
	pointcut NumberTextFigure_getValueHandler() : execution(int NumberTextFigure.getValue(..));
	
    
    Object around() throws InternalError : FigureAttributes_cloneHandler() {
    	try {
			return proceed();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
    }
    
    int around()  : NumberTextFigure_getValueHandler() {
    	 int value = 0;
    	
    	try {
			value = proceed();
		}
    	catch (NumberFormatException e) {
			value = 0;
		}
		return value;
    }

}
