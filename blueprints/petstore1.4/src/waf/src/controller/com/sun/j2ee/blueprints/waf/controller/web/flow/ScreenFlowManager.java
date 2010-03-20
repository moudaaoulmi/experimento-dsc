/*
 * Copyright 2004-2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package com.sun.j2ee.blueprints.waf.controller.web.flow;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.j2ee.blueprints.waf.controller.web.URLMapping;
import com.sun.j2ee.blueprints.waf.controller.web.URLMappingsXmlDAO;
import com.sun.j2ee.blueprints.waf.controller.web.util.WebKeys;

/**
 * This file looks at the Request URL and maps the request
 * to the page for the web-templating mechanism.
 */
public class ScreenFlowManager implements java.io.Serializable {

    private HashMap screens;
    private HashMap urlMappings;
    private HashMap exceptionMappings;
    private HashMap screenDefinitionMappings;
    private String defaultScreen = "";
    private ServletContext context;

    public ScreenFlowManager() {
        screens = new HashMap();
    }

    /**
     * EH - Refactored to aspect WafControllerWebHandler.
     */                                                         
    public void init(ServletContext context) {
        this.context = context;
        String requestMappingsURL = null;
        requestMappingsURL = context.getResource("/WEB-INF/mappings.xml").toString();
        urlMappings = (HashMap)context.getAttribute(WebKeys.URL_MAPPINGS);
        ScreenFlowData screenFlowData = URLMappingsXmlDAO.loadScreenFlowData(requestMappingsURL);
        defaultScreen = screenFlowData.getDefaultScreen();
        exceptionMappings = screenFlowData.getExceptionMappings();
    }

    /**
     * The UrlMapping object contains information that will match
     * a url to a mapping object that contains information about
     * the current screen, the WebAction that is needed to
     * process a request, and the WebAction that is needed
     * to insure that the propper screen is displayed.
    */

    private URLMapping getURLMapping(String urlPattern) {
        if ((urlMappings != null) && urlMappings.containsKey(urlPattern)) {
            return (URLMapping)urlMappings.get(urlPattern);
        } else {
            return null;
        }
    }

    public String getExceptionScreen(String exceptionClassName) {
        return (String)exceptionMappings.get(exceptionClassName);
    }


    /**
     * Using the information we have in the request along with
     * The url map for the current url we will insure that the
     * propper current screen is selected based on the settings
     * in both the screendefinitions.xml file and requestmappings.xml
     * files.
     * 
     * EH - Refactored to aspect WafControllerWebHandler.
    */   
    public void forwardToNextScreen(HttpServletRequest request, HttpServletResponse response)
              throws java.io.IOException, FlowHandlerException, javax.servlet.ServletException {
        // set the presious screen
        String fullURL = request.getRequestURI();
        // get the screen name
        String selectedURL = defaultScreen;
        int lastPathSeparator = fullURL.lastIndexOf("/") + 1;
        if (lastPathSeparator != -1) {
            selectedURL = fullURL.substring(lastPathSeparator, fullURL.length());
        }
        String currentScreen = "";
        URLMapping urlMapping = getURLMapping(selectedURL);
        if (urlMapping != null) {
            if (!urlMapping.useFlowHandler()) {
                currentScreen = urlMapping.getScreen();
            } else {
                // load the flow handler
                FlowHandler handler = null;
                String flowHandlerString = urlMapping.getFlowHandler();
                currentScreen = internalForwardToNextScreen(handler, flowHandlerString, request, urlMapping);
            }
        }
        if (currentScreen == null) {
            System.err.println("ScreenFlowManager: Screen not found for " + selectedURL);
            throw new RuntimeException("Screen not found for " + selectedURL);
        }
        context.getRequestDispatcher("/" + currentScreen).forward(request, response);

    }
    
    /**
     * Created during EH - Refactoring to aspect WafControllerWebHandler.
     */
    private String internalForwardToNextScreen(FlowHandler handler, String flowHandlerString, HttpServletRequest request, URLMapping urlMapping) {
    	String currentScreen = "";
        handler = (FlowHandler)getClass().getClassLoader().loadClass(flowHandlerString).newInstance();
        // invoke the processFlow(HttpServletRequest)
        handler.doStart(request);
        String flowResult = handler.processFlow(request);
        handler.doEnd(request);
        currentScreen = urlMapping.getResultScreen(flowResult);
        // if there were no screens by the id then assume that the result was
        //the screen itself
        if (currentScreen == null) currentScreen = flowResult;
        return currentScreen;
    	
    }
    
    /**
            go through the list and use the Class.isAssignableFrom(Class method)
            to see it is a subclass of one of the exceptions
            
            EH - Refactoring to aspect WafControllerWebHandler.
    */
    public String getExceptionScreen(Throwable e) {
        Iterator it = exceptionMappings.keySet().iterator();
        while (it.hasNext()) {
            String exceptionName = (String)it.next();
            Class targetExceptionClass = null;            
            System.err.println("Checking exception: " + exceptionName + " against " + e.getClass().getName());
            targetExceptionClass = internalGetExceptionScreen(exceptionName);
            System.err.println("Checking targetExceptionClass: " + targetExceptionClass);
            // check if the exception is a sub class of matches the exception
            if ((targetExceptionClass != null) &&
                targetExceptionClass.isAssignableFrom(e.getClass())) {
                System.err.println("ScreenFlowManager: found exception for " + exceptionName + " matches");;
                return "/" + (String)exceptionMappings.get(exceptionName);
            }
        }
        return null;
    }
    
    /**
     * Created during EH - Refactoring to aspect WafControllerWebHandler.
     */    
    private Class internalGetExceptionScreen(String exceptionName) {
    	return this.getClass().getClassLoader().loadClass(exceptionName);
    }
	

    public void setDefaultScreen(String defaultScreen) {
        this.defaultScreen = defaultScreen;
    }

    /**
     * Returs the current screen
     */

    public String getCurrentScreen(HttpSession session)  {
        return (String)session.getAttribute(WebKeys.CURRENT_SCREEN);
    }
}


