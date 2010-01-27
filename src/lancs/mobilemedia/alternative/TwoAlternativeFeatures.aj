/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 30 Aug 2007
 * 
 */
package lancs.mobilemedia.alternative;

import javax.microedition.lcdui.Command;

import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect TwoAlternativeFeatures {

	// ********  AlbumListScreen  ********* //
	
	//[NC] Added in the scenario 07
	public static final Command exitCommand = new Command("Back", Command.STOP, 2);
	
	//public void initMenu()
	pointcut initMenu(AlbumListScreen screen):
		execution( public void AlbumListScreen.initMenu() ) && this(screen);
	
	before(AlbumListScreen screen): initMenu(screen) {
		screen.addCommand(exitCommand);
	}

	// ********  SelectMediaController  ********* //
	
	public BaseController SelectMediaController.imageController;
	public AlbumData SelectMediaController.imageAlbumData;

	public BaseController SelectMediaController.getImageController() {
		return imageController;
	}

	public void SelectMediaController.setImageController(BaseController imageController) {
		this.imageController = imageController;
	}

	public AlbumData SelectMediaController.getImageAlbumData() {
		return imageAlbumData;
	}

	public void SelectMediaController.setImageAlbumData(AlbumData imageAlbumData) {
		this.imageAlbumData = imageAlbumData;
	}

}
