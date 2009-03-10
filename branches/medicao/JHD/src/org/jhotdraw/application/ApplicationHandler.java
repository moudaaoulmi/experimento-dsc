package org.jhotdraw.application;

import org.jhotdraw.exception.GeneralException;

public class ApplicationHandler extends GeneralException {

	public void DrawApplicationOpen(DrawApplication dA) {
		dA.exit();
	}

	public void drawApplicationSaveDrawing(DrawApplication dA, String msg) {
		dA.showStatus(msg);
	}
}
