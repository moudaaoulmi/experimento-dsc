package lancs.mobilemedia.exception;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

//@ExceptionHandler
public abstract aspect CheckedPersistenceAndMediaExceptionHandler {
	
	public abstract pointcut checkedPersistenceMechanismAndMediaNotFoundException();
	
	declare soft: PersistenceMechanismException: checkedPersistenceMechanismAndMediaNotFoundException();
	declare soft: MediaNotFoundException: checkedPersistenceMechanismAndMediaNotFoundException();
	
	boolean around(MediaController controller): checkedPersistenceMechanismAndMediaNotFoundException() && this(controller) {
		try {
			return proceed(controller);
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert( "Error", "The selected item was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
		    return false;
		} catch (PersistenceMechanismException e) {
			Alert alert = new Alert( "Error", "The mobile database can open this item 1", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
			return false;
		}
	}
	

}
