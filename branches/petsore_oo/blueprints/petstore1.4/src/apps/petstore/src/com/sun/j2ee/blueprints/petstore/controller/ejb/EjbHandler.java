package com.sun.j2ee.blueprints.petstore.controller.ejb;

import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

public class EjbHandler {
	
	public void throwGeneralFailureExceptionHandler(String msg,Exception e) {
		throw new GeneralFailureException(msg + e);
	}
		
}
