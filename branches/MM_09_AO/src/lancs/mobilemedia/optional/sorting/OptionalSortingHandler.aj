package lancs.mobilemedia.optional.sorting;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.exception.OptionalFavouritesSortingHandler;

public privileged aspect OptionalSortingHandler extends OptionalFavouritesSortingHandler{

	public pointcut checkedMechanismException(): execution(void SortingAspect.internalAfterShowImage(MediaController, String));

}
