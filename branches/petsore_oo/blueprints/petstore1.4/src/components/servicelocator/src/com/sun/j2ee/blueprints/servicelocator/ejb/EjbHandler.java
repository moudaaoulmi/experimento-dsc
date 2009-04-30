package com.sun.j2ee.blueprints.servicelocator.ejb;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler
public class EjbHandler extends GeneralException {

	/**
	 * O nome do método é genérico pois há reuso de código!
	 */
	public void throwServiceLocatorExceptionHandler(Exception e)
			throws ServiceLocatorException {
		throw new ServiceLocatorException(e);
	}

}
