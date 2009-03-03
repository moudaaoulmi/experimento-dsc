package com.sun.j2ee.blueprints.signon.web;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterConfig;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.sun.j2ee.blueprints.signon.ejb.SignOnLocal;

public aspect SignonWebHandler {
	
	pointcut doPostHandler() : 
		execution(public  void CreateUserServlet.doPost(HttpServletRequest, HttpServletResponse));
	pointcut getSignOnEjbHandler() : 
		execution(private SignOnLocal CreateUserServlet.getSignOnEjb());	
	pointcut loadDocumentHandler() : 
		execution(private  Element SignOnDAO.loadDocument(URL));	
	pointcut initHandler() : 
		execution(public void SignOnFilter.init(FilterConfig));
	pointcut getSignOnFilterEjbHandler() : 
		execution(private SignOnLocal SignOnFilter.getSignOnEjb());	
	declare soft : CreateException : doPostHandler() ||
		getSignOnEjbHandler() || 
		getSignOnFilterEjbHandler();
	declare soft : NamingException : getSignOnEjbHandler() || 
		getSignOnFilterEjbHandler();
	declare soft : SAXParseException : loadDocumentHandler();
	declare soft : SAXException : loadDocumentHandler();
	declare soft : MalformedURLException : loadDocumentHandler() ||
		initHandler();
	declare soft : IOException : loadDocumentHandler();
	declare soft : ParserConfigurationException : loadDocumentHandler();		
	void around(HttpServletRequest request, HttpServletResponse  response) throws IOException : 
		doPostHandler() && args(request,response) {
		try {
			proceed(request,response);
		} catch (CreateException ce) {
			System.out.println("CreateUserServlet:: redirecting to user creation error error url"  );			
			response.sendRedirect("user_creation_error.jsp");
		}
	}
	Element around() : loadDocumentHandler() {
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
	void around() : initHandler() {
		try {
			proceed();
		} catch (MalformedURLException ex) {
			System.err.println("SignonFilter: malformed URL exception: " + ex);
		}
	}
	Object around() throws ServletException : getSignOnFilterEjbHandler() || getSignOnEjbHandler(){
		try{
			return proceed();
		}catch(RuntimeException e){	
			throw e;
		}catch(Exception e){			
			throw new ServletException("Failed to Create SignOn EJB: caught " + e);
		}
	}
}