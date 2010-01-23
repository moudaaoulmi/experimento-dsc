/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 13 Aug 2007
 * 
 */
package lancs.mobilemedia.optional.copy;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreFullException;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.screens.AddMediaToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

/**
 * @author Eduardo Figueiredo
 * [EF] Become privileged in order to have access to private attributes 
 * in classes AlbumData, MediaAccessor and PlayMusicScreen.
 * 
 * TODO EF: remove?? What is the difference from this aspect and CopyPhotoAspect?
 */
public abstract aspect CopyMultiMediaAspect {

	// ********  AlbumData  ********* //
	
	/**
	 * @param mediaData
	 * @param albumname
	 * @throws InvalidMediaDataException
	 * @throws PersistenceMechanismException
	 */
	public void AlbumData.addMediaData(MediaData mediaData, String albumname) throws InvalidMediaDataException, PersistenceMechanismException {
		mediaAccessor.addMediaData(mediaData, albumname); 
	}
	
	public pointcut playMultiMedia(String selectedMediaName) :
		call(public boolean MediaController.playMultiMedia(String)) && args(selectedMediaName);

	private String mediaName = "";
	
	before(String selectedMediaName): playMultiMedia(selectedMediaName) {
		mediaName = selectedMediaName;
	}

	public abstract pointcut handleCommandAction(CopyTargets controller, Command c);

	boolean around(CopyTargets controller, Command c): handleCommandAction(controller, c) {
		boolean handled = proceed(controller, c);
		
		if (handled) return true;
		
		String label = c.getLabel();
		System.out.println("<* CopyMultiMediaAspect.handleCommandAction() *> " + label);
		
	  	 // [NC] Added in the scenario 07
		/** Case: Copy photo to a different album */
		if (label.equals("Copy")) {
			AddMediaToAlbum copyPhotoToAlbum = new AddMediaToAlbum("Copy Media to Album");
			copyPhotoToAlbum.setItemName(mediaName);
			copyPhotoToAlbum.setLabePath("Copy to Album:");
			copyPhotoToAlbum.setCommandListener(controller);
	        Display.getDisplay(controller.midlet).setCurrent(copyPhotoToAlbum);
			return true;
			
		} else if (label.equals("Save Item")) {
			try {
				MediaData mediaData = null;	
				try {
					mediaData = controller.getAlbumData().getMediaInfo(mediaName);
				} catch (MediaNotFoundException e) {
					Alert alert = new Alert("Error", "The selected media was not found in the mobile device", null, AlertType.ERROR);
					Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
				}
				String albumname = ((AddMediaToAlbum) controller.getCurrentScreen()).getPath();
				String newMediaName = ((AddMediaToAlbum) controller.getCurrentScreen()).getItemName();
				mediaData.setMediaLabel(newMediaName);
				controller.getAlbumData().addMediaData(mediaData, albumname); 
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
			// [NC] Changed in the scenario 07: just the first line below to support generic AbstractController
			((AlbumListScreen) controller.getAlbumListScreen()).repaintListAlbum(controller.getAlbumData().getAlbumNames());
			controller.setCurrentScreen( controller.getAlbumListScreen() );
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}
		return false;
	}
}
