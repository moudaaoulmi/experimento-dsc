/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 17 Jun 2007
 * 
 */
package lancs.mobilemedia.core.ui.controller;


import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;


import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.screens.AddMediaToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.NewLabelScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

/**
 * @author Eduardo Figueiredo
 * Added in the Scenario 02
 */
public class MediaController extends MediaListController {

	private MediaData media;
	private NewLabelScreen screen;

	public MediaController (MainUIMidlet midlet, AlbumData albumData, AlbumListScreen albumListScreen) {
		super(midlet, albumData, albumListScreen);
	}

	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		System.out.println( "<* PhotoController.handleCommand() *> " + label);
		
		/** Case: Save Add photo * */
		if (label.equals("Add")) {
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ADDPHOTOTOALBUM_SCREEN);
			AddMediaToAlbum form = new AddMediaToAlbum("Add new item to Album");
			form.setCommandListener(this);
			setCurrentScreen(form);
			return true;

		/** Case: Add photo * */
		} else if (label.equals("Save Item")) {
			
			internalHandleCommand();
			
			return goToPreviousScreen();
			/** Case: Delete selected Photo from recordstore * */
		} else if (label.equals("Delete")) {
			String selectedMediaName = getSelectedMediaName();
			
			internalHandleCommand2(selectedMediaName);
			
			showMediaList(getCurrentStoreName());
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
			return true;

		/** Case: Edit photo label
		 *  [EF] Added in the scenario 02 */
		} else if (label.equals("Edit Label")) {
			String selectedImageName = getSelectedMediaName();
			
			internalHandleCommand3(selectedImageName);
			
			return true;
			
			/** Case: Save new Photo Label */
		} else if (label.equals("Save")) {
			String newLabel = this.screen.getLabelName();
			this.getMedia().setMediaLabel(newLabel);
			internalHandleCommand4();
			return goToPreviousScreen();
		
		/** Case: Go to the Previous or Fallback screen * */
		} else if (label.equals("Back")) {
			return goToPreviousScreen();

			/** Case: Cancel the current screen and go back one* */
		} else if (label.equals("Cancel")) {
			return goToPreviousScreen();

		}

		return false;
	}

	private void internalHandleCommand() {
		getAlbumData().addNewMediaToAlbum(((AddMediaToAlbum) getCurrentScreen()).getItemName(), 
				((AddMediaToAlbum) getCurrentScreen()).getPath(), getCurrentStoreName());
	}

	private void internalHandleCommand2(String selectedMediaName) {
		getAlbumData().deleteMedia(getCurrentStoreName(), selectedMediaName);
	}
	
	private void internalHandleCommand3(String selectedImageName) {
		media = getAlbumData().getMediaInfo(selectedImageName);
		ScreenSingleton.getInstance().setCurrentScreenName(Constants.EDIT_LABEL_SCREEN);
		NewLabelScreen formScreen = new NewLabelScreen("Edit Label Item", NewLabelScreen.LABEL_PHOTO);
		formScreen.setCommandListener(this);
		this.setScreen(formScreen);
		setCurrentScreen(formScreen);
		formScreen = null;
	}
	
	private void internalHandleCommand4(){
		updateMedia(media);
	}
	
	// [EF] Scenario 02: Increase visibility (package to public) in order to give access to aspect CountViewsAspect	
	public void updateMedia(MediaData media) throws InvalidMediaDataException, PersistenceMechanismException {
		getAlbumData().updateMediaInfo(media, media);
	}
	
    /**
     * Get the last selected image from the Photo List screen.
	 * TODO: This really only gets the selected List item. 
	 * So it is only an image name if you are on the PhotoList screen...
	 * Need to fix this
	 */
	public String getSelectedMediaName() {
		List selected = (List) Display.getDisplay(midlet).getCurrent();
		if (selected == null)
		    System.out.println("Current List from display is NULL!");
		String name = selected.getString(selected.getSelectedIndex());
		return name;
	}
	
	/**
	 * TODO rename to getMediaController
	 * [EF] Update in scenario 05, expose join point to CopyPhotoAspect and SMSAspect
	 * @param mediaName
	 * @return
	 */
	public AbstractController getMediaController(String imageName) {
		return this;
	}
	
   /**
    * TODO [EF] update this method or merge with method of super class.
     * Go to the previous screen
	 */
    private boolean goToPreviousScreen() {
	    System.out.println("<* MediaController.goToPreviousScreen() *>");
		String currentScreenName = ScreenSingleton.getInstance().getCurrentScreenName();
	    if (currentScreenName.equals(Constants.ALBUMLIST_SCREEN)) {
		    System.out.println("Can't go back here...Should never reach this spot");
		} else if (currentScreenName.equals(Constants.IMAGE_SCREEN)) {		    
		    //Go to the image list here, not the main screen...
		    showMediaList(getCurrentStoreName());
		    ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
		    return true;
		}
    	else if ( (currentScreenName.equals(Constants.ADDPHOTOTOALBUM_SCREEN)) ||
    			(currentScreenName.equals(Constants.EDIT_LABEL_SCREEN)) ){
    		showMediaList(getCurrentStoreName());
		    ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGELIST_SCREEN);
		    return true;
    	}
	    return false;
    } 

	/**
	 * @param image the image to set
	 */
	public void setMedia(MediaData media) {
		this.media = media;
	}

	/**
	 * @return the image
	 */
	public MediaData getMedia() {
		return media;
	}

	public void setScreen(NewLabelScreen screen) {
		this.screen = screen;
	}

	public NewLabelScreen getScreen() {
		return screen;
	}
}
