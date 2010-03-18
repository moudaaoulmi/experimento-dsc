package lancs.mobilemedia.optional.smsorcapturephoto;

import lancs.mobilemedia.alternative.photo.ImageMediaAccessor;
import lancs.mobilemedia.exception.CheckedRecordStoreExceptionHandler;
//import lancs.mobilemedia.exception.ExceptionHandler;

//@ExceptionHandler
public privileged aspect OptionalSmsOrCapturePhotoHandler extends CheckedRecordStoreExceptionHandler {
	
	public pointcut checkedRecordStoreException(): execution(void ImageMediaAccessor.addImageData(String, byte[], String));
	
}
