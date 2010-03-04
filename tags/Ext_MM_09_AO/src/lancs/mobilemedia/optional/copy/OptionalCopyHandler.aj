package lancs.mobilemedia.optional.copy;

import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.datamodel.MediaAccessor;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;

public aspect OptionalCopyHandler {
	
	pointcut addMediaDataHandler(): execution(void MediaAccessor.addMediaData(MediaData, String));
	pointcut internalAroundHandleCommandActionHandler(): execution(boolean CopyMultiMediaAspect.internalAroundHandleCommandAction(CopyTargets));	
	pointcut internalAroundHandleCommandActionHandler2() : execution(MediaData internalAroundHandleCommandAction(CopyTargets, MediaData));
	
	declare soft: RecordStoreException: addMediaDataHandler();
	declare soft: InvalidMediaDataException: internalAroundHandleCommandActionHandler();
	declare soft: PersistenceMechanismException: internalAroundHandleCommandActionHandler();
	declare soft: MediaNotFoundException: internalAroundHandleCommandActionHandler2();
	
	
	
	MediaData around(CopyTargets controller, MediaData mediaData): internalAroundHandleCommandActionHandler2() && args(controller, mediaData) {
		try {
			return proceed(controller, mediaData);
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert("Error", "The selected media was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
		}
		return mediaData;
	}
	
	void around(MediaAccessor mediaAccessor) throws PersistenceMechanismException: addMediaDataHandler() && this(mediaAccessor) {
		try {
			proceed(mediaAccessor);
		} catch (RecordStoreException e) {
			throw new PersistenceMechanismException();
		}finally{
			try {
				mediaAccessor.mediaRS.closeRecordStore();
				mediaAccessor.mediaInfoRS.closeRecordStore();
			} catch (RecordStoreNotOpenException e) {
				e.printStackTrace();
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	boolean around(CopyTargets controller) : internalAroundHandleCommandActionHandler() && args(controller){
		try {
			return proceed(controller);
		} catch (InvalidMediaDataException e) {
			Alert alert = null;
			if (e instanceof MediaPathNotValidException)
				alert = new Alert("Error", "The path is not valid", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The music file format is not valid", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
			return true;
			// alert.setTimeout(5000);
		} catch (PersistenceMechanismException e) {
			Alert alert = null;
			if (e.getCause() instanceof RecordStoreFullException)
				alert = new Alert("Error", "The mobile database is full", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The mobile database can not add a new music", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
		}
		return false;
	}

}
