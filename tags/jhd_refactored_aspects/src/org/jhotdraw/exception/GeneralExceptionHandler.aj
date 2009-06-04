package org.jhotdraw.exception;

import java.awt.MediaTracker;
import org.jhotdraw.util.Iconkit;
import org.jhotdraw.samples.javadraw.JavaDrawApp;

public aspect GeneralExceptionHandler {
	
	// ---------------------------
	// Declare Soft's
	// ---------------------------
	declare soft: Exception: Iconkit_loadRegisteredImages()||
							 JavaDrawApp_createImagesMenu();

	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut Iconkit_loadRegisteredImages(): 
		call(* MediaTracker.waitForAll(..)) &&
		withincode(* Iconkit.loadRegisteredImages(..));

	pointcut JavaDrawApp_createImagesMenu():
		call(* JavaDrawApp.createImagesMenuInternal(..)) &&
		withincode(* JavaDrawApp.createImagesMenu(..));

	// ---------------------------
	// Advice's
	// ---------------------------
	void around(): Iconkit_loadRegisteredImages() ||
				   JavaDrawApp_createImagesMenu(){
		try {
			proceed();
		} catch (Exception e) {
			// do nothing
		}
	}
}
