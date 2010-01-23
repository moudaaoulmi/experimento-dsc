/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 29 Aug 2007
 * 
 */
package lancs.mobilemedia.alternative.music;

import java.io.InputStream;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStoreFullException;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.AlbumController;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.controller.MediaListController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.screens.AddMediaToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.MediaListScreen;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.MediaPathNotValidException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect MusicAspect extends AbstractMusicAspect {

	before(MainUIMidlet midlet): startApp(midlet) {
		// [NC] Added in the scenario 07
		midlet.musicModel = new MusicAlbumData();
		
		// [NC] Added in the scenario 07
		AlbumListScreen albumMusic = new AlbumListScreen();
		midlet.musicRootController = new BaseController(midlet, midlet.musicModel, albumMusic);
		
		MediaListController musicListController = new MediaListController(midlet, midlet.musicModel, albumMusic);
		musicListController.setNextController(midlet.musicRootController);
		
		AlbumController albumMusicController = new AlbumController(midlet, midlet.musicModel, albumMusic);
		albumMusicController.setNextController(musicListController);
		albumMusic.setCommandListener(albumMusicController);
	}
	
	// ********  MediaController  ********* //
	
	//public boolean handleCommand(Command command)
	pointcut handleCommandAction(MediaController controller, Command c):
		execution(public boolean MediaController.handleCommand(Command)) && args(c) && this(controller);
	
	boolean around(MediaController controller, Command c): handleCommandAction(controller, c) {
		boolean handled = proceed(controller, c);
		
		if (handled) return true;
		
		String label = c.getLabel();
		System.out.println("<* MusicAspect.around handleCommandAction *> ::handleCommand: " + label);
		
		// [NC] Added in the scenario 07
		if (label.equals("Play")) {
			String selectedMediaName = controller.getSelectedMediaName();
			return controller.playMultiMedia(selectedMediaName);		
		}
		return false;
	}
	
	// public void addNewMediaToAlbum(String, String, String) 
	pointcut addNewMediaToAlbum(AlbumData albumData, MediaController controller):
		call(public void AlbumData.addNewMediaToAlbum(..)) && target(albumData) && this(controller) 
		&& withincode(public boolean MediaController.handleCommand(Command));
	
	after(AlbumData albumData, MediaController controller) : addNewMediaToAlbum(albumData, controller) {
		try {
			// [NC] Added in the scenario 07
			if (albumData instanceof MusicAlbumData){
				albumData.loadMediaDataFromRMS( controller.getCurrentStoreName());
				MediaData mymedia = albumData.getMediaInfo(((AddMediaToAlbum) controller.getCurrentScreen()).getItemName());
				
				mymedia.setTypeMedia( ((AddMediaToAlbum) controller.getCurrentScreen()).getItemType() );
				albumData.updateMediaInfo(mymedia, mymedia);
			}
			
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
	
	// [NC] Added in the scenario 07
	public boolean MediaController.playMultiMedia(String selectedMediaName) {
		InputStream storedMusic = null;
		try {
			MediaData mymedia = getAlbumData().getMediaInfo(selectedMediaName);
			if (mymedia.getTypeMedia().equals(MediaData.MUSIC)) {
				storedMusic = ((MusicAlbumData) getAlbumData()).getMusicFromRecordStore(getCurrentStoreName(), selectedMediaName);
				PlayMusicScreen playscree = new PlayMusicScreen(midlet, storedMusic, mymedia.getTypeMedia(), this);
				MusicPlayController controller = new MusicPlayController(midlet, getAlbumData(), (AlbumListScreen) getAlbumListScreen(), playscree);
				this.setNextController(controller);
			}
			return true;
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert( "Error", "The selected item was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
		    return false;
		} 
		catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", "The mobile database can open this item 1", null, AlertType.ERROR);
			Display.getDisplay(midlet).setCurrent(alert, Display.getDisplay(midlet).getCurrent());
			return false;
		}
	}
	
	// ********  MediaListScreen  ********* //
	
	// [NC] Added in the scenario 07: to support more than one screen purpose
	public static final int PLAYMUSIC = 2;
	
	// [NC] Added in the scenario 07
	public static final Command playCommand = new Command("Play", Command.ITEM, 1);
	
	// public void initMenu()
	pointcut initMenu(MediaListScreen listScreen):
		execution(public void MediaListScreen.initMenu()) && this(listScreen);
	
	after(MediaListScreen listScreen) : initMenu(listScreen) {
		//Add the core application commands always
		// [NC] Added in the scenario 07: to support more than one screen purpose
		if (listScreen.getTypeOfScreen() == PLAYMUSIC)
			listScreen.addCommand(playCommand);
	}
	
	//public PhotoViewScreen.PhotoViewScreen(Image)
	pointcut constructor(AbstractController controller) :
		call(MediaListScreen.new(..)) && this(controller);

	after(AbstractController controller) returning (MediaListScreen listScreen): constructor(controller) {
		// [NC] Added in the scenario 07	
		if (controller.getAlbumData() instanceof MusicAlbumData)
			listScreen.setTypeOfScreen(PLAYMUSIC);
	}

	// ********  AddMediaToAlbum  ********* //
	
	// [NC] Added in the scenario 07
	TextField AddMediaToAlbum.itemtype = new TextField("Type of media", "", 20, TextField.ANY);
	
	pointcut newAddMediaToAlbum() :
		call(AddMediaToAlbum.new(..));
	
	after() returning (AddMediaToAlbum addMediaToAlbum): newAddMediaToAlbum() {
		// [NC] Added in the scenario 07
		addMediaToAlbum.append(addMediaToAlbum.getItemType());
	}

	// [NC] Added in the scenario 07
	public String AddMediaToAlbum.getItemType() {
		return itemtype.getString();
	}
}
