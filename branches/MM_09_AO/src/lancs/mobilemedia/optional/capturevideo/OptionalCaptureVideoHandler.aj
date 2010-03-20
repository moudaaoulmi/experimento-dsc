package lancs.mobilemedia.optional.capturevideo;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;
//import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
//import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
//import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.optional.capture.CaptureVideoScreen;

//@ExceptionHandler
public aspect OptionalCaptureVideoHandler extends PrintStackTraceAbstractExceptionHandler { 
	
	//public pointcut checkedMechanismException() : internalHandleCommand2(); 
	
	pointcut startCaptureHandler() : execution(void CaptureVideoScreen.startCapture());
	pointcut pauseCaptureHandler() : execution(void CaptureVideoScreen.pauseCapture());
	pointcut internalHandleCommand() : execution (void VideoCaptureController.internalHandleCommand(String, String));
	pointcut internalHandleCommand2() : execution (void VideoCaptureController.internalHandleCommand2(String, String));
	
	public pointcut printStackTraceException() : internalHandleCommand2() || pauseCaptureHandler() || startCaptureHandler() 
	|| internalHandleCommand();
	
//	declare soft: MediaNotFoundException : printStackTraceException();
//	declare soft: Exception : pauseCaptureHandler() || startCaptureHandler();
//	declare soft: InvalidMediaDataException : internalHandleCommand() || internalHandleCommand2();
//	declare soft: PersistenceMechanismException : internalHandleCommand() || internalHandleCommand2();
	//declare soft: MediaNotFoundException : internalHandleCommand2();
	
//	void around(): pauseCaptureHandler() || startCaptureHandler() 
//					|| internalHandleCommand() || internalHandleCommand2(){
//		try {
//			proceed();
//		} catch(RuntimeException e) {
//			throw e;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	

//	void around(): internalHandleCommand() || internalHandleCommand2(){
//		try {
//			proceed();
//		} catch (InvalidMediaDataException e) {
//			e.printStackTrace();
//		} catch (PersistenceMechanismException e) {
//			e.printStackTrace();
//		}
//	}
	
}
