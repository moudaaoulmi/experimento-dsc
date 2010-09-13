package org.maze.eimp.yahoo;

import java.io.IOException;

import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.MimeMessage;

import ymsg.network.AccountLockedException;
import ymsg.network.LoginRefusedException;

public privileged aspect YahooHandler {

	pointcut internalRunnableHandler(): execution(private boolean YahooConnection.internalRunnable(boolean));

	pointcut logoutHandler(): execution(public void YahooConnection.logout());

	pointcut addBuddyHandler(): execution(public void YahooConnection.addBuddy(Buddy));

	pointcut removeBuddyHandler(): execution(public void YahooConnection.removeBuddy(Buddy));

	pointcut setStatusHandler(): execution(public void YahooConnection.setStatus(String));

	pointcut sendInstantMessageHandler(): execution(public void YahooConnection.sendInstantMessage(MimeMessage, Buddy));

	declare soft: AccountLockedException:internalRunnableHandler();
	declare soft: LoginRefusedException:internalRunnableHandler();
	declare soft: IOException:internalRunnableHandler()|| logoutHandler()||addBuddyHandler() || removeBuddyHandler()||setStatusHandler()|| sendInstantMessageHandler();

	boolean around(YahooConnection yahoo, boolean succ): internalRunnableHandler()&& args(succ)&& this(yahoo){
		try {
			return proceed(yahoo, succ);
		} catch (IllegalStateException e) {
			yahoo.fireLoginError(e.getMessage());
			succ = false;
			yahoo.log(e.getMessage());
		} catch (AccountLockedException e) {
			yahoo.fireLoginError(e.getMessage());
			succ = false;
			yahoo.log(e.getMessage());
		} catch (LoginRefusedException e) {
			yahoo.fireLoginError(e.getMessage());
			succ = false;
			yahoo.log(e.getMessage());
		} catch (IOException e) {
			yahoo.fireLoginError(e.getMessage());
			yahoo.log(e.getMessage());
		}
		return succ;
	}

	void around(YahooConnection yahoo): setStatusHandler()&& this(yahoo){
		try {
			proceed(yahoo);
		} catch (IllegalArgumentException e) {
			yahoo.log(e.getMessage());
		} catch (IOException e) {
			yahoo.log(e.getMessage());
		}
	}

	void around(YahooConnection yahoo):(logoutHandler()||addBuddyHandler()||removeBuddyHandler()||sendInstantMessageHandler()) && this(yahoo){
		try {
			proceed(yahoo);
		} catch (IllegalStateException e) {
			yahoo.log(e.toString());
		} catch (IOException e) {
			yahoo.log(e.toString());
		}
	}

}
