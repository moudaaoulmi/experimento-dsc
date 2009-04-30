package com.sun.j2ee.blueprints.catalog.dao;

import java.sql.*;
import javax.sql.*;


import javax.naming.NamingException;

import org.xml.sax.SAXException;

import java.lang.*;

//import com.sun.j2ee.blueprints.catalog.dao.GenericCatalogDAO.ParsingDoneException;
import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.catalog.model.Category;
import com.sun.j2ee.blueprints.catalog.model.Item;
import com.sun.j2ee.blueprints.catalog.model.Page;
import com.sun.j2ee.blueprints.catalog.model.Product;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;


@ExceptionHandler


public class DaoHandler extends GeneralException{
	
	//catch (NamingException ne)
	public void getDAOHandler(Exception ne) throws CatalogDAOSysException{
        throw new CatalogDAOSysException("CatalogDAOFactory.getDAO:"+ ne.getClass().getName() + "while getting DAO type : \n" + ne.getMessage());
    } 
	
	public static void ignore(Exception e){}
	
	/**
	 * Reusado
	 * 
	//catch (Exception se) 
	public void getDAO2Handler(Exception se) throws CatalogDAOSysException{
        throw new CatalogDAOSysException("CatalogDAOFactory.getDAO:  Exception while getting DAO type : \n" + se.getMessage());
    }*/
	
	//catch (Exception exception) 
	public void genericCatalogDAOHandler(Exception exception) throws CatalogDAOSysException{
		  exception.printStackTrace(System.err);
		  System.err.println(exception);
	      throw new CatalogDAOSysException(exception.getMessage());
	}

	//catch (NamingException exception) 
	public static DataSource getDataSourceHandler(NamingException exception) throws CatalogDAOSysException {
	      throw new CatalogDAOSysException("NamingException while looking up DB context : " +
	                                       exception.getMessage());
	}
	
	
	public void getCategoryFINALLYHandler(Connection connection, PreparedStatement statement, ResultSet resultSet) {
		GenericCatalogDAO.closeAll(connection, statement, resultSet);
	}
	
	public void getHandler(SQLException exception) throws CatalogDAOSysException {
	      throw new CatalogDAOSysException("SQLException: " + exception.getMessage());
	} 
	

	//catch (Exception exception)
	public static void mainHandler(Exception exception) { 
        exception.printStackTrace(System.err);
        System.err.println(exception);
        System.exit(2);
	}
	
	//catch (ServiceLocatorException slx) 
	public static DataSource getDataSourceHandler(ServiceLocatorException slx) throws CatalogDAOSysException {
        throw new CatalogDAOSysException("NamingException while looking up DB context : " +
                               slx.getMessage());
    }
	
	//catch (SQLException se) 
	public void getCategories2Handler(SQLException se) throws CatalogDAOSysException {
        se.printStackTrace(System.err);
        throw new CatalogDAOSysException("SQLException: " + se.getMessage());
      }
	
}//DaoHandler{}
