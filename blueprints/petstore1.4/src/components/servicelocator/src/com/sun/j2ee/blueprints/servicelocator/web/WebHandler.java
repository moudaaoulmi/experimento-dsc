package com.sun.j2ee.blueprints.servicelocator.web;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class WebHandler extends GeneralException{

	/**
	 * O nome do método é genérico pois há reuso de código!
	 */
	public void throwServiceLocatorExceptionHandler(Exception e) throws ServiceLocatorException{
		 throw new ServiceLocatorException(e);
	}
	
	public void staticHandler(ServiceLocatorException se){
		 System.err.println(se);
	      se.printStackTrace(System.err);
	}
}
