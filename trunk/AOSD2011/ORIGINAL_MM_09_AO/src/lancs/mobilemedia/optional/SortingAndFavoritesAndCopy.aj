/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 9 Aug 2007
 * 
 */
package lancs.mobilemedia.optional;

import lancs.mobilemedia.optional.copy.CopyMultiMediaAspect;
import lancs.mobilemedia.optional.favourites.FavouritesAspect;
import lancs.mobilemedia.optional.favourites.PersisteFavoritesAspect;
import lancs.mobilemedia.optional.sorting.SortingAspect;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect SortingAndFavoritesAndCopy {

	declare precedence : CopyMultiMediaAspect, FavouritesAspect, SortingAspect, PersisteFavoritesAspect; // [EF] Checked Ok??

}
