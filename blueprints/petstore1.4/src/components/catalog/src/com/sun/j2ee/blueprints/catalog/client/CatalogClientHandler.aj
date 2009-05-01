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
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect CatalogClientHandler {

	// CatalogDAOSysException is already a RuntimeException
	// declare soft : CatalogDAOSysException : searchItemsFromDAOHandler() ||
	// getCategoriesFromDAOHandler() ||
	// getProductsFromDAOHandler() ||
	// getItemsFromDAOHandler() ||
	// getItemFromDAOHandler();
	declare soft : javax.ejb.CreateException : getCatalogEJBHandler();
	declare soft : ServiceLocatorException : getCatalogEJBHandler();

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

	Object around()throws CatalogException :
					searchItemsFromDAOHandler() || 
					getCategoriesFromDAOHandler() || 
					getProductsFromDAOHandler() ||
					getItemsFromDAOHandler() || 
					getItemFromDAOHandler() {
		try{
			return proceed();
		}catch(CatalogDAOSysException se){
			throw new CatalogException(se.getMessage());
		}
	}

	CatalogLocal around() throws CatalogException : getCatalogEJBHandler(){
		try {
			return proceed();
		} catch (javax.ejb.CreateException cx) {
			throw new CatalogException(
					"CatalogHelper: failed to create CatalogLocal EJB: caught "
							+ cx);
		} catch (ServiceLocatorException slx) {
			throw new CatalogException(
					"CatalogHelper: failed to look up Catalog Home: caught "
							+ slx);
		}
	}

}
