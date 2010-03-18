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
public aspect VideoSelector {

	// ********  SelectMediaController  ********* //
	
	public BaseController SelectMediaController.videoController;
	public AlbumData SelectMediaController.videoAlbumData;
	
	public BaseController SelectMediaController.getVideoController() {
		return videoController;
	}

	public void SelectMediaController.setVideoController(BaseController videoController) {
		this.videoController = videoController;
	}

	public AlbumData SelectMediaController.getVideoAlbumData() {
		return videoAlbumData;
	}

	public void SelectMediaController.setVideoAlbumData(AlbumData videoAlbumData) {
		this.videoAlbumData = videoAlbumData;
	}

	//public boolean handleCommand(Command command)
	pointcut handleCommandAction(SelectMediaController controller, Command command): 
		 execution(public boolean SelectMediaController.handleCommand(Command )) && args(command) && this(controller);

	after(SelectMediaController controller, Command command): handleCommandAction(controller, command) {
		String label = command.getLabel();
     	if (label.equals("Select")) {
 			List down = (List) Display.getDisplay(controller.midlet).getCurrent();
 			if (down.getString(down.getSelectedIndex()).equals("Videos"))
 				controller.videoController.init(controller.videoAlbumData);
      	}
	}
}
