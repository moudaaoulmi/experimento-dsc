package com.sun.j2ee.blueprints.signon.ejb;

import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class EjbHandler extends GeneralException{
	
	public void ejbCreateHandler(NamingException ne) {
		throw new EJBException("SignOnEJB Got naming exception! " + ne.getMessage());
	}
	
	public boolean authenticateHandler(){
		return false;
	}

}
