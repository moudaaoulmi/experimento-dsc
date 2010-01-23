/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 5 Jun 2008
 * 
 */
package lancs.mobilemedia.optional;

import lancs.mobilemedia.optional.favourites.FavouritesAspect;
import lancs.mobilemedia.optional.favourites.PersisteFavoritesAspect;
import lancs.mobilemedia.optional.sorting.SortingAspect;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect SortingAndFavorites {

	declare precedence : FavouritesAspect, SortingAspect, PersisteFavoritesAspect; // [EF] Checked Ok!

}
