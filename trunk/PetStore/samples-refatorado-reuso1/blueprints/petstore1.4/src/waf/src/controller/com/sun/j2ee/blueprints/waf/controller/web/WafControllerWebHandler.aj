/*
 * Created on 01/10/2005
 */
package com.sun.j2ee.blueprints.waf.controller.web;

import java.io.IOException;
import java.io.OptionalDataException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSessionEvent;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import com.sun.j2ee.blueprints.util.aspect.ExceptionGenericAspect;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.controller.ejb.EJBControllerLocal;
import com.sun.j2ee.blueprints.waf.event.EventException;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.waf.controller.web.action.HTMLAction;
import com.sun.j2ee.blueprints.waf.controller.web.action.HTMLActionException;
import com.sun.j2ee.blueprints.waf.controller.web.flow.FlowHandler;
import com.sun.j2ee.blueprints.waf.controller.web.flow.FlowHandlerException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Raquel Maranhao
 */
public aspect WafControllerWebHandler extends ExceptionGenericAspect {

	/*** DefaultComponentManager ***/
	pointcut getWebControllerHandler() :  
		execution(public WebController DefaultComponentManager.getWebController(HttpSession));
	pointcut getEJBControllerHandler() : 
		execution(public EJBControllerLocal DefaultComponentManager.getEJBController(HttpSession));
	
	public pointcut aroundExceptionDoNothingHandler() : 
		execution(public void DefaultComponentManager.sessionDestroyed(HttpSessionEvent));
	
	/*** MainServlet ***/
	pointcut initGetResourceHandler() : 
		call(URL ServletContext.getResource(String)) && 
		withincode(public void MainServlet.init(ServletConfig));
	pointcut internalDoProcessHandler() : 
		execution(private void MainServlet.internalDoProcess(HttpServletRequest, HttpServletResponse, ServletContext));
	
	/*** RequestProcessor ***/
	pointcut internalGetActionHandler() : 
		execution(private HTMLAction RequestProcessor.internalGetAction(String));
	
	/*** DefaultWebController ***/
	pointcut destroyHandler() : 
		execution(public synchronized void destroy(HttpSession));
	
	/*** URLMappingsXmlDAO ***/
	pointcut loadDocumentHandler() : 	
		execution(public static Element URLMappingsXmlDAO.loadDocument(String));
	
	/*** com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager ***/
	pointcut flowInitGetResourceHandler() : 
		call(URL ServletContext.getResource(String)) && 
		withincode(public void com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.init(ServletContext));
	pointcut internalForwardToNextScreenHandler() : 
		execution(private String com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.internalForwardToNextScreen(FlowHandler, String, HttpServletRequest, URLMapping));
	pointcut internalGetExceptionScreen() :  
		execution(private Class com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.internalGetExceptionScreen(String));
	
	/*** com.sun.j2ee.blueprints.waf.controller.web.flow.handlers.ClientStateFlowHandler ***/
	pointcut internalProcessFlowHandler() :  
		execution(private void com.sun.j2ee.blueprints.waf.controller.web.flow.handlers.ClientStateFlowHandler.internalProcessFlow(HttpServletRequest, String, String, byte[]));
	
	/*** com.sun.j2ee.blueprints.waf.util.I18nUtil ***/
	pointcut convertJISEncodingHandler() :  
		call(String.new(byte[], String)) && 
		withincode(public static String com.sun.j2ee.blueprints.waf.util.I18nUtil.convertJISEncoding(String));
		
	
	
	
	declare soft : CreateException : getEJBControllerHandler();
	declare soft : ServiceLocatorException : getEJBControllerHandler() || 
		getWebControllerHandler();
	declare soft : MalformedURLException : initGetResourceHandler() || 
		loadDocumentHandler() ||
		flowInitGetResourceHandler();
	declare soft : EventException : internalDoProcessHandler();
	declare soft : HTMLActionException : internalDoProcessHandler();
	declare soft : RemoveException : destroyHandler();
	declare soft : SAXParseException : loadDocumentHandler();
	declare soft : SAXException : loadDocumentHandler();
	declare soft : IOException : loadDocumentHandler() || 
		internalProcessFlowHandler() ||
		getWebControllerHandler();
	declare soft : ClassNotFoundException : internalGetExceptionScreen() || 
		internalProcessFlowHandler() ||
		getWebControllerHandler() ||
		internalGetActionHandler() ||
		internalForwardToNextScreenHandler();
	declare soft : OptionalDataException : internalProcessFlowHandler();
	declare soft : UnsupportedEncodingException : convertJISEncodingHandler();
	declare soft : IllegalAccessException : internalGetActionHandler() ||
		internalForwardToNextScreenHandler();
	declare soft : InstantiationException : internalGetActionHandler() ||
		internalForwardToNextScreenHandler();
	declare soft : ParserConfigurationException : loadDocumentHandler();
	declare soft : FlowHandlerException : internalDoProcessHandler() ||
		internalForwardToNextScreenHandler(); 
		
	
	
	
	
	after() throwing(Exception exc) throws RuntimeException : 
		getWebControllerHandler() {
		throw new RuntimeException ("Cannot create bean of class WebController: " + exc);
	}
	
	after() throwing(CreateException ce) throws GeneralFailureException :   
		getEJBControllerHandler() {
		throw new GeneralFailureException(ce.getMessage());
	}

	after() throwing(ServiceLocatorException slx) throws GeneralFailureException :   
		getEJBControllerHandler() {
		throw new GeneralFailureException(slx.getMessage());
	}

	/*
	void around() : sessionDestroyedHandler() {
		try {
			proceed();
	     } catch(Exception exe){
	     	// ignore the exception
	     }
	}
	*/
	
	URL around() : initGetResourceHandler() {
		try {
			return proceed();
		} catch(MalformedURLException ex) {
			System.err.println("MainServlet: initializing ScreenFlowManager malformed URL exception: " + ex);
			return null;
		}
	}
	
	void around(MainServlet ms,HttpServletRequest request, HttpServletResponse response, ServletContext context) 
	throws IOException, ServletException : 
		internalDoProcessHandler() && 
		args(request, response, context) && 
		target(ms) {   
		try {
			proceed(ms, request, response, context);
		} catch(Throwable ex) {
	        String className = ex.getClass().getName();
	        String nextScreen = ms.getScreenFlowManager().getExceptionScreen(ex);
	        // put the exception in the request
	        request.setAttribute("javax.servlet.jsp.jspException", ex);
	        if (nextScreen == null) {
	            // send to general error screen
	            ex.printStackTrace();
	            throw new ServletException("MainServlet: unknown exception: " + className);
	        }
	        context.getRequestDispatcher(nextScreen).forward(request, response);
		}
	}
	
    HTMLAction around() : internalGetActionHandler() {
        try {
            return proceed();
        } catch (Exception ex) {
            System.err.println("RequestProcessor caught loading action: " + ex);
            return null;
        }
    }
	
    void around() :  destroyHandler() {
	    try {
	        proceed();
	    } catch(RemoveException re){
	        // ignore, after all its only a remove() call!
	        Debug.print(re);
	    }
    }

    Element around() : loadDocumentHandler() { 
    	try {
    		return proceed();
        } catch (SAXParseException err) {
            System.err.println ("URLMappingsXmlDAO ** Parsing error" + ", line " +
                        err.getLineNumber () + ", uri " + err.getSystemId ());            
            System.err.println("URLMappingsXmlDAO error: " + err.getMessage ());
            return null;
        } catch (SAXException e) {
            System.err.println("URLMappingsXmlDAO error: " + e);
            return null;
        } catch (MalformedURLException mfx) {
            System.err.println("URLMappingsXmlDAO error: " + mfx);
            return null;
        } catch (IOException e) {
            System.err.println("URLMappingsXmlDAO error: " + e);
            return null;
        } catch (Exception pce) {
            System.err.println("URLMappingsXmlDAO error: " + pce);
            return null;
        }        
    }
    
	URL around() : flowInitGetResourceHandler() {
		try {
			return proceed();
		} catch(MalformedURLException ex) {
			System.err.println("ScreenFlowManager: initializing ScreenFlowManager malformed URL exception: " + ex);
			return null;
		}
	}
    
	String around() : internalForwardToNextScreenHandler() {
		try {
			return proceed();			
	    } catch (Exception ex) {
	    	System.err.println("ScreenFlowManager caught loading handler: " + ex);
	        return null;
	    }
	}
	
	Class around(String exceptionName) : internalGetExceptionScreen() && args(exceptionName) {
		try {
			return proceed(exceptionName);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ScreenFlowManager: Could not load exception " + exceptionName);
            return null;
        }    	
	}
	
	void around() : internalProcessFlowHandler() {
		try {
			proceed();
        } catch (OptionalDataException ode) {
            System.err.println("ClientCacheLinkFlowHandler caught: " + ode);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ClientCacheLinkFlowHandler caught: " + cnfe);
        } catch (IOException iox) {
            System.err.println("ClientCacheLinkFlowHandler caught: " + iox);
        }
	}
	
	String around() : convertJISEncodingHandler() {
		try {
			return proceed();
		} catch (UnsupportedEncodingException uex) {
			return null;
		}
	}
}
