package com.sun.j2ee.blueprints.processmanager.ejb;

import javax.ejb.EJBException;
import javax.naming.NamingException;


public class EjbHandler {
	public void ejbCreateHandler(NamingException ne) {
		throw new EJBException("Got naming exception! " + ne.getMessage());
		
	}

}
