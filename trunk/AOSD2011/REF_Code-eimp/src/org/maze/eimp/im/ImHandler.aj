package org.maze.eimp.im;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

public privileged aspect ImHandler extends PrintStackTraceAbstractExceptionHandler{

	pointcut internalGetConnectionHandler(): execution( private void Account.internalGetConnection());

	public pointcut printStackTraceException(): internalGetConnectionHandler();
	
	declare soft: InstantiationException : printStackTraceException();
	declare soft: IllegalAccessException: printStackTraceException();
	declare soft: ClassNotFoundException: printStackTraceException();
//	declare soft: InstantiationException : internalGetConnectionHandler();
//	declare soft: IllegalAccessException: internalGetConnectionHandler();
//	declare soft: ClassNotFoundException:internalGetConnectionHandler();
	
//	void around(): internalGetConnectionHandler(){
//		try{
//			proceed();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
}
