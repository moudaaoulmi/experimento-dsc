package com.sun.j2ee.blueprints.waf.controller;

import com.sun.j2ee.blueprints.util.tracer.Debug;

@ExceptionHandler
public privileged aspect ControllerHandler {

	pointcut internalGetCommandHandler(): execution(private static Command CommandFactory.internalGetCommand(String,Command));

	declare soft: Exception : internalGetCommandHandler();

	Command around(String commandName, Command command): internalGetCommandHandler() && args(commandName, command) {
		try {
			return proceed(commandName, command);
		} catch (Exception ex) {
			Debug.print("CommandFactory: error loading command " + commandName
					+ " :" + ex);
		}
		return command;
	}

}
