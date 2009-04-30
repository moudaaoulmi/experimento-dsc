package com.sun.j2ee.blueprints.servicelocator.ejb;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler
public class EjbHandler extends GeneralException {

	/**
	 * O nome do m�todo � gen�rico pois h� reuso de c�digo!
	 */
	public void throwServiceLocatorExceptionHandler(Exception e)
			throws ServiceLocatorException {
		throw new ServiceLocatorException(e);
	}

}
