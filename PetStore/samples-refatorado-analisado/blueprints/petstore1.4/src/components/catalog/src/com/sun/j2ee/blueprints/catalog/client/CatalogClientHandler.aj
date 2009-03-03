package com.sun.j2ee.blueprints.catalog.client;

import java.util.Locale;

import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.catalog.model.Page;
import com.sun.j2ee.blueprints.catalog.model.Item;
import com.sun.j2ee.blueprints.catalog.ejb.CatalogLocal;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.aspect.AspectUtil;

public aspect CatalogClientHandler {

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
	declare soft : javax.ejb.CreateException : getCatalogEJBHandler();
	declare soft : ServiceLocatorException : getCatalogEJBHandler();
	Object around() throws CatalogException: 
		searchItemsFromDAOHandler() || 
		getCategoriesFromDAOHandler() || 
		getProductsFromDAOHandler() ||
		getItemsFromDAOHandler() || 
		getItemFromDAOHandler() ||
		getCatalogEJBHandler(){
		try {
			return proceed();
		} catch (Exception e) {
			if (AspectUtil.verifyJointPoint(thisJoinPoint,
					"private CatalogLocal CatalogHelper.getCatalogEJB()")) {
				if (e instanceof javax.ejb.CreateException) {
					throw new CatalogException(
							"CatalogHelper: failed to create CatalogLocal EJB: caught "
									+ e);
				}
				throw new CatalogException(
						"CatalogHelper: failed to look up Catalog Home: caught "
								+ e);
			}
			throw new CatalogException(e.getMessage());
		}
	}
}