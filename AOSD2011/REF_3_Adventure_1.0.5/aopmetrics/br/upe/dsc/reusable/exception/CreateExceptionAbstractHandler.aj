package br.upe.dsc.reusable.exception;

import javax.ejb.CreateException;


@ExceptionHandler
public abstract privileged aspect CreateExceptionAbstractHandler {

	public abstract pointcut createExceptionHandler();

	Object around() throws CreateException : createExceptionHandler(){
		try {
			return proceed();
		} catch (Exception e) {
			throw new CreateException(e.getMessage());
		}
	}

}
