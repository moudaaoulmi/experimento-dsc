package lancs.mobilemedia.optional.capture;
import  java.lang.Exception;

import javax.microedition.media.MediaException;

//import lancs.mobilemedia.exception.CheckedExceptionHandler;
//import lancs.mobilemedia.exception.ExceptionHandler;
import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

//@ExceptionHandler
public privileged aspect OptionalCaptureHandler extends PrintStackTraceAbstractExceptionHandler {

//	public pointcut checkedMechanismException() : execution(void CaptureVideoScreen.internalCaptureVideoScreen())
//												||internalCaptureVideoScreenHandler2()
//												||execution(void CaptureVideoScreen.internalSetVisibleVideo());
	
	public pointcut printStackTraceException() : execution(void CaptureVideoScreen.internalCaptureVideoScreen())
												||internalCaptureVideoScreenHandler2()
												||execution(void CaptureVideoScreen.internalSetVisibleVideo());
	
	pointcut internalCaptureVideoScreenHandler2() : execution(void CaptureVideoScreen.internalCaptureVideoScreen2());

	declare soft: MediaException : internalCaptureVideoScreenHandler2();
	
	void around(CaptureVideoScreen captureVideoScreen): internalCaptureVideoScreenHandler2() && this(captureVideoScreen){
		try {
			proceed(captureVideoScreen);
		}catch (MediaException me) {
			captureVideoScreen.videoControl.setDisplayLocation(5, 5);
			try {
				captureVideoScreen.videoControl.setDisplaySize(captureVideoScreen.getWidth() - 10, captureVideoScreen.getHeight() - 10);
			} catch (Exception e) {}
			captureVideoScreen.repaint();
		}
	}
		
}
