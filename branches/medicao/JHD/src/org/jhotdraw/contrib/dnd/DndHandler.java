package org.jhotdraw.contrib.dnd;

import java.awt.dnd.DropTargetDropEvent;

import org.jhotdraw.exception.ExceptionHandler;
import org.jhotdraw.exception.GeneralException;

@ExceptionHandler
public class DndHandler extends GeneralException {

	public void dNDHelperDrop(DropTargetDropEvent dtde) {
		dtde.dropComplete(false);
	}
}
