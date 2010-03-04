package lancs.mobilemedia.exception;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;

public abstract aspect AlternativeMusicVideoHandler {
	
	public abstract pointcut checkedPersistenceMechanismException();
	
	declare soft: PersistenceMechanismException: checkedPersistenceMechanismException();
	
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
