/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 9 Aug 2007
 * 
 */
package lancs.mobilemedia.optional;

import lancs.mobilemedia.optional.copy.CopyAspect;
import lancs.mobilemedia.optional.favourites.FavouritesAspect;
import lancs.mobilemedia.optional.favourites.PersisteFavoritesAspect;
import lancs.mobilemedia.optional.sms.SMSAspect;
import lancs.mobilemedia.optional.sorting.SortingAspect;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect SortingAndFavoritesAndCopyAndSMS {

	declare precedence : SMSAspect, CopyAspect, FavouritesAspect, SortingAspect, PersisteFavoritesAspect; // [EF] Checked Ok??

}
