package org.jhotdraw.standard;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import org.jhotdraw.util.StorableInput;
import java.util.List;
import javax.swing.JOptionPane;

import org.aspectj.lang.SoftException;

public privileged aspect StandardExceptionHandler {
	
	// Declare Soft
	declare soft : IOException : AbstractFigure_cloneHander() || AbstractFigure_internalCloneHandler() || StandardFigureSelection_internalGetDataHandler();
	declare soft : ClassNotFoundException:	AbstractFigure_cloneHander();
	declare soft : CloneNotSupportedException  :AbstractLocator_cloneHandler();
	declare soft : Exception: SelectAreaTracker_internaldrawXORRectHandler() || ToolButton_internalToolButtonHandler();
	declare soft : InterruptedException :  StandardDrawing_internalLockHandler();
	declare soft : Throwable : StandardDrawingView_DrawingViewMouseListener_mousePressedHandler() || 
				   StandardDrawingView_DrawingViewMouseListener_mouseReleasedHandler() ||
				   StandardDrawingView_DrawingViewMouseMotionListener_mouseDraggedHanlder() || 
				   StandardDrawingView_DrawingViewMouseMotionListener_mouseMovedHandler();
	
	
	//Pointcuts
	pointcut AbstractFigure_cloneHander(): execution( Object AbstractFigure.clone(..));

	pointcut AbstractFigure_internalCloneHandler(): execution( void AbstractFigure.cloneWrite(..));

	pointcut AbstractLocator_cloneHandler(): execution ( Object AbstractLocator.clone());

	pointcut SelectAreaTracker_internaldrawXORRectHandler(): execution( void SelectAreaTracker.InternalDrawXORRect(..));

	pointcut StandardDrawing_internalLockHandler() : execution( void StandardDrawing.internalLock(..));
	
	pointcut StandardDrawingView_DrawingViewMouseListener_mousePressedHandler(): execution ( void StandardDrawingView.DrawingViewMouseListener.mousePressed(..)); 
	
	pointcut StandardDrawingView_DrawingViewMouseListener_mouseReleasedHandler(): execution ( void StandardDrawingView.DrawingViewMouseListener.mouseReleased(..));
	
	pointcut StandardDrawingView_DrawingViewMouseMotionListener_mouseDraggedHanlder(): execution ( void StandardDrawingView.DrawingViewMouseMotionListener.mouseDragged(..));
	
	pointcut StandardDrawingView_DrawingViewMouseMotionListener_mouseMovedHandler(): execution ( void StandardDrawingView.DrawingViewMouseMotionListener.mouseMoved(..));
	
	pointcut StandardFigureSelection_internalGetDataHandler(): execution( void StandardFigureSelection.internalGetData(..));
	
	pointcut ToolButton_internalToolButtonHandler(): execution( void ToolButton.internalToolButton(..));

	

	Object around():  AbstractFigure_cloneHander(){
		Object result = null;
		try {
			result = proceed();
		} catch (IOException e) {
			System.err.println(e.toString());
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found: " + e);
		}
		return result;
	}

	void around():  AbstractFigure_internalCloneHandler(){
		try {
			proceed();
		} catch (IOException e) {
			System.err.println("Class not found: " + e);
		}
	}

	Object around() throws InternalError:  AbstractLocator_cloneHandler(){
		try {
			return proceed();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	void around(Rectangle r, Graphics g) : SelectAreaTracker_internaldrawXORRectHandler() && args(r,g) {
		try {
			proceed(r, g);
		} finally {
			g.dispose(); // SF bugtracker id: #490663
		}
	}

	void around() : StandardDrawing_internalLockHandler()  || ToolButton_internalToolButtonHandler(){
		try {
			proceed();
		} catch (InterruptedException e) {
			
		}
	}
	
	/** REUSE ______
	void around(): ToolButton_internalToolButtonHandler(){
		try{
			proceed();
		}catch (Exception e) {
			// ignore exception
		}
	}*/
	
	void around() : StandardDrawingView_DrawingViewMouseListener_mousePressedHandler() || 
					StandardDrawingView_DrawingViewMouseListener_mouseReleasedHandler() ||
					StandardDrawingView_DrawingViewMouseMotionListener_mouseDraggedHanlder() || 
					StandardDrawingView_DrawingViewMouseMotionListener_mouseMovedHandler() {
		
		try{
			proceed();
		}catch(Throwable t){
			StandardDrawingView currentObject = (StandardDrawingView) thisJoinPoint.getThis();
			JOptionPane.showMessageDialog(
					currentObject,
		            t.getClass().getName() + " - " + t.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
				t.printStackTrace();
		}
	}
	
	void around() : StandardFigureSelection_internalGetDataHandler(){
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
