/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 13 Aug 2007
 * 
 */
package lancs.mobilemedia.alternative.photo.optional;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import lancs.mobilemedia.alternative.photo.PhotoViewScreen;

/**
 * @author Eduardo Figueiredo
 */
public aspect CopyAndPhoto {

	// ********  PhotoViewScreen  ********* //
	
	/* [EF] Added in scenario 05 */
	public static final Command copyCommand = new Command("Copy", Command.ITEM, 1);
	
	//public PhotoViewScreen.PhotoViewScreen(Image)
	pointcut constructor(Image image) :
		call(PhotoViewScreen.new(Image)) && args(image);

	after(Image image) returning (PhotoViewScreen f): constructor(image) {
		f.addCommand(copyCommand);
	}
}
