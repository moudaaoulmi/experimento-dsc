package lancs.mobilemedia.optional.copySMS;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.lcdui.Command;

import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.exception.OptionalCopySMSCaptureVideoHandler;

import org.aspectj.lang.SoftException;


public aspect OptionalCopySMSHandler extends OptionalCopySMSCaptureVideoHandler{

	public pointcut checkedMechanismException() : execution(MediaData PhotoViewController.internalProcessImageData(MediaData)); 
	pointcut internalHanldeCommand() : execution(void PhotoViewController.internalHandleCommand());
	pointcut handleCommand() : execution(public boolean PhotoViewController.handleCommand(Command));
//	pointcut internalProcessImageData() : ;
	
	declare soft : InvalidMediaDataException : internalHanldeCommand();
	declare soft : PersistenceMechanismException : internalHanldeCommand();
//	declare soft : MediaNotFoundException : internalProcessImageData();
	
	void around(PhotoViewController photoViewController): internalHanldeCommand() && this(photoViewController){
		try {
			proceed(photoViewController);
		} catch (InvalidMediaDataException e) {
			Alert alert = null;
			if (e instanceof MediaPathNotValidException)
				alert = new Alert("Error", "The path is not valid", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The image file format is not valid", null, AlertType.ERROR);
			Display.getDisplay(photoViewController.midlet).setCurrent(alert, Display.getDisplay(photoViewController.midlet).getCurrent());
			throw new SoftException(e);
			//return true;
			// alert.setTimeout(5000);
		} catch (PersistenceMechanismException e) {
			Alert alert = null;
			if (e.getCause() instanceof RecordStoreFullException)
				alert = new Alert("Error", "The mobile database is full", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The mobile database can not add a new photo", null, AlertType.ERROR);
			Display.getDisplay(photoViewController.midlet).setCurrent(alert, Display.getDisplay(photoViewController.midlet).getCurrent());
		}
	}
	
	boolean around() : handleCommand(){
		try{
			return proceed();
		}catch(SoftException e){
			return true;
		}
	}
	
//	MediaData around() : internalProcessImageData(){
//		try {
//			return proceed();
//		} catch (MediaNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
}