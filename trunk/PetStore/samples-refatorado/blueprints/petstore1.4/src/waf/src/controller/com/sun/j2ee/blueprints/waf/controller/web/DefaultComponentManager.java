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
package com.sun.j2ee.blueprints.waf.controller.web;

import java.beans.Beans;

// J2EE Imports
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

// WAF Imports
import com.sun.j2ee.blueprints.waf.controller.web.util.WebKeys;
import com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator;
import com.sun.j2ee.blueprints.waf.util.JNDINames;
import com.sun.j2ee.blueprints.waf.controller.web.WebController;
import com.sun.j2ee.blueprints.waf.controller.ejb.EJBControllerLocalHome;
import com.sun.j2ee.blueprints.waf.controller.ejb.EJBControllerLocal;


/**
 * This implmentation class of the ComponentManager provides
 * access to services in the web tier and ejb tier.
 *
 */
public class DefaultComponentManager implements ComponentManager, java.io.Serializable {

    protected ServiceLocator sl = null;

    public DefaultComponentManager() {
        sl = ServiceLocator.getInstance();
    }

    /**
     * EH - Refactored to aspect WafControllerWebHandler.
     */                                         
    public WebController getWebController(HttpSession session) {
        ServletContext context = session.getServletContext();
        WebController wcc =  (WebController)context.getAttribute(WebKeys.WEB_CONTROLLER);
        if ( wcc == null ) {
            String wccClassName = sl.getString(JNDINames.DEFAULT_WEB_CONTROLLER);
            wcc = (WebController) Beans.instantiate(this.getClass().getClassLoader(), wccClassName);
            wcc.init(context);
         }
         return wcc;
    }
    
    /**
     * EH - Refactored to aspect WafControllerWebHandler.
     */                                         
    public EJBControllerLocal getEJBController(HttpSession session) {
        EJBControllerLocal ccEjb = (EJBControllerLocal)session.getAttribute(WebKeys.EJB_CONTROLLER);
        if (ccEjb == null) {
            EJBControllerLocalHome home = (EJBControllerLocalHome)sl.getLocalHome(JNDINames.EJB_CONTROLLER_EJBHOME);
            ccEjb = home.create();
        }
        return ccEjb;
    }

    /**
     *
     * Create the WebController which in turn should create the
     * EJBClientController.
     *
     */
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        sl = ServiceLocator.getInstance();
        session.setAttribute(WebKeys.COMPONENT_MANAGER, this);
    }

    /**
     *
     * Destroy the WebClientController which in turn should destroy the
     * EJBClientController.
     * 
     * EH - Refactored to aspect WafControllerWebHandler.
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        WebController wcc = getWebController(session);
        if (wcc != null) {
            wcc.destroy(session);
        }
    }
}


