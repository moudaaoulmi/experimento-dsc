package lancs.mobilemedia.optional.smsorcapturephoto;

import lancs.mobilemedia.alternative.photo.ImageMediaAccessor;
import lancs.mobilemedia.exception.CheckedRecordStoreException;

import javax.microedition.rms.RecordStoreException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public privileged aspect OptionalSmsOrCapturePhotoHandler extends CheckedRecordStoreException {
	
	public pointcut checkedRecordStoreException(): execution(void ImageMediaAccessor.addImageData(String, byte[], String));
	
//	pointcut addImageDataHandler(): execution(void ImageMediaAccessor.addImageData(String, byte[], String));
//	
//	declare soft: RecordStoreException: addImageDataHandler();
//	
//	void around() throws PersistenceMechanismException : addImageDataHandler() {
//		try {
//			proceed();
//		} catch (RecordStoreException e) {
//			throw new PersistenceMechanismException();
//		}
//	}

}
