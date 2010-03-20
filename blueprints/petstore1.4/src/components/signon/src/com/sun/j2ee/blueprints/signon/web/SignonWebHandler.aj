/*
 * Created on 28/09/2005
 */
package com.sun.j2ee.blueprints.signon.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import petstore.exception.ExceptionHandler;

import com.sun.j2ee.blueprints.signon.ejb.SignOnLocal;
/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect SignonWebHandler {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : CreateException : createUserServlet_doPostHandler() ||
		createUserServlet_getSignOnEjbHandler() || 
		signOnFilter_getSignOnFilterEjbHandler();
	declare soft : NamingException : createUserServlet_getSignOnEjbHandler() || 
		signOnFilter_getSignOnFilterEjbHandler();
	declare soft : SAXParseException : signOnDAO_loadDocumentHandler();
	declare soft : SAXException : signOnDAO_loadDocumentHandler();
	declare soft : MalformedURLException : signOnDAO_loadDocumentHandler() ||
		signOnFilter_initHandler();
	declare soft : IOException : signOnDAO_loadDocumentHandler();
	declare soft : ParserConfigurationException : signOnDAO_loadDocumentHandler();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** CreateUserServlet ***/
	pointcut createUserServlet_doPostHandler() : 
		execution(public  void CreateUserServlet.doPost(HttpServletRequest, HttpServletResponse));
	pointcut createUserServlet_getSignOnEjbHandler() : 
		execution(private SignOnLocal CreateUserServlet.getSignOnEjb());
	
	/*** SignOnDAO ***/
	pointcut signOnDAO_loadDocumentHandler() : 
		execution(private  Element SignOnDAO.loadDocument(URL));
	
	/*** SignOnFilter ***/
	pointcut signOnFilter_initHandler() : 
		execution(public void SignOnFilter.init(FilterConfig));
	pointcut signOnFilter_getSignOnFilterEjbHandler() : 
		execution(private SignOnLocal SignOnFilter.getSignOnEjb());
	
	// ---------------------------
    // Advice's
    // ---------------------------		
	void around(HttpServletRequest request, HttpServletResponse  response) throws IOException : 
		createUserServlet_doPostHandler() && args(request,response) {
		try {
			proceed(request,response);
		} catch (CreateException ce) {
			System.out.println("CreateUserServlet:: redirecting to user creation error error url"  );			
			response.sendRedirect("user_creation_error.jsp");
		}
	}

	Element around() : signOnDAO_loadDocumentHandler() {
		try {
			return proceed();
        } catch (SAXParseException err) {
            System.err.println ("SignOnDAO ** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.err.println("SignOnDAO error: " + err.getMessage ());
            return null;
        } catch (Exception pce) {
            System.err.println("SignOnDAO error: " + pce);
            return null;
        }
	}
	
	void around() : signOnFilter_initHandler() {
		try {
			proceed();
		} catch (MalformedURLException ex) {
			System.err.println("SignonFilter: malformed URL exception: " + ex);
		}
	}

	SignOnLocal around() throws ServletException : 
		signOnFilter_getSignOnFilterEjbHandler() || createUserServlet_getSignOnEjbHandler(){
		try{
			return proceed();
		}catch(CreateException cx){
			throw new ServletException("Failed to Create SignOn EJB: caught " + cx);
		}catch(NamingException nx){
			throw new ServletException("Failed to Create SignOn EJB: caught " + nx);
		}		
	}
		
}
