/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 14 Jun 2008
 * 
 */
package lancs.mobilemedia.optional;

import lancs.mobilemedia.optional.copy.CopyMultiMediaAspect;
import lancs.mobilemedia.alternative.music.optional.CopyAndMusic;
import lancs.mobilemedia.optional.favourites.FavouritesAspect;
import lancs.mobilemedia.optional.favourites.PersisteFavoritesAspect;
import lancs.mobilemedia.optional.sms.SMSAspect;
import lancs.mobilemedia.optional.sorting.SortingAspect;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect OptionaFeaturesButVideo {

	declare precedence : SMSAspect, CopyMultiMediaAspect, CopyAndMusic, FavouritesAspect, SortingAspect, PersisteFavoritesAspect;

}
