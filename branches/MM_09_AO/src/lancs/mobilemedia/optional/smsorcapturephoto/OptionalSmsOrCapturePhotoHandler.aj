package lancs.mobilemedia.optional.smsorcapturephoto;

import lancs.mobilemedia.alternative.photo.ImageMediaAccessor;
import lancs.mobilemedia.exception.CheckedRecordStoreException;

public privileged aspect OptionalSmsOrCapturePhotoHandler extends CheckedRecordStoreException {
	
	public pointcut checkedRecordStoreException(): execution(void ImageMediaAccessor.addImageData(String, byte[], String));
	
}
