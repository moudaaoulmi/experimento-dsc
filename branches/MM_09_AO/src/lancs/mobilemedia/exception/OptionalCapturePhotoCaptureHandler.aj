package lancs.mobilemedia.exception;

public abstract aspect OptionalCapturePhotoCaptureHandler {
	
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
