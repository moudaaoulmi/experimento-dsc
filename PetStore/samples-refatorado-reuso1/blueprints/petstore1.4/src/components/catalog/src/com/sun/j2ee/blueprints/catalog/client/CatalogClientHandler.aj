/*
 * Created on 25/09/2005
 */
package com.sun.j2ee.blueprints.catalog.client;

import java.util.Locale;

import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.catalog.model.Page;
import com.sun.j2ee.blueprints.catalog.model.Item;
import com.sun.j2ee.blueprints.catalog.ejb.CatalogLocal;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;


/**
 * @author Raquel Maranhao
 */
public aspect CatalogClientHandler {
	
	/*** CatalogHelper ***/
	pointcut searchItemsFromDAOHandler() : 
		execution(private Page CatalogHelper.searchItemsFromDAO(String, int, int, Locale));
	pointcut getCategoriesFromDAOHandler() : 
		execution(private Page CatalogHelper.getCategoriesFromDAO(int, int, Locale));
	pointcut getProductsFromDAOHandler() : 
		execution(private Page CatalogHelper.getProductsFromDAO(String, int, int, Locale));
	pointcut getItemsFromDAOHandler() : 
		execution(private Page CatalogHelper.getItemsFromDAO(String, int, int, Locale));
	pointcut getItemFromDAOHandler() : 
		execution(private Item CatalogHelper.getItemFromDAO(String, Locale));
	pointcut getCatalogEJBHandler() : 
		execution(private CatalogLocal CatalogHelper.getCatalogEJB());
	
	
	
	//CatalogDAOSysException is already a RuntimeException
//	declare soft : CatalogDAOSysException : searchItemsFromDAOHandler() ||
//		getCategoriesFromDAOHandler() || 
//		getProductsFromDAOHandler() || 
//		getItemsFromDAOHandler() || 
//		getItemFromDAOHandler();
	declare soft : javax.ejb.CreateException : getCatalogEJBHandler();
	declare soft : ServiceLocatorException : getCatalogEJBHandler();
	
	
	
	after() throwing(CatalogDAOSysException se) throws CatalogException :
		searchItemsFromDAOHandler() || 
		getCategoriesFromDAOHandler() || 
		getProductsFromDAOHandler() ||
		getItemsFromDAOHandler() || 
		getItemFromDAOHandler() {
        throw new CatalogException(se.getMessage());
    }

	after() throwing(javax.ejb.CreateException cx) throws CatalogException : 
		getCatalogEJBHandler() {
		throw new CatalogException("CatalogHelper: failed to create CatalogLocal EJB: caught " + cx);
	}
	
	after() throwing(ServiceLocatorException slx) throws CatalogException : 
		getCatalogEJBHandler() {
		throw new CatalogException("CatalogHelper: failed to look up Catalog Home: caught " + slx);
	}
	
}
