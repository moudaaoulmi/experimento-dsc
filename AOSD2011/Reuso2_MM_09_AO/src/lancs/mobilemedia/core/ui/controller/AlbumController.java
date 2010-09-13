/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 22 Jun 2007
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
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.NewLabelScreen;
import lancs.mobilemedia.core.util.Constants;

/**
 * @author Eduardo Figueiredo
 * Added in the Scenario 04.
 * Purpose: simplify method handleCommand() in the BaseController.
 */
public class AlbumController extends AbstractController {

	public AlbumController(MainUIMidlet midlet, AlbumData albumData, AlbumListScreen albumListScreen) {
		super(midlet, albumData, albumListScreen);
	}
	
	/* (non-Javadoc)
	 * @see ubc.midp.MobileMedia.core.ui.controller.ControllerInterface#handleCommand(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public boolean handleCommand(Command command) {
		String label = command.getLabel();
      	System.out.println( "<* AlbumController.handleCommand() *> " + label);
		
      	if (label.equals("Reset")) {
			System.out.println("<* BaseController.handleCommand() *> Reset Photo Album");			
		    resetMediaData();
		    ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		/** Case: Create PhotoAlbum **/
		}else if (label.equals("New Album")) {
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.NEWALBUM_SCREEN);
			NewLabelScreen canv = new NewLabelScreen("Add new Photo Album", NewLabelScreen.NEW_ALBUM);
			canv.setCommandListener(this);
			setCurrentScreen(canv);
			canv = null;
			return true;
		/** Case: Delete Album Photo**/
		}else if (label.equals("Delete Album")) {
			List down = (List) Display.getDisplay(midlet).getCurrent();
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.CONFIRMDELETEALBUM_SCREEN);
			ScreenSingleton.getInstance().setCurrentStoreName(down.getString(down.getSelectedIndex()));
			String message = "Would you like to remove the album "+ScreenSingleton.getInstance().getCurrentStoreName();
			Alert deleteConfAlert = new Alert("Delete Photo Album", message,null,AlertType.CONFIRMATION);
			deleteConfAlert.setTimeout(Alert.FOREVER);
			deleteConfAlert.addCommand(new Command("Yes - Delete", Command.OK, 2));
			deleteConfAlert.addCommand(new Command("No - Delete", Command.CANCEL, 2));
			setAlbumListAsCurrentScreen(deleteConfAlert);
			deleteConfAlert.setCommandListener(this);
			return true;	
		/**
		 *  Case: Yes delete Photo Album  **/
		}else if (label.equals("Yes - Delete")) {
				return deleteDefault();
		/** 
		 * Case: No delete Photo Album **/
		}else if (label.equals("No - Delete")) {
			goToPreviousScreen();
			return true;	
		/** 
		 * Case: Save new Photo Album  **/
		} else if (label.equals("Save")) {
			return saveDefault();
		}
		return false;
	}
	
    /**
	 * This option is mainly for testing purposes. If the record store
	 * on the device or emulator gets into an unstable state, or has too 
	 * many images, you can reset it, which clears the record stores and
	 * re-creates them with the default images bundled with the application 
	 */
	private void resetMediaData() {
		if (getAlbumData() == null) System.out.println("<* AlbumController.resetMediaData() *> getAlbumData() == null");
       	getAlbumData().resetMediaData();
     
		if (getAlbumListScreen() == null) System.out.println("<* AlbumController.resetMediaData() *> getAlbumListScreen() == null");
        //Clear the names from the album list
        for (int i = 0; i < getAlbumListScreen().size(); i++) {
        	getAlbumListScreen().delete(i);
        }
        
        //Get the default ones from the album
        String[] albumNames = getAlbumData().getAlbumNames();
		if (albumNames == null) System.out.println("<* AlbumController.resetMediaData() *> albumNames == null");
        for (int i = 0; i < albumNames.length; i++) {
        	if (albumNames[i] != null) {
        		//Add album name to menu list
        		getAlbumListScreen().append(albumNames[i], null);
        	}
        }
        setCurrentScreen(getAlbumListScreen());
    }
	
	
	public boolean saveDefault(){
		internalSaveDefault();
		goToPreviousScreen();
		return true;
	}

	private void internalSaveDefault(){ 
		if (getCurrentScreen() instanceof NewLabelScreen) {
			NewLabelScreen currentScreen = (NewLabelScreen)getCurrentScreen();
			if (currentScreen.getFormType() == NewLabelScreen.NEW_ALBUM)
				getAlbumData().createNewAlbum(currentScreen.getLabelName());
			else if (currentScreen.getFormType() == NewLabelScreen.LABEL_PHOTO) {}
		}
	}
	
	public boolean deleteDefault(){
		internalDeleteDefault();	
		goToPreviousScreen();
		return true;
	}
	
	private void internalDeleteDefault(){
		getAlbumData().deleteAlbum(ScreenSingleton.getInstance().getCurrentStoreName());
	}
	


    private void goToPreviousScreen() {
	    System.out.println("<* AlbumController.goToPreviousScreen() *>");
	 // [NC] Changed in the scenario 07: just the first line below to support generic AbstractController
	    ((AlbumListScreen) getAlbumListScreen()).repaintListAlbum(getAlbumData().getAlbumNames());
		setCurrentScreen( getAlbumListScreen() );
		ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
    }
}
