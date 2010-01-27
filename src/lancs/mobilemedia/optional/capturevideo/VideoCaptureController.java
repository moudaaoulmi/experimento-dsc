package lancs.mobilemedia.optional.capturevideo;


import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

import lancs.mobilemedia.alternative.video.VideoAlbumData;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AddMediaToAlbum;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.optional.capture.CaptureVideoScreen;
import lancs.mobilemedia.core.ui.datamodel.MediaData;

public class VideoCaptureController extends AbstractController {
	
	private CaptureVideoScreen pmscreen;
	private AddMediaToAlbum saveVideoToAlbum;

	public VideoCaptureController(MainUIMidlet midlet, AlbumData albumData,
			List albumListScreen, CaptureVideoScreen pmscreen) {
		super(midlet, albumData, albumListScreen);
		this.pmscreen = pmscreen;
	}

	public boolean handleCommand(Command command) {
		String label = command.getLabel();
		System.out.println( "<* VideoCaptureController.handleCommand() *> " + label);

		if (label.equals("Start")) {
			pmscreen.startCapture();
			return true;
		} else if (label.equals("Stop")) {
			pmscreen.pauseCapture();
			saveVideoToAlbum = new AddMediaToAlbum("Save Video");
			saveVideoToAlbum.setItemName("MyVideo");
			saveVideoToAlbum.setLabePath("Save to Album:");
			saveVideoToAlbum.setCommandListener(this);
			saveVideoToAlbum.setCapturedMedia(pmscreen.getByteArrays());
	        Display.getDisplay(midlet).setCurrent(saveVideoToAlbum);
			return true;
		} else if (label.equals("Save Item")) {
			String videoname = ((AddMediaToAlbum) getCurrentScreen()).getItemName();
			String albumname = ((AddMediaToAlbum) getCurrentScreen()).getPath();
			try {
				byte[] capturedMediaByte = saveVideoToAlbum.getCapturedMedia();
				// TODO [EF] Workaround since getCapturedMedia() is not working.
				if ((capturedMediaByte == null) || (capturedMediaByte.length==0)) {
					System.out.println( "<* VideoCaptureController.handleCommand() *> captured video is null, adding Fish to the record store!");
					InputStream inputStream = (InputStream) this.getClass().getResourceAsStream("/images/fish.mpg");
					String str = inputStream.toString();
					capturedMediaByte = str.getBytes();
				}
				((VideoAlbumData)getAlbumData()).addVideoData(videoname, albumname, capturedMediaByte);
			} catch (InvalidMediaDataException e) {
				e.printStackTrace();
			} catch (PersistenceMechanismException e) {
				e.printStackTrace();
			}
			
			try {
				((VideoAlbumData)getAlbumData()).loadMediaDataFromRMS(albumname);
				MediaData media = ((VideoAlbumData)getAlbumData()).getMediaInfo(videoname);
				media.setTypeMedia("video/mpeg");
				((VideoAlbumData)getAlbumData()).updateMediaInfo(media, media);
			} catch (MediaNotFoundException e) {
				e.printStackTrace();
			} catch (InvalidMediaDataException e) {
				e.printStackTrace();
			} catch (PersistenceMechanismException e) {
				e.printStackTrace();
			}
			goToListAlbumScreen();
			return true;
		} else if ((label.equals("Back")) || (label.equals("Cancel"))){
			pmscreen.pauseCapture();
			// [NC] Changed in the scenario 07: just the first line below to support generic AbstractController
			goToListAlbumScreen();
			return true;
		}
		return false;
	}
	
	void goToListAlbumScreen() {
		((AlbumListScreen) getAlbumListScreen()).repaintListAlbum(getAlbumData().getAlbumNames());
		setCurrentScreen( getAlbumListScreen() );
		ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
	}
}
