package com.sun.j2ee.blueprints.customer.ejb;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class EjbHandler extends GeneralException{
	
	public void ejbPostCreateHandler(Exception e) throws CreateException {
        throw new CreateException("could not lookup ejb. Exception is " + e.getMessage());
	}

}
