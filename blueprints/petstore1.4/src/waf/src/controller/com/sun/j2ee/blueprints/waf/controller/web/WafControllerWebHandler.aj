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
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import com.sun.j2ee.blueprints.util.aspect.ExceptionGenericAspect;
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

	// ---------------------------
    // Declare soft's
    // ---------------------------
//	declare soft : CreateException : defaultComponentManager_getEJBControllerHandler();
	declare soft : ServiceLocatorException : 
		defaultComponentManager_getWebControllerHandler();
	declare soft : MalformedURLException : mainServlet_initGetResourceHandler() || 
		URLMappingsXmlDAO_loadDocumentHandler() ||
		screenFlowManager_flowInitGetResourceHandler();
	declare soft : EventException : mainServlet_internalDoProcessHandler();
	declare soft : HTMLActionException : mainServlet_internalDoProcessHandler();
//	declare soft : RemoveException : defaultWebController_destroyHandler();
	declare soft : SAXParseException : URLMappingsXmlDAO_loadDocumentHandler();
	declare soft : SAXException : URLMappingsXmlDAO_loadDocumentHandler();
	declare soft : IOException : URLMappingsXmlDAO_loadDocumentHandler() || 
		clientStateFlowHandler_internalProcessFlowHandler() ||
		defaultComponentManager_getWebControllerHandler();
	declare soft : ClassNotFoundException : screenFlowManager_internalGetExceptionScreen() || 
		clientStateFlowHandler_internalProcessFlowHandler() ||
		defaultComponentManager_getWebControllerHandler() ||
		requestProcessor_internalGetActionHandler() ||
		screenFlowManager_internalForwardToNextScreenHandler();
	declare soft : OptionalDataException : clientStateFlowHandler_internalProcessFlowHandler();
	declare soft : UnsupportedEncodingException : i18nUtil_convertJISEncodingHandler();
	declare soft : IllegalAccessException : requestProcessor_internalGetActionHandler() ||
		screenFlowManager_internalForwardToNextScreenHandler();
	declare soft : InstantiationException : requestProcessor_internalGetActionHandler() ||
		screenFlowManager_internalForwardToNextScreenHandler();
	declare soft : ParserConfigurationException : URLMappingsXmlDAO_loadDocumentHandler();
	declare soft : FlowHandlerException : mainServlet_internalDoProcessHandler() ||
		screenFlowManager_internalForwardToNextScreenHandler(); 
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** DefaultComponentManager ***/
	pointcut defaultComponentManager_getWebControllerHandler() :  
		execution(public WebController DefaultComponentManager.getWebController(HttpSession));
	
	public pointcut aroundExceptionDoNothingHandler() : 
		execution(public void DefaultComponentManager.sessionDestroyed(HttpSessionEvent));
	
	/*** MainServlet ***/
	pointcut mainServlet_initGetResourceHandler() : 
		call(URL ServletContext.getResource(String)) && 
		withincode(public void MainServlet.init(ServletConfig));
	pointcut mainServlet_internalDoProcessHandler() : 
		execution(private void MainServlet.internalDoProcess(HttpServletRequest, HttpServletResponse, ServletContext));
	
	/*** RequestProcessor ***/
	pointcut requestProcessor_internalGetActionHandler() : 
		execution(private HTMLAction RequestProcessor.internalGetAction(String));
	
	/*** DefaultWebController ***/
//	pointcut defaultWebController_destroyHandler() : 
//		execution(public synchronized void destroy(HttpSession));
	
	/*** URLMappingsXmlDAO ***/
	pointcut URLMappingsXmlDAO_loadDocumentHandler() : 	
		execution(public static Element URLMappingsXmlDAO.loadDocument(String));
	
	/*** com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager ***/
	pointcut screenFlowManager_flowInitGetResourceHandler() : 
		call(URL ServletContext.getResource(String)) && 
		withincode(public void com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.init(ServletContext));
	pointcut screenFlowManager_internalForwardToNextScreenHandler() : 
		execution(private String com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.internalForwardToNextScreen(FlowHandler, String, HttpServletRequest, URLMapping));
	pointcut screenFlowManager_internalGetExceptionScreen() :  
		execution(private Class com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.internalGetExceptionScreen(String));
	
	/*** com.sun.j2ee.blueprints.waf.controller.web.flow.handlers.ClientStateFlowHandler ***/
	pointcut clientStateFlowHandler_internalProcessFlowHandler() :  
		execution(private void com.sun.j2ee.blueprints.waf.controller.web.flow.handlers.ClientStateFlowHandler.internalProcessFlow(HttpServletRequest, String, String, byte[]));
	
	/*** com.sun.j2ee.blueprints.waf.util.I18nUtil ***/
	pointcut i18nUtil_convertJISEncodingHandler() :  
		call(String.new(byte[], String)) && 
		withincode(public static String com.sun.j2ee.blueprints.waf.util.I18nUtil.convertJISEncoding(String));
		
	// ---------------------------
    // Advice's
    // ---------------------------	
	WebController around() throws RuntimeException : defaultComponentManager_getWebControllerHandler(){
		try{
			return proceed();
		}catch(Exception exc){
			throw new RuntimeException ("Cannot create bean of class WebController: " + exc);
		}
	}
//		
//	EJBControllerLocal around() throws GeneralFailureException : defaultComponentManager_getEJBControllerHandler(){
//		try{
//			return proceed();
//		}catch(CreateException ce){
//			throw new GeneralFailureException(ce.getMessage());
//		}catch(ServiceLocatorException slx){
//			throw new GeneralFailureException(slx.getMessage());
//		}
//	}
	

	/*
	void around() : sessionDestroyedHandler() {
		try {
			proceed();
	     } catch(Exception exe){
	     	// ignore the exception
	     }
	}
	*/
	
	void around(MainServlet ms,HttpServletRequest request, HttpServletResponse response, ServletContext context) 
	throws IOException, ServletException : 
		mainServlet_internalDoProcessHandler() && 
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
//	
//    void around() :  defaultWebController_destroyHandler() {
//	    try {
//	        proceed();
//	    } catch(RemoveException re){
//	        // ignore, after all its only a remove() call!
//	        Debug.print(re);
//	    }
//    }

    Element around() : URLMappingsXmlDAO_loadDocumentHandler() { 
    	try {
    		return proceed();
        } catch (SAXParseException err) {
            System.err.println ("URLMappingsXmlDAO ** Parsing error" + ", line " +
                        err.getLineNumber () + ", uri " + err.getSystemId ());            
            System.err.println("URLMappingsXmlDAO error: " + err.getMessage ());
            return null;
        }  catch (Exception pce) {
            System.err.println("URLMappingsXmlDAO error: " + pce);
            return null;
        }        
    }
    
	URL around() : screenFlowManager_flowInitGetResourceHandler() {
		try {
			return proceed();
		} catch(MalformedURLException ex) {
			System.err.println("ScreenFlowManager: initializing ScreenFlowManager malformed URL exception: " + ex);
			return null;
		}
	}
    
	URL around() : mainServlet_initGetResourceHandler() {
		try {
			return proceed();
		} catch(MalformedURLException ex) {
			System.err.println("MainServlet: initializing ScreenFlowManager malformed URL exception: " + ex);
			return null;
		}
	}
	
	String around() : screenFlowManager_internalForwardToNextScreenHandler() {
		try {
			return proceed();			
	    } catch (Exception ex) {
	    	System.err.println("ScreenFlowManager caught loading handler: " + ex);
	        return null;
	    }
	}
	
	HTMLAction around() : requestProcessor_internalGetActionHandler() {
        try {
            return proceed();
        } catch (Exception ex) {
            System.err.println("RequestProcessor caught loading action: " + ex);
            return null;
        }
    }
	
	Class around(String exceptionName) : screenFlowManager_internalGetExceptionScreen() && args(exceptionName) {
		try {
			return proceed(exceptionName);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ScreenFlowManager: Could not load exception " + exceptionName);
            return null;
        }    	
	}
	
	void around() : clientStateFlowHandler_internalProcessFlowHandler() {
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
	
	String around() : i18nUtil_convertJISEncodingHandler() {
		try {
			return proceed();
		} catch (UnsupportedEncodingException uex) {
			return null;
		}
	}
}
