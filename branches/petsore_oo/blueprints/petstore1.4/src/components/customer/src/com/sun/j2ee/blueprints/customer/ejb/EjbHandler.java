package com.sun.j2ee.blueprints.customer.ejb;

import javax.ejb.CreateException;

public class EjbHandler {
	
	public void ejbPostCreateHandler(Exception e) throws CreateException {
        throw new CreateException("could not lookup ejb. Exception is " + e.getMessage());
	}

}
