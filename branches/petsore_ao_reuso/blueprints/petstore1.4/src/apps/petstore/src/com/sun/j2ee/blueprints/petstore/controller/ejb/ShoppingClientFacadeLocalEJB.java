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

package com.sun.j2ee.blueprints.petstore.controller.ejb;


// J2EE imports
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.sun.j2ee.blueprints.cart.ejb.ShoppingCartLocal;
import com.sun.j2ee.blueprints.cart.ejb.ShoppingCartLocalHome;
import com.sun.j2ee.blueprints.customer.ejb.CustomerLocal;
import com.sun.j2ee.blueprints.customer.ejb.CustomerLocalHome;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;
import com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;

/**
 * Session Bean implementation for ShoppingClientFacadeLocal EJB.
 *
 * Provide a facade to all of the ejbs related to a shopping client
 */
public class ShoppingClientFacadeLocalEJB implements SessionBean {

    private SessionContext sc = null;

    private ShoppingCartLocal cart = null;
    private CustomerLocal customer = null;
    private String userId = null;

    public ShoppingClientFacadeLocalEJB() {}


    public void ejbCreate() {
    }

    public void setSessionContext(SessionContext sc) {
        this.sc = sc;
    }

    public String getUserId() {
         return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Asume that the customer userId has been set
     * 
     * EH - Refactored to aspect PetstoreEjbHandler.
     */
    public CustomerLocal getCustomer() throws FinderException {
        if (userId == null) {
            throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of customer: userId is not set" );
        }
        ServiceLocator sl = new ServiceLocator();
        CustomerLocalHome home =(CustomerLocalHome)sl.getLocalHome(JNDINames.CUSTOMER_EJBHOME);
        customer = home.findByPrimaryKey(userId);
        return customer;
    }

    /**
     * EH - Refactored to aspect PetstoreEjbHandler. 
     */
    public CustomerLocal createCustomer(String userId) {
        ServiceLocator sl = new ServiceLocator();
        CustomerLocalHome home =(CustomerLocalHome)sl.getLocalHome(JNDINames.CUSTOMER_EJBHOME);
        customer = home.create(userId);
        this.userId = userId;
        return customer;
    }

    /**
     * EH - Refactored to aspect PetstoreEjbHandler. 
     */
    public ShoppingCartLocal getShoppingCart() {
        if (cart == null) {
            ServiceLocator sl = new ServiceLocator();
            ShoppingCartLocalHome home =(ShoppingCartLocalHome)sl.getLocalHome(JNDINames.SHOPPING_CART_EJBHOME);
            cart = home.create();
        }
        return cart;
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {}

    public void ejbPassivate() {}
}


