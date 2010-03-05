package lancs.mobilemedia.exception;

import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;

//@ExceptionHandler
public abstract aspect CheckedMediaNotFoundExceptionHandler {

	public abstract pointcut checkedMechanismException();
	
	declare soft: MediaNotFoundException : checkedMechanismException();
	
	Object around() : checkedMechanismException(){
		try {
			return proceed();
		} catch (MediaNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
