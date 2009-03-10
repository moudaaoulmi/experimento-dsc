package org.jhotdraw.contrib.dnd;

import java.awt.dnd.DropTargetDropEvent;

import org.jhotdraw.exception.GeneralException;

public class DndHandler extends GeneralException {

	public void dNDHelperDrop(DropTargetDropEvent dtde) {
		dtde.dropComplete(false);
	}
}
