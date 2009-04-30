package com.sun.j2ee.blueprints.petstore.controller.ejb;

import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler
public class EjbHandler extends GeneralException {

	public void throwGeneralFailureExceptionHandler(String msg, Exception e) {
		throw new GeneralFailureException(msg + e);
	}

}
