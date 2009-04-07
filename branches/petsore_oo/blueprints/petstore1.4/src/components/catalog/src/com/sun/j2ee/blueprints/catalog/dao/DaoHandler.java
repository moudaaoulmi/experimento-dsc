package com.sun.j2ee.blueprints.catalog.dao;

import java.sql.SQLException;

import javax.naming.NamingException;
import java.lang.*;

import com.sun.j2ee.blueprints.catalog.dao.GenericCatalogDAO.ParsingDoneException;
import com.sun.j2ee.blueprints.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;


public class DaoHandler {
	
	//catch (NamingException ne)
	public void getDAO1Handler(NamingException ne) throws CatalogDAOSysException{
        throw new CatalogDAOSysException("CatalogDAOFactory.getDAO:  NamingException while getting DAO type : \n" + ne.getMessage());
    } 
	
	//catch (Exception se) 
	public void getDAO2Handler(Exception se) throws CatalogDAOSysException{
        throw new CatalogDAOSysException("CatalogDAOFactory.getDAO:  Exception while getting DAO type : \n" + se.getMessage());
    }
	
	//catch (Exception exception) 
	public void genericCatalogDAOHandler(Exception exception) throws CatalogDAOSysException{
		  exception.printStackTrace(System.err);
		  System.err.println(exception);
	      throw new CatalogDAOSysException(exception.getMessage());
	}

	//catch (NamingException exception) 
	public static void getDataSourceHandler(NamingException exception) throws CatalogDAOSysException {
	      throw new CatalogDAOSysException("NamingException while looking up DB context : " +
	                                       exception.getMessage());
	}
	
	//catch (Exception exception) 
	public static void closeAllHandler(Exception exception){
		//ignore
	}
	
	//catch (SQLException exception) 
	public void getCategoryHandler(SQLException exception) throws CatalogDAOSysException {
	      throw new CatalogDAOSysException("SQLException: " + exception.getMessage());
	} 
	
	public void getCategoryFINALLYHandler(Connection connection, PreparedStatement statement, ResultSet resultSet) {
	    GenericCatalogDAO obj = new GenericCatalogDAO();  
		obj.closeAll(connection, statement, resultSet);
	}
	
	 //catch (NumberFormatException exception)
	public void parseIntHandler(NumberFormatException exception){
         //throw new SAXException(exception);
    }
	
	//catch (ParsingDoneException exception) 
	public void loadSQLStatementsHandler(ParsingDoneException exception){
		// Ignored
	} 
	
	//catch (Exception exception)
	public static void mainHandler(Exception exception) { 
        exception.printStackTrace(System.err);
        System.err.println(exception);
        System.exit(2);
	}
	
	//catch (ServiceLocatorException slx) 
	public static void getDataSourceHandler(ServiceLocatorException slx) throws CatalogDAOSysException {
        throw new CatalogDAOSysException("NamingException while looking up DB context : " +
                               slx.getMessage());
    }
	
	//catch (SQLException se) 
	public void getCategoriesHandler(SQLException se) throws CatalogDAOSysException {
        se.printStackTrace(System.err);
        throw new CatalogDAOSysException("SQLException: " + se.getMessage());
      }
	
}//DaoHandler{}
