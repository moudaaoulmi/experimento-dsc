package com.sun.j2ee.blueprints.servicelocator.ejb;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

public class EjbHandler {
	
	/**
	 * O nome do m�todo � gen�rico pois h� reuso de c�digo!
	 */
	public void throwServiceLocatorExceptionHandler(Exception e) throws ServiceLocatorException{
		 throw new ServiceLocatorException(e);
	}

}
