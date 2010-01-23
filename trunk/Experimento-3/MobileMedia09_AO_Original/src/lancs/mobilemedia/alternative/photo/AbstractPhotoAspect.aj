/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 14 Jun 2008
 * 
 */
package lancs.mobilemedia.alternative.photo;

import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.alternative.AbstractAlternativeFeature;

/**
 * @author Eduardo Figueiredo
 *
 */
public abstract aspect AbstractPhotoAspect extends AbstractAlternativeFeature {

	// ********  MainUIMidlet  ********* //
	
	//(m v C) Controller
	// [NC] Added in the scenario 07
	// [EF] Attributes are public because SMSAspect uses them.
	// [EF] PhotoAndMusicAspect also uses.
	public BaseController MainUIMidlet.imageRootController;

	//Model (M v c)
	// [NC] Added in the scenario 07
	public AlbumData MainUIMidlet.imageModel;

}
