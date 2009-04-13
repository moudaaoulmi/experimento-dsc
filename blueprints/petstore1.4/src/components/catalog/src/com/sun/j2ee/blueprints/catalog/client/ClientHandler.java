package com.sun.j2ee.blueprints.catalog.client;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.catalog.ejb.CatalogLocal;
import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.catalog.model.Item;
import com.sun.j2ee.blueprints.catalog.model.Page;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

public class ClientHandler {

	public Page throwCatalogException1Handler(CatalogDAOSysException se) throws CatalogException {
		throw new CatalogException(se.getMessage());		
	}
	
	public Item throwCatalogException2Handler(CatalogDAOSysException se) throws CatalogException {
		throw new CatalogException(se.getMessage());		
	}
	
	public CatalogLocal getCatalogEJBHandler(String msg,Exception cx) throws CatalogException  {
		throw new CatalogException(msg + cx);		
	}
	
	
}
