package org.maze.eimp.xmpp;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.maze.eimp.im.Buddy;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

public privileged aspect XmppHandler extends PrintStackTraceAbstractExceptionHandler{

	pointcut runHandler():execution(public void Thread.run()) && within(JabberConnection);

	pointcut internalAddBuddyHandler(): execution(private void JabberConnection.internalAddBuddy(Buddy, Roster));
	
	public pointcut printStackTraceException(): internalAddBuddyHandler();

	declare soft: XMPPException: runHandler()|| printStackTraceException();
//	declare soft: XMPPException: runHandler()|| internalAddBuddyHandler();

	void around(): runHandler(){
		try {
			proceed();
		} catch (XMPPException e) {
			System.out.println("xmpp login exception:");
			e.printStackTrace();
		}
	}

//	void around(): internalAddBuddyHandler() {
//		try {
//			proceed();
//		} catch (XMPPException e) {
//			e.printStackTrace();
//		}
//	}

}
