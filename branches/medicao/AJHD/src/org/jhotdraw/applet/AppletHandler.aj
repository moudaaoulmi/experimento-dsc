package org.jhotdraw.applet;

import java.io.IOException;
import java.net.URL;


public privileged aspect AppletHandler {
	
	declare soft: IOException : DrawApplet_readFromStorableInput() || DrawApplet_readFromObjectInput() || DrawApplet_showHelp();
	
	declare soft: ClassNotFoundException : DrawApplet_readFromObjectInput();
	
	pointcut DrawApplet_readFromStorableInput(): execution (* DrawApplet.readFromStorableInput(..)) ;

	pointcut DrawApplet_readFromObjectInput(): execution (* DrawApplet.readFromObjectInput(..)) ;
	
	pointcut DrawApplet_showHelp(): execution (* DrawApplet.showHelp(..)) ;
	
	void around(): DrawApplet_readFromStorableInput(){
		DrawApplet drawApplet = null;
		try {
			proceed();
		}
		catch (IOException e) {
			drawApplet = (DrawApplet) thisJoinPoint.getThis();
			drawApplet.initDrawing();
			drawApplet.showStatus("Error:" + e);
		}
		//@AJHD added
		//catch the soft exception instead of the IO one, and get the wrapped one for report
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
		catch (IOException e) {
			drawApplet = (DrawApplet) thisJoinPoint.getThis();
			drawApplet.initDrawing();
			drawApplet.showStatus("Error: " + e);
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
