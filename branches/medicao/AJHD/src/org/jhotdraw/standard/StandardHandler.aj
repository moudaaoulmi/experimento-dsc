package org.jhotdraw.standard;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import org.aspectj.lang.SoftException;

public privileged aspect StandardHandler {

	pointcut AbstractFigure_internalClone2(): execution( private Object AbstractFigure.internalClone2(..));

	pointcut AbstractFigure_internalClone(): execution( private void AbstractFigure.internalClone(..));

	pointcut AbstractLocator_clone(): execution (public Object AbstractLocator.clone());

	pointcut SelectAreaTracker_internaldrawXORRect(): execution(private void SelectAreaTracker.internaldrawXORRect(..));

	pointcut StandardDrawing_internalLock() : execution(private void StandardDrawing.internalLock(..));
	
	//pointcut StandardDrawingView_DrawingViewMouseListener_mousePressed(): execution(public void mousePressed(..));
	
	pointcut StandardFigureSelection_internalGetData(): execution(private void StandardFigureSelection.internalGetData(..));
	
	pointcut ToolButton_internalToolButton(): execution(private void ToolButton.internalToolButton(..));

	// Declare Soft

	declare soft : IOException : AbstractFigure_internalClone2() || AbstractFigure_internalClone() || StandardFigureSelection_internalGetData();
	declare soft : ClassNotFoundException:	AbstractFigure_internalClone2();
	declare soft : CloneNotSupportedException  :AbstractLocator_clone();
	declare soft : Exception: SelectAreaTracker_internaldrawXORRect();
	declare soft : InterruptedException :  StandardDrawing_internalLock();
	//declare soft : Throwable : StandardDrawingView_DrawingViewMouseListener_mousePressed();
	declare soft : Exception: ToolButton_internalToolButton();

	Object around(Object clone, InputStream input):  AbstractFigure_internalClone2() && args(clone,input){
		try {
			return proceed(clone, input);
		} catch (IOException e) {
			System.err.println(e.toString());
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found: " + e);
		}
		return clone;
	}

	void around():  AbstractFigure_internalClone(){
		try {
			proceed();
		} catch (IOException e) {
			System.err.println("Class not found: " + e);
		}
	}

	Object around() throws InternalError:  AbstractLocator_clone(){
		try {
			return proceed();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	void around(Rectangle r, Graphics g) : SelectAreaTracker_internaldrawXORRect() && args(r,g) {
		try {
			proceed(r, g);
		} finally {
			g.dispose(); // SF bugtracker id: #490663
		}
	}

	void around() : StandardDrawing_internalLock() {
		try {
			proceed();
		} catch (InterruptedException e) {
		}
	}
	
//	void around(StandardDrawingView d) : StandardDrawingView_DrawingViewMouseListener_mousePressed()&& this(d){
//		try{
//			proceed(d);
//		}catch(Throwable e){
//			//standardDrawingView.handleMouseEventException(e); 			
//		}
//	}
	
	void around() : StandardFigureSelection_internalGetData(){
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
	
	void around(): ToolButton_internalToolButton(){
		try{
			proceed();
		}catch (Exception e) {
			// ignore exception
		}
	}

}
