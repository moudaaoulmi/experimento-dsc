package lancs.mobilemedia.optional.copy;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;

// TODO [EF] I think this class can be removed
public abstract class CopyTargets extends AbstractController {
	
	private String mediaName;
	
	public CopyTargets(MainUIMidlet midlet, AlbumData albumData,
			List albumListScreen) {
		super(midlet, albumData, albumListScreen);
	}
	
	  // [NC] Added in the scenario 07
	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public abstract boolean handleCommand(Command command); 

}