package com.sun.j2ee.blueprints.waf.controller.ejb;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.controller.Command;

@ExceptionHandler
public privileged aspect EjbHandler {
	
	pointcut internalProcessEventHandler(): execution(private EJBCommand StateMachine.internalProcessEvent(EJBCommand ,	Command));
	
	
	EJBCommand around(EJBCommand ejbCommand): internalProcessEventHandler() && args(ejbCommand, *) {
		try {
			return proceed(ejbCommand);
		} catch (ClassCastException cx) {
			Debug.print("StateMachine: Command not EJBCommand");
		}
		return ejbCommand;
	}

}
