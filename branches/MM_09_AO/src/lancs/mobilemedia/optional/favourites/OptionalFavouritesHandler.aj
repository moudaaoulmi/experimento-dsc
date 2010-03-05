package lancs.mobilemedia.optional.favourites;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.exception.CheckedInvalidPersistenceMediaNotFoundExceptionHandler;
//import lancs.mobilemedia.exception.ExceptionHandler;

//@ExceptionHandler
public privileged aspect OptionalFavouritesHandler extends CheckedInvalidPersistenceMediaNotFoundExceptionHandler {
	
	public pointcut checkedMechanismException(): execution(void internalAroundHandlerCommandAction(MediaController, String));

}
