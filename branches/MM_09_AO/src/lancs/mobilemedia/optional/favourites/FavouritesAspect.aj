/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 9 Aug 2007
 * 
 */
package lancs.mobilemedia.optional.favourites;

import javax.microedition.lcdui.Command;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.controller.MediaListController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.MediaData;
import lancs.mobilemedia.core.ui.screens.MediaListScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.core.util.MediaUtil;

/**
 * @author Eduardo Figueiredo
 *
 */
public privileged aspect FavouritesAspect {

	// ********  MediaController  ********* //

	// TODO [EF] This pointcut is already defined in SortingAspect
	//public boolean PhotoController.handleCommand(Command, Displayable)
	pointcut handleCommandAction(MediaController controller, Command c):
		execution(public boolean MediaController.handleCommand(Command)) && args(c) && this(controller);
	
	boolean around(MediaController controller, Command c): handleCommandAction(controller, c) {
		boolean handled = proceed(controller, c);
		
		if (handled) return true;
		
		String label = c.getLabel();
		System.out.println("<* FavouritesAspect.around handleCommandAction *> ::handleCommand: " + label);
		
		/** Case: Set photo as favorite 
		 * [EF] Added in the scenario 03 **/
		if (label.equals("Set Favorite")) {
		   	String selectedMediaName = controller.getSelectedMediaName();
		   	internalAroundHandlerCommandAction(controller, selectedMediaName); 
			return true;
				
		/** Case: View favorite photos 
		 * [EF] Added in the scenario 03 **/
		} else if (label.equals("View Favorites")) {
			favorite = true;
			controller.showMediaList(controller.getCurrentStoreName());
			ScreenSingleton.getInstance().setCurrentScreenName( Constants.IMAGELIST_SCREEN );
			return true;
		}
		
		return false;
	}

	private void internalAroundHandlerCommandAction(MediaController controller,	String selectedMediaName){
		MediaData media= controller.getAlbumData().getMediaInfo(selectedMediaName);
		media.toggleFavorite();
		controller.updateMedia(media);
	}
	
	boolean favorite = false;
	
	// ********  MediaListController  ********* //
	
	//public int PhotoListScreen.append(ImageData)
	pointcut append(MediaListController controller, MediaData media):
		call(public int MediaListScreen.append(MediaData)) && args(media) && this(controller);
	
	int around(MediaListController controller, MediaData media) : append(controller, media) {
		boolean flag = true;
		// [EF] Check if favorite is true (Add in the Scenario 03)
		if (favorite) {
			if ( !(media.isFavorite()) ) flag = false;
		}
		if (flag) return proceed(controller, media);
		return 0;
	}
	
	// TODO [EF] This pointcut is already defined in the CountViewsAspect aspect
	//public void PhotoListController.appendImages(ImageData[], PhotoListScreen)
	pointcut appendMedias(MediaListController controller, MediaData[] medias, MediaListScreen mediaList):
		call(public void MediaListController.appendMedias(MediaData[], MediaListScreen)) && args(medias, mediaList) && this(controller);
	
	after(MediaListController controller, MediaData[] medias, MediaListScreen mediaList): appendMedias(controller, medias, mediaList) {
		favorite = false;
	}
	
	// ********  MediaData  ********* //
	
	// [EF] Added in the scenario 03 
	private boolean MediaData.favorite = false;
	
	/**
	 * [EF] Added in the scenario 03
	 */
	public void MediaData.toggleFavorite() {
		this.favorite = ! favorite;
	}
	
	/**
	 * [EF] Added in the scenario 03
	 * @param favorite
	 */
	public void MediaData.setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	/**
	 * [EF] Added in the scenario 03
	 * @return the favorite
	 */
	public boolean MediaData.isFavorite() {
		return favorite;
	}

	// ********  MediaListScreen  ********* //
	
	// [EF] Added in the scenario 03 
	public static final Command favoriteCommand = new Command("Set Favorite", Command.ITEM, 1);
	public static final Command viewFavoritesCommand = new Command("View Favorites", Command.ITEM, 1);
	
	// TODO [EF] This pointcut is already defined in the CountViewsAspect aspect
	//public void PhotoListScreen.initMenu()
	pointcut initMenu(MediaListScreen screen):
		execution(public void MediaListScreen.initMenu()) && this(screen);
	
	after(MediaListScreen screen) : initMenu(screen) {
		// [EF] Added in the scenario 03 
		screen.addCommand(favoriteCommand);
		screen.addCommand(viewFavoritesCommand);
	}
	
	// ********  MediaUtil  ********* //
	
	//ImageData ImageUtil.createImageData(String, String, String, int, String)
	pointcut createMediaData(MediaUtil mediaUtil, String fidString, String albumLabel, String mediaLabel, int endIndex, String iiString):
		execution(MediaData MediaUtil.createMediaData(String, String, String, int, String)) && args(fidString, albumLabel, mediaLabel, endIndex, iiString) && this(mediaUtil);
	
	MediaData around(MediaUtil mediaUtil, String fidString, String albumLabel, String mediaLabel, int endIndex, String iiString) : createMediaData(mediaUtil, fidString, albumLabel, mediaLabel, endIndex, iiString) {
		// [EF] Favorite (Scenario 03)
		boolean favorite = false;
		int startIndex = mediaUtil.endIndex + 1;
		mediaUtil.endIndex = iiString.indexOf(MediaUtil.DELIMITER, startIndex);
		if (mediaUtil.endIndex == -1)
			mediaUtil.endIndex = iiString.length();
		favorite = (iiString.substring(startIndex, mediaUtil.endIndex)).equalsIgnoreCase("true");
		MediaData mediaData = proceed(mediaUtil, fidString, albumLabel, mediaLabel, mediaUtil.endIndex, iiString);
		mediaData.setFavorite(favorite);
		return mediaData;
	}
}
