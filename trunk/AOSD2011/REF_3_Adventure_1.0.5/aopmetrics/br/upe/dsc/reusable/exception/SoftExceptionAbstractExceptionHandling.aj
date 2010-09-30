package br.upe.dsc.reusable.exception;

import org.aspectj.lang.*;

import adventure.exception.ExceptionHandler;

@ExceptionHandler
public abstract aspect SoftExceptionAbstractExceptionHandling {
	
	public abstract pointcut emptySoftExceptionHandler();
	
	void around(): emptySoftExceptionHandler(){
		try {
			proceed();
		} catch (SoftException e) {
			// nothing
		}
	}

}
