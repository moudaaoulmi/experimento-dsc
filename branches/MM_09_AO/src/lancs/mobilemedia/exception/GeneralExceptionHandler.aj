package lancs.mobilemedia.exception;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import lancs.mobilemedia.core.ui.controller.MediaController;

import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import java.io.InputStream;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public privileged aspect GeneralExceptionHandler {

	pointcut checkedPersistenceMechanismException(): (execution(boolean MediaController.internalPlayVideoMedia(String, InputStream))) 
													 || (execution(boolean MediaController.internalPlayMultiMedia(String, InputStream)));
	//public abstract pointcut checkedMediaNotFoundException();
	
//	declare soft: MediaNotFoundException: checkedMediaNotFoundException();
	declare soft: PersistenceMechanismException: checkedPersistenceMechanismException();
//	
//	Object around(): checkedMediaNotFoundException() {
//		try {
//			return proceed();
//		} catch (MediaNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	boolean around(MediaController controller): checkedPersistenceMechanismException() && this(controller) {
		try {
			return proceed(controller);
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", "The mobile database can open this item 1", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
			return false;
		}
	}
	
}
