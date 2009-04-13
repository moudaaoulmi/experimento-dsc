package com.sun.j2ee.blueprints.contactinfo.ejb;

import javax.ejb.CreateException;


public class EjbHandler {
	
	public void throwCreateExceptionHandler()throws CreateException {
		throw new CreateException("ContactInfoEJB error: ServiceLocator exception looking up address");
		
	}

}
