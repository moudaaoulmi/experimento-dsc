package com.sun.j2ee.blueprints.signon.ejb;

import javax.ejb.EJBException;
import javax.naming.NamingException;

public class EjbHandler {
	
	public void ejbCreateHandler(NamingException ne) {
		throw new EJBException("SignOnEJB Got naming exception! " + ne.getMessage());
	}
	
	public boolean authenticateHandler(){
		return false;
	}

}
