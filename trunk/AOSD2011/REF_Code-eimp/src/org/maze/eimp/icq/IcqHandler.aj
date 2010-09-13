package org.maze.eimp.icq;

import net.kano.joustsim.oscar.StateEvent;

public privileged aspect IcqHandler {

	pointcut handleStateChangeHandler(): execution(public void handleStateChange(StateEvent));

	declare soft: Exception: handleStateChangeHandler();

	void around(): handleStateChangeHandler()&& within(ICQConnection){
		try {
			proceed();
		} catch (Exception e) {
			System.err.println("Error " + e);
		}
	}

}
