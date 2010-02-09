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
import lancs.mobilemedia.alternative.AbstractAlternativeFeature;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect VideoNotPhotoNotMusic extends AbstractAlternativeFeature {

	after(MainUIMidlet midlet): startApp(midlet) {
		midlet.videoRootController.init(midlet.videoModel);
		System.out.println("<* VideoNotPhotoNotMusic *>");
	}
}
