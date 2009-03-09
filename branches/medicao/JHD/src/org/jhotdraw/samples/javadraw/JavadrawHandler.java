package org.jhotdraw.samples.javadraw;

import java.io.IOException;

import javax.swing.JApplet;

import org.jhotdraw.framework.Drawing;

public class JavadrawHandler {
	
	public void mouseUpHandler(Exception exception,JApplet fApplet){
		fApplet.showStatus(exception.toString());
	}
	
	public static void executeCommandMenuHandler(Exception e){
		e.printStackTrace();
	}

}
