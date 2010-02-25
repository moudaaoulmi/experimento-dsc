package lancs.mobilemedia.core.ui.controller;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Command;
import javax.microedition.rms.RecordStoreFullException;
import org.aspectj.lang.SoftException;

import lancs.mobilemedia.lib.exceptions.InvalidAlbumNameException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.core.ui.controller.AbstractController;




public privileged aspect CoreUiControllerHandler {

	private String[] messages = {"The mobile database can not delete this photo album", "The mobile database can not delete this photo"};
	
	pointcut internalDeleteDefaultHandler() : execution(void AlbumController.internalDeleteDefault());
	pointcut internalHandleCommandHandler4() : execution(void MediaController.internalHandleCommand4());
	pointcut saveDefaultHandler() : execution(boolean AlbumController.saveDefault());
	pointcut internalSaveDefaultHandler() : execution(void AlbumController.internalSaveDefault());	
	pointcut handleCommandHandler() : execution(boolean MediaController.handleCommand(Command));
	pointcut internalHandleCommandHandler() : execution(void MediaController.internalHandleCommand());
	pointcut internalHandleCommandHandler2() : execution(void MediaController.internalHandleCommand2(String));
	pointcut internalHandleCommandHandler3() : execution(void MediaController.internalHandleCommand3(String));
	
	declare soft: PersistenceMechanismException: internalSaveDefaultHandler() || internalDeleteDefaultHandler() || internalHandleCommandHandler() || internalHandleCommandHandler2() || internalHandleCommandHandler4();
	declare soft: InvalidAlbumNameException: internalSaveDefaultHandler();
	declare soft: InvalidMediaDataException: internalHandleCommandHandler() || internalHandleCommandHandler4();
	declare soft: MediaNotFoundException: internalHandleCommandHandler2() || internalHandleCommandHandler3();
	
	void around(AlbumController albumController): internalSaveDefaultHandler() && this(albumController){
		try {			
			proceed(albumController);
		} catch (PersistenceMechanismException e) {
			Alert alert = null;
			if (e.getCause() instanceof  RecordStoreFullException)
				alert = new Alert( "Error", "The mobile database is full", null, AlertType.ERROR);
			else
				alert = new Alert( "Error", "The mobile database can not add a new photo album", null, AlertType.ERROR);
			Display.getDisplay(albumController.midlet).setCurrent(alert, Display.getDisplay(albumController.midlet).getCurrent());
			throw new SoftException(e);
			//return true;
	    } catch (InvalidAlbumNameException e) {
	    	Alert alert = new Alert( "Error", "You have provided an invalid Photo Album name", null, AlertType.ERROR);
			Display.getDisplay(albumController.midlet).setCurrent(alert, Display.getDisplay(albumController.midlet).getCurrent());
			throw new SoftException(e);
			//return true;
		}
	}
	
	void around(MediaController mediaController): internalHandleCommandHandler() && this(mediaController){
		try {
			proceed(mediaController);
		} catch (InvalidMediaDataException e) {
			Alert alert = null;
			if (e instanceof MediaPathNotValidException)
				alert = new Alert("Error", "The path is not valid", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The file format is not valid", null, AlertType.ERROR);
			Display.getDisplay(mediaController.midlet).setCurrent(alert, Display.getDisplay(mediaController.midlet).getCurrent());
			throw new SoftException(e);
			//return true;
			// alert.setTimeout(5000);
		} catch (PersistenceMechanismException e) {
			Alert alert = null;
			if (e.getCause() instanceof RecordStoreFullException)
				alert = new Alert("Error", "The mobile database is full", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The mobile database can not add a new photo", null, AlertType.ERROR);
			Display.getDisplay(mediaController.midlet).setCurrent(alert, Display.getDisplay(mediaController.midlet).getCurrent());
		}
	}
	
	boolean around(): saveDefaultHandler() || handleCommandHandler(){
		try{
			return proceed();
		}catch(SoftException e){
			return true;
		}
	}
	
	void around(AbstractController albumController) : (internalDeleteDefaultHandler() || internalHandleCommandHandler4()) && this(albumController){ 
		try {
			proceed(albumController);	
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", messages[thisEnclosingJoinPointStaticPart.getId()], null, AlertType.ERROR);
	        Display.getDisplay(albumController.midlet).setCurrent(alert, Display.getDisplay(albumController.midlet).getCurrent());
		}
	}
	
	void around(MediaController mediaController) : internalHandleCommandHandler4()&& this(mediaController){
		try {				
			proceed(mediaController);
		} catch (InvalidMediaDataException e) {
			Alert alert = null;
			if (e instanceof MediaPathNotValidException)
				alert = new Alert("Error", "The path is not valid", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The image file format is not valid", null, AlertType.ERROR);
			Display.getDisplay(mediaController.midlet).setCurrent(alert, Display.getDisplay(mediaController.midlet).getCurrent());
		} 
	}
	
	void around(MediaController mediaController): internalHandleCommandHandler2() && this(mediaController){
		try {
			proceed(mediaController);
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert("Error", "The mobile database can not delete this item", null, AlertType.ERROR);
			Display.getDisplay(mediaController.midlet).setCurrent(alert, Display.getDisplay(mediaController.midlet).getCurrent());
			throw new SoftException(e);
			//return true;
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert("Error", "The selected item was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(mediaController.midlet).setCurrent(alert, Display.getDisplay(mediaController.midlet).getCurrent());
			throw new SoftException(e);
			//return true;
		}
	}
	
	void around(MediaController mediaController) : internalHandleCommandHandler3() && this(mediaController){
		try {
			proceed(mediaController);		
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert( "Error", "The selected item was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(mediaController.midlet).setCurrent(alert, Display.getDisplay(mediaController.midlet).getCurrent());
		}
	}
	
	
}
