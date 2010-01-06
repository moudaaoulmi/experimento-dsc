package org.maze.eimp.msn;

import java.io.IOException;
import org.maze.eimp.im.Buddy;

public privileged aspect MsnHanlder {

	pointcut addBuddyHandler2(): execution(public void MSNSession.addBuddy(Buddy));

	pointcut internalSendMessageHandler(): execution(private void MSNSession.internalSendMessage(org.maze.eimp.im.MimeMessage));

	declare soft: IOException: addBuddyHandler2()|| internalSendMessageHandler();

	void around():  addBuddyHandler2()||internalSendMessageHandler(){
		try {
			proceed();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
