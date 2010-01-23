/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 22 Jul 2007
 * 
 */
package lancs.mobilemedia.optional.copySMS;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreFullException;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
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
 * [EF] Added in Scenario 05
 */
public class PhotoViewController extends AbstractController {

	String mediaName = "";
	
	/**
	 * @param midlet
	 * @param nextController
	 * @param albumData
	 * @param albumListScreen
	 * @param currentScreenName
	 */
	public PhotoViewController(MainUIMidlet midlet, AlbumData albumData, AlbumListScreen albumListScreen, String imageName) {
		super(midlet, albumData, albumListScreen);
		this.mediaName = imageName;
	}

	/* (non-Javadoc)
	 * @see ubc.midp.MobileMedia.core.ui.controller.ControllerInterface#handleCommand(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public boolean handleCommand(Command c) {
		String label = c.getLabel();
		System.out.println( "<* PhotoViewController.handleCommand() *> " + label);

		/** Case: Copy photo to a different album */
		if (label.equals("Copy")) {
			AddMediaToAlbum copyPhotoToAlbum = new AddMediaToAlbum("Copy Photo to Album");
			/* [NC] Added in scenario 06 */
			processCopy(copyPhotoToAlbum);
			Display.getDisplay(midlet).setCurrent(copyPhotoToAlbum);
			return true;
		}
		
		/** Case: Save a copy in a new album */
		else if (label.equals("Save Item")) {
			try {
				String photoname = ((AddMediaToAlbum) getCurrentScreen()).getItemName();
				String albumname = ((AddMediaToAlbum) getCurrentScreen()).getPath();
				MediaData imageData = processImageData(photoname, albumname);
				if (imageData != null)
					this.addImageData(imageData, albumname);
			} catch (InvalidMediaDataException e) {
				Alert alert = null;
				if (e instanceof MediaPathNotValidException)
					alert = new Alert("Error", "The path is not valid", null, AlertType.ERROR);
				else
					alert = new Alert("Error", "The image file format is not valid", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
				return true;
				// alert.setTimeout(5000);
			} catch (PersistenceMechanismException e) {
				Alert alert = null;
				if (e.getCause() instanceof RecordStoreFullException)
					alert = new Alert("Error", "The mobile database is full", null, AlertType.ERROR);
				else
					alert = new Alert("Error", "The mobile database can not add a new photo", null, AlertType.ERROR);
				Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
			}
			((AlbumListScreen) getAlbumListScreen()).repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen( getAlbumListScreen() );
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}else if ((label.equals("Cancel")) || (label.equals("Back"))){
			// [NC] Changed in the scenario 07: just the first line below to support generic AbstractController
			((AlbumListScreen) getAlbumListScreen()).repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen( getAlbumListScreen() );
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}
		
		return false;
	}

	/* [NC] Added as a result of a refactoring in scenario 06 */	
	private void processCopy(AddMediaToAlbum copyPhotoToAlbum) {
		copyPhotoToAlbum.setItemName(mediaName);
		copyPhotoToAlbum.setLabePath("Copy to Album:");
		copyPhotoToAlbum.setCommandListener(this);
	}
	
	/* [NC] Added as a result of a refactoring in scenario 06 */	
	private MediaData processImageData(String photoName, String albumname) throws InvalidMediaDataException, PersistenceMechanismException {
		MediaData imageData = null;
		try {
			imageData = getAlbumData().getMediaInfo(mediaName);
		} catch (MediaNotFoundException e) {
			e.printStackTrace();
		}
		return imageData;
	}

	/* [NC] Added as a result of a refactoring in scenario 06 */
	public void addImageData(MediaData imageData, String albumname) throws InvalidMediaDataException, PersistenceMechanismException {
		this.getAlbumData().addMediaData(imageData, albumname);		
	}
}
