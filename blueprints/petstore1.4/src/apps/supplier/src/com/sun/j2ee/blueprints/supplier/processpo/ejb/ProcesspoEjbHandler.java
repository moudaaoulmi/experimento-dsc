package com.sun.j2ee.blueprints.supplier.processpo.ejb;

import javax.ejb.EJBException;

public class ProcesspoEjbHandler {
	
	/**
	 * O nome do m�todo � gen�rico pois h� reuso de c�digo!
	 */
	public void throwEjbExceptionHandler(Exception e) {
		 throw new EJBException(e);	
	}
	
	public void onMessageHandler(Exception e){
		e.printStackTrace();
	    throw new EJBException(e);
	}

}
