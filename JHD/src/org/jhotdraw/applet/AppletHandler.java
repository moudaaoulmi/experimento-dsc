package org.jhotdraw.applet;


public class AppletHandler {

	public void drawAppleTreadFromStorableInput(DrawApplet dA, Exception e,
			String msg) {
		dA.initDrawing();
		dA.showStatus(msg + e);
	}

	public void drawAppletShowHelp(DrawApplet dA){
		dA.showStatus("Help file not found");
	}
}
