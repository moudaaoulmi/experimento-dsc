package com.sun.j2ee.blueprints.petstore.controller.ejb;

import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler
public class EjbHandler extends GeneralException {

	public void throwGeneralFailureExceptionHandler(String msg, Exception e) {
		throw new GeneralFailureException(msg + e);
	}

}
