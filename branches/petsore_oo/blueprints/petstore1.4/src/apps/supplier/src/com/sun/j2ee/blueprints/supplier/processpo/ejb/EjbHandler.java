package com.sun.j2ee.blueprints.supplier.processpo.ejb;

import javax.ejb.EJBException;

public class EjbHandler {
	
	/**
	 * O nome do m�todo � gen�rico pois h� reuso de c�digo!
	 */
	public void ejbExceptionHandler(Exception e) {
		 throw new EJBException(e);	
	}
	
	public void onMessageHandler(Exception e){
		e.printStackTrace();
	    throw new EJBException(e);
	}

}
