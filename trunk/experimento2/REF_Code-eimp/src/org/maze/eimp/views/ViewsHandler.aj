package org.maze.eimp.views;

import java.io.IOException;

import org.eclipse.ui.PartInitException;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

public privileged aspect ViewsHandler extends PrintStackTraceAbstractExceptionHandler{

	pointcut internalRunnableHandler(): execution(private void internalRunnable()) && within(BuddyListView);

	pointcut internalRunHandler(): execution(private void MultiSessionBoardView.internalRun(String, String));

	pointcut internalRunHandler2(): execution(private void TableMultiSessionBoardView.internalRun(String, String));
	
	public pointcut printStackTraceException(): internalRunnableHandler();
	
	declare soft: PartInitException: printStackTraceException();
	//declare soft: PartInitException: internalRunnableHandler();
	declare soft: IOException: internalRunHandler()||internalRunHandler2();

//	void around():internalRunnableHandler() {
//		try {
//			proceed();
//		} catch (PartInitException e) {
//			e.printStackTrace();
//		}
//	}

	void around(MultiSessionBoardView msbv):internalRunHandler()&& this(msbv) {
		try {
			proceed(msbv);
		} catch (IOException e) {
			msbv.showMessage("Failed to write file! ");
		}
	}

	void around(TableMultiSessionBoardView t):internalRunHandler2()&& this(t) {
		try {
			proceed(t);
		} catch (IOException e) {
			t.showMessage("Failed to write file! ");
		}
	}

}
