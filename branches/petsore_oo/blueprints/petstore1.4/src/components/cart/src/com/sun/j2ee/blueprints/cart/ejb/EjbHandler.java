package com.sun.j2ee.blueprints.cart.ejb;

import java.util.Collection;

import com.sun.j2ee.blueprints.catalog.client.CatalogException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class EjbHandler extends GeneralException{

	public void getItemsHandler(CatalogException cce) {
		System.out.println("ShoppingCartEJB caught: " + cce);
	}

}
