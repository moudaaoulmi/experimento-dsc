package lancs.mobilemedia.alternative.video;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;


public class PlayVideoController extends AbstractController{
	
	private PlayVideoScreen pmscreen;
	
	public PlayVideoController(MainUIMidlet midlet, AlbumData albumData,
			List albumListScreen, PlayVideoScreen pmscreen) {
		super(midlet, albumData, albumListScreen);
		this.pmscreen = pmscreen;
	}

	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		System.out.println( "<* PlayVideoController.handleCommand() *> " + label);

		/** Case: Copy photo to a different album */
		if (label.equals("Start")) {
			pmscreen.startVideo();
			return true;
		}else if (label.equals("Stop")) {
			pmscreen.stopVideo();
				return true;
		}else if ((label.equals("Back"))||(label.equals("Cancel"))){
			pmscreen.stopVideo();
			// [NC] Changed in the scenario 07: just the first line below to support generic AbstractController
			((AlbumListScreen) getAlbumListScreen()).repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen( getAlbumListScreen() );
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}
		return false;
	}
}

