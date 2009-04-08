package com.sun.j2ee.blueprints.catalog.client;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.catalog.ejb.CatalogLocal;
import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.catalog.model.Item;
import com.sun.j2ee.blueprints.catalog.model.Page;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

public class ClientHandler {

	public Page searchItemsFromDAOperformHandler(CatalogDAOSysException se) throws CatalogException {
		throw new CatalogException(se.getMessage());		
	}
	
	public Item getItemFromDAOHandler(CatalogDAOSysException se) throws CatalogException {
		throw new CatalogException(se.getMessage());		
	}
	
	public CatalogLocal getCatalogEJB1Handler(javax.ejb.CreateException cx) throws CatalogException  {
		throw new CatalogException("CatalogHelper: failed to create CatalogLocal EJB: caught " + cx);		
	}
	
	public CatalogLocal getCatalogEJB2Handler(ServiceLocatorException slx) throws CatalogException  {
		throw new CatalogException("CatalogHelper: failed to look up Catalog Home: caught " + slx);	
	}

	
}
