package org.jhotdraw.samples.javadraw;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.jhotdraw.util.CommandMenu;


public privileged aspect JavadrawExceptionHandler {

	// ---------------------------
	// Declare soft's
	// ---------------------------
	declare soft: MalformedURLException: FollowURLTool_getDocumentBaseHandler();
	declare soft: Exception: JavaDrawApp_executeComandMenu1Handler();
	declare soft: IOException: JavaDrawViewer_openStreamHandler();
	declare soft: InterruptedException: Animator_runHandler();

	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut FollowURLTool_getDocumentBaseHandler(): call( URL.new(..) )
		 && withincode(void FollowURLTool.mouseUp(..));

	pointcut JavaDrawApp_executeComandMenu1Handler():
		execution(private static void JavaDrawApp.internalExecuteCommandMenu(Iterator));
	
	pointcut JavaDrawApp_internalCreateImagesMenuHandler():
		execution(private void JavaDrawApp.internalCreateImagesMenu(..));
	
	pointcut  JavaDrawViewer_openStreamHandler():
		execution(void JavaDrawViewer.loadDrawing(..));

    pointcut Animator_runHandler(): execution(private void Animator.internalRun(long));
    				

	// ---------------------------
	// Advice's
	// ---------------------------
/**
 * Foi tratada j‡ pela persistencia
 * 
 *
	void around(): loadDrawingHandler(){
		JavaDrawViewer jDV = (JavaDrawViewer) thisJoinPoint.getThis();
		try {
			proceed();
		} 
		// @AJHD added
		// catch the soft exception instead of the IO one, and get the wrapped
		// one for report
		catch (SoftException e) {
			jDV.fDrawing = jDV.createDrawing();
			System.err
					.println("Error when Loading: " + e.getWrappedThrowable());
			jDV.showStatus("Error when Loading: " + e.getWrappedThrowable());
		}
	}*/

	URL around(): FollowURLTool_getDocumentBaseHandler(){
		URL url = null;
		try {
			url = proceed();
		} catch (MalformedURLException exception) {
			FollowURLTool fUT = (FollowURLTool) thisJoinPoint.getThis();
			fUT.fApplet.showStatus(exception.toString());
		}
		return url;
	}
	

	void around(): JavaDrawApp_executeComandMenu1Handler(){
		try {
			proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void around(CommandMenu menu, File imagesDirectory): JavaDrawApp_internalCreateImagesMenuHandler()
				&& args( menu, imagesDirectory)
	{
		try{
			proceed( menu, imagesDirectory);
		}catch( Exception e){
			//do nothing
		}
		
	}
	
	void around(): JavaDrawViewer_openStreamHandler(){
		try {
			proceed();
		}catch (IOException e) {
			JavaDrawViewer obj = (JavaDrawViewer) thisJoinPoint.getThis();
			obj.fDrawing = obj.createDrawing();
			System.err.println("Error when Loading: " + e);
			obj.showStatus("Error when Loading: " + e);
		}
	}
	
	void around(long tm): Animator_runHandler() && args(tm){
		try {
			 proceed(tm);
		}  catch (InterruptedException e) {
				//break;
		}
	}
}
