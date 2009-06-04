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

package com.sun.j2ee.blueprints.signon.ejb;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.DuplicateKeyException;

import com.sun.j2ee.blueprints.signon.user.ejb.UserLocal;
import com.sun.j2ee.blueprints.signon.user.ejb.UserLocalHome;

public class SignOnEJB implements SessionBean {

    private static final String USER_HOME_ENV_NAME = "java:comp/env/ejb/User";
    private InitialContext ic = null;
    private UserLocalHome ulh = null;

    public void ejbCreate() throws CreateException {
       ic = new InitialContext();
       ulh = (UserLocalHome) ic.lookup(USER_HOME_ENV_NAME);
    }

    /**
     * business method used to check if a user is allowed to sign on
     * 
     * EH - Refactored to aspect SignonEjbHandler.
     */
    public boolean authenticate(String userName, String password) {
        UserLocal user = ulh.findByPrimaryKey(userName);
        return user.matchPassword(password);
    }
    /** business method to called to create new users **/
    public void createUser(String userName, String password) throws CreateException {
      UserLocal user = ulh.create(userName, password);
    }

    public void setSessionContext(SessionContext c) { }
    public void ejbRemove() { }

    //empty for StateLess Session EJBs
    public void ejbActivate() { }
    //empty for StateLess Session EJBs
    public void ejbPassivate() { }
}
