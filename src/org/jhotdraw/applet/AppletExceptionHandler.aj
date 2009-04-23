package org.jhotdraw.applet;

import java.io.IOException;

public privileged aspect AppletExceptionHandler {
	
	
	// Pointcuts
	
	pointcut DrawApplet_readFromStorableInputHandler(): execution (private void DrawApplet.readFromStorableInput(..)) ;

	pointcut DrawApplet_readFromObjectInput(): execution (private void  DrawApplet.readFromObjectInput(..)) ;
	
	pointcut DrawApplet_showHelp(): execution (protected void DrawApplet.showHelp(..)) ;
	
	// intetypes
	
	declare soft: IOException : DrawApplet_readFromStorableInputHandler() || DrawApplet_readFromObjectInput() || DrawApplet_showHelp();
	
	declare soft: ClassNotFoundException : DrawApplet_readFromObjectInput();
	
	
	// advices
	
	void around(): DrawApplet_readFromStorableInputHandler() || DrawApplet_readFromObjectInput(){
		DrawApplet drawApplet = null;
		try {
			proceed();
		}
		catch (IOException e) {
			drawApplet = (DrawApplet) thisJoinPoint.getThis();
			drawApplet.initDrawing();
			drawApplet.showStatus("Error:" + e);
		}
	}
	
	void around(): DrawApplet_readFromStorableInputHandler(){
		DrawApplet drawApplet = null;
		try {
			proceed();
		}
		catch (org.aspectj.lang.SoftException e) {
			drawApplet = (DrawApplet) thisJoinPoint.getThis();
			drawApplet.showStatus("Error: " + e.getWrappedThrowable());
		}
    }
	
	void around(): DrawApplet_readFromObjectInput(){
		DrawApplet drawApplet = null;
		try {
			proceed();
		}
		catch (ClassNotFoundException e) {
			drawApplet = (DrawApplet) thisJoinPoint.getThis();
			drawApplet.initDrawing();
			drawApplet.showStatus("Class not found: " + e);
		}
    }
	
	void around(): DrawApplet_showHelp(){
		DrawApplet drawApplet = null;
		try {
			proceed();
		}
		catch (IOException e) {
			drawApplet = (DrawApplet) thisJoinPoint.getThis();
			drawApplet.showStatus("Help file not found");
		}
    }
	
}
