package com.sun.j2ee.blueprints.petstore.controller.web.actions;

import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class ActionsHandler extends GeneralException{
	public Integer performHandler(Integer quantity){
		 return quantity = new Integer(0);
	}

}
