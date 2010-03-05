package lancs.mobilemedia.optional.sorting;

import lancs.mobilemedia.core.ui.controller.MediaController;
import lancs.mobilemedia.exception.CheckedInvalidPersistenceMediaNotFoundExceptionHandler;
//import lancs.mobilemedia.exception.ExceptionHandler;

//@ExceptionHandler
public privileged aspect OptionalSortingHandler extends CheckedInvalidPersistenceMediaNotFoundExceptionHandler {

	public pointcut checkedMechanismException(): execution(void SortingAspect.internalAfterShowImage(MediaController, String));

}
