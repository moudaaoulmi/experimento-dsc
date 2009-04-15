package com.sun.j2ee.blueprints.supplier.processpo.ejb;

import javax.ejb.EJBException;

public class ProcesspoEjbHandler {
	
	/**
	 * O nome do método é genérico pois há reuso de código!
	 */
	public void throwEjbExceptionHandler(Exception e) {
		 throw new EJBException(e);	
	}
	
	public void onMessageHandler(Exception e){
		e.printStackTrace();
	    throw new EJBException(e);
	}

}
