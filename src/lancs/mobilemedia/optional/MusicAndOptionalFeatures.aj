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

import lancs.mobilemedia.alternative.music.MusicAspect;
import lancs.mobilemedia.alternative.music.optional.CopyAndMusic;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect MusicAndOptionalFeatures {

	declare precedence : CopyMultiMediaAspect, FavouritesAspect, SortingAspect, PersisteFavoritesAspect; // [EF] Checked Ok??

	declare precedence : MusicAspect, CopyAndMusic; // [EF] Checked Ok??

}
