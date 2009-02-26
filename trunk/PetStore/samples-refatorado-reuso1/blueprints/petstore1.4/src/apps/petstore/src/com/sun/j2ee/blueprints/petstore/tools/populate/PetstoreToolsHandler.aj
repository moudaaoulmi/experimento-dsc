/*
 * Created on 18/09/2005
 */
package com.sun.j2ee.blueprints.petstore.tools.populate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import com.sun.j2ee.blueprints.util.aspect.ExceptionGenericAspect;
import com.sun.j2ee.blueprints.address.ejb.AddressLocal;
import com.sun.j2ee.blueprints.contactinfo.ejb.ContactInfoLocal;
import com.sun.j2ee.blueprints.creditcard.ejb.CreditCardLocal;
import com.sun.j2ee.blueprints.customer.account.ejb.AccountLocal;
import com.sun.j2ee.blueprints.customer.ejb.CustomerLocal;
import com.sun.j2ee.blueprints.customer.profile.ejb.ProfileLocal;
import com.sun.j2ee.blueprints.petstore.tools.populate.PopulateServlet.ParsingDoneException;
import com.sun.j2ee.blueprints.signon.user.ejb.UserLocal;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author Raquel Maranhao
 */
public aspect PetstoreToolsHandler extends ExceptionGenericAspect {
	
	/*** AccountPopulator ***/
	pointcut createAccountHandler() : 
		execution(private AccountLocal AccountPopulator.createAccount(ContactInfoLocal, CreditCardLocal));
	
	/*** AddressPopulator ***/
	pointcut createAddressHandler() : 
		execution(private AddressLocal AddressPopulator.createAddress(String, String, String, String, String, String));
	
	/*** CatalogPopulator ***/
	pointcut internalItemPopulatorDropTablesHandler() : 
		execution(private void CatalogPopulator.internalItemPopulatorDropTables(ItemPopulator, Connection)); 
	pointcut internalProductPopulatorDropTablesHandler() : 
		execution(private void CatalogPopulator.internalProductPopulatorDropTables(ProductPopulator, Connection)); 
	pointcut internalCategoryPopulatorDropTablesHandler() : 
		execution(private void CatalogPopulator.internalCategoryPopulatorDropTables(CategoryPopulator, Connection)); 

	/*** CategoryPopulator ***/
	pointcut internalCategoryDetailsPopulatorHandler() :  
		execution(private void CategoryPopulator.internalCategoryDetailsPopulator(CategoryDetailsPopulator, Connection));	

	/*** ContactInfoPopulator ***/
	pointcut createContactInfoHandler() : 
		execution(private ContactInfoLocal ContactInfoPopulator.createContactInfo(String, String, String, String, AddressLocal));
	
	/*** CreditCardPopulator ***/
	pointcut createCreditCardHandler() : 
		execution(private CreditCardLocal CreditCardPopulator.createCreditCard(String, String, String));
	
	/*** CustomerPopulator ***/
	pointcut checkHandler() : 
		execution(public boolean CustomerPopulator.check());
	
	public pointcut aroundExceptionDoNothingHandler() : 
		execution(private void CustomerPopulator.internalRemoveExistingCustomer(String));

	pointcut createCustomerHandler() : 
		execution(private CustomerLocal CustomerPopulator.createCustomer(String, AccountLocal, ProfileLocal));
	
	/*** ItemPopulator ***/
	pointcut internalItemDetailsPopulatorDropTablesHandler() : 
		execution(private void ItemPopulator.internalItemDetailsPopulatorDropTables(ItemDetailsPopulator, Connection));	
	
	/*** PopulateServlet ***/	
	pointcut initHandler() : 
		execution(public void PopulateServlet.init(ServletConfig));
	pointcut internalPopulateHandler() : 
		execution(private void PopulateServlet.internalPopulate(String, HttpServletRequest, HttpServletResponse, String));
	pointcut internalCheckHandler() : 
		execution(private boolean PopulateServlet.internalCheck(CatalogPopulator, CustomerPopulator, UserPopulator, Connection));
	pointcut populateHandler() : 
		execution(private boolean PopulateServlet.populate(boolean));
	pointcut internalSetupHandler() : 
		execution(private void PopulateServlet.internalSetup(CatalogPopulator, CustomerPopulator, UserPopulator, Connection, XMLReader));
	pointcut internalGetReaderHandler() : 
		execution(private XMLReader PopulateServlet.internalGetReader());
	pointcut internalPopulateLogicHandler() : 
		execution(private boolean PopulateServlet.internalPopulateLogic(Connection, boolean, boolean, XMLReader));
	pointcut getConnectionHandler() : 
		execution(protected Connection PopulateServlet.getConnection());
	pointcut getResourceHandler() : 
		execution(private String PopulateServlet.getResource(String));
	pointcut loadSQLStatementsHandler() : 
		execution(private void PopulateServlet.loadSQLStatements(SAXParser, String, InputSource));
	
	/*** PopulateUtils ***/
	pointcut executeSQLStatementHandler() :
		execution(* PopulateUtils.executeSQLStatement(Connection, String, String[], XMLDBHandler));
	
	/*** ProductPopulator ***/
	pointcut internalProductDetailsPopulatorDropTablesHandler() : 
		execution(private void ProductPopulator.internalProductDetailsPopulatorDropTables(ProductDetailsPopulator, Connection));
	
	/*** ProfilePopulator ***/
	pointcut createProfileHandler() : 
		execution(private ProfileLocal ProfilePopulator.createProfile(String , String , boolean , boolean ));
	
	/*** UserPopulator ***/
	pointcut userPopulatorCheckHandler() : 
		execution(public boolean UserPopulator.check());
	pointcut createUserHandler() :
		execution(private UserLocal UserPopulator.createUser(String, String));
	pointcut internalRemoveExistingUserHandler() :
		execution(private UserLocal UserPopulator.internalRemoveExistingUser(String, UserLocal));
	pointcut mainUserPopulatorHandler() : 
		execution(* UserPopulator.main(String[]));
	
	/*** XMLDBHandler ***/
	pointcut startElementHandler() : 
		execution(public void XMLDBHandler.startElement(String, String, String, Attributes));
	pointcut endElementHandler() : 
		execution(public void XMLDBHandler.endElement(String, String, String));
	pointcut getValueHandler() : 
		execution(public int XMLDBHandler.getValue(String, int));
		
	
	
	declare soft : NamingException : createCreditCardHandler() || 
		checkHandler() ||
		createCustomerHandler() ||
		getConnectionHandler() || 
		createProfileHandler() ||
		userPopulatorCheckHandler() ||
		createAccountHandler() || 
		createAddressHandler() || 
		createContactInfoHandler() || 
		createCreditCardHandler() || 
		createUserHandler();
	declare soft : CreateException : createCreditCardHandler() || 
		createCustomerHandler() || 
		createProfileHandler() ||
		createAccountHandler() || 
		createAddressHandler() || 
		createContactInfoHandler() || 
		createCreditCardHandler() || 
		createUserHandler();
	declare soft : FinderException : checkHandler() || 
		internalRemoveExistingUserHandler() ||
		userPopulatorCheckHandler() ||
		aroundExceptionDoNothingHandler();
	declare soft : RemoveException : createCustomerHandler() || 
		internalRemoveExistingUserHandler() ||
		aroundExceptionDoNothingHandler();
	declare soft : IOException : initHandler() || 
		internalSetupHandler() ||
		mainUserPopulatorHandler();
	declare soft : ParserConfigurationException : initHandler() || 
		internalGetReaderHandler() ||
		mainUserPopulatorHandler();
	declare soft : SAXException : initHandler() || 
		internalGetReaderHandler() ||
		internalSetupHandler() || 
		mainUserPopulatorHandler();
	declare soft : PopulateException : internalItemPopulatorDropTablesHandler() || 
		internalProductPopulatorDropTablesHandler() || 
		internalCategoryPopulatorDropTablesHandler() || 
		internalCategoryDetailsPopulatorHandler() || 
		internalItemDetailsPopulatorDropTablesHandler() || 
		internalPopulateHandler() || 
		internalCheckHandler() || 
		internalProductDetailsPopulatorDropTablesHandler() || 
		startElementHandler() || 
		endElementHandler() || 
		mainUserPopulatorHandler();
	declare soft : MalformedURLException : getResourceHandler();
	declare soft : ParsingDoneException : loadSQLStatementsHandler();
	declare soft : SQLException : executeSQLStatementHandler() || 
		getConnectionHandler();
	
	
	
	after() throwing(Exception exception) throws PopulateException : 
		createAccountHandler() ||
		createAddressHandler() || 
		createContactInfoHandler() ||
		createCreditCardHandler() ||
		createCustomerHandler() || 
		createProfileHandler() || 
		createUserHandler() {
		throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
	}
	
	void around() : 
		internalItemPopulatorDropTablesHandler() || 
		internalProductPopulatorDropTablesHandler() || 
		internalCategoryPopulatorDropTablesHandler() || 
		internalCategoryDetailsPopulatorHandler() || 
		internalItemDetailsPopulatorDropTablesHandler() {
	    try {
	        proceed();
	    } catch (PopulateException exception) {
        }  	
	}
	
	boolean around() : 
		checkHandler() || 
		userPopulatorCheckHandler() {
		try {
			return proceed();
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	/*
	void around() : 
		internalRemoveExistingCustomerHandler() {
		try {
			proceed();
	    } catch (Exception exception) {}  	
	}
	*/

	after() throwing(Exception exception) throws javax.servlet.ServletException : 
		initHandler()  {
		throw new ServletException(exception);
	}
	
	void around(PopulateServlet ps,	String forcefully, HttpServletRequest request, HttpServletResponse response, String errorPageURL)  
		throws ServletException, IOException : 
		internalPopulateHandler() && target(ps) && 
		args(forcefully, request, response, errorPageURL) {
			try {
				proceed(ps, forcefully, request, response, errorPageURL);
			} catch(PopulateException exception) {
		        System.err.println(exception.getRootCause().getMessage());
		        if (errorPageURL == null) {
		          throw new ServletException("Populate exception occured :" + exception.getMessage(), exception.getRootCause());
		        } else {
		          ps.redirect(request, response, errorPageURL);
		        }
			}
	}
	
	boolean around() : 
		internalCheckHandler() {
		try {
			return proceed();
	    } catch (PopulateException exception) {
	        return false;
	    }
	}
	
	after() throwing(Exception exception) throws PopulateException : 
		internalSetupHandler() || 
		internalGetReaderHandler() {
		throw new PopulateException(exception);
	}
	
	boolean around(Connection connection, boolean forcefully, boolean alreadyPopulated, XMLReader reader) : 
		internalPopulateLogicHandler() && 
		args(connection, forcefully, alreadyPopulated, reader) {
		try {
			return proceed(connection, forcefully, alreadyPopulated, reader);
	    } finally {
	      try {
	        if (connection != null) {
	          connection.close();
	        }
	      } catch (Exception exception) {}
	    }
	}
	
	after() throwing(Exception exception) throws PopulateException : 
		getConnectionHandler() {
		throw new PopulateException("Can't get catalog data source connection", exception);
	}

	String around(PopulateServlet ps, String path) throws IOException : 
		getResourceHandler() && target(ps) && args(path) {
		try {
			return proceed(ps,path);
	    } catch (MalformedURLException exception) {
	    	String url = null;
    		URL u = ps.getServletContext().getResource(path);
    		url = u != null ? u.toString() : path;
	        //System.err.println("Made " + url + " from " + path);
	        return url;
	    }
	}

	void around() : 
		loadSQLStatementsHandler() {
		try {
			proceed();
		} catch (ParsingDoneException exception) {} // Ignored			
	}	

	after(Connection connection, String sqlStatement, String[] parameterNames, XMLDBHandler handler) throwing(SQLException exception) throws PopulateException : 
		executeSQLStatementHandler() && 
		args(connection, sqlStatement, parameterNames, handler) {
		throw new PopulateException(sqlStatement, exception);
	}

	void around() : 
		internalProductDetailsPopulatorDropTablesHandler() {
	    try {
	        proceed();
	    } catch (PopulateException exception) {}	
	}
	
	UserLocal around() : 
		internalRemoveExistingUserHandler() {
		try {
			return proceed();
		} catch (Exception exception) {
			return null;
		}
	}
	
	void around() :
		mainUserPopulatorHandler() {
		try {
			proceed();
        } catch (Exception exception) {
	        System.err.println(exception.getMessage() + ": " + exception);
	        System.exit(2);
	    }		
	}

	after() throwing(PopulateException exception) throws SAXException : 
		startElementHandler() || 
		endElementHandler() {
		throw new SAXException(exception.getMessage(), exception.getRootCause());
	}

	int around(String name, int defaultValue) :
		getValueHandler() && args(name, defaultValue){
		try {
			return proceed(name, defaultValue);
	    } catch (NumberFormatException exception) {
	    	return defaultValue;
	    }
	}
	
}