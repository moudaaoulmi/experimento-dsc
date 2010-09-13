package lancs.mobilemedia.optional.capturephoto;
import  java.lang.Exception;
import  lancs.mobilemedia.optional.capture.CaptureVideoScreen;

public aspect OptionalCapturePhotoHandler {

	pointcut takePictureHandler() : execution(byte[] CaptureVideoScreen.takePicture());
	
	declare soft: Exception: takePictureHandler();
	
	byte[] around(): takePictureHandler(){
		try {
			return proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
