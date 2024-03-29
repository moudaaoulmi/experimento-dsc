package lancs.mobilemedia.exception;

import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;

//@ExceptionHandler
public abstract aspect CheckedInvalidMediaDataExceptionHandler {
	
	public abstract pointcut checkedMechanismException();
	
	declare soft: InvalidMediaDataException: checkedMechanismException();
		
	void around(): checkedMechanismException(){
		try {
			proceed();
		} catch (InvalidMediaDataException e) {
			e.printStackTrace();
		} 
	}
}

// Esse aspecto � para fornecer reuso entre as classes OptionalCaptureVideoHandler  e DataModelAspectEH 
