package lancs.mobilemedia.alternative.video;

import java.io.InputStream;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.AlbumController;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.controller.MediaListController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.MediaListScreen;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public privileged aspect VideoAspect extends AbstractVideoAspect {
	
	// ********  MainUIMidlet  ********* //
	
	before(MainUIMidlet midlet): startApp(midlet) {
		midlet.videoModel = new VideoAlbumData();
		
		AlbumListScreen albumVideo = new AlbumListScreen();
		midlet.videoRootController = new BaseController(midlet, midlet.videoModel, albumVideo);
		
		MediaListController videoListController = new MediaListController(midlet, midlet.videoModel, albumVideo);
		videoListController.setNextController(midlet.videoRootController);
		
		AlbumController albumVideoController = new AlbumController(midlet, midlet.videoModel, albumVideo);
		albumVideoController.setNextController(videoListController);
		albumVideo.setCommandListener(albumVideoController);
	}
	
	// ********  AlbumData  ********* //
	
	public void AlbumData.addVideoData(String videoname, String albumname, byte[] video)
	throws InvalidMediaDataException, PersistenceMechanismException {
		if (mediaAccessor instanceof VideoMediaAccessor)
			((VideoMediaAccessor)mediaAccessor).addVideoData(videoname, albumname, video);
	}
	
	// ********  MediaController  ********* //
	
	//public boolean handleCommand(Command command)
	pointcut handleCommandAction(MediaController controller, Command c):
		execution(public boolean MediaController.handleCommand(Command)) && args(c) && this(controller);
	
	boolean around(MediaController controller, Command c): handleCommandAction(controller, c) {
		boolean handled = proceed(controller, c);
		
		if (handled) return true;
		
		String label = c.getLabel();
		System.out.println("<* VideoAspect.around handleCommandAction *> ::handleCommand: " + label);
		
		// [NC] Added in the scenario 07
		if (label.equals("Play Video")) {
			String selectedMediaName = controller.getSelectedMediaName();
			return controller.playVideoMedia(selectedMediaName);		
		}
		
		return false;
	}
	
	private boolean MediaController.playVideoMedia(String selectedMediaName) {
		InputStream storedMusic = null;
		try {
			MediaData mymedia = getAlbumData().getMediaInfo(selectedMediaName);
			if (mymedia.getTypeMedia().equals(MediaData.VIDEO)) {
				storedMusic = ((VideoAlbumData) getAlbumData()).getVideoFromRecordStore(getCurrentStoreName(), selectedMediaName);
				PlayVideoScreen playscree = new PlayVideoScreen(midlet,storedMusic, mymedia.getTypeMedia(),this);
				playscree.setVisibleVideo();
				PlayVideoController controller = new PlayVideoController(midlet, getAlbumData(), (AlbumListScreen) getAlbumListScreen(), playscree);
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
	public static final int MediaListScreen.PLAYVIDEO = 3;
	
	// [NC] Added in the scenario 07
	public static final Command playCommand = new Command("Play Video", Command.ITEM, 1);
	
	// public void initMenu()
	pointcut initMenu(MediaListScreen listScreen):
		execution(public void MediaListScreen.initMenu()) && this(listScreen);
	
	after(MediaListScreen listScreen) : initMenu(listScreen) {
		//Add the core application commands always
		// [NC] Added in the scenario 07: to support more than one screen purpose
		if (listScreen.getTypeOfScreen() == MediaListScreen.PLAYVIDEO)
			listScreen.addCommand(playCommand);
	}
	
	pointcut constructor(AbstractController controller) :
		call(MediaListScreen.new(..)) && this(controller);

	after(AbstractController controller) returning (MediaListScreen listScreen): constructor(controller) {
		// [NC] Added in the scenario 07	
		if (controller.getAlbumData() instanceof VideoAlbumData)
			listScreen.setTypeOfScreen(MediaListScreen.PLAYVIDEO);
	}
}
