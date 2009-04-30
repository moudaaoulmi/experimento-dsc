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


package com.sun.j2ee.blueprints.waf.view.template;

import java.io.IOException;
import java.net.URL;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Enumeration;

// J2EE imports
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.sun.j2ee.blueprints.waf.controller.web.util.WebKeys;

public class TemplateServlet extends HttpServlet {

    private HashMap allScreens;
    private ServletConfig config;
    private ServletContext context;
    private String defaultLocale;
    private boolean cachePreviousScreenAttributes = false;
    private boolean cachePreviousScreenParameters = false;
    private static final String PREVIOUS_SCREEN = "PREVIOUS";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.config = config;
        this.context = config.getServletContext();
        // enable the caching of previous screen attributes
        String cachePreviousScreenAttributesString = config.getInitParameter("cache_previous_screen_attributes");
        if (cachePreviousScreenAttributesString != null) {
            if (cachePreviousScreenAttributesString.toLowerCase().equals("true")) {
                System.out.println("TemplateServlet: Enabled caching of previous screen attributes.");
                cachePreviousScreenAttributes = true;
            }
        }
        // enable the caching of previous screen parameters
        String cachePreviousScreenParametersString = config.getInitParameter("cache_previous_screen_parameters");
        if (cachePreviousScreenParametersString != null) {
            if (cachePreviousScreenParametersString.toLowerCase().equals("true")) {
                System.out.println("TemplateServlet: Enabled caching of previous screen parameters.");
                cachePreviousScreenParameters = true;
            }
        }
        allScreens = new HashMap();
        defaultLocale = config.getInitParameter("default_locale");
        if (defaultLocale == null) {
            defaultLocale = (Locale.getDefault()).toString();
        }
        String locales = config.getInitParameter("locales");
        if (locales != null) {
            StringTokenizer strTok = new StringTokenizer(locales,",");
            while (strTok.hasMoreTokens()) {
                initScreens(config.getServletContext(), strTok.nextToken());
            }
        }
    }

    /**
     * EH - Refactored to aspect WafViewTemplateHandler.
     */    
    private void initScreens(ServletContext context, String language) {
        URL screenDefinitionURL = null;
        screenDefinitionURL = context.getResource("/WEB-INF/screendefinitions_" + language + ".xml");
        if (screenDefinitionURL != null) {
            Screens screenDefinitions = ScreenDefinitionDAO.loadScreenDefinitions(screenDefinitionURL);
            if (screenDefinitions != null) {
                allScreens.put(language, screenDefinitions);
            } else {
                System.err.println("Template Servlet Error Loading Screen Definitions: Confirm that file at URL /WEB-INF/screendefinitions_" + language + ".xml contains the screen definitions");
            }
        } else {
            System.err.println("Template Servlet Error Loading Screen Definitions: URL /WEB-INF/screendefinitions_" + language + ".xml not found");
        }
    }

    public void doPost (HttpServletRequest request,
                        HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    public void doGet (HttpServletRequest request,
                       HttpServletResponse  response)
        throws IOException, ServletException {
        process(request, response);
    }

    public void process (HttpServletRequest request,
                          HttpServletResponse  response)
        throws IOException, ServletException {

        String screenName = null;
        String localeString = null;
        String selectedUrl = request.getRequestURI();
        if (request.getSession().getAttribute(WebKeys.CURRENT_URL) != null) {
            request.getSession().removeAttribute(WebKeys.CURRENT_URL);
        }

        String languageParam = request.getParameter("locale");
        // The language when specified as a parameter overrides the language set in the session
        if (languageParam != null) {
            localeString = languageParam;
        } else if (request.getSession().getAttribute(WebKeys.LOCALE) != null) {
            localeString = ((Locale)request.getSession().getAttribute(WebKeys.LOCALE)).toString();
        }
        if (allScreens.get(localeString) == null) {
           localeString = defaultLocale;
        }

        // get the screen name
        int lastPathSeparator = selectedUrl.lastIndexOf("/") + 1;
        int lastDot = selectedUrl.lastIndexOf(".");
        if (lastPathSeparator != -1 && lastDot != -1 && lastDot > lastPathSeparator) {
            screenName = selectedUrl.substring(lastPathSeparator, lastDot);
        }
         // check if request is for the previous screen
        if (screenName.equals(PREVIOUS_SCREEN)) {
            String longScreenName  =  (String)request.getSession().getAttribute(WebKeys.PREVIOUS_SCREEN);
            int lastDot2 = longScreenName.lastIndexOf(".");
            if ( lastDot2 != -1 && lastDot2 > 0) {
                screenName = longScreenName.substring(0, lastDot2);
             }
            // put the request attributes stored in the session in the request
            if (cachePreviousScreenParameters) {
                Map previousParams = (Map)request.getSession().getAttribute(WebKeys.PREVIOUS_REQUEST_PARAMETERS);
                Map params = (Map)request.getParameterMap();
                Iterator it = previousParams.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String)it.next();
                    Object value = previousParams.get(key);
                    params.put(key,value);
                }
            }
            // put in the previous request attributes
            if (cachePreviousScreenAttributes) {
                Map previousAttributes = (Map)request.getSession().getAttribute(WebKeys.PREVIOUS_REQUEST_ATTRIBUTES);
                Iterator it2 = previousAttributes.keySet().iterator();
                // put the request attributes stored in the session in the request
                while (it2.hasNext()) {
                    String key = (String)it2.next();
                    Object value = previousAttributes.get(key);
                    request.setAttribute(key,value);
                }
            }
        } else {
                  String extension = selectedUrl.substring(lastDot, selectedUrl.length());
                  request.getSession().setAttribute(WebKeys.PREVIOUS_SCREEN, screenName + extension);
                  if (cachePreviousScreenParameters) {
                      // copy all the parameters into a new map
                      HashMap newParams =  new HashMap();
                      Map params = (Map)request.getParameterMap();
                      Iterator it = params.keySet().iterator();
                     // put the request attributes stored in the session in the request
                     while (it.hasNext()) {
                         String key = (String)it.next();
                         Object value = params.get(key);
                         newParams.put(key,value);
                     }
                     request.getSession().setAttribute(WebKeys.PREVIOUS_REQUEST_PARAMETERS, newParams);
                  }
                  if (cachePreviousScreenAttributes) {
                     // put the request attributes into a map
                     HashMap attributes = new HashMap();
                     Enumeration enumeration = request.getAttributeNames();
                     while (enumeration.hasMoreElements()) {
                         String key = (String)enumeration.nextElement();
                         Object value = request.getAttribute(key);
                         attributes.put(key, value);
                     }
                     request.getSession().setAttribute(WebKeys.PREVIOUS_REQUEST_ATTRIBUTES, attributes);
                  }
        }
        // get the screen information for the coresponding language
        Screen screen = null;
        if (screenName != null){
            Screens localeScreens = (Screens)allScreens.get(localeString);
            if (localeScreens != null) screen = (Screen)localeScreens.getScreen(screenName);
            // default to the default locale screen if it was not defined in the locale specific definitions
            if (screen == null) {
                System.err.println("Screen not Found loading default from " + defaultLocale);
                localeScreens = (Screens)allScreens.get(defaultLocale);
                screen = (Screen)localeScreens.getScreen(screenName);
            }
            if (screen != null) {
                request.setAttribute(WebKeys.CURRENT_SCREEN, screen);
                String templateName = localeScreens.getTemplate(screenName);
                if (templateName != null) {
                    insertTemplate(request, response, templateName);
                }
            } else {
                response.setContentType("text/html;charset=8859_1");
                PrintWriter out = response.getWriter();
                out.println("<font size=+4>Error:</font><br>Definition for screen " + screenName + " not found");
            }
        }
    }

    /**
     * EH - Refactored to aspect WafViewTemplateHandler.
     */        
    private void insertTemplate(HttpServletRequest request,
                                HttpServletResponse response,
                                String templateName)
        throws IOException, ServletException {

        // This method tries to wrap the request dispatcher call with-in
        // a transaction for making the local EJB access efficient. However,
        // it is not a fatal error, if such a transaction can not be started
        // for some reason, so it handles them gracefully.

        boolean tx_started = false;
        UserTransaction ut = null;
        Object[] values = new Object[2];
        
        values = internalGetUserTransaction();
        
        if (values[0] != null)
        	tx_started = ((Boolean)values[0]).booleanValue();
        ut = (UserTransaction) values[1];
        
        internalGetRequestDispatcher(request, response, templateName, tx_started, ut);        
        
    }

    /**
     * Created during EH Refactoring to aspect WafViewTemplateHandler.
     */            
    private Object[] internalGetUserTransaction() {
    	Object[] values = new Object[2];
        boolean tx_started = false;
        UserTransaction ut = null;
        // Lookup the UserTransaction object
        InitialContext ic = new InitialContext();
        ut = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        ut.begin();         // start the transaction.
        tx_started = true;

        values[0] = Boolean.valueOf(tx_started);
        values[1] = ut;
        return values;
    }
    
    /**
     * Created during EH Refactoring to aspect WafViewTemplateHandler.
     */                
    private void internalGetRequestDispatcher(HttpServletRequest request,
                                HttpServletResponse response,
                                String templateName,
								boolean tx_started, 
								UserTransaction ut) throws IOException, ServletException {
     
    	context.getRequestDispatcher(templateName).forward(request, response);    	
    }
        
}