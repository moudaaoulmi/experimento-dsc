package com.sun.j2ee.blueprints.catalog.ejb;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.catalog.model.Category;
import com.sun.j2ee.blueprints.catalog.model.Item;
import com.sun.j2ee.blueprints.catalog.model.Page;
import com.sun.j2ee.blueprints.catalog.model.Product;
import com.sun.j2ee.blueprints.util.tracer.Debug;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler


public class EjbHandler extends GeneralException{
	
	public void createHandler(CatalogDAOSysException se){
		Debug.println("Exception getting dao " + se);
        throw new EJBException(se.getMessage());
	}
//	public Category getCategoryHandler(CatalogDAOSysException se){
//		throw new EJBException(se.getMessage());
//	}
//	
//	public Page getCategoriesHandler(CatalogDAOSysException se){
//		throw new EJBException(se.getMessage());
//	}
//	
//	public Product getProductHandler(CatalogDAOSysException se){
//		throw new EJBException(se.getMessage());
//	}
//	
//	 public Item getItemHandler(CatalogDAOSysException se){
//		throw new EJBException(se.getMessage());
//	}
}	
