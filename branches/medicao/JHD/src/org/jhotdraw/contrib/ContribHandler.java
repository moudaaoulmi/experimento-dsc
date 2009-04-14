package org.jhotdraw.contrib;

import javax.swing.JInternalFrame;

import org.jhotdraw.exception.ExceptionHandler;
import org.jhotdraw.exception.GeneralException;

@ExceptionHandler
public class ContribHandler extends GeneralException {

	public void customSelectionToolShowPopupMenu() {
		// For some reason, the component
		// apparently isn't showing on the
		// screen (huh?). Never mind - don't
		// show the popup..
	}

	public void mDIDesktopPaneAddToDesktop(JInternalFrame frame) {
		frame.toBack();
	}
}
