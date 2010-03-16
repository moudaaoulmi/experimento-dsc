package lancs.mobilemedia.optional.capturevideo;

import  lancs.mobilemedia.optional.capture.CaptureVideoScreen;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import  java.lang.Exception;


public aspect OptionalCaptureVideoHandler {
	
	pointcut pauseCaptureHandler() : execution(void CaptureVideoScreen.pauseCapture());
	pointcut internalHandleCommand() : execution (void VideoCaptureController.internalHandleCommand(String, String));
	pointcut internalHandleCommand2() : execution (void VideoCaptureController.internalHandleCommand2(String, String));
	pointcut startCaptureHandler() : execution(void CaptureVideoScreen.startCapture());
	
	declare soft: Exception : pauseCaptureHandler() || startCaptureHandler();
	declare soft: InvalidMediaDataException : internalHandleCommand() || internalHandleCommand2();
	declare soft: PersistenceMechanismException : internalHandleCommand() || internalHandleCommand2();
	declare soft: MediaNotFoundException : internalHandleCommand2();
	
	void around(): pauseCaptureHandler() || startCaptureHandler() || internalHandleCommand2() || internalHandleCommand() {
		try {
			proceed();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	void around(): internalHandleCommand2(){
//		try {
//			proceed();
//		} catch (MediaNotFoundException e) {
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
