package org.jhotdraw.applet;

import java.applet.Applet;

public class AppletHandler {

	public void drawAppleTreadFromStorableInput(DrawApplet dA, Exception e,
			String msg) {
		dA.initDrawing();
		dA.showStatus(msg + e);
	}

	public void drawAppletShowHelp(Applet dA, String msg){
		dA.showStatus(msg);
	}
}
