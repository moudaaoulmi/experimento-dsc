package com.sun.j2ee.blueprints.servicelocator.ejb;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

public class EjbHandler {
	
	/**
	 * O nome do método é genérico pois há reuso de código!
	 */
	public void throwServiceLocatorExceptionHandler(Exception e) throws ServiceLocatorException{
		 throw new ServiceLocatorException(e);
	}

}
