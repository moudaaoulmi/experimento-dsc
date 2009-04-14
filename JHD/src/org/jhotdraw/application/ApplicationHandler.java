package org.jhotdraw.application;

import org.jhotdraw.exception.ExceptionHandler;
import org.jhotdraw.exception.GeneralException;

@ExceptionHandler
public class ApplicationHandler extends GeneralException {

	public void DrawApplicationOpen(DrawApplication dA) {
		dA.exit();
	}

	public void drawApplicationSaveDrawing(DrawApplication dA, String msg) {
		dA.showStatus(msg);
	}
}
