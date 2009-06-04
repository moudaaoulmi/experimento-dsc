package com.sun.j2ee.blueprints.catalog.client;

import com.sun.j2ee.blueprints.catalog.ejb.CatalogLocal;
import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler
public class ClientHandler extends GeneralException {

	public void throwCatalogExceptionHandler(CatalogDAOSysException se)
			throws CatalogException {
		throw new CatalogException(se.getMessage());
	}

	public CatalogLocal getCatalogEJBHandler(String msg, Exception cx)
			throws CatalogException {
		throw new CatalogException(msg + cx);
	}

}
