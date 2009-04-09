package com.sun.j2ee.blueprints.petstore.tools.populate;

import org.xml.sax.SAXException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class ToolPopulateHandler {
	
	/** Coloquei o nome 'create' para reusar porque tem muitos
	 * createAddress(), createAccount(), etc,,, tudo 
	 * com o mesmo catch!
	 * 
	 */
	 //catch (Exception exception) 
	public void createHandler(Exception exception) throws PopulateException{
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
	public void initHandler(Exception exception){	
	   throw new ServletException(exception);
	}
	
	//catch(PopulateException exception)
	public void doPostHandler(PopulateException exception, HttpServletRequest request, 
			HttpServletResponse response,String errorPageURL ){
	      System.err.println(exception.getRootCause().getMessage());
	      if (errorPageURL == null) {
	        throw new ServletException("Populate exception occured :" + exception.getMessage(), exception.getRootCause());
	      } else {
	        redirect(request, response, errorPageURL);
	      }
	    }
	
	//catch (PopulateException exception) 
	public void startElementHandler(PopulateException exception) throws SAXException {
        throw new SAXException(exception.getMessage(), exception.getRootCause());
    }
	
//	//catch (NumberFormatException exception)
//	public void getValueHandler(){}
	
	//catch (Exception exception) 
	public static void mainHandler (Exception exception) {
        System.err.println(exception.getMessage() + ": " + exception);
        System.exit(2);
	}
	
	//catch (SQLException exception) 
	public void executeSQLStatementHandler(SQLException exception) throws PopulateException  {
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
	public void getConnectionHandler(Exception exception) throws PopulateException  {
	      throw new PopulateException("Can't get catalog data source connection", exception);
	}
	
	//catch (MalformedURLException exception) 
	public void getResourceHandler(MalformedURLException exception, String path){
	      URL u = getServletContext().getResource(path);
	      url = u != null ? u.toString() : path;
	    }
	
}//ToolPopulateHandler{}
