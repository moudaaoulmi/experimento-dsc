package com.sun.j2ee.blueprints.contactinfo.ejb;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;


public class EjbHandler {
	
	public void postCreateHandler()throws CreateException {
		throw new CreateException("ContactInfoEJB error: ServiceLocator exception looking up address");
		
	}

}
