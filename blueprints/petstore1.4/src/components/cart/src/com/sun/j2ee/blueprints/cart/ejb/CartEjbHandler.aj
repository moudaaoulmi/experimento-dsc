/*
 * Created on 25/09/2005
 */
package com.sun.j2ee.blueprints.cart.ejb;

import java.util.ArrayList;

import com.sun.j2ee.blueprints.catalog.client.CatalogException;
import com.sun.j2ee.blueprints.catalog.client.CatalogHelper;
import com.sun.j2ee.blueprints.catalog.model.Item;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect CartEjbHandler {

	/*** ShoppingCartLocalEJB ***/
	pointcut internalGetItemsAddItemHandler() : 
		execution(private Item ShoppingCartLocalEJB.internalGetItemsAddItem(Item, CatalogHelper, String, Integer, ArrayList));

	declare soft : CatalogException : internalGetItemsAddItemHandler();
	
	Item around(Item item, CatalogHelper catalog, String key, Integer value, ArrayList items) : 
		internalGetItemsAddItemHandler() && 
		args(item, catalog, key, value, items) {
		try {
			return proceed(item, catalog, key, value, items);
		} catch(CatalogException cce) {
			System.out.println("ShoppingCartEJB caught: " + cce);
			return item;
		}
	}
}
