/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 30 Aug 2007
 * 
 */
package lancs.mobilemedia.alternative.photoMusic;

import javax.microedition.lcdui.Display;

import lancs.mobilemedia.alternative.SelectMediaController;
import lancs.mobilemedia.alternative.SelectTypeOfMedia;
import lancs.mobilemedia.alternative.photo.*;
import lancs.mobilemedia.alternative.music.*;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect PhotoAndMusicAspect {

	declare precedence : MusicAspect, PhotoAspect, PhotoAndMusicAspect;
	
	// ********  MainUIMidlet  ********* //
	
	//public void startApp()
	pointcut startApp(MainUIMidlet midlet):
		execution( public void MainUIMidlet.startApp() ) && this(midlet);
	
	after(MainUIMidlet midlet): startApp(midlet) {
		BaseController imageRootController = midlet.imageRootController;
		AlbumData imageModel = midlet.imageModel;

		BaseController musicRootController = midlet.musicRootController;
		AlbumData musicModel = midlet.musicModel;

		AlbumListScreen albumListScreen = (AlbumListScreen)imageRootController.getAlbumListScreen();
		
		// [NC] Added in the scenario 07
		SelectMediaController selectcontroller = new SelectMediaController(midlet, imageModel, albumListScreen);
		selectcontroller.setNextController(imageRootController);
		
		selectcontroller.setImageAlbumData(imageModel);
		selectcontroller.setImageController(imageRootController);
		
		selectcontroller.setMusicAlbumData(musicModel);
		selectcontroller.setMusicController(musicRootController);
		
		SelectTypeOfMedia mainscreen = new SelectTypeOfMedia();
		mainscreen.initMenu();
		mainscreen.append("Photos");
		mainscreen.append("Music");
		mainscreen.setCommandListener(selectcontroller);
		Display.getDisplay(midlet).setCurrent(mainscreen);
		setMainMenu(mainscreen);
	}

	// ********  BaseController  ********* //
	
	//private boolean goToPreviousScreen())
	pointcut goToPreviousScreen(BaseController controller):
		execution( private boolean BaseController.goToPreviousScreen() ) && this(controller);
	
	boolean around(BaseController controller) : goToPreviousScreen(controller) {
		boolean returned = proceed(controller);
		if (returned) return true;
		
    	String currentScreenName = ScreenSingleton.getInstance().getCurrentScreenName();
		// [NC] Added in the scenario 07
		if ((currentScreenName == null) || (currentScreenName.equals(Constants.ALBUMLIST_SCREEN))) {	
			controller.setCurrentScreen( getMainMenu() );
			return true;
		}
		return false;
	}
	
	// ********  ScreenSingleton  ********* //
	
	// [NC] Added in the scenario 07
	private SelectTypeOfMedia mainscreen;
	
	public SelectTypeOfMedia getMainMenu(){
		return mainscreen;
	}
	
	public void setMainMenu(SelectTypeOfMedia screen){
		mainscreen = screen;
	}
}
