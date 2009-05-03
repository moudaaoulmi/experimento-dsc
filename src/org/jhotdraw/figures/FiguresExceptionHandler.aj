package org.jhotdraw.figures;



public privileged aspect FiguresExceptionHandler {
	
	declare soft : CloneNotSupportedException : FigureAttributes_cloneHandler();
	
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
