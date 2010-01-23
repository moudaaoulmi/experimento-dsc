// [NC] Added in the scenario 07

package lancs.mobilemedia.alternative;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;


public class SelectMediaController extends AbstractController {

	public SelectMediaController(MainUIMidlet midlet, AlbumData imageAlbumData,	List albumListScreen) {
		super(midlet, imageAlbumData, albumListScreen);
	}
	
	public boolean handleCommand(Command command) {
		String label = command.getLabel();
      	System.out.println( "<* SelectMediaController.handleCommand() *>: " + label);
      	// [EF] PhotoSelector, MusicSelector, and VideoSelecto intercept this method.
      	return false;
	}

}
