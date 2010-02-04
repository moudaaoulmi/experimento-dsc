package lancs.mobilemedia.optional.capture;
import  java.lang.Exception;

import javax.microedition.media.MediaException;


public privileged aspect OptionalCaptureHandler {

	pointcut internalCaptureVideoScreenHandler() : execution(void CaptureVideoScreen.internalCaptureVideoScreen());
	pointcut internalCaptureVideoScreenHandler2() : execution(void CaptureVideoScreen.internalCaptureVideoScreen2());
	pointcut internalinternalSetVisibleVideoHandler() : execution(void CaptureVideoScreen.internalSetVisibleVideo());
	
	declare soft: Exception : internalCaptureVideoScreenHandler() || internalCaptureVideoScreenHandler2() || internalinternalSetVisibleVideoHandler(); 
	declare soft: MediaException : internalCaptureVideoScreenHandler2();

	void around() : internalCaptureVideoScreenHandler(){
		try {
			proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	void around(CaptureVideoScreen captureVideoScreen): internalCaptureVideoScreenHandler2() && this(captureVideoScreen){
		try {
			proceed(captureVideoScreen);
		}catch (MediaException me) {
			captureVideoScreen.videoControl.setDisplayLocation(5, 5);
			try {
				captureVideoScreen.videoControl.setDisplaySize(captureVideoScreen.getWidth() - 10, captureVideoScreen.getHeight() - 10);
			} catch (Exception e) {
			}
			captureVideoScreen.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void around() : internalinternalSetVisibleVideoHandler(){
		try {
			proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
		
}
