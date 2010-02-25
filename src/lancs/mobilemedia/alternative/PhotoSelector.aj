/*
 * Lancaster University
 * Computing Department
 * 
 * Created by Eduardo Figueiredo
 * Date: 14 Jun 2008
 * 
 */
package lancs.mobilemedia.alternative;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect PhotoSelector {

	//public boolean handleCommand(Command command)
	pointcut handleCommandAction(SelectMediaController controller, Command command): 
		 execution(public boolean SelectMediaController.handleCommand(Command )) && args(command) && this(controller);

	boolean around(SelectMediaController controller, Command command): handleCommandAction(controller, command) {
		if (proceed(controller, command)) return true;
		String label = command.getLabel();
      	System.out.println( "<* PhotoSelector.handleCommand() *>: " + label);
     	if (label.equals("Select")) {
 			List down = (List) Display.getDisplay(controller.midlet).getCurrent();
 			String textOption = down.getString( down.getSelectedIndex() );
 			if (textOption.equals("Photos")) {
 				controller.imageController.init(controller.imageAlbumData);
 	 			return true;
 			}
      	}
     	return false;
	}
}
