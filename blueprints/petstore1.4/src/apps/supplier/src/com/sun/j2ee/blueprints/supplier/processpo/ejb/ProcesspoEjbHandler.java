package com.sun.j2ee.blueprints.supplier.processpo.ejb;

import javax.ejb.EJBException;

import exception.ExceptionHandler;
import exception.GeneralException;


@ExceptionHandler


public class ProcesspoEjbHandler extends GeneralException{
	
	public void onMessageHandler(Exception e){
		e.printStackTrace();
	    throw new EJBException(e);
	}

}
