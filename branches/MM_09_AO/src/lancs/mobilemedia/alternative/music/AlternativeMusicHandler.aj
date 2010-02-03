package lancs.mobilemedia.alternative.music;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreFullException;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import java.io.InputStream;
import lancs.mobilemedia.alternative.music.PlayMusicScreen;

public privileged aspect AlternativeMusicHandler {
	
	pointcut internalAddNewMediaToAlbumHandler() : execution(* MusicAspect.internalAddNewMediaToAlbum(AlbumData, MediaController));
	
	pointcut internalPlayMultiMediaHandler(): execution(boolean MediaController.internalPlayMultiMedia(String, InputStream));
	
	pointcut playMusicScreenHandler(): execution (PlayMusicScreen.new(MainUIMidlet, InputStream, String, AbstractController));
	
	pointcut startPlayHandler(): execution(void PlayMusicScreen.startPlay());
	
	pointcut pausePlayHandler(): execution(void PlayMusicScreen.pausePlay());
	
	declare soft: InvalidMediaDataException: internalAddNewMediaToAlbumHandler();
	
	declare soft: PersistenceMechanismException: internalAddNewMediaToAlbumHandler() || internalPlayMultiMediaHandler();
	
	declare soft: MediaNotFoundException: internalAddNewMediaToAlbumHandler() || internalPlayMultiMediaHandler();
	
	declare soft: Exception: playMusicScreenHandler() || startPlayHandler() || pausePlayHandler();
	
	void around(MediaController controller): internalAddNewMediaToAlbumHandler() && args(*,controller) {
		try{
			proceed(controller);
		// TODO [EF] Replicated handlers from the method handleCommandAction in MediaController. 
		// TODO Nelio, try to reuse these handlers somehow
		} catch (InvalidMediaDataException e) {
			Alert alert = null;
			if (e instanceof MediaPathNotValidException)
				alert = new Alert("Error", "The path is not valid", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The file format is not valid", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
		} catch (PersistenceMechanismException e) {
			Alert alert = null;
			if (e.getCause() instanceof RecordStoreFullException)
				alert = new Alert("Error", "The mobile database is full", null, AlertType.ERROR);
			else
				alert = new Alert("Error", "The mobile database can not add a new photo", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
			
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert("Error", "The selected item was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
//			return true; // TODO [EF] This should be the return value from method handleCommandAction.
		}
	}
	
	boolean around(MediaController mediaController): internalPlayMultiMediaHandler() && this(mediaController) {
		try{
			return proceed(mediaController);
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert( "Error", "The selected item was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(mediaController.midlet).setCurrent(alert, Display.getDisplay(mediaController.midlet).getCurrent());
		    return false;
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", "The mobile database can open this item 1", null, AlertType.ERROR);
			Display.getDisplay(mediaController.midlet).setCurrent(alert, Display.getDisplay(mediaController.midlet).getCurrent());
			return false;
		}
	}
	
	Object around() : playMusicScreenHandler() {
		try{
			return proceed();
		} catch(Exception e) {
			System.err.println(e);
		}
		return null;
	}
	
	void around() : startPlayHandler() {
		try {
			proceed();
		} catch(Exception e) {
			System.err.println(e);
		}
	}
	
	void around() : pausePlayHandler() {
		try {
			proceed();
		} catch(Exception e) {
			System.err.println(e);
		}
	}

}
