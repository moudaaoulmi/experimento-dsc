/* Copyright 2004 Sun Microsystems, Inc.  All rights reserved.  You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: 
 http://adventurebuilder.dev.java.net/LICENSE.txt
 $Id: DefaultComponentManager.java,v 1.2 2004/05/26 00:07:30 inder Exp $ */

package com.sun.j2ee.blueprints.waf.controller.web;

import java.beans.Beans;

// J2EE Imports
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ejb.*;
import javax.naming.*;

// Service Locator Imports
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator;

// WAF Imports
import com.sun.j2ee.blueprints.waf.controller.web.util.WebKeys;
import com.sun.j2ee.blueprints.waf.util.JNDINames;
import com.sun.j2ee.blueprints.waf.controller.*;
import com.sun.j2ee.blueprints.waf.controller.web.WebController;
import com.sun.j2ee.blueprints.waf.controller.ejb.*;

// tracer imports
import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This implmentation class of the ComponentManager provides access to services
 * in the web tier and ejb tier.
 * 
 */
public class DefaultComponentManager implements ComponentManager,
		java.io.Serializable {

	protected ServiceLocator sl = null;

	public DefaultComponentManager() {
		sl = ServiceLocator.getInstance();
	}

	public EJBControllerLocal getEJBController(HttpSession session) {
		EJBControllerLocal ccEjb = (EJBControllerLocal) session
				.getAttribute(WebKeys.EJB_CONTROLLER);
		if (ccEjb == null) {
			ccEjb = internalGetEJBController(ccEjb);
		}
		return ccEjb;
	}

	private EJBControllerLocal internalGetEJBController(EJBControllerLocal ccEjb)
			throws ServiceLocatorException, GeneralFailureException {
		EJBControllerLocalHome home = (EJBControllerLocalHome) sl
				.getLocalHome(JNDINames.EJB_CONTROLLER_EJBHOME);
		ccEjb = home.create();
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

	public WebController getWebController(HttpSession session) {
		WebController wcc = (WebController) session
				.getAttribute(WebKeys.WEB_CONTROLLER);
		if (wcc == null) {
			wcc = internalGetWebController(session, wcc);
			session.setAttribute(WebKeys.WEB_CONTROLLER, wcc);
		}
		return wcc;
	}

	private WebController internalGetWebController(HttpSession session,
			WebController wcc) throws RuntimeException {
		String wccClassName = sl.getString(JNDINames.DEFAULT_WEB_CONTROLLER);
		wcc = (WebController) Beans.instantiate(this.getClass()
				.getClassLoader(), wccClassName);
		wcc.init(session);
		return wcc;
	}

	/**
	 * Destroy the EJBClientController
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		EJBControllerLocal controllerEJB = getEJBController(se.getSession());
		internalSessionDestroyed(controllerEJB);
	}

	private void internalSessionDestroyed(EJBControllerLocal controllerEJB)
			throws EJBException {
		controllerEJB.remove();
	}
}
