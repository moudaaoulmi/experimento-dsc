package com.sun.j2ee.blueprints.cart.ejb;

import java.util.Collection;

import com.sun.j2ee.blueprints.catalog.client.CatalogException;

public class EjbHandler {

	// private EjbHandler ejbHandler = new EjbHandler();

	public void getItemsHandler(CatalogException cce) {
		System.out.println("ShoppingCartEJB caught: " + cce);
	}

}
