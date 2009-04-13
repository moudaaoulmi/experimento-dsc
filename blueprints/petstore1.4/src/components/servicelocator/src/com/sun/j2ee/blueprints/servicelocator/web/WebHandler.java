package com.sun.j2ee.blueprints.servicelocator.web;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

public class WebHandler {

	/**
	 * O nome do método é genérico pois há reuso de código!
	 */
	public void serviceLocatorExceptionHandler(Exception e) throws ServiceLocatorException{
		 throw new ServiceLocatorException(e);
	}
	
	public void staticHandler(ServiceLocatorException se){
		 System.err.println(se);
	      se.printStackTrace(System.err);
	}
}
