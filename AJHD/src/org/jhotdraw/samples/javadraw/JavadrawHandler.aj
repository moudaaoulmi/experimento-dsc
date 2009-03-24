package org.jhotdraw.samples.javadraw;

import java.net.MalformedURLException;

public privileged aspect JavadrawHandler {

	// ---------------------------
	// Declare soft's
	// ---------------------------
	/**declare soft: MalformedURLException: mouseUpHandler();*/
	declare soft: Exception: executeComandMenu1Handler();
	/** declare soft: IOException: loadDrawingHandler(); */

	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut mouseUpHandler(): 
		execution(* FollowURLTool.mouseUp(..));

	pointcut executeComandMenu1Handler():
		execution(* JavaDrawApp.executeComandMenu1(..));

	/** pointcut loadDrawingHandler():
		execution(* JavaDrawViewer.loadDrawing(..)); */

	// ---------------------------
	// Advice's
	// ---------------------------
/**
 * Nao tenho como refatorar porque acessa uma
 * variavel privada -> "fDrawing"
 * 
 * 
	void around(): loadDrawingHandler(){
		JavaDrawViewer jDV = (JavaDrawViewer) thisJoinPoint.getThis();
		try {
			proceed();
		} catch (IOException e) {
			jDV.fDrawing = jDV.createDrawing();
			System.err.println("Error when Loading: " + e);
			jDV.showStatus("Error when Loading: " + e);
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

	/**
	void around(): mouseUpHandler(){
		try {
			proceed();
		} catch (MalformedURLException exception) {
			FollowURLTool fUT = (FollowURLTool) thisJoinPoint.getThis();
			fUT.fApplet.showStatus(exception.toString());
		}
	}
	*/

	void around(): executeComandMenu1Handler(){
		try {
			proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
