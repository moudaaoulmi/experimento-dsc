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
import com.sun.j2ee.blueprints.util.aspect.AspectUtil;
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

public aspect WafControllerWebHandler extends ExceptionGenericAspect {

	pointcut getWebControllerHandler() :  
		execution(public WebController DefaultComponentManager.getWebController(HttpSession));
	pointcut getEJBControllerHandler() : 
		execution(public EJBControllerLocal DefaultComponentManager.getEJBController(HttpSession));
	public pointcut aroundExceptionDoNothingHandler() : 
		execution(public void DefaultComponentManager.sessionDestroyed(HttpSessionEvent));
	pointcut initGetResourceHandler() : 
		call(URL ServletContext.getResource(String)) && 
		withincode(public void MainServlet.init(ServletConfig));
	pointcut internalDoProcessHandler() : 
		execution(private void MainServlet.internalDoProcess(HttpServletRequest, HttpServletResponse, ServletContext));
	pointcut internalGetActionHandler() : 
		execution(private HTMLAction RequestProcessor.internalGetAction(String));
	pointcut destroyHandler() : 
		execution(public synchronized void destroy(HttpSession));
	pointcut loadDocumentHandler() : 	
		execution(public static Element URLMappingsXmlDAO.loadDocument(String));
	pointcut flowInitGetResourceHandler() : 
		call(URL ServletContext.getResource(String)) && 
		withincode(public void com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.init(ServletContext));
	pointcut internalForwardToNextScreenHandler() : 
		execution(private String com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.internalForwardToNextScreen(FlowHandler, String, HttpServletRequest, URLMapping));
	pointcut internalGetExceptionScreen() :  
		execution(private Class com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.internalGetExceptionScreen(String));
	pointcut internalProcessFlowHandler() :  
		execution(private void com.sun.j2ee.blueprints.waf.controller.web.flow.handlers.ClientStateFlowHandler.internalProcessFlow(HttpServletRequest, String, String, byte[]));
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
	WebController around()throws RuntimeException : 
		getWebControllerHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			throw new RuntimeException ("Cannot create bean of class WebController: " + e);
		}		
	}	
	EJBControllerLocal around() throws GeneralFailureException :   
		getEJBControllerHandler() {
		try{
			return proceed();
		}catch(RuntimeException e){
			throw e;
		}catch(Exception e){			
			throw new GeneralFailureException(e.getMessage());
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
    	} catch (RuntimeException e){
    		throw e;
        } catch (Exception err) {        	
        	if (err instanceof SAXParseException) {
        		SAXParseException e = (SAXParseException) err;
        		System.err.println ("URLMappingsXmlDAO ** Parsing error" + ", line " +
                        e.getLineNumber () + ", uri " + e.getSystemId ());    				
			}       
            System.err.println("URLMappingsXmlDAO error: " + err.getMessage ());
            return null;
        }        
    }	
    Object around() : 
		internalForwardToNextScreenHandler()||
		flowInitGetResourceHandler()||
		internalGetActionHandler()||
		initGetResourceHandler() {
		try {
			return proceed();
		} catch(RuntimeException e){
			throw e;
	    } catch (Exception ex) {
	    	String str = null;	    	
	    	if(AspectUtil.verifyJointPoint(thisJoinPoint,"private String com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.internalForwardToNextScreen(FlowHandler, String, HttpServletRequest, URLMapping)")){
	    		str = "ScreenFlowManager caught loading handler: ";
	    	}else if(AspectUtil.verifyJointPoint(thisJoinPoint,"public void com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.init(ServletContext)")){
	    		str = "ScreenFlowManager: initializing ScreenFlowManager malformed URL exception: ";
	    	}else if(AspectUtil.verifyJointPoint(thisJoinPoint,"public void com.sun.j2ee.blueprints.waf.controller.web.flow.ScreenFlowManager.init(ServletContext)")){
	    		str = "RequestProcessor caught loading action: ";
	    	}else{
	    		str = "MainServlet: initializing ScreenFlowManager malformed URL exception: ";
	    	}	    	
	    	System.err.println(str + ex);
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
		} catch (RuntimeException e){
			throw e;
        } catch (Exception ode) {        	
            System.err.println("ClientCacheLinkFlowHandler caught: " + ode);
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
