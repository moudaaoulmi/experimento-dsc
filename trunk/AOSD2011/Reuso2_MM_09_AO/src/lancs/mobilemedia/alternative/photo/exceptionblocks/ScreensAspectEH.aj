

package lancs.mobilemedia.alternative.photo.exceptionblocks;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

import lancs.mobilemedia.alternative.photo.PhotoViewScreen;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
//import lancs.mobilemedia.exception.ExceptionHandler;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

//@ExceptionHandler
public aspect ScreensAspectEH {

	//Method public PhotoViewScreen.new 1- block - Scenario 1
	pointcut PhotoViewScreenConstructor(AlbumData mod, String name): 
	     execution(public PhotoViewScreen.new(AlbumData, String))&&(args(mod,name));
	
	declare soft: MediaNotFoundException: execution(public PhotoViewScreen.new(AlbumData, String));
	declare soft: PersistenceMechanismException: execution(public PhotoViewScreen.new(AlbumData, String));
	
	void around(AlbumData mod, String name): PhotoViewScreenConstructor(mod, name){
		try{
			proceed(mod, name);
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert( "Error", "The selected image can not be found", null, AlertType.ERROR);
			alert.setTimeout(5000);
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", "It was not possible to recovery the selected image", null, AlertType.ERROR);
			alert.setTimeout(5000);
		}
	}
}
