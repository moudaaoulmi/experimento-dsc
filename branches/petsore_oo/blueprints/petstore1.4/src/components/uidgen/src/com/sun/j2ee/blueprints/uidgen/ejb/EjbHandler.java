package com.sun.j2ee.blueprints.uidgen.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

public class EjbHandler {

	public void ejbCreateHandler(NamingException ne) {
		throw new EJBException("UniqueIdGeneratorEJB Got naming exception! " + ne.getMessage());
	}
	
	public void getCounterHandler(CreateException ce, String name) {
		throw new EJBException("Could not create counter " + name + ". Error: " + ce.getMessage());
	}
}
