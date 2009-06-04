package com.sun.j2ee.blueprints.customer.ejb;

import javax.ejb.CreateException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class EjbHandler extends GeneralException{
	
	public void ejbPostCreateHandler(Exception e) throws CreateException {
        throw new CreateException("could not lookup ejb. Exception is " + e.getMessage());
	}

}
