package com.sun.j2ee.blueprints.supplier.processpo.ejb;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;


@ExceptionHandler


public class ProcesspoEjbHandler extends GeneralException{
	
	public void onMessageHandler(Exception e){
		e.printStackTrace();
	    throw new EJBException(e);
	}

}
