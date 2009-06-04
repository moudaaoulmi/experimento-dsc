package org.jhotdraw.contrib;

import java.awt.IllegalComponentStateException;
import java.awt.geom.NoninvertibleTransformException;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

import org.jhotdraw.exception.ExceptionHandler;

@ExceptionHandler
public aspect ContribHandler {

	// pointcuts
	
	pointcut MDIDesktopPane_internalSetMaximum(): execution(private void MDIDesktopPane.internalSetMaximum(JInternalFrame));
	pointcut MDIDesktopPane_internalSetSelected(): execution(private void MDIDesktopPane.internalSetSelected(JInternalFrame));
	pointcut CTXWindowMenu_buildChildMenusPartOne() :execution( private void CTXWindowMenu.internalBuildChildMenus(JInternalFrame)) ;
	pointcut WindowMenu_buildChildMenusPartOneHandler() :execution( private void WindowMenu.internalBuildChildMenus(JInternalFrame));
	pointcut MiniMapView_scrollSubjectTo(): execution(protected void MiniMapView.scrollSubjectTo(..));
	pointcut CustomSelectionTool_showPopupMenu(): execution(protected void CustomSelectionTool.showPopupMenu(..));

	// intertypes

	declare soft:  PropertyVetoException: MDIDesktopPane_internalSetMaximum() || 
			MDIDesktopPane_internalSetSelected() || CTXWindowMenu_buildChildMenusPartOne() || 
			WindowMenu_buildChildMenusPartOneHandler();
	declare soft: NoninvertibleTransformException: MiniMapView_scrollSubjectTo();

	// advices

	void around(JInternalFrame frame): (MDIDesktopPane_internalSetMaximum() || CTXWindowMenu_buildChildMenusPartOne()|| WindowMenu_buildChildMenusPartOneHandler())&& args(frame) {
		try {
			proceed(frame);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	void around(JInternalFrame frame): MDIDesktopPane_internalSetSelected() && args(frame){
		try {
			proceed(frame);
		} catch (PropertyVetoException e) {
			frame.toBack();
		}
	}
	
	/**
	 * 
	 * Tive que mudar para afetar o método todo (scrollSubjectTo) por causa do
	 * statement 'return'que tem dentro do catch
	 * 
	 * @return
	 */
	void around(): MiniMapView_scrollSubjectTo(){
		try {
			proceed();
		} catch (NoninvertibleTransformException nite) {
			nite.printStackTrace();
		}
	}

	/**
	 * 
	 * Tive que mudar para afetar o método todo (showPopupMenu) por causa do
	 * statement 'return'que tem dentro do catch
	 * 
	 */
	void around(): CustomSelectionTool_showPopupMenu(){
		try {
			proceed();
		} catch (IllegalComponentStateException nite) {
			// For some reason, the component
			// apparently isn't showing on the
			// screen (huh?). Never mind - don't
			// show the popup..
			// return;
		}
	}

}