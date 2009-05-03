package com.sun.j2ee.blueprints.petstore.controller.web.actions;

import javax.ejb.FinderException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class ActionsHandler extends GeneralException{
	public Integer performHandler(Integer quantity){
		 return quantity = new Integer(0);
	}

}
