package org.jhotdraw.contrib;

import java.awt.IllegalComponentStateException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.beans.PropertyVetoException;
import  javax.swing.*;

public aspect ContribHandler {

	declare soft:  PropertyVetoException: setSelectedHandler() || setSelectedHandler2() ;
	declare soft: NoninvertibleTransformException: getViewToMiniMapTransformHandler();
	//declare soft: IllegalComponentStateException: getLocationOnScreenHandler();  -------- ƒ RUNTIME EXCEPTION ----------
	
	
    pointcut setSelectedHandler(): call (* JInternalFrame.setSelected(..)) && withincode( * *.buildChildMenusPartOne(..) )   
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.cascadeFrames(..)) 
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.tileFramesHorizontally(..)) 
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.tileFramesVertically(..)) 
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.arrangeFramesVertically(..))
    ||   call(* JInternalFrame.setMaximum(..)) && withincode(* MDIDesktopPane.arrangeFramesHorizontally(..)) ;
    
    
    pointcut setSelectedHandler2(): call(* JInternalFrame.setSelected(..)) &&
											withincode( * MDIDesktopPane.addToDesktop(..) );
    pointcut getViewToMiniMapTransformHandler(): call(* AffineTransform.createInverse(..)) &&
    										withincode(* MiniMapView.scrollSubjectTo(..));
    pointcut getLocationOnScreenHandler(): call(* Component.getLocationOnScreen(..)) &&
										    withincode(* CustomSelectionTool.showPopupMenu(..));

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
	
	
    AffineTransform around(): getViewToMiniMapTransformHandler(){
		try {
			 return proceed();
		}
		catch (NoninvertibleTransformException nite) {
			nite.printStackTrace();
			return null;
		}
	}
	
    /** verificar de pode colocar null */
    Point around(): getLocationOnScreenHandler(){
    	Point obj = null;
		try {
		    obj = proceed();
		}
		catch (IllegalComponentStateException nite) {
			return null;
		}
		return obj;
	}
   

}