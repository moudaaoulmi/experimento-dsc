package org.jhotdraw.contrib.dnd;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;

import org.jhotdraw.Exception.ExceptionHandler;



@ExceptionHandler
public aspect DndHandler {

	declare soft: IOException :DNDHelper_processReceivedData();
	
	declare soft: UnsupportedFlavorException:DNDHelper_processReceivedData();
	
	pointcut DNDHelper_processReceivedData(): execution (* DNDHelper.processReceivedData(..)) ;
	
	pointcut DNDHelper_createDropTarget(): execution (* DNDHelper.createDropTarget(..)) ;
	
	pointcut JHDDropTargetListener_internalDrop(): execution (* JHDDropTargetListener.internalDrop(..)) ;
	
	Object around(): DNDHelper_processReceivedData() {
		Object retorno = null;
		try {
			return retorno = proceed();
		
		}catch (java.io.IOException ioe) {
			System.err.println(ioe);
		}
		catch (UnsupportedFlavorException ufe) {
			System.err.println(ufe);
		}
		catch (ClassCastException cce) {
			System.err.println(cce);
		}
		return retorno;
		
    }
	
	DropTarget around(): DNDHelper_createDropTarget() {
		DropTarget retorno = null;
		try {
			return retorno = proceed();
		
		}catch (NullPointerException npe) {
			System.err.println("View Failed to initialize to DND.");
			System.err.println("Container likely did not have peer before the DropTarget was added");
			System.err.println(npe);
			npe.printStackTrace();
		}
		return retorno;
		
    }
	
	void around(java.awt.dnd.DropTargetDropEvent dtde): JHDDropTargetListener_internalDrop() && args(dtde){
		try {
			proceed(dtde);
		
		}catch (NullPointerException npe) {
			npe.printStackTrace();
			dtde.dropComplete(false);
		}
		
		
    }
}
