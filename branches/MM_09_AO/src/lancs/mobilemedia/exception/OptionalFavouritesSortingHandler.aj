package lancs.mobilemedia.exception;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public abstract aspect OptionalFavouritesSortingHandler {

	public abstract pointcut checkedMechanismException();
	
	declare soft: InvalidMediaDataException: checkedMechanismException();
	declare soft: PersistenceMechanismException: checkedMechanismException();
	declare soft: MediaNotFoundException: checkedMechanismException();
	
	void around(MediaController controller): checkedMechanismException() && args(controller, *) {
		try {
			proceed(controller);
		// TODO Nelio, I add these handlers here just to remove erros. Please, check them.	
		} catch (InvalidMediaDataException e) { 
		} catch (PersistenceMechanismException e) {			
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert("Error", "The selected photo was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
		}
	}
}
