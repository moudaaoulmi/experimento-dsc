package org.maze.eimp.msn;

import java.io.IOException;
import org.maze.eimp.im.Buddy;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

public privileged aspect MsnHanlder extends PrintStackTraceAbstractExceptionHandler{

	pointcut addBuddyHandler2(): execution(public void MSNSession.addBuddy(Buddy));

	pointcut internalSendMessageHandler(): execution(private void MSNSession.internalSendMessage(org.maze.eimp.im.MimeMessage));
	
	public pointcut printStackTraceException(): addBuddyHandler2() || internalSendMessageHandler();

	declare soft: IOException: printStackTraceException();

//	declare soft: IOException: addBuddyHandler2()|| internalSendMessageHandler();

//	void around():  addBuddyHandler2()||internalSendMessageHandler(){
//		try {
//			proceed();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
