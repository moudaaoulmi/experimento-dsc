package com.sun.j2ee.blueprints.petstore.tools.populate;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;

import com.sun.j2ee.blueprints.address.ejb.AddressLocal;
import com.sun.j2ee.blueprints.contactinfo.ejb.ContactInfoLocal;
import com.sun.j2ee.blueprints.creditcard.ejb.CreditCardLocal;
import com.sun.j2ee.blueprints.customer.account.ejb.AccountLocal;
import com.sun.j2ee.blueprints.customer.ejb.CustomerLocal;
import com.sun.j2ee.blueprints.customer.profile.ejb.ProfileLocal;
import com.sun.j2ee.blueprints.signon.user.ejb.UserLocal;

public class ToolPopulateHandler {
	
	
	 //catch (Exception exception) 
	public AccountLocal createAccountHandler(Exception exception) throws PopulateException{
	      throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
	}
	
	public AddressLocal createAddressHandler(Exception exception) throws PopulateException{
	      throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
	}
	
	public ContactInfoLocal createContactInfoHandler(Exception exception) throws PopulateException{
	      throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
	}
	
	public CreditCardLocal createCreditCardHandler(Exception exception) throws PopulateException{
	      throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
	}
	
	public CustomerLocal createCustomerHandler(Exception exception) throws PopulateException{
	      throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
	}
	public ProfileLocal createProfileHandler(Exception exception) throws PopulateException{
	      throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
	}
	
	public UserLocal createUserHandler(Exception exception) throws PopulateException{
	      throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
	}
	
	
	/**
	 * MÉTODO criado para reusar, pois haviam vários catchs 
	 * não tinham nada como tratamento
	 */
	//catch (PopulateException exception)
	public void ignoreHandler(){
	      // System.err.println(exception.getRootCause().getMessage());
	}
	
	//catch (PopulateException exception)
//	public void dropTablesHandler(){
//	      // System.err.println(exception.getRootCause().getMessage());
//	}

	//catch (Exception e)
	public boolean checkHandler(){
        return false;
   }
	
//	//catch (Exception exception) 
//	public void createCustomerHandler(){
//  	  //ignore
//    }
	
	//catch (Exception exception) 
	public void initHandler(Exception exception) throws ServletException{	
	   throw new ServletException(exception);
	}
	
	/** Nao pode refatorar porque dentro do
	 *  catch existe um metodo privado
	 * 
	 *
	//catch(PopulateException exception)
	public void doPostHandler(PopulateException exception, HttpServletRequest request, 
			HttpServletResponse response,String errorPageURL ){
	      System.err.println(exception.getRootCause().getMessage());
	      if (errorPageURL == null) {
	        throw new ServletException("Populate exception occured :" + exception.getMessage(), exception.getRootCause());
	      } else {
	        PopulateServlet obj = new PopulateServlet();
	    	//n„o pode metodo privado  
	        obj.redirect(request, response, errorPageURL);
	      }
	    }
	*/
	
	//catch (PopulateException exception) 
	public void startElementHandler(PopulateException exception) throws SAXException {
        throw new SAXException(exception.getMessage(), exception.getRootCause());
    }
	
//	//catch (NumberFormatException exception)
	public int getValueHandler(){
		return 0;
	}
	
	//catch (Exception exception) 
	public static void mainHandler (Exception exception) {
        System.err.println(exception.getMessage() + ": " + exception);
        System.exit(2);
	}
	
	//catch (SQLException exception) 
	public boolean executeSQLStatementHandler(String sqlStatement,SQLException exception) throws PopulateException  {
        throw new PopulateException(sqlStatement, exception);
    }
	
	// catch (Exception exception)
	public void populateHandler (Exception exception) throws PopulateException {
	      throw new PopulateException(exception);
	}
	
	/** 
	 * 
	 * Caso interessantes porque dentro do FINALLY 
	 * um bloco TRY/CATCH dentro.  COLOCAR COMO CENÁRIO
	 * 
	 */
	
	public void populateFINALLYHandler(Connection connection ){
		try {
	        if (connection != null) {
	          connection.close();
	        }
	      } catch (Exception exception) {}
	}
	
	 //catch (Exception exception)
	public Connection getConnectionHandler(Exception exception) throws PopulateException  {
	      throw new PopulateException("Can't get catalog data source connection", exception);
	}
	
	//catch (MalformedURLException exception) 
	public String getResourceHandler(MalformedURLException exception, String path,String url) throws MalformedURLException{
	    PopulateServlet obj = new PopulateServlet();  
		URL u = obj.getServletContext().getResource(path);
	     return  url = u != null ? u.toString() : path;
	    }
	
}//ToolPopulateHandler{}
