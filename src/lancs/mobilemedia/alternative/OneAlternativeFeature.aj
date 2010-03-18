package lancs.mobilemedia.alternative;

import javax.microedition.lcdui.Command;

import lancs.mobilemedia.core.ui.screens.AlbumListScreen;

public aspect OneAlternativeFeature {

		// ********  AlbumListScreen  ********* //
		
		//[NC] Added in the scenario 07
		public static final Command exitCommand = new Command("Exit", Command.STOP, 2);
		
		//public void initMenu()
		pointcut initMenu(AlbumListScreen screen):
			execution( public void AlbumListScreen.initMenu() ) && this(screen);
		
		before(AlbumListScreen screen): initMenu(screen) {
			screen.addCommand(exitCommand);
		}
}