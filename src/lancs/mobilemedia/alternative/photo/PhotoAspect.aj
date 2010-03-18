/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 29 Aug 2007
 * 
 */
package lancs.mobilemedia.alternative.photo;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.AlbumController;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.controller.MediaListController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.screens.MediaListScreen;
import lancs.mobilemedia.core.util.Constants;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect PhotoAspect extends AbstractPhotoAspect {

	// ********  MainUIMidlet  ********* //
	
	before(MainUIMidlet midlet): startApp(midlet) {
		// [NC] Added in the scenario 07
		midlet.imageModel = new ImageAlbumData();
		
		// [NC] Added in the scenario 07
		AlbumListScreen album = new AlbumListScreen();
		midlet.imageRootController = new BaseController(midlet, midlet.imageModel, album);
		
		// [EF] Add in scenario 04: initialize sub-controllers
		MediaListController photoListController = new MediaListController(midlet, midlet.imageModel, album);
		photoListController.setNextController(midlet.imageRootController);
		
		AlbumController albumController = new AlbumController(midlet, midlet.imageModel, album);
		albumController.setNextController(photoListController);
		album.setCommandListener(albumController);
	}

	// ********  MediaController  ********* //
	
	//public boolean handleCommand(Command command)
	pointcut handleCommandAction(MediaController controller, Command c):
		execution(public boolean MediaController.handleCommand(Command)) && args(c) && this(controller);
	
	boolean around(MediaController controller, Command c): handleCommandAction(controller, c) {
		boolean handled = proceed(controller, c);
		
		if (handled) return true;
		
		String label = c.getLabel();
		System.out.println("<* PhotoAspect.around handleCommandAction *> ::handleCommand: " + label);
		
		// [NC] Added in the scenario 07
		if (label.equals("View")) {
			String selectedImageName = controller.getSelectedMediaName();
			controller.showImage(selectedImageName);
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.IMAGE_SCREEN);
			return true;
		}
		return false;
	}

	/**
	 * Show the current image that is selected
	 */
	// [NC] Added in the scenario 07
	public void MediaController.showImage(String name) {
//[EF] Instead of replicating this code, I change to use the method "getSelectedImageName()". 		
		Image storedImage = null;
		storedImage = ((ImageAlbumData)getAlbumData()).getImageFromRecordStore(getCurrentStoreName(), name);
		//We can pass in the image directly here, or just the name/model pair and have it loaded
		PhotoViewScreen canv = new PhotoViewScreen(storedImage);
		canv.setCommandListener( this );
		AbstractController nextcontroller = getMediaController(name); 
		canv.setCommandListener( nextcontroller );
		setCurrentScreen(canv);
	}
	
	// ********  MediaListScreen  ********* //
	
	public static final int MediaListScreen.SHOWPHOTO = 1;
	
	// [NC] Added in the scenario 07
	public static final Command viewCommand = new Command("View", Command.ITEM, 1);
	
	// public void initMenu()
	pointcut initMenu(MediaListScreen listScreen):
		execution(public void MediaListScreen.initMenu()) && this(listScreen);
	
	after(MediaListScreen listScreen) : initMenu(listScreen) {
		//Add the core application commands always
		// [NC] Added in the scenario 07: to support more than one screen purpose
		if (listScreen.getTypeOfScreen() == MediaListScreen.SHOWPHOTO)
			listScreen.addCommand(viewCommand);
	}

	//public PhotoViewScreen.PhotoViewScreen(Image)
	pointcut constructor(AbstractController controller) :
		call(MediaListScreen.new(..)) && this(controller);

	after(AbstractController controller) returning (MediaListScreen listScreen): constructor(controller) {
		// [NC] Added in the scenario 07
		if (controller.getAlbumData() instanceof ImageAlbumData)
			listScreen.setTypeOfScreen(MediaListScreen.SHOWPHOTO);
	}
}
