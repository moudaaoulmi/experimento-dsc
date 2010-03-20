package lancs.mobilemedia.optional.capturevideo;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;
import lancs.mobilemedia.optional.capture.CaptureVideoScreen;

public aspect OptionalCaptureVideoHandler extends PrintStackTraceAbstractExceptionHandler { 
	
	public pointcut printStackTraceException() : (execution (void VideoCaptureController.internalHandleCommand2(String, String))) || 
												 (execution(void CaptureVideoScreen.pauseCapture())) || 
												 (execution(void CaptureVideoScreen.startCapture())) || 
												 (execution (void VideoCaptureController.internalHandleCommand(String, String)));
	
	declare soft : Exception : printStackTraceException();
}
