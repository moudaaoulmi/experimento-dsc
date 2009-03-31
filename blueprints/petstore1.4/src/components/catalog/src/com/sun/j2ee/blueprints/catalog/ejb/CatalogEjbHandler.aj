/*
 * Created on 25/09/2005
 */
package com.sun.j2ee.blueprints.catalog.ejb;

import com.sun.j2ee.blueprints.catalog.model.Item;
import com.sun.j2ee.blueprints.catalog.model.Page;
import com.sun.j2ee.blueprints.catalog.model.Product;

import java.util.Locale;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.catalog.model.Category;

/**
 * @author Raquel Maranhao
 */
public aspect CatalogEjbHandler {
	
	/*** CatalogEJB ***/
	pointcut getCategoryHandler() : 
		execution(public Category CatalogEJB.getCategory(String, Locale));
	pointcut getCategoriesHandler() : 
		execution(public Page CatalogEJB.getCategories(int, int, Locale));
	pointcut getProductsHandler() : 
		execution(public Page CatalogEJB.getProducts(String, int, int, Locale));
	pointcut getProductHandler() : 
		execution(public Product CatalogEJB.getProduct(String, Locale));
	pointcut getItemsHandler() : 
		execution(public Page CatalogEJB.getItems(String, int, int, Locale));
	pointcut getItemHandler() : 
		execution(public Item CatalogEJB.getItem(String, Locale));
	pointcut searchItemsHandler() : 
		execution(public Page CatalogEJB.searchItems(String, int, int, Locale));
	
	
	
//	declare soft : CatalogDAOSysException : getCategoryHandler() || 
//		getCategoriesHandler() ||
//		getProductsHandler() || 
//		getProductHandler() || 
//		getItemsHandler() ||
//		getItemHandler() ||
//		searchItemsHandler();
	
	
	
	after() throwing(CatalogDAOSysException se) throws EJBException : 
		getCategoryHandler() ||
		getCategoriesHandler() ||
		getProductsHandler() ||
		getProductHandler() || 
		getItemsHandler() ||
		getItemHandler() ||
		searchItemsHandler() {
        throw new EJBException(se.getMessage());
    }


}
