/*
 * Created on 01/10/2005
 */
package com.sun.j2ee.blueprints.waf.view.template;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspTagException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.j2ee.blueprints.util.aspect.ExceptionGenericAspect;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect WafViewTemplateHandler extends ExceptionGenericAspect {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : SAXParseException : screenDefinitionDAO_loadDocument();
	declare soft : SAXException : screenDefinitionDAO_loadDocument();
	declare soft : MalformedURLException : screenDefinitionDAO_loadDocument() ||
		templateServlet_initScreensGetResourceHandler();
	declare soft : IOException : screenDefinitionDAO_loadDocument() || 
		aroundExceptionDoNothingHandler() ||
		insertTag_doEndTagHandler();
	declare soft : ParserConfigurationException : screenDefinitionDAO_loadDocument();
	declare soft : ServletException : insertTag_doEndTagHandler();
	declare soft : NamingException : templateServlet_internalGetUserTransactionHandler();
	declare soft : NotSupportedException : templateServlet_internalGetUserTransactionHandler();
	declare soft : SystemException : templateServlet_internalGetUserTransactionHandler();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** ScreenDefinitionDAO ***/
	pointcut screenDefinitionDAO_loadDocument() :  
		execution(public static Element ScreenDefinitionDAO.loadDocument(URL));
	
	/*** TemplateServlet ***/
	pointcut templateServlet_initScreensGetResourceHandler() : 
		call(URL ServletContext.getResource(String)) && 
		withincode(private void TemplateServlet.initScreens(ServletContext, String));
	pointcut templateServlet_internalGetUserTransactionHandler() : 
		execution(* TemplateServlet.internalGetUserTransaction());		
	pointcut templateServlet_internalGetRequestDispatcherHandler() : 
		execution(private void TemplateServlet.internalGetRequestDispatcher(..));
		
	
	/*** com.sun.j2ee.blueprints.waf.view.template.tags.InsertTag ***/
	public pointcut aroundExceptionDoNothingHandler() :   
		execution(private void com.sun.j2ee.blueprints.waf.view.template.tags.InsertTag.internalDoStartTag1());
	pointcut insertTag_internalDoStartTag2Handler() :  
		execution(private void com.sun.j2ee.blueprints.waf.view.template.tags.InsertTag.internalDoStartTag2());
	pointcut insertTag_doEndTagHandler() : 
		execution(public int com.sun.j2ee.blueprints.waf.view.template.tags.InsertTag.doEndTag());

	// ---------------------------
    // Advice's
    // ---------------------------	
	Element around() : screenDefinitionDAO_loadDocument() {
		try {
			return proceed();
        } catch (SAXParseException err) {
            System.err.println ("ScreenFlowXmlDAO ** Parsing error" + ", line " +
                        err.getLineNumber () + ", uri " + err.getSystemId ());
            System.err.println("ScreenFlowXmlDAO error: " + err.getMessage ());
            return null;
        } catch (Exception pce) {
            System.err.println("ScreenFlowXmlDAO error: " + pce);
            return null;
        }
	}
	
	URL around() : templateServlet_initScreensGetResourceHandler() {
		try {
			return proceed();
        } catch (MalformedURLException ex) {
            System.err.println("TemplateServlet: malformed URL exception: " + ex);
            return null;
        }
	}
			
	Object[] around() : templateServlet_internalGetUserTransactionHandler() {
		Object[] values = new Object[2];
        values[0] = Boolean.FALSE;
        values[1] = null;		
        try{
            values = proceed();
        } catch (NamingException ne) {
            // it should not have happened, but it is a recoverable error.
            // Just dont start the transaction.
            ne.printStackTrace();
	    } catch (NotSupportedException nse) {
            // Again this is a recoverable error.
            nse.printStackTrace();
	    } catch (SystemException se) {
            // Again this is a recoverable error.
            se.printStackTrace();
	    }
	    return values;
    }

	void around(boolean tx_started,UserTransaction ut) : 
		templateServlet_internalGetRequestDispatcherHandler() && 
		args(..,tx_started,ut) {
        try{
            proceed(tx_started,ut);
        } finally {
            // commit the transaction if it was started earlier successfully.
            try {
                if (tx_started && ut != null) {
                    ut.commit();
                }
            } catch (IllegalStateException re) {
                re.printStackTrace();
            } catch (RollbackException re) {
                re.printStackTrace();
            } catch (HeuristicMixedException hme) {
                hme.printStackTrace();
            } catch (HeuristicRollbackException hre) {
                hre.printStackTrace();
            } catch (SystemException se) {
                se.printStackTrace();
            }
        }
    }
	
	void around() throws JspTagException : insertTag_internalDoStartTag2Handler(){
		try {
			proceed();
		} catch (NullPointerException e){
            throw new JspTagException("Error extracting Screen from session: " + e);
        }
	}
	
	int around() : insertTag_doEndTagHandler() {
		try {
			return proceed();
        } catch (Exception ex) {
            System.err.println("InsertTag:doEndTag caught: " + ex);
            ex.printStackTrace();
            return com.sun.j2ee.blueprints.waf.view.template.tags.InsertTag.EVAL_PAGE;
       }       
	}
	
}
