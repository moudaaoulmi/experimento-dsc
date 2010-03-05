package com.sun.j2ee.blueprints.petstore.tools.populate;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

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
import petstore.exception.ExceptionHandler;


/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect PetstoreToolsHandler {
	private Map connect = new HashMap();
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : NamingException : createCreditCardHandler() || 
									 createCustomerHandler() ||
									 getConnectionHandler() || 
									 createProfileHandler() ||
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
	
	declare soft : RemoveException : createCustomerHandler();
	
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
	
//	declare soft : ParsingDoneException : loadSQLStatementsHandler();
	
	declare soft : SQLException : executeSQLStatementHandler() || 
								  getConnectionHandler();
	
	// ---------------------------
	// Pointcut's
	// ---------------------------
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
//	public pointcut aroundExceptionDoNothingHandler() : 
//		execution(private void CustomerPopulator.internalRemoveExistingCustomer(String));
	
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
//	pointcut loadSQLStatementsHandler() : 
//		execution(private void PopulateServlet.loadSQLStatements(SAXParser, String, InputSource));
//	
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
	pointcut createUserHandler() :
		execution(private UserLocal UserPopulator.createUser(String, String));
	pointcut mainUserPopulatorHandler() : 
		execution(* UserPopulator.main(String[]));
	
	/*** XMLDBHandler ***/
	pointcut startElementHandler() : 
		execution(public void XMLDBHandler.startElement(String, String, String, Attributes));
	pointcut endElementHandler() : 
		execution(public void XMLDBHandler.endElement(String, String, String));
	
	pointcut connectionHandler():
		call(* PopulateServlet.getConnection()) && 
		withincode(private boolean PopulateServlet.internalPopulateLogic(..));	
	
	// ---------------------------
    // Advice's
    // ---------------------------
	Connection around(): connectionHandler(){
		Connection c = proceed();
    	connect.put(Thread.currentThread().getName(), c);
    	return c;
    }	
	
	Object around() : internalPopulateLogicHandler() {
		Object result = null;
		try {
			result = proceed();
	    } finally {
	    	try {
	    		Connection con = (Connection)connect.get(Thread.currentThread().getName()); 
	    		if (con != null)
	    			con.close();
	    	} catch (Exception exception) {}
	    }
	    return result;
	}
	
	
	Object around()	throws PopulateException : 
		createAccountHandler() ||
		createAddressHandler() || 
		createContactInfoHandler() ||
		createCreditCardHandler() ||
		createCustomerHandler() || 
		createProfileHandler() || 
		createUserHandler() {
		try{
			return proceed();
		}catch (Exception exception){
			throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
		}
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
	
	void around()throws javax.servlet.ServletException : 
		initHandler()  {
		try{
			proceed();
		}catch(Exception exception){
			throw new ServletException(exception);
		}
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
	
	Object around() throws PopulateException : 
		internalSetupHandler() || 
		internalGetReaderHandler() {
		try{
			return proceed();
		}catch(Exception exception){
			throw new PopulateException(exception);
		}
	}

	Connection around() throws PopulateException : 
		getConnectionHandler() {
		try{
			return proceed();
		}catch(Exception exception){
			throw new PopulateException("Can't get catalog data source connection", exception);
		}
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
	
//	void around() : 
//		loadSQLStatementsHandler() {
//		try {
//			proceed();
//		} catch (ParsingDoneException exception) {} // Ignored			
//	}	

	Object around(Connection connection, String sqlStatement, String[] parameterNames, XMLDBHandler handler) throws PopulateException : 
		executeSQLStatementHandler() && 
		args(connection, sqlStatement, parameterNames, handler){
		
		try{
			return proceed(connection, sqlStatement, parameterNames, handler);
		}catch(SQLException exception){
				throw new PopulateException(sqlStatement, exception);
		}
	}

	void around() : 
		internalProductDetailsPopulatorDropTablesHandler() {
	    try {
	        proceed();
	    } catch (PopulateException exception) {}	
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
	void around() throws SAXException : 
		startElementHandler() || endElementHandler(){
	
		try{
			proceed();
		}catch(PopulateException exception){
			throw new SAXException(exception.getMessage(), exception.getRootCause());
		}		
	}
}