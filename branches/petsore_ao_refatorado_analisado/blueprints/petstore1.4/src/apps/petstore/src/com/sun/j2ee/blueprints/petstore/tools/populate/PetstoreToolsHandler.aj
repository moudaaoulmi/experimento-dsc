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
import com.sun.j2ee.blueprints.util.aspect.AspectUtil;

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
import javax.servlet.ServletException ;

public aspect PetstoreToolsHandler extends ExceptionGenericAspect {

	pointcut createAccountHandler() : 
		execution(private AccountLocal AccountPopulator.createAccount(ContactInfoLocal, CreditCardLocal));	
	pointcut createAddressHandler() : 
		execution(private AddressLocal AddressPopulator.createAddress(String, String, String, String, String, String));		
	pointcut internalItemPopulatorDropTablesHandler() : 
		execution(private void CatalogPopulator.internalItemPopulatorDropTables(ItemPopulator, Connection));	
	pointcut internalProductPopulatorDropTablesHandler() : 
		execution(private void CatalogPopulator.internalProductPopulatorDropTables(ProductPopulator, Connection)); 
	pointcut internalCategoryPopulatorDropTablesHandler() : 
		execution(private void CatalogPopulator.internalCategoryPopulatorDropTables(CategoryPopulator, Connection)); 
	pointcut internalCategoryDetailsPopulatorHandler() :  
		execution(private void CategoryPopulator.internalCategoryDetailsPopulator(CategoryDetailsPopulator, Connection));	
	pointcut createContactInfoHandler() : 
		execution(private ContactInfoLocal ContactInfoPopulator.createContactInfo(String, String, String, String, AddressLocal));
	pointcut createCreditCardHandler() : 
		execution(private CreditCardLocal CreditCardPopulator.createCreditCard(String, String, String));
	pointcut checkHandler() : 
		execution(public boolean CustomerPopulator.check());
	public pointcut aroundExceptionDoNothingHandler() : 
		execution(private void CustomerPopulator.internalRemoveExistingCustomer(String));
	pointcut createCustomerHandler() : 
		execution(private CustomerLocal CustomerPopulator.createCustomer(String, AccountLocal, ProfileLocal));
	pointcut internalItemDetailsPopulatorDropTablesHandler() : 
		execution(private void ItemPopulator.internalItemDetailsPopulatorDropTables(ItemDetailsPopulator, Connection));	
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
	pointcut executeSQLStatementHandler() :
		execution(* PopulateUtils.executeSQLStatement(Connection, String, String[], XMLDBHandler));
	pointcut internalProductDetailsPopulatorDropTablesHandler() : 
		execution(private void ProductPopulator.internalProductDetailsPopulatorDropTables(ProductDetailsPopulator, Connection));
	pointcut createProfileHandler() : 
		execution(private ProfileLocal ProfilePopulator.createProfile(String , String , boolean , boolean ));
	pointcut userPopulatorCheckHandler() : 
		execution(public boolean UserPopulator.check());
	pointcut createUserHandler() :
		execution(private UserLocal UserPopulator.createUser(String, String));
	pointcut internalRemoveExistingUserHandler() :
		execution(private UserLocal UserPopulator.internalRemoveExistingUser(String, UserLocal));
	pointcut mainUserPopulatorHandler() : 
		execution(* UserPopulator.main(String[]));
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
	Object around() throws PopulateException : 
		createAccountHandler() ||
		createAddressHandler() || 
		createContactInfoHandler() ||
		createCreditCardHandler() ||
		createCustomerHandler() || 
		createProfileHandler() || 
		createUserHandler() ||
		internalSetupHandler()||
		internalGetReaderHandler()||
		getConnectionHandler(){
		try{
			return proceed();
		}catch(Exception exception){
			if(AspectUtil.verifyJointPoint(thisJoinPoint,"private void PopulateServlet.internalSetup(CatalogPopulator, CustomerPopulator, UserPopulator, Connection, XMLReader)")||
					AspectUtil.verifyJointPoint(thisJoinPoint,"execution(private XMLReader PopulateServlet.internalGetReader()")){
				throw new PopulateException (exception);
			}else if(AspectUtil.verifyJointPoint(thisJoinPoint,"execution(protected Connection PopulateServlet.getConnection())")){
				throw new PopulateException("Can't get catalog data source connection", exception);
			}else{
				throw new PopulateException ("Could not create: " + exception.getMessage(), exception);									
			}					
		}
	}	
	void around() : 
		internalItemPopulatorDropTablesHandler() || 
		internalProductPopulatorDropTablesHandler() || 
		internalCategoryPopulatorDropTablesHandler() || 
		internalCategoryDetailsPopulatorHandler() || 
		internalItemDetailsPopulatorDropTablesHandler() ||
		internalProductDetailsPopulatorDropTablesHandler(){
		try {
	        proceed();
	    } catch (PopulateException exception) {
        }  	
	}	
	boolean around() : 
		checkHandler() || 
		userPopulatorCheckHandler()||
		internalCheckHandler() { // foi inserido apos rerefatoracao.
		try {
			return proceed();
	    } catch (Exception e) {
	        return false;
	    }
	}	
	void around()throws ServletException : initHandler(){
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
	String around(PopulateServlet ps, String path) throws IOException : 
		getResourceHandler() && target(ps) && args(path) {
		try {
			return proceed(ps,path);
	    } catch (MalformedURLException exception) {
	    	String url = null;
    		URL u = ps.getServletContext().getResource(path);
    		url = u != null ? u.toString() : path;
	        // System.err.println("Made " + url + " from " + path);
	        return url;
	    }
	}	
	void around() : 
		loadSQLStatementsHandler() {
		try {
			proceed();
		} catch (ParsingDoneException exception) {} // Ignored
	}	
	boolean around(Connection connection, String sqlStatement, String[] parameterNames, XMLDBHandler handler) throws PopulateException:executeSQLStatementHandler() && 
		args(connection, sqlStatement, parameterNames, handler) {
		try{
			return proceed(connection,sqlStatement,parameterNames,handler);
		}catch(SQLException exception ){
			throw new PopulateException(sqlStatement, exception);
		}
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
	Object around()throws SAXException : startElementHandler() || 
		endElementHandler() {
		try{
			return proceed();
		}catch(PopulateException exception){		
			throw new SAXException(exception.getMessage(), exception.getRootCause());
		}
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