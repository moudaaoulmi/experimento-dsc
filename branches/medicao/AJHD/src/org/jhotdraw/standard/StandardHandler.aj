package org.jhotdraw.standard;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import org.aspectj.lang.SoftException;

public privileged aspect StandardHandler {
	
	// Declare Soft
	declare soft : IOException : internalClone2Hander() || internalCloneHandler() || internalGetDataHandler();
	declare soft : ClassNotFoundException:	internalClone2Hander();
	declare soft : CloneNotSupportedException  :cloneHandler();
	declare soft : Exception: internaldrawXORRectHandler();
	declare soft : InterruptedException :  internalLockHandler();
	//declare soft : Throwable : StandardDrawingView_DrawingViewMouseListener_mousePressed();
	declare soft : Exception: internalToolButtonHandler();
	
	
	//Pointcuts
	pointcut internalClone2Hander(): execution( private Object AbstractFigure.internalClone2(..));

	pointcut internalCloneHandler(): execution( private void AbstractFigure.internalClone(..));

	pointcut cloneHandler(): execution (public Object AbstractLocator.clone());

	pointcut internaldrawXORRectHandler(): execution(private void SelectAreaTracker.internaldrawXORRect(..));

	pointcut internalLockHandler() : execution(private void StandardDrawing.internalLock(..));
	
	//pointcut StandardDrawingView_DrawingViewMouseListener_mousePressed(): execution(public void mousePressed(..));
	
	pointcut internalGetDataHandler(): execution(private void StandardFigureSelection.internalGetData(..));
	
	pointcut internalToolButtonHandler(): execution(private void ToolButton.internalToolButton(..));

	

	Object around(Object clone, InputStream input):  internalClone2Hander() && args(clone,input){
		try {
			return proceed(clone, input);
		} catch (IOException e) {
			System.err.println(e.toString());
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found: " + e);
		}
		return clone;
	}

	void around():  internalCloneHandler(){
		try {
			proceed();
		} catch (IOException e) {
			System.err.println("Class not found: " + e);
		}
	}

	Object around() throws InternalError:  cloneHandler(){
		try {
			return proceed();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	void around(Rectangle r, Graphics g) : internaldrawXORRectHandler() && args(r,g) {
		try {
			proceed(r, g);
		} finally {
			g.dispose(); // SF bugtracker id: #490663
		}
	}

	void around() : internalLockHandler()  || internalToolButtonHandler(){
		try {
			proceed();
		} catch (InterruptedException e) {
			
		}
	}
	
	/** REUSE ______
	void around(): internalToolButtonHandler(){
		try{
			proceed();
		}catch (Exception e) {
			// ignore exception
		}
	}*/
	
//	void around(StandardDrawingView d) : StandardDrawingView_DrawingViewMouseListener_mousePressed()&& this(d){
//		try{
//			proceed(d);
//		}catch(Throwable e){
//			//standardDrawingView.handleMouseEventException(e); 			
//		}
//	}
	
	void around() : internalGetDataHandler(){
		try{
			proceed();
		}catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.toString());
		}
		//@AJHD added
		//catch the soft exception instead of the IO one, and get the wrapped one for report
		catch (SoftException e) {
			System.err.println(e.getWrappedThrowable().toString());
		}
	}
	
	

}
