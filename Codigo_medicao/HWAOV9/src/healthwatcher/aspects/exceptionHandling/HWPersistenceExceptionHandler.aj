package healthwatcher.aspects.exceptionHandling;

import healthwatcher.view.servlets.HWServlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.exceptions.RepositoryException;
import lib.util.HTMLCode;

import org.aspectj.lang.SoftException;

/**
 * This aspect handles repository exceptions in the servlets
 */
public aspect HWPersistenceExceptionHandler {

	// Makes soft all IO exceptions raised in this aspect
	declare soft : IOException : within(HWPersistenceExceptionHandler+);
	
	void around(HttpServletResponse response) : 
		execution(* HWServlet+.do*(HttpServletRequest, HttpServletResponse)) &&
		args(.., response) {
		
		try {
			
			proceed(response);
			
        } catch (SoftException e) {
        	if (e.getWrappedThrowable() instanceof RepositoryException) {
	        	PrintWriter out = response.getWriter();
	        	out.println(HTMLCode.errorPage(e.getWrappedThrowable().getMessage()));
	            out.close();
        	} else {
        		throw e;
        	}
		}
	}
}
