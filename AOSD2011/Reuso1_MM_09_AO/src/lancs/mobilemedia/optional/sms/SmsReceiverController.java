package lancs.mobilemedia.optional.sms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

import lancs.mobilemedia.alternative.photo.PhotoViewScreen;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.AbstractController;
import lancs.mobilemedia.core.ui.controller.ScreenSingleton;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.screens.AlbumListScreen;
import lancs.mobilemedia.core.util.Constants;
import lancs.mobilemedia.optional.copySMS.PhotoViewController;

public class SmsReceiverController extends AbstractController {
	
	byte[] incomingImageData;
	
	public SmsReceiverController(MainUIMidlet midlet, AlbumData albumData, AlbumListScreen albumListScreen) {
		super(midlet, albumData, albumListScreen);
	}

 	/**
	 * Handle SMS specific events.
	 * If we are given a standard command that is handled by the BaseController, pass 
	 * the handling off to our super class with the else clause
	 */
	public boolean handleCommand(Command c) {
		String label = c.getLabel();
      	System.out.println("<* SmsReceiverController::handleCommand() *> " + label);
		
      	/** Case: ... **/
      	if (label.equals("Accept Photo")) {	
      		Image image = Image.createImage(incomingImageData, 0, incomingImageData.length);
         	PhotoViewScreen canv = new PhotoViewScreen(image);
        	//[NC] Changed in scenario 08 to support bytes
	     	canv.setImage(incomingImageData);
			canv.setFromSMS(true);
			canv.setCommandListener(new PhotoViewController(this.midlet, getAlbumData(), (AlbumListScreen)getAlbumListScreen(), "NoName"));
			this.setCurrentScreen(canv);
	   		return true;

      	} else if (label.equals("Reject Photo")) {
	      	//TODO: Go back to whatever screen they were previously on?
	      	((AlbumListScreen) getAlbumListScreen()).repaintListAlbum(getAlbumData().getAlbumNames());
			setCurrentScreen( getAlbumListScreen() );
			ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
			return true;
      					
	      /* For All commands not handled here, send them to the super class */
      	} else if (label.equals("Ok")) {
	    	  ((AlbumListScreen) getAlbumListScreen()).repaintListAlbum(getAlbumData().getAlbumNames());
				setCurrentScreen( getAlbumListScreen() );
				ScreenSingleton.getInstance().setCurrentScreenName(Constants.ALBUMLIST_SCREEN);
				return true;
      	}
	      
  		return false;
	}
	
	public void setIncommingData(byte[] incomingImageData){
		this.incomingImageData = incomingImageData;
	}
}