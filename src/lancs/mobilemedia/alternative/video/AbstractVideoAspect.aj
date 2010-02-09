/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 21 Aug 2008
 * 
 */
package lancs.mobilemedia.alternative.video;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.BaseController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;
import lancs.mobilemedia.alternative.AbstractAlternativeFeature;

/**
 * @author Eduardo Figueiredo
 *
 */
public abstract aspect AbstractVideoAspect extends AbstractAlternativeFeature {

	// ********  MainUIMidlet  ********* //
	
	//(m v C) Controller
	// [NC] Added in the scenario 08
	public BaseController MainUIMidlet.videoRootController;
	public AlbumData MainUIMidlet.videoModel;
	
}
