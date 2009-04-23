/*
 * Created on 21/09/2005
 */
package com.sun.j2ee.blueprints.supplier.tools.populate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.sun.j2ee.blueprints.supplier.inventory.ejb.InventoryLocal;
import com.sun.j2ee.blueprints.supplier.tools.populate.PopulateException;
import com.sun.j2ee.blueprints.util.aspect.ExceptionGenericAspect;

/**
 * @author rmaranhao
 */
public aspect SupplierToolsPopulateHandler extends ExceptionGenericAspect {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : PopulateException : internalPopulateHandler() || 
									   internalInventoryPopulatorCheckHandler() || 
									   internalStartElementHandler() || 
									   internalEndElementHandler();
	declare soft : MalformedURLException : getResourceHandler();
	declare soft : SAXException : populateHandler() || 
								  internalInventoryPopulatorHandler();
	declare soft : ParserConfigurationException : populateHandler();
	declare soft : NamingException : checkHandler() || 
									 createInventoryHandler();
	declare soft : FinderException : checkHandler() || 
									 aroundExceptionDoNothingHandler();
	declare soft : RemoveException : aroundExceptionDoNothingHandler();
	declare soft : CreateException : createInventoryHandler();
	declare soft : IOException : internalInventoryPopulatorHandler();

	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** InventoryPopulator ***/
	pointcut checkHandler() : 
		execution(public boolean InventoryPopulator.check());
	
	//GenericAspect
	public pointcut aroundExceptionDoNothingHandler() :
		execution(private void InventoryPopulator.internalRemoveExistingInventory(String));
	
	pointcut createInventoryHandler() :
		execution(private InventoryLocal InventoryPopulator.createInventory(String, int));
	
	/*** PopulateServlet ***/
	pointcut internalPopulateHandler() : 
		execution(private void PopulateServlet.internalPopulate(String, String, HttpServletRequest, HttpServletResponse));
	
	pointcut internalInventoryPopulatorCheckHandler() : 
		execution(private boolean PopulateServlet.internalInventoryPopulatorCheck(InventoryPopulator));
	
	pointcut internalInventoryPopulatorHandler() : 
		execution(private void PopulateServlet.internalInventoryPopulator(InventoryPopulator, XMLReader));
	
	pointcut populateHandler() : 
		execution(private boolean PopulateServlet.populate(boolean ));	
	
	pointcut getResourceHandler() : 
		execution(private String PopulateServlet.getResource(String));	
	
	/*** XMLDBHandler ***/
	pointcut internalStartElementHandler() : 
		execution(private void XMLDBHandler.internalStartElement());
	
	pointcut internalEndElementHandler() : 
		execution(private void XMLDBHandler.internalEndElement());	
	
	pointcut getValueHandler() : 
		execution(public int XMLDBHandler.getValue(String, int));
	
	// ---------------------------
    // Advice's
    // ---------------------------
	boolean around() : checkHandler() {
		try {
			return proceed();
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	/* GenericAspect
	void around() : 
		internalRemoveExistingInventoryHandler() {		
		try {
			proceed();
		} catch(Exception exception) {
			//TODO: Do nothing!
		}
	}
	*/
	InventoryLocal around() throws PopulateException :
		createInventoryHandler() {
		try{
			return proceed();
		}
		catch(Exception exception){
			throw new PopulateException ("Could not create: " + exception.getMessage(), exception);
		}
	}
	
	Object around()throws PopulateException :
		internalInventoryPopulatorHandler() || 
		populateHandler() {
		try{
			return proceed();
		}		catch(Exception e){
			throw new PopulateException(e);
		}
	}

	void around(PopulateServlet ps, String forcefully, String errorPageURL, HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException : 
		internalPopulateHandler() && 
		args(forcefully, errorPageURL, request, response) && 
		target(ps) {
		try {
			proceed(ps, forcefully, errorPageURL, request, response);
		} catch(PopulateException exception) {
			System.err.println(exception.getRootCause().getMessage());
	        if (errorPageURL == null) {
	          throw new ServletException("Populate exception occured :" + exception.getMessage(), exception.getRootCause());
	        } else {
	          ps.redirect(request, response, errorPageURL);
	        }
		}
	}

	boolean around() : internalInventoryPopulatorCheckHandler() {
		try {
			return proceed();
	    } catch (PopulateException exception) {
	      	return false;
		}  	
	}
	
	String around(PopulateServlet ps, String path) throws IOException : 
		getResourceHandler() &&  
		args(path) && target(ps) {
		try {
			return proceed(ps,path);
	    } catch (MalformedURLException exception) {
	    	URL u = null;
    		u = ps.getServletContext().getResource(path);
	        return u != null ? u.toString() : path;
        }
	}
	
	void around() throws SAXException : 
		internalStartElementHandler() || 
		internalEndElementHandler() {
		try {
			proceed();
		} catch(PopulateException exception) {
			throw new SAXException(exception.getMessage(), exception);
		}
	}
	
	int around(String name, int defaultValue) : 
		getValueHandler() && args(name, defaultValue) {
		try {
			return proceed(name, defaultValue);
		} catch (NumberFormatException exception) {
			return defaultValue;
		}
	}
		
}