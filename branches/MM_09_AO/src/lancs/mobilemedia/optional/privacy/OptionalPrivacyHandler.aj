package lancs.mobilemedia.optional.privacy;

import lancs.mobilemedia.core.ui.controller.AlbumController;
import lancs.mobilemedia.core.ui.datamodel.MediaAccessor;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

import lancs.mobilemedia.lib.exceptions.PersistenceMechanismException;
import lancs.mobilemedia.lib.exceptions.InvalidAlbumNameException;

public privileged aspect OptionalPrivacyHandler {

	pointcut addPasswordHandler(): execution(void MediaAccessor.addPassword(String, String));
	pointcut internalGetPasswordHandler(): execution(String MediaAccessor.internalGetPassword(String, String));
	pointcut internalAroundHandleCommandAction2Handler(): execution(boolean PrivacyAspect.internalAroundHandleCommandAction2(AlbumController));
	pointcut internalAroundHandleCommandAction3Handler(): execution(void PrivacyAspect.internalAroundHandleCommandAction3(AlbumController));
	pointcut internalAroundHandleCommandAction4Handler(): execution(void PrivacyAspect.internalAroundHandleCommandAction4(AlbumController));
	pointcut internalAroundSaveActionHandler(): execution(boolean internalAroundSaveAction(AlbumController));
	
	declare soft: RecordStoreException: addPasswordHandler() || internalGetPasswordHandler();
	declare soft: PersistenceMechanismException: 	internalAroundHandleCommandAction2Handler() 
				                                 || internalAroundHandleCommandAction3Handler()
				                                 || internalAroundHandleCommandAction4Handler()
				                                 || internalAroundSaveActionHandler();
	declare soft: InvalidAlbumNameException:   internalAroundSaveActionHandler() 
											|| internalAroundHandleCommandAction2Handler();
	
	void around(): addPasswordHandler() {
		try {
			proceed();
		} catch(RecordStoreException e){
			
		}
	}
	
	String around() : internalGetPasswordHandler() {
		try {
			return proceed();
		} catch(RecordStoreException e){}
		return null;
	}
	
	boolean around(AlbumController controller): (internalAroundHandleCommandAction2Handler() || internalAroundSaveActionHandler())&& args(controller) {
		try {
			return proceed(controller);
		} catch (PersistenceMechanismException e) {
			Alert alert = null;
			if (e.getCause() instanceof  RecordStoreFullException)
				alert = new Alert( "Error", "The mobile database is full", null, AlertType.ERROR);
			else
				alert = new Alert( "Error", "The mobile database can not add a new photo album", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
			return true;
	    } catch (InvalidAlbumNameException e) {
	    	Alert alert = new Alert( "Error", "You have provided an invalid Photo Album name", null, AlertType.ERROR);
			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
			return true;
		}
	}

// #Reuse# above	
//	boolean around(AlbumController controller) : internalAroundSaveActionHandler() && args(controller) {
//		try {
//			return proceed(controller);
//		} catch (PersistenceMechanismException e) {
//			Alert alert = null;
//			if (e.getCause() instanceof  RecordStoreFullException)
//				alert = new Alert( "Error", "The mobile database is full", null, AlertType.ERROR);
//			else
//				alert = new Alert( "Error", "The mobile database can not add a new photo album", null, AlertType.ERROR);
//			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
//			return true;
//	    } catch (InvalidAlbumNameException e) {
//	    	Alert alert = new Alert( "Error", "You have provided an invalid Photo Album name", null, AlertType.ERROR);
//			Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
//			return true;
//		}
//	}
	
	void around(AlbumController controller) : (internalAroundHandleCommandAction3Handler() || internalAroundHandleCommandAction4Handler()) && args(controller){
		try {
			proceed(controller);
		} catch (PersistenceMechanismException e) {
			//System.out.println(e);
			Alert alert = new Alert( "Error", "The mobile database can not delete this photo album", null, AlertType.ERROR);
	        Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
		}
	}

// #Reuse# above	
//	void around(AlbumController controller) : internalAroundHandleCommandAction4Handler() && args(controller){
//		try {
//			proceed(controller);
//		} catch (PersistenceMechanismException e) {
//			Alert alert = new Alert( "Error", "The mobile database can not delete this photo album", null, AlertType.ERROR);
//	        Display.getDisplay(controller.midlet).setCurrent(alert, Display.getDisplay(controller.midlet).getCurrent());
//		}
//	}
	

}
