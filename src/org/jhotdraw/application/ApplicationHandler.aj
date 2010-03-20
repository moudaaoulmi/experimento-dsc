package org.jhotdraw.application;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.jhotdraw.exception.ExceptionHandler;
import br.upe.dsc.reusable.exception.*;

@ExceptionHandler
public aspect ApplicationHandler extends EmptyBlockAbstractExceptionHandling {
	
	public pointcut emptyBlockException(): DrawApplication_executeCommandMenu();
	
	declare soft: InterruptedException :DrawApplication_internalOpen();
	declare soft: InvocationTargetException :DrawApplication_internalOpen() ;
	declare soft: IOException :DrawApplication_saveDrawing() || DrawApplication_loadDrawing();
	declare soft: Exception :DrawApplication_newLookAndFeel() || DrawApplication_executeCommandMenu();
	
	
	pointcut DrawApplication_internalOpen(): execution (* DrawApplication.internalOpen(..)) ;
	pointcut DrawApplication_saveDrawing(): execution (* DrawApplication.saveDrawing(..) );
	pointcut DrawApplication_loadDrawing(): execution (* DrawApplication.loadDrawing(..) );
	pointcut DrawApplication_newLookAndFeel(): execution (* DrawApplication.newLookAndFeel(..) );
	pointcut DrawApplication_executeCommandMenu(): execution (* DrawApplication.executeCommandMenu(..) );
	
	void around(Runnable r) : DrawApplication_internalOpen() && args(r){
		DrawApplication drawApplication = null;
		try {
			proceed(r);
		}
		catch(java.lang.InterruptedException ie) {
			drawApplication = (DrawApplication) thisJoinPoint.getThis();
			System.err.println(ie.getMessage());
			ie.printStackTrace();
			drawApplication.exit();
		}
		catch(java.lang.reflect.InvocationTargetException ite) {
			drawApplication = (DrawApplication) thisJoinPoint.getThis();
			System.err.println(ite.getMessage());
			ite.printStackTrace();
			drawApplication.exit();
		}
    }
	
	void around(): DrawApplication_saveDrawing() {
		DrawApplication drawApplication = null;
		try {
			proceed();
		}
		catch(IOException e) {
			drawApplication = (DrawApplication) thisJoinPoint.getThis();
			drawApplication.showStatus(e.toString());
		}
		
    }
	
	void around(): DrawApplication_loadDrawing() {
		DrawApplication drawApplication = null;
		try {
			proceed();
		}
		catch (IOException e) {
			drawApplication = (DrawApplication) thisJoinPoint.getThis();
			drawApplication.showStatus("Error: " + e);
		}
		//@AJHD added
		//catch the soft exception instead of the IO one, and get the wrapped one for report
		catch (org.aspectj.lang.SoftException e) {
			drawApplication = (DrawApplication) thisJoinPoint.getThis();
			drawApplication.showStatus("Error: " + e.getWrappedThrowable());
		}
		
    }
	
	void around(): DrawApplication_newLookAndFeel() {
		try {
			proceed();
		}
		catch (Exception e) {
			System.err.println(e);
		}
		
    }
	
//	void around(): DrawApplication_executeCommandMenu() {
//		try {
//			proceed();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//    }

}
