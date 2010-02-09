package lancs.mobilemedia.optional.pim;

import javax.microedition.lcdui.Command;

import lancs.mobilemedia.optional.pim.PimController;
import lancs.mobilemedia.core.ui.controller.*;
import lancs.mobilemedia.alternative.photo.PhotoViewScreen;


public privileged aspect PimAspect {
	
	
	//******************* PhotoViewScreen *********************/
	
	public static final Command pimCommand = new Command("Add to Contact List", Command.ITEM, 1);
	
	pointcut initMenu(PhotoViewScreen screen):
		execution(public PhotoViewScreen.new(..)) && this(screen);

	after(PhotoViewScreen screen) : initMenu(screen) {
		screen.addCommand(pimCommand);
	}
	
	
// ********  MediaController  ********* //
	
	//public AbstractController PhotoController.getMediaController(String mediaName)
	pointcut getMediaController(MediaController controller, String imageName): 
		 (call(public AbstractController MediaController.getMediaController(String)) && this(controller))&& args (imageName);
	
	AbstractController around (MediaController controller, String imageName): getMediaController(controller, imageName) {
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		AbstractController nextcontroller = proceed(controller, imageName);
		PimController control = new PimController(controller.midlet);//, controller.getAlbumData(), (AlbumListScreen) controller.getAlbumListScreen(), imageName);
		control.setNextController(nextcontroller);
		return control;
	}	
	
}
