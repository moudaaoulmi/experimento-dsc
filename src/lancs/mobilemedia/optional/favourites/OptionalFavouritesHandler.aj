package lancs.mobilemedia.optional.favourites;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.lib.exceptions.InvalidMediaDataException;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.lib.exceptions.MediaNotFoundException;

public privileged aspect OptionalFavouritesHandler {
	
	pointcut internalAroundHandlerCommandActionHandler(): execution(void internalAroundHandlerCommandAction(MediaController, String));
	
	declare soft: InvalidMediaDataException: internalAroundHandlerCommandActionHandler();
	
	declare soft: PersistenceMechanismException: internalAroundHandlerCommandActionHandler();
	
	declare soft: MediaNotFoundException: internalAroundHandlerCommandActionHandler();
	
	void around(MediaController controller): internalAroundHandlerCommandActionHandler() && args(controller, *) {
		try {
			proceed(controller);
		// TODO Nelio, I add these handlers here just to remove errros. Please, check them.
		} catch (InvalidMediaDataException e) { 
		} catch (PersistenceMechanismException e) {			
		} catch (MediaNotFoundException e) {
			Alert alert = new Alert( "Error", "The selected photo was not found in the mobile device", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
		}
	}

}
