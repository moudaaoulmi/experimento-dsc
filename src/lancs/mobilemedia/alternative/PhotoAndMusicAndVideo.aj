package lancs.mobilemedia.alternative;

import javax.microedition.lcdui.Display;

import lancs.mobilemedia.alternative.photo.*;
import lancs.mobilemedia.alternative.music.*;
import lancs.mobilemedia.alternative.video.*;
import lancs.mobilemedia.optional.*;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;

public aspect PhotoAndMusicAndVideo {

		declare precedence : MusicAspect, VideoAspect, PhotoAspect, PhotoAndMusicAndVideo, OptionalFeatureAspect; // Check? 
		
		// ********  MainUIMidlet  ********* //
		
		//public void startApp()
		pointcut startApp(MainUIMidlet midlet):
			execution( public void MainUIMidlet.startApp() ) && this(midlet);
		
		after(MainUIMidlet midlet): startApp(midlet) {
			BaseController imageRootController = midlet.imageRootController;
			AlbumData imageModel = midlet.imageModel;

			BaseController musicRootController = midlet.musicRootController;
			AlbumData musicModel = midlet.musicModel;
			
			BaseController videoRootController = midlet.videoRootController;
			AlbumData videoModel = midlet.videoModel;
			AlbumListScreen albumListScreen = (AlbumListScreen)imageRootController.getAlbumListScreen();
			
			// [NC] Added in the scenario 07
			SelectMediaController selectcontroller = new SelectMediaController(midlet, imageModel, albumListScreen);
			selectcontroller.setNextController(imageRootController);
			
			selectcontroller.setImageAlbumData(imageModel);
			selectcontroller.setImageController(imageRootController);
			
			selectcontroller.setMusicAlbumData(musicModel);
			selectcontroller.setMusicController(musicRootController);
			
			selectcontroller.setVideoAlbumData(videoModel);
			selectcontroller.setVideoController(videoRootController);
			SelectTypeOfMedia mainscreen = new SelectTypeOfMedia();
			mainscreen.initMenu();
			mainscreen.append("Photos");
			mainscreen.append("Music");
			mainscreen.append("Videos");
			mainscreen.setCommandListener(selectcontroller);
			Display.getDisplay(midlet).setCurrent(mainscreen);
			ScreenSingleton.getInstance().setMainMenu(mainscreen);
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
				controller.setCurrentScreen( ScreenSingleton.getInstance().getMainMenu() );
				return true;
			}
			return false;
		}
		
		// ********  ScreenSingleton  ********* //
		
		// [NC] Added in the scenario 07
		private SelectTypeOfMedia ScreenSingleton.mainscreen;
		
		public SelectTypeOfMedia ScreenSingleton.getMainMenu(){
			return mainscreen;
		}
		
		public void ScreenSingleton.setMainMenu(SelectTypeOfMedia screen){
			mainscreen = screen;
		}
}