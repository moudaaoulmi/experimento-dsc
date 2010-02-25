/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 14 Jun 2008
 * 
 */
package lancs.mobilemedia.alternative.photo;

import lancs.mobilemedia.core.ui.MainUIMidlet;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect PhotoNotVideoNotMusic extends AbstractPhotoAspect {

	after(MainUIMidlet midlet): startApp(midlet) {
		midlet.imageRootController.init(midlet.imageModel);
		System.out.println("<* PhotoNotVideoNotMusic *>");
	}
}
