package org.jhotdraw.samples.javadraw;

import java.io.IOException;
import org.jhotdraw.exception.GeneralException;
import org.jhotdraw.framework.Drawing;

public class JavadrawHandler extends GeneralException {

	public void javaDrawAppCreateImagesMenu() {
		// do nothing
	}

	public void javaDrawViewerLoadDrawing(Drawing fDrawing, JavaDrawViewer jDV,
			IOException e) {
		fDrawing = jDV.createDrawing();
		System.err.println("Error when Loading: " + e);
		jDV.showStatus("Error when Loading: " + e);
	}

}
