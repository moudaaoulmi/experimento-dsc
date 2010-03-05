package lancs.mobilemedia.optional.capturephoto;

import lancs.mobilemedia.optional.capture.CaptureVideoScreen;
import lancs.mobilemedia.exception.OptionalCapturePhotoCaptureHandler;

public aspect OptionalCapturePhotoHandler extends OptionalCapturePhotoCaptureHandler {

	public pointcut checkedMechanismException() : execution(byte[] CaptureVideoScreen.takePicture());
	
}
