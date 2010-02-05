/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 7 Aug 2008
 * 
 */
package lancs.mobilemedia.alternative.music;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.alternative.AbstractAlternativeFeature;

/**
 * @author Eduardo Figueiredo
 *
 */
public abstract aspect AbstractMusicAspect extends AbstractAlternativeFeature {

	// ********  MainUIMidlet  ********* //
	
	// [NC] Added in the scenario 07
	// [EF] Attributes are public because PhotoAndMusicAspect uses them.
	public BaseController MainUIMidlet.musicRootController;
	
	// [NC] Added in the scenario 07
	public AlbumData MainUIMidlet.musicModel;

}
