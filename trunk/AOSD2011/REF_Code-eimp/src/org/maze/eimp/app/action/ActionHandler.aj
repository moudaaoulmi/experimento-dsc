package org.maze.eimp.app.action;

import java.io.IOException;

import org.maze.eimp.util.MessageArchiver;

public privileged aspect ActionHandler {

	pointcut internalRunHandler(): execution(private void ExportToTxtAction.internalRun(String , MessageArchiver , String ));

	declare soft: IOException: internalRunHandler();

	void around(ExportToTxtAction exp): internalRunHandler()&& this(exp){
		try {
			proceed(exp);
		} catch (IOException e) {
			exp.w.showMessage(Messages
					.getString("ExportToTxtAction.E_FailedWriteFile")); //$NON-NLS-1$
		}
	}

}
