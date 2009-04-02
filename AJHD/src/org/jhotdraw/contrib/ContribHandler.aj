package org.jhotdraw.contrib;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.beans.PropertyVetoException;
import  javax.swing.*;

import org.jhotdraw.ExceptionHandler;


@ExceptionHandler
public aspect ContribHandler {

	declare soft:  PropertyVetoException: setSelectedHandler() || setSelectedHandler2() ;
	declare soft: NoninvertibleTransformException: scrollSubjectToHandler();
	//declare soft: IllegalComponentStateException: showPopupMenuHandler();  -------- É RUNTIME EXCEPTION ----------
	
	
    pointcut setSelectedHandler(): call (* JInternalFrame.setSelected(..)) && withincode( * *.buildChildMenusPartOne(..) )   
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.cascadeFrames(..)) 
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.tileFramesHorizontally(..)) 
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.tileFramesVertically(..)) 
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.arrangeFramesVertically(..))
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.arrangeFramesHorizontally(..)) ;
    
    
    pointcut setSelectedHandler2(): call(* JInternalFrame.setSelected(..)) &&
											withincode( * MDIDesktopPane.addToDesktop(..) );
    pointcut scrollSubjectToHandler(): execution(* MiniMapView.scrollSubjectTo(..));
    pointcut showPopupMenuHandler(): execution(* CustomSelectionTool.showPopupMenu(..));

    void around(): setSelectedHandler(){
    	try {
    		proceed();
		}
		catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
    
    
    void around(JInternalFrame frame): setSelectedHandler2()  && args(frame){
    	try {
    		proceed(frame);
		}
    	catch (PropertyVetoException e) {
			frame.toBack();
		}
	}
	
	/**
	 * 
	 * Tive que mudar para afetar o método todo
	 * (scrollSubjectTo) por causa do statement 
	 * 'return'que tem dentro do catch
	 * 
	 * @return
	 */
    void around(): scrollSubjectToHandler(){
		try {
			 proceed();
		}
		catch (NoninvertibleTransformException nite) {
			nite.printStackTrace();
		}
	}
	
    /** 
	 * 
	 * Tive que mudar para afetar o método todo
	 * (showPopupMenu) por causa do statement 
	 * 'return'que tem dentro do catch 
	 * 
	 */
    void  around(): showPopupMenuHandler(){
		try {
		    proceed();
		}
		catch (IllegalComponentStateException nite) {
			// For some reason, the component
			// apparently isn't showing on the
			// screen (huh?). Never mind - don't
			// show the popup..
			//return;
		}
	}
   

}