/*
 * Created on 25/09/2005
 */
package com.sun.j2ee.blueprints.catalog.dao;

import javax.naming.NamingException;

import com.sun.j2ee.blueprints.catalog.dao.GenericCatalogDAO.ParsingDoneException;
import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.catalog.model.Item;
import com.sun.j2ee.blueprints.util.aspect.ExceptionGenericAspect;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.sun.j2ee.blueprints.catalog.model.Category;
import com.sun.j2ee.blueprints.catalog.model.Page;
import com.sun.j2ee.blueprints.catalog.model.Product;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Raquel Maranhao
 */
public aspect CatalogDAOHandler extends ExceptionGenericAspect {
	
    Map connection = new HashMap();
    //Connection connection = null;
    Map statement = new HashMap();
    //PreparedStatement statement = null;
    Map resultSet = new HashMap();
    //ResultSet resultSet = null;
    declare soft : NamingException : getDataSourceHandler() || 
							 		 getDAOHandler() || 
							 		 newGenericCatalogDAOHandler();
    declare soft : SQLException : getCategoryHandler() || 
								  getCategoriesHandler() || 
								  getProductHandler() || 
								  getProductsHandler() || 
								  getItemHandler() || 
								  getItemsHandler() || 
								  searchItemsHandler() || 
								  getCategoryPointBaseHandler() ||
								  getCategoriesPointBaseHandler() ||
								  getProductPointBaseHandler()  || 
								  getProductsPointBaseHandler() || 
								  getItemPointBaseHandler() || 
								  getItemsPointBaseHandler() ||
								  searchItemsPointBaseHandler() ||
								  resultSetClose() ||
								  preparedStatementClose() || 
								  connectionClose();

    //declare soft : NumberFormatException : parseIntHandler();
    declare soft : ParsingDoneException : loadSQLStatementsHandler();
    declare soft : ServiceLocatorException : getDataSourcePointBaseHandler();
    declare soft : ClassNotFoundException : getDAOHandler();
    declare soft : IllegalAccessException : getDAOHandler();
    declare soft : InstantiationException : getDAOHandler();
    declare soft : ParserConfigurationException : newGenericCatalogDAOHandler();
    declare soft : SAXException : newGenericCatalogDAOHandler();
    declare soft : IOException : newGenericCatalogDAOHandler();

	
	/*** CatalogDAOFactory ***/
	pointcut getDAOHandler() :  
		execution(public CatalogDAO CatalogDAOFactory.getDAO());
	
	/*** GenericCatalogDAO ***/
	pointcut newGenericCatalogDAOHandler() :
		execution(GenericCatalogDAO.new(..));
	pointcut getDataSourceHandler() : 
		execution(DataSource GenericCatalogDAO.getDataSource());
	pointcut closeAll() : 
		withincode(protected static void GenericCatalogDAO.closeAll(Connection, PreparedStatement, ResultSet));
	pointcut resultSetClose() : 
		closeAll() && call(* ResultSet.close());
	pointcut preparedStatementClose() : 
		closeAll() && call(* Statement.close());
	pointcut connectionClose() : 
		closeAll() && call(* Connection.close());
	
	//GenericAspect
	public pointcut aroundExceptionDoNothingHandler() :
	    resultSetClose() ||
		preparedStatementClose() || 
		connectionClose();
	
	pointcut getCategoryHandler() : 
		execution(public Category GenericCatalogDAO.getCategory(String, Locale));
	pointcut getConnectionHandler() : 
		call(Connection DataSource.getConnection()) && 
		(withincode(public Category GenericCatalogDAO.getCategory(..)) || 
 	     withincode(public Page GenericCatalogDAO.getCategories(..)) || 
	     withincode(public Product GenericCatalogDAO.getProduct(..)) || 
	     withincode(public Page GenericCatalogDAO.getProducts(..)) || 
	     withincode(public Item GenericCatalogDAO.getItem(..)) || 
	     withincode(public Page GenericCatalogDAO.getItems(..)) || 
	     withincode(public Page GenericCatalogDAO.searchItems(..)));	
	pointcut buildSQLStatementHandler() : 
		call(PreparedStatement GenericCatalogDAO.buildSQLStatement(Connection, Map, String, String[])) && 
		(withincode(public Category GenericCatalogDAO.getCategory(..)) || 
 	     withincode(public Page GenericCatalogDAO.getCategories(..)) || 
	     withincode(public Product GenericCatalogDAO.getProduct(..)) || 
	     withincode(public Page GenericCatalogDAO.getProducts(..)) || 
	     withincode(public Item GenericCatalogDAO.getItem(..)) || 
	     withincode(public Page GenericCatalogDAO.getItems(..)) || 
	     withincode(public Page GenericCatalogDAO.searchItems(..)));	
	pointcut executeQueryHandler() : 
		call(ResultSet PreparedStatement.executeQuery()) && 
		(withincode(public Category GenericCatalogDAO.getCategory(..)) || 
 	     withincode(public Page GenericCatalogDAO.getCategories(..)) || 
	     withincode(public Product GenericCatalogDAO.getProduct(..)) || 
	     withincode(public Page GenericCatalogDAO.getProducts(..)) || 
	     withincode(public Item GenericCatalogDAO.getItem(..)) || 
	     withincode(public Page GenericCatalogDAO.getItems(..)) || 
	     withincode(public Page GenericCatalogDAO.searchItems(..)));	
	pointcut getCategoriesHandler() :
		execution(public Page GenericCatalogDAO.getCategories(int, int, Locale));
	pointcut getProductHandler() : 
		execution(public Product GenericCatalogDAO.getProduct(String, Locale));
	pointcut getProductsHandler() : 
		execution(public Page GenericCatalogDAO.getProducts(String, int, int, Locale));
	pointcut getItemHandler() : 
		execution(public Item GenericCatalogDAO.getItem(String, Locale));
	pointcut getItemsHandler() : 
		execution(public Page GenericCatalogDAO.getItems(String, int, int, Locale));
	pointcut searchItemsHandler() : 
		execution(public Page GenericCatalogDAO.searchItems(String, int, int, Locale));	
	pointcut parseIntHandler() : 
		execution(private int GenericCatalogDAO.internalParseInt(String)); 
	pointcut loadSQLStatementsHandler() : 
		execution(private void GenericCatalogDAO.loadSQLStatements(SAXParser, String, InputSource));
	pointcut mainHandler() : 
		execution(public static void GenericCatalogDAO.main(String[]));
	
	/*** PointbaseCatalogDAO ***/
	pointcut getDataSourcePointBaseHandler() : 
		execution(protected static DataSource PointbaseCatalogDAO.getDataSource());
	pointcut getCategoryPointBaseHandler() : 
		execution(public Category PointbaseCatalogDAO.getCategory(String, Locale));
	pointcut getCategoriesPointBaseHandler() : 
		execution(public Page PointbaseCatalogDAO.getCategories(int, int, Locale));	
	pointcut getProductPointBaseHandler() : 
		execution(public Product PointbaseCatalogDAO.getProduct(String, Locale));
	pointcut getProductsPointBaseHandler() : 
		execution(public Page PointbaseCatalogDAO.getProducts(String, int, int, Locale));
	pointcut getItemPointBaseHandler() : 
		execution(public Item PointbaseCatalogDAO.getItem(String, Locale));
	pointcut getItemsPointBaseHandler() : 
		execution(public Page PointbaseCatalogDAO.getItems(String, int, int, Locale));
	pointcut searchItemsPointBaseHandler() : 
		execution(public Page PointbaseCatalogDAO.searchItems(String, int, int, Locale));
	
	
	
	CatalogDAO around() throws CatalogDAOSysException : 
		getDAOHandler() {
		try{
			return proceed();
		}catch (Exception se){
			if (se instanceof NamingException) {
				throw new CatalogDAOSysException("CatalogDAOFactory.getDAO:  NamingException while getting DAO type : \n" + se.getMessage());
			} else  {
				throw new CatalogDAOSysException("CatalogDAOFactory.getDAO:  Exception while getting DAO type : \n" + se.getMessage());
			}		
		}
	}
	
	void around() throws CatalogDAOSysException :
		newGenericCatalogDAOHandler() {
		try{
			proceed();
		}catch(Exception exception){
			if (thisJoinPoint.getArgs() != null) 
				exception.printStackTrace(System.err);
			System.err.println(exception);
			throw new CatalogDAOSysException(exception.getMessage());
		}
	}

	DataSource around() throws CatalogDAOSysException : getDataSourceHandler(){
		try{
			return proceed();
		}catch(NamingException exception){
			throw new CatalogDAOSysException("NamingException while looking up DB context : " +
                    exception.getMessage());
		}
	}
	
//	after() throwing(NamingException exception) throws CatalogDAOSysException : 
//		getDataSourceHandler() {
//		throw new CatalogDAOSysException("NamingException while looking up DB context : " +
//                                       exception.getMessage());
//    }

	/* GenericAspect
	void around() : 
		resultSetClose() ||
		preparedStatementClose() || 
		connectionClose() {
		try {
			proceed();
		} catch(Exception exception) {
			//Do nothing
		}
    }	
    */
	
	after() returning(Connection con) : 
		getConnectionHandler() {
    	//Save inner method variable to local(multi-thread)
	    connection.put(Thread.currentThread().getName(), con);
		//connection = con;
	}

	after() returning(PreparedStatement st) : 
		buildSQLStatementHandler() {
    	//Save inner method variable to local(multi-thread)
	    statement.put(Thread.currentThread().getName(), st);
		//statement = st;
	}
	
	after() returning(ResultSet rs) : 
		executeQueryHandler() {
    	//Save inner method variable to local(multi-thread)
	    resultSet.put(Thread.currentThread().getName(), rs);
		//resultSet = rs;
	}
	
	Object around() : getCategoryHandler() || 
		getCategoriesHandler() || 
		getProductHandler() || 
		getProductsHandler() ||
		getItemHandler() || 
		getItemsHandler() ||
		searchItemsHandler() {
		try {
			return proceed();
		} catch (SQLException exception) {
			throw new CatalogDAOSysException("SQLException: " + exception.getMessage());
		} finally {
			GenericCatalogDAO.closeAll((Connection)connection.get(Thread.currentThread().getName()), 
			        (PreparedStatement)statement.get(Thread.currentThread().getName()), 
			        (ResultSet)resultSet.get(Thread.currentThread().getName()));
		}
		
	}
	
	int around() : parseIntHandler() {
		try {
			return proceed();
		} catch (NumberFormatException exception) {
          //throw new SAXException(exception);
		  return 0;
	    }
	}
	
	void around() :  loadSQLStatementsHandler() {
		try {
			proceed();
		} catch(ParsingDoneException exception) {
			// Ignored		
		}
	}
	
	void around() : mainHandler() {
		try {
			proceed();
		} catch(Exception exception) {
		    exception.printStackTrace(System.err);
		    System.err.println(exception);
		    System.exit(2);
		}
	}
	
	DataSource around() throws CatalogDAOSysException : getDataSourcePointBaseHandler(){
		try{
			return proceed();
		}catch(ServiceLocatorException slx){
			throw new CatalogDAOSysException("NamingException while looking up DB context : " +
                    slx.getMessage());
		}
	}

	Object around() throws CatalogDAOSysException : 
		getCategoryPointBaseHandler() || 
		getCategoriesPointBaseHandler() ||
		getProductPointBaseHandler() ||
		getProductsPointBaseHandler() ||
		getItemPointBaseHandler() ||
		getItemsPointBaseHandler() || 
		searchItemsPointBaseHandler(){
		
		try{
			return proceed();
		}catch(SQLException se){
			throw new CatalogDAOSysException("SQLException: " + se.getMessage());
		}
	}
	
}
