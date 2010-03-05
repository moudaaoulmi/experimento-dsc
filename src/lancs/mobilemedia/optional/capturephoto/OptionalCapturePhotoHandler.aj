package lancs.mobilemedia.optional.capturephoto;

import lancs.mobilemedia.exception.CheckedExceptionHandler;
//import lancs.mobilemedia.exception.ExceptionHandler;
import lancs.mobilemedia.optional.capture.CaptureVideoScreen;

//@ExceptionHandler
public aspect OptionalCapturePhotoHandler extends CheckedExceptionHandler {
	
	public pointcut checkedMechanismException() : execution(byte[] CaptureVideoScreen.takePicture());
	
}
