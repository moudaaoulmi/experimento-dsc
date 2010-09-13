package org.maze.eimp.views;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The Instant Messenger perspective displays the buddies in the left part of
 * the window and the chat sessions in the main part.
 * 
 * @see IPerspectiveFactory
 * @author Ringo De Smet
 */
public class InstantMessengerPerspectiveFactory
	implements IPerspectiveFactory {

	public InstantMessengerPerspectiveFactory() {
	}

	/**
	 * Although the editor area is set to invisible, we put the buddy list to
	 * the left of it and the chat sessions to the right of it.
	 */
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView(
			"org.maze.eimp.views.BuddyListView",
			IPageLayout.LEFT,
			0.3f,
			layout.getEditorArea());
		layout.addView(
			"org.maze.eimp.views.MultiSessionBoardView",
			IPageLayout.RIGHT,
			0.7f,
			layout.getEditorArea());
	}
}
