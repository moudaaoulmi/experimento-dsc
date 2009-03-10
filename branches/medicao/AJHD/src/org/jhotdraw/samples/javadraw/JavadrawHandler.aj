package org.jhotdraw.samples.javadraw;

import java.io.IOException;
import java.net.MalformedURLException;
import org.aspectj.lang.SoftException;

public privileged aspect JavadrawHandler {

	// ---------------------------
	// Declare soft's
	// ---------------------------
	declare soft: MalformedURLException: FollowURLTool_mouseUpInternal();

	declare soft: Exception: JavaDrawApp_createImagesMenu() ||
							 JavaDrawApp_executeComandMenu1();
	declare soft: IOException: JavaDrawViewer_loadDrawing();
	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut FollowURLTool_mouseUpInternal(): 
		execution(* FollowURLTool.mouseUpInternal(..));

	pointcut JavaDrawApp_createImagesMenu():
		call(* JavaDrawApp.createImagesMenuInternal(..)) &&
		withincode(* JavaDrawApp.createImagesMenu(..));

	pointcut JavaDrawApp_executeComandMenu1():
		execution(* JavaDrawApp.executeComandMenu1(..));
	
	pointcut JavaDrawViewer_loadDrawing():
		execution(* JavaDrawViewer.loadDrawing(..));

	// ---------------------------
	// Advice's
	// ---------------------------

void around(): JavaDrawViewer_loadDrawing(){
	JavaDrawViewer jDV = (JavaDrawViewer) thisJoinPoint.getThis();
	try{
		proceed();
	}
	catch (IOException e) {
		jDV.fDrawing = jDV.createDrawing();
		System.err.println("Error when Loading: " + e);
		jDV.showStatus("Error when Loading: " + e);
	}
	//@AJHD added
	//catch the soft exception instead of the IO one, and get the wrapped one for report
	catch (SoftException e) {
		jDV.fDrawing = jDV.createDrawing();
		System.err.println("Error when Loading: " + e.getWrappedThrowable());
		jDV.showStatus("Error when Loading: " + e.getWrappedThrowable());
	}
}

	void around(): FollowURLTool_mouseUpInternal(){
		try {
			proceed();
		} catch (MalformedURLException exception) {
			FollowURLTool fUT = (FollowURLTool) thisJoinPoint.getThis();
			fUT.fApplet.showStatus(exception.toString());
		}
	}

	void around(): JavaDrawApp_createImagesMenu(){
		try {
			proceed();
		} catch (Exception e) {
			// do nothing
		}
	}
	
	void around(): JavaDrawApp_executeComandMenu1(){
		try {
			proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
