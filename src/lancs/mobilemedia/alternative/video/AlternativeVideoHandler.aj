package lancs.mobilemedia.alternative.video;

import java.io.IOException;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.core.ui.controller.MediaController;
import java.io.InputStream;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.media.MediaException;
import lancs.mobilemedia.alternative.video.VideoMediaAccessor;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.exception.AlternativeMusicVideoHandler;

import javax.microedition.rms.RecordStoreException;

public privileged aspect AlternativeVideoHandler extends AlternativeMusicVideoHandler {
	
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
	
	
	void around(): startVideoHandler() || stopVideo(){
		try{
			proceed();
		} catch(Exception e) {
	    	e.printStackTrace();
	    } 
	}
	
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
	
//	boolean around(MediaController controller): internalPlayVideoMediaHandler() && this(controller){
//		try {
//			return proceed(controller);
//		} catch (MediaNotFoundException e) {
//			Alert alert = new Alert( "Error", "The selected item was not found in the mobile device", null, AlertType.ERROR);
//			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
//		    return false;
//		}
//		catch (PersistenceMechanismException e) {
//			Alert alert = new Alert( "Error", "The mobile database can open this item 1", null, AlertType.ERROR);
//			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
//			return false;
//		}
//	}	
	
	// Unable to reuse because this class already has reuse strategy 
	MediaData around(): internalResetRecordStoreHandler() {
		try {
			return proceed();
		} catch (MediaNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	void around() throws PersistenceMechanismException : addVideoDataHandler() {
		try {
			proceed();
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}
	}

}
