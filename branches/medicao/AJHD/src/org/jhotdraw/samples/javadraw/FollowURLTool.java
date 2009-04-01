/*
 * @(#)FollowURLTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.samples.javadraw;

import javax.swing.JApplet;
import java.awt.event.*;
import java.net.*;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.AbstractTool;

/**
 * @version <$CURRENT_VERSION$>
 */
class FollowURLTool extends AbstractTool {
	private JApplet fApplet;

	FollowURLTool(DrawingEditor newDrawingEditor, JApplet applet) {
		super(newDrawingEditor);
		fApplet = applet;
	}

	/**
	 * Handles mouse move events in the drawing view.
	 */
	public void mouseMove(MouseEvent e, int x, int y) {
		String urlstring = null;
		Figure figure = drawing().findFigureInside(x, y);
		if (figure != null) {
			urlstring = (String) figure
					.getAttribute(FigureAttributeConstant.URL);
		}
		if (urlstring != null) {
			fApplet.showStatus(urlstring);
		} else {
			fApplet.showStatus("");
		}
	}

	
	 
	public void mouseUp(MouseEvent e, int x, int y) {
		Figure figure = getActiveDrawing().findFigureInside(x, y);
		if (figure == null) {
			return;
		}
		String urlstring = (String) figure
				.getAttribute(FigureAttributeConstant.URL);
		if (urlstring == null) {
			return;
		}
		URL url = null;
		
		//try {
			url = new URL(fApplet.getDocumentBase(), urlstring);
//		} catch (MalformedURLException e1) {
//			fApplet.showStatus(e1.toString());
//		}
		fApplet.getAppletContext().showDocument(url);
	}


}
