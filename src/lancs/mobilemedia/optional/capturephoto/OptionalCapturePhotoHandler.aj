package lancs.mobilemedia.optional.capturephoto;

import java.lang.Exception;
import lancs.mobilemedia.optional.capture.CaptureVideoScreen;
import lancs.mobilemedia.exception.OptionalCapturePhotoCaptureHandler;

public aspect OptionalCapturePhotoHandler extends OptionalCapturePhotoCaptureHandler {

	public pointcut checkedMechanismException() : execution(byte[] CaptureVideoScreen.takePicture());
	
	//declare soft: Exception: checkedMechanismException();
	
//	byte[] around(): checkedMechanismException(){
//		try {
//			return proceed();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	
}
