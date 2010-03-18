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

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect MusicNotPhotoNotVideo extends AbstractMusicAspect {

	after(MainUIMidlet midlet): startApp(midlet) {
		midlet.musicRootController.init(midlet.musicModel);
		System.out.println("<* MusicNotPhotoNotVideo *>");
	}
	
}
