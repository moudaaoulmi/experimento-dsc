package br.upe.dsc.reusable.exception;

@ExceptionHandler
public abstract privileged aspect EmptyBlockAbstractExceptionHandling {

	public abstract pointcut emptyBlockException();
	
	Object around(): emptyBlockException(){
		try{
			proceed();
		} catch(RuntimeException e) {
			throw e;
		} catch(Exception e){ }
		return null;
	}
}
