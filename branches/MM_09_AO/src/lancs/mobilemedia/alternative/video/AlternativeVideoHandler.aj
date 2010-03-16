package lancs.mobilemedia.alternative.video;

import java.io.IOException;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.core.ui.controller.MediaController;
import java.io.InputStream;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import javax.microedition.media.MediaException;
import lancs.mobilemedia.alternative.video.VideoMediaAccessor;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.exception.CheckedPersistenceAndMediaExceptionHandler;
//import lancs.mobilemedia.exception.ExceptionHandler;

import javax.microedition.rms.RecordStoreException;

//@ExceptionHandler
public privileged aspect AlternativeVideoHandler extends CheckedPersistenceAndMediaExceptionHandler {
	
	// Implementing abstract pointcut. Reuse strategy
	public pointcut checkedPersistenceMechanismAndMediaNotFoundException(): execution(boolean MediaController.internalPlayVideoMedia(String, InputStream));
	
	pointcut internalPlayVideScreenHandler() : execution (void PlayVideoScreen.internalPlayVideoScreenHandler());
	pointcut internalPlayVideScreenHandler3() : execution (void PlayVideoScreen.internalPlayVideoScreenHandler3(int, int));
	pointcut startVideoHandler() : execution(void PlayVideoScreen.startVideo());
	pointcut stopVideo() : execution(void PlayVideoScreen.stopVideo());
	pointcut internalPlayVideScreenHandler2() : execution (void PlayVideoScreen.internalPlayVideoScreenHandler2(AbstractController));
	pointcut inputStreamToBytesHandler(): execution(byte[] VideoMediaAccessor.inputStreamToBytes(InputStream));
	pointcut internalResetRecordStoreHandler(): execution(MediaData VideoMediaAccessor.internalResetRecordStore(MediaData));
	pointcut addVideoDataHandler(): execution(void VideoMediaAccessor.addVideoData(String, String, byte[]));
	
	declare soft: Exception: startVideoHandler() || stopVideo();
	declare soft: IOException: internalPlayVideScreenHandler() || inputStreamToBytesHandler();
	declare soft: MediaException: internalPlayVideScreenHandler() || internalPlayVideScreenHandler3();
	declare soft: MediaNotFoundException: internalResetRecordStoreHandler();
	declare soft: RecordStoreException: addVideoDataHandler();
	
	
	Object around(): startVideoHandler() || stopVideo() || internalResetRecordStoreHandler(){
		try{
			return proceed();
		} catch(RuntimeException e) {
			throw e;
		} catch(Exception e) {
	    	e.printStackTrace();
	    }
		return null;
	}
	
	// Unable to reuse because this class already has reuse strategy 
//	MediaData around(): internalResetRecordStoreHandler() {
//		try {
//			return proceed();
//		} catch (MediaNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	void around(): internalPlayVideScreenHandler() {
		try{
			proceed();
		} catch (Exception e) {
			System.out.println("Error ao criar o player:");
			e.printStackTrace();
		}
	}
	
	void around(): internalPlayVideScreenHandler2() {
		try {
			proceed();
		} catch (Exception e) {
			System.out.println("Error criar or controler" + e.getMessage());
		}
	}
	
	void around(): internalPlayVideScreenHandler3() {
		try {
			proceed();
		} catch (Exception e) {
			System.out.println("Error ao definir a tela");
			e.printStackTrace();
		}
	}
	
	
	void around() throws PersistenceMechanismException : addVideoDataHandler() {
		try {
			proceed();
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}
	}

}
