package lancs.mobilemedia.optional.sorting;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.core.ui.controller.MediaController;

public privileged aspect OptionalSortingHandler {
	
	pointcut internalAfterShowImageHandler(): execution(void SortingAspect.internalAfterShowImage(MediaController, String));
		
	declare soft: MediaNotFoundException: internalAfterShowImageHandler();
	declare soft: InvalidMediaDataException: internalAfterShowImageHandler();
	declare soft: PersistenceMechanismException: internalAfterShowImageHandler();
		
	void around(MediaController controller): internalAfterShowImageHandler() && args(controller, *) {
		try {
			proceed(controller);
			// TODO Nelio, I add these handlers here just to remove errros. Please, check them.
		} catch (InvalidMediaDataException e) { 
		} catch (PersistenceMechanismException e) {
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert("Error", "The selected photo was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
		}
	}

}
