package org.maze.eimp.aim;

import java.io.IOException;

import org.maze.eimp.exception.ExceptionHandler;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.MimeMessage;

import com.wilko.jaim.JaimException;

@ExceptionHandler
public privileged aspect AimHandler {

	pointcut internalCreateAIMConnectionHandler(): execution(private void AIMConnection.internalCreateAIMConnection());

	pointcut internalCreateAIMConnectionHandler2(): execution(private void AIMConnection.internalCreateAIMConnection2(Buddy));

	pointcut internalLoginHandler(): execution(private void AIMConnection.internalLogin());

	pointcut internalLogoutHandler(): execution(private void AIMConnection.internalLogout());

	pointcut internalUpdateServerSideBuddiesHandler(): execution(private void AIMConnection.internalUpdateServerSideBuddies(Buddy));

	pointcut internalAddBuddyHandler(): execution(private void AIMConnection.internalAddBuddy());

	pointcut internalSetStatusHandler(): execution(private void AIMConnection.internalSetStatus(String));

	pointcut internalSendInstantMessageHandler(): execution(private void AIMConnection.internalSendInstantMessage(MimeMessage, Buddy));

	declare soft: JaimException: internalCreateAIMConnectionHandler() || internalCreateAIMConnectionHandler2()|| internalLoginHandler() || internalUpdateServerSideBuddiesHandler();

	declare soft: IOException: internalLoginHandler() || internalLogoutHandler()||internalAddBuddyHandler()||internalSetStatusHandler()|| internalSendInstantMessageHandler();

	void around(AIMConnection aim): (internalCreateAIMConnectionHandler()||internalCreateAIMConnectionHandler2()||internalLoginHandler()) && this(aim){
		try {
			proceed(aim);
		} catch (JaimException e) {
			aim.log(e);
		}
	}

	void around(AIMConnection aim): (internalLogoutHandler()|| internalAddBuddyHandler()||internalSendInstantMessageHandler()||internalLoginHandler()||internalSetStatusHandler())&& this(aim){
		try {
			proceed(aim);
		} catch (IOException e) {
			aim.log(e);
		}
	}

	void around(AIMConnection aim): internalUpdateServerSideBuddiesHandler()&& this(aim){
		try {
			proceed(aim);
		} catch (JaimException e) {
			aim.log(e.toString());
		}
	}

}
