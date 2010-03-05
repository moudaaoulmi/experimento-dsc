package lancs.mobilemedia.exception;

//@ExceptionHandler
public abstract aspect CheckedExceptionHandler {
	
	public abstract pointcut checkedMechanismException();
	
	declare soft: Exception: checkedMechanismException();
		
	Object around(): checkedMechanismException(){
		try {
			return proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
