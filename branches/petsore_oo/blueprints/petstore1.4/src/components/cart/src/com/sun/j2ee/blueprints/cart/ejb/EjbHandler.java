package com.sun.j2ee.blueprints.cart.ejb;

import com.sun.j2ee.blueprints.catalog.client.CatalogException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler
public class EjbHandler extends GeneralException{

	public void getItemsHandler(CatalogException cce) {
		System.out.println("ShoppingCartEJB caught: " + cce);
	}

}
