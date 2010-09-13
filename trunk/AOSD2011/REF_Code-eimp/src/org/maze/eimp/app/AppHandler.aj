package org.maze.eimp.app;

import java.io.FileNotFoundException;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;


public privileged aspect AppHandler extends PrintStackTraceAbstractExceptionHandler{	

	pointcut loadfileHander(): execution(private void AboutDialog.loadfile());	
	
	public pointcut printStackTraceException(): loadfileHander();
	declare soft: FileNotFoundException: printStackTraceException();

//  declare soft: FileNotFoundException: loadfileHander();

//	void around(): loadfileHander(){
//		try {
//			proceed();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

	
}
