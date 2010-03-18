/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 14 Jun 2008
 * 
 */
package lancs.mobilemedia.alternative;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect MusicSelector {

	// ********  SelectMediaController  ********* //
	
	public BaseController SelectMediaController.musicController;
	public AlbumData SelectMediaController.musicAlbumData;
	
	public BaseController SelectMediaController.getMusicController() {
		return musicController;
	}

	public void SelectMediaController.setMusicController(BaseController musicController) {
		this.musicController = musicController;
	}

	public AlbumData SelectMediaController.getMusicAlbumData() {
		return musicAlbumData;
	}

	public void SelectMediaController.setMusicAlbumData(AlbumData musicAlbumData) {
		this.musicAlbumData = musicAlbumData;
	}

	//public boolean handleCommand(Command command)
	pointcut handleCommandAction(SelectMediaController controller, Command command): 
		 execution(public boolean SelectMediaController.handleCommand(Command )) && args(command) && this(controller);

	boolean around(SelectMediaController controller, Command command): handleCommandAction(controller, command) {
		if (proceed(controller, command)) return true;
		String label = command.getLabel();
      	System.out.println( "<* MusicSelector.handleCommand() *>: " + label);
     	if (label.equals("Select")) {
 			List down = (List) Display.getDisplay(controller.midlet).getCurrent();
 			String textOption = down.getString( down.getSelectedIndex() );
 			if (textOption.equals("Music")) {
 				controller.musicController.init(controller.musicAlbumData);
 				return true;
 			}
      	}
     	return false;
	}
}
