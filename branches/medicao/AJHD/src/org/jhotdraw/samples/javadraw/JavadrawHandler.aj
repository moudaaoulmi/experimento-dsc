package org.jhotdraw.samples.javadraw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;


public privileged aspect JavadrawHandler {

	// ---------------------------
	// Declare soft's
	// ---------------------------
	declare soft: MalformedURLException: FollowURLTool_getDocumentBaseHandler() || JavaDrawViewer_loadDrawingHandler();
	declare soft: Exception: JavaDrawApp_executeComandMenu1Handler();
	declare soft: IOException: JavaDrawViewer_openStreamHandler();
	declare soft: InterruptedException: Animator_runHandler();

	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut FollowURLTool_getDocumentBaseHandler(): call( URL.new(..) )
		 && withincode(void FollowURLTool.mouseUp(..));

	pointcut JavaDrawApp_executeComandMenu1Handler():
		execution(void JavaDrawApp.executeComandMenu1(..));
	
	pointcut  JavaDrawViewer_openStreamHandler():
		call( InputStream URL.openStream(..)) && 
		withincode(void JavaDrawViewer.loadDrawing(..));

    pointcut JavaDrawViewer_loadDrawingHandler(): call( URL.new(..) )
	 && withincode(void JavaDrawViewer.loadDrawing(..));
    
    pointcut Animator_runHandler(): execution(void Animator.extracaoBreak(..));
    				

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


	URL around(): JavaDrawViewer_loadDrawingHandler(){
		URL url = null;
		try {
			url = proceed();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return url;
	}
    
    
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
	
	InputStream around(): JavaDrawViewer_openStreamHandler(){
		
		JavaDrawViewer obj = (JavaDrawViewer) thisJoinPoint.getThis();
		InputStream in = null;
		
		try {
			in =  proceed();
		}catch (IOException e) {
				obj.fDrawing = obj.createDrawing();
				System.err.println("Error when Loading: " + e);
				obj.showStatus("Error when Loading: " + e);
		}
		return in;
	}
	
	void around(): Animator_runHandler(){
		try {
			 proceed();
		}  catch (InterruptedException e) {
				//break;
		}
	}
    
	
	
}
