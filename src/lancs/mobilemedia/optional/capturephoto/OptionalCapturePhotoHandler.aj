package lancs.mobilemedia.optional.capturephoto;

//import lancs.mobilemedia.exception.CheckedExceptionHandler;
//import lancs.mobilemedia.exception.ExceptionHandler;
import lancs.mobilemedia.optional.capture.CaptureVideoScreen;
import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

//@ExceptionHandler
public aspect OptionalCapturePhotoHandler extends PrintStackTraceAbstractExceptionHandler {
	
	//public pointcut checkedMechanismException() : execution(byte[] CaptureVideoScreen.takePicture());
	public pointcut printStackTraceException() : execution(byte[] CaptureVideoScreen.takePicture());
	
}
