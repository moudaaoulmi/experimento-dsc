package br.upe.dsc.reusable.exception;


public abstract aspect PrintStackTraceAbstractExceptionHandler {

	public abstract pointcut printStackTraceException();
	
	declare soft: Exception: printStackTraceException(); 
	
	Object around() : printStackTraceException(){
		try{
			return proceed();
		} catch(RuntimeException e) {
			throw e;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null; 
	}
}