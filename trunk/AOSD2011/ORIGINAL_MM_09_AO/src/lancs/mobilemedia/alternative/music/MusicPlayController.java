// [NC] Added in the scenario 07
package lancs.mobilemedia.alternative.music;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;


public class MusicPlayController extends AbstractController {
	
	private PlayMusicScreen pmscreen;
	
	public MusicPlayController(MainUIMidlet midlet, AlbumData albumData,
			List albumListScreen, PlayMusicScreen pmscreen) {
		super(midlet, albumData, albumListScreen);
		this.pmscreen = pmscreen;
	}

	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		System.out.println( "<* MusicPlayController.handleCommand() *> " + label);

		/** Case: Copy photo to a different album */
		if (label.equals("Start")) {
			pmscreen.startPlay();
			return true;
		}else if (label.equals("Stop")) {
			pmscreen.pausePlay();
				return true;
		}else if ((label.equals("Back"))||(label.equals("Cancel"))){
			pmscreen.pausePlay();
			// [NC] Changed in the scenario 07: just the first line below to support generic AbstractController
			((AlbumListScreen) getAlbumListScreen()).repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen( getAlbumListScreen() );
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
		}
		
		return false;
	}
}
