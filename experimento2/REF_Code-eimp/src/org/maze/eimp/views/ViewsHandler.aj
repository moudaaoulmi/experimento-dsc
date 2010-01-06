package org.maze.eimp.views;

import java.io.IOException;

import org.eclipse.ui.PartInitException;

public privileged aspect ViewsHandler {

	pointcut internalRunnableHandler(): execution(private void internalRunnable()) && within(BuddyListView);

	pointcut internalRunHandler(): execution(private void MultiSessionBoardView.internalRun(String, String));

	pointcut internalRunHandler2(): execution(private void TableMultiSessionBoardView.internalRun(String, String));

	declare soft: PartInitException: internalRunnableHandler();
	declare soft: IOException: internalRunHandler()||internalRunHandler2();

	void around():internalRunnableHandler() {
		try {
			proceed();
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

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
