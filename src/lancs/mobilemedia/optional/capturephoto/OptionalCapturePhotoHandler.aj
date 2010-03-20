package lancs.mobilemedia.optional.capturephoto;

import lancs.mobilemedia.optional.capture.CaptureVideoScreen;
import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

public aspect OptionalCapturePhotoHandler extends PrintStackTraceAbstractExceptionHandler {
	
	public pointcut printStackTraceException() : execution(byte[] CaptureVideoScreen.takePicture());
	
	declare soft: Exception : printStackTraceException();
	
}
