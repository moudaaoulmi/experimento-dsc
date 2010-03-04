package lancs.mobilemedia.optional.favourites;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.exception.OptionalFavouritesSortingHandler;

public privileged aspect OptionalFavouritesHandler extends OptionalFavouritesSortingHandler {
	
	public pointcut checkedMechanismException(): execution(void internalAroundHandlerCommandAction(MediaController, String));

}
