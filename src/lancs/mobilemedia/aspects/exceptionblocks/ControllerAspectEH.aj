

package lancs.mobilemedia.aspects.exceptionblocks;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreFullException;

import lancs.mobilemedia.core.ui.controller.AlbumController;
import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.core.ui.controller.MediaListController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.lib.exceptions.UnavailablePhotoAlbumException;

privileged aspect ControllerAspectEH {
	
	//public boolean AlbumController.handleCommand(Command c, Displayable d) block 1 - Scenario 5
	pointcut handleCommand1(String nameStore, AlbumController controler): 
		 (call(public void AlbumData.deleteAlbum(String)) && args(nameStore) && this(controler))&& (withincode(public boolean AlbumController.handleCommand(Command)));
	//private void AlbumController.resetMediaData() block 1 - Scenario 3
	pointcut resetMediaData(AlbumController controler): 
		execution(private void AlbumController.resetMediaData())&&this(controler);
	//private void PhotoListController.showImageList(String) block 1 - Scenario 3
	pointcut showMediaList(String recordName, MediaListController controler): 
		execution(public void MediaListController.showMediaList(String))&&this(controler)&&args(recordName);
	//public void  PhotoController.showImage() block 1 - Scenario 3
	pointcut showImage(MediaController controler): 
		execution(public void MediaController.showImage(String))&&this(controler);
	
	declare soft: PersistenceMechanismException : (call(public void AlbumData.deleteAlbum(String)))&& (withincode(public boolean AlbumController.handleCommand(Command)));
	declare soft: PersistenceMechanismException :  execution(private void AlbumController.resetMediaData());
	declare soft: UnavailablePhotoAlbumException :  execution(public void MediaListController.showMediaList(String));
	declare soft: PersistenceMechanismException : execution(public void MediaController.showImage(String));
	declare soft: MediaNotFoundException : execution(public void MediaController.showImage(String));
	
	void around(String nameStore, AlbumController controler): handleCommand1(nameStore,controler){
		try{
			proceed(nameStore,controler);
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", "The mobile database can not delete this photo album", null, AlertType.ERROR);
	        Display.getDisplay(controler.midlet).setCurrent(alert, Display.getDisplay(controler.midlet).getCurrent());
		}
	}
	
	
	
	void around(AlbumController controler): resetMediaData(controler){
		try{
			proceed(controler);
		} catch (PersistenceMechanismException e) {
			Alert alert = null;
			if (e.getCause() instanceof  RecordStoreFullException)
				alert = new Alert( "Error", "The mobile database is full", null, AlertType.ERROR);
			else
				alert = new Alert( "Error", "It is not possible to reset the database", null, AlertType.ERROR);
			Display.getDisplay(controler.midlet).setCurrent(alert, Display.getDisplay(controler.midlet).getCurrent());
		    return;
		}	
	}
	
	
	
	void around(String recordName, MediaListController controler): showMediaList(recordName, controler){
		try{
			proceed(recordName,controler);
		}  catch (UnavailablePhotoAlbumException e) {
			Alert alert = new Alert( "Error", "The list of photos can not be recovered", null, AlertType.ERROR);
			Display.getDisplay(controler.midlet).setCurrent(alert, Display.getDisplay(controler.midlet).getCurrent());
		    return;
		}
	}
	
	
	
	void around(MediaController controler): showImage(controler){
		try{
			proceed(controler);
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert( "Error", "The selected photo was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(controler.midlet).setCurrent(alert, Display.getDisplay(controler.midlet).getCurrent());
	        return;
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", "The mobile database can open this photo", null, AlertType.ERROR);
			Display.getDisplay(controler.midlet).setCurrent(alert, Display.getDisplay(controler.midlet).getCurrent());
	        return;
		}
	}
}
