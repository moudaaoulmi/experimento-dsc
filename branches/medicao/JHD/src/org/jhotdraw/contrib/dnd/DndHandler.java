package org.jhotdraw.contrib.dnd;

import java.awt.dnd.DropTargetDropEvent;

public class DndHandler {

	// TODO Caso de reuso + fácil em OO do que em AO
	public void dNDHelperProcessReceivedData(Exception e) {
		System.err.println(e);
	}

	public void dNDCHelperCreateDropTarget(NullPointerException npe) {
		System.err.println("View Failed to initialize to DND.");
		System.err
				.println("Container likely did not have peer before the DropTarget was added");
		System.err.println(npe);
		npe.printStackTrace();
	}

	public void dNDHelperDrop(NullPointerException npe, DropTargetDropEvent dtde) {
		npe.printStackTrace();
		dtde.dropComplete(false);
	}
}
