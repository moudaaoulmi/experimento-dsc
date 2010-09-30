package com.sun.j2ee.blueprints.waf.controller.impl;

import java.io.IOException;
import java.io.OptionalDataException;

import javax.servlet.http.HttpServletRequest;

import com.sun.j2ee.blueprints.util.tracer.Debug;

//@ExceptionHandler
public privileged aspect ImplHandler {

	pointcut internalProcessFlowHandler(): execution(private void ClientStateFlowHandler.internalProcessFlow(HttpServletRequest , String ,	String , byte[]));

	declare soft: OptionalDataException :internalProcessFlowHandler();
	declare soft: ClassNotFoundException :internalProcessFlowHandler();
	declare soft: IOException :internalProcessFlowHandler();

	void around(): internalProcessFlowHandler() {
		try {
			proceed();
		} catch (java.io.OptionalDataException ode) {
			Debug.print("ClientCacheLinkFlowHandler caught: " + ode);
		} catch (java.lang.ClassNotFoundException cnfe) {
			Debug.print("ClientCacheLinkFlowHandler caught: " + cnfe);
		} catch (java.io.IOException iox) {
			Debug.print("ClientCacheLinkFlowHandler caught: " + iox);
		}
	}

}
