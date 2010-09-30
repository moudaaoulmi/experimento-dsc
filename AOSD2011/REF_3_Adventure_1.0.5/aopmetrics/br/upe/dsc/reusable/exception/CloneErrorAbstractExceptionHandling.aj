package br.upe.dsc.reusable.exception;

@ExceptionHandler
public abstract privileged aspect CloneErrorAbstractExceptionHandling {
	
	declare soft : CloneNotSupportedException : cloneNonSuportedGeneral();
	abstract pointcut cloneNonSuportedGeneral();
	
	Object around() throws InternalError : cloneNonSuportedGeneral() {
    	try {
			return proceed();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
    }
}
