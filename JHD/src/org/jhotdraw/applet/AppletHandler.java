package org.jhotdraw.applet;

import java.applet.Applet;

import org.jhotdraw.exception.GeneralException;

public class AppletHandler extends GeneralException{

	public void drawAppleTreadFromStorableInput(DrawApplet dA, Exception e,
			String msg) {
		dA.initDrawing();
		dA.showStatus(msg + e);
	}

	public void drawAppletShowHelp(Applet dA, String msg){
		dA.showStatus(msg);
	}
}
