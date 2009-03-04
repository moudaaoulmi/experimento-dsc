package org.jhotdraw.application;

import java.io.IOException;

public class ApplicationHandler {

	public void DrawApplicationOpen(Exception e, DrawApplication dA) {
		System.err.println(e.getMessage());
		dA.exit();
	}

	public void drawApplicationSaveDrawing(IOException e, DrawApplication dA) {
		dA.showStatus(e.toString());
	}

	public void drawApplicationLoadDrawing(DrawApplication dA, IOException e) {
		dA.showStatus("Error: " + e);
	}

	public void drawApplicationNewLookAndFeel(Exception e) {
		System.err.println(e);
	}
}
