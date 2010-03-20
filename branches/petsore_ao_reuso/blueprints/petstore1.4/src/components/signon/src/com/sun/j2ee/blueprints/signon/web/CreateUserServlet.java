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

package com.sun.j2ee.blueprints.signon.web;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.j2ee.blueprints.signon.ejb.SignOnLocal;
import com.sun.j2ee.blueprints.signon.ejb.SignOnLocalHome;


public class CreateUserServlet extends HttpServlet {

	/**
	 * EH - Refactored to aspect SignonWebHandler.
	 */
     public  void doPost(HttpServletRequest request, HttpServletResponse  response)
        throws IOException, ServletException {
        // convert to a http servlet request for now

        // get the user name
        String userName = request.getParameter(SignOnFilter.FORM_USER_NAME);
        // get the password
        String password = request.getParameter(SignOnFilter.FORM_PASSWORD);
        //validate against the registered users
        System.out.println("CreateUserServlet create user: username=" + userName);
        System.out.println("CreateUserServlet create user: password=" + password);
        SignOnLocal signOn = getSignOnEjb();
        signOn.createUser(userName, password);
        // restore the request attributes and parameters -- maybe later
        // place a true boolean in the session
        String targetURL = (String)request.getSession().getAttribute(SignOnFilter.ORIGINAL_URL);
        // redirect to the original destination
        request.getSession().setAttribute(SignOnFilter.SIGNED_ON_USER, new Boolean(true));
        System.out.println("CreateUserServlet:: create good redirecting to original requested url=" + targetURL );
        response.sendRedirect(targetURL);
     }

 	/**
 	 * EH - Refactored to aspect SignonWebHandler.
 	 */     
     private SignOnLocal getSignOnEjb() throws ServletException {
        SignOnLocal signOn = null;
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/ejb/SignOn");
        SignOnLocalHome home =(SignOnLocalHome)o;
        signOn = home.create();
        return signOn;
     }
}

