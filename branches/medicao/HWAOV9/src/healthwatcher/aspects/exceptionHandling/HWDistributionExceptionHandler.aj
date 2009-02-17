package healthwatcher.aspects.exceptionHandling;

import healthwatcher.view.servlets.HWServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.exceptions.CommunicationException;
import lib.util.HTMLCode;

import org.aspectj.lang.SoftException;

/**
 * This aspect handles transaction exceptions in the servlets
 */
public aspect HWDistributionExceptionHandler {

	// Makes soft all IO exceptions raised in this aspect
	declare soft : IOException : within(HWDistributionExceptionHandler+);
	
	void around(HttpServletResponse response) : 
		execution(* HWServlet+.do*(HttpServletRequest, HttpServletResponse)) &&
		args(response) {
		
		PrintWriter out = response.getWriter();
		try {
			
			proceed(response);
			
		} catch (SoftException e) {
			if (e.getWrappedThrowable() instanceof CommunicationException) {
				out.println(HTMLCode.errorPage(e.getWrappedThrowable().getMessage()));
				out.close();
			} else if (e.getWrappedThrowable() instanceof RemoteException) {
				out.println(HTMLCode.errorPage("Comunication error, please try again later."));
				out.close();	
			} else {
				throw e;
	        }
		}
	}
}
