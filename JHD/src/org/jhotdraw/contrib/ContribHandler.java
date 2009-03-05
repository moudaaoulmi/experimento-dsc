package org.jhotdraw.contrib;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

public class ContribHandler {
	
	public void cTXWindowMenuBuildChildMenus(Exception e){
		e.printStackTrace();
	}
	
	public void customSelectionToolShowPopupMenu(){
		// For some reason, the component
		// apparently isn't showing on the
		// screen (huh?). Never mind - don't
		// show the popup..
	
	}
	
	public void mDIDesktopPaneAddToDesktop(JInternalFrame frame){
		frame.toBack();
	}

}
