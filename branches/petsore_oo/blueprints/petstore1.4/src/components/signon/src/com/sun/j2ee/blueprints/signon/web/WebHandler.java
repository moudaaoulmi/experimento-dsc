package com.sun.j2ee.blueprints.signon.web;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler


public class WebHandler extends GeneralException{

	public void initHandler(MalformedURLException ex){
		System.err.println("SignonFilter: malformed URL exception: " + ex);
	}

	public void getSignOnEjbHandler(Exception cx) throws ServletException {
		throw new ServletException("Failed to Create SignOn EJB: caught " + cx);
	}

	public void loadDocument1Handler(SAXParseException err){
		System.err.println ("SignOnDAO ** Parsing error" + ", line " +
				err.getLineNumber () + ", uri " + err.getSystemId ());
		System.err.println("SignOnDAO error: " + err.getMessage ());
	}

	public void loadDocument2Handler(Exception e){
		System.err.println("SignOnDAO error: " + e);
	}

	public void doPostHandler(CreateException ce, HttpServletResponse  response) throws IOException {
		System.out.println("CreateUserServlet:: redirecting to user creation error error url"  );
		response.sendRedirect("user_creation_error.jsp");
	}
}
