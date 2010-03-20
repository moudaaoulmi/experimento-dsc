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
package com.sun.j2ee.blueprints.petstore.controller.web.actions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.j2ee.blueprints.petstore.controller.events.CartEvent;
import com.sun.j2ee.blueprints.waf.controller.web.action.HTMLActionException;
import com.sun.j2ee.blueprints.waf.controller.web.action.HTMLActionSupport;
import com.sun.j2ee.blueprints.waf.event.Event;
import com.sun.j2ee.blueprints.waf.event.EventResponse;

/**
 * Implementation of CartHTMLAction that processes a
 * user change in the shopping cart.
 *
 * Changes include:
 *    adding items
 *    removing items
 *    updating item quantities
 *    emptying the cart
 *
 */

public final class CartHTMLAction extends HTMLActionSupport {

    /**
     * EH - Refactored to aspect PetstoreWebHandler. 
     */                	
    public Event perform(HttpServletRequest request)
        throws HTMLActionException {
        // Extract attributes we will need
        String actionType= (String)request.getParameter("action");
        HttpSession session = request.getSession();
        // get the shopping cart helper

        CartEvent event = null;
        if (actionType == null) return null;
        if (actionType.equals("purchase")) {
            String itemId = request.getParameter("itemId");
            event = new CartEvent(CartEvent.ADD_ITEM, itemId);
        }
        else if (actionType.equals("remove")) {
            String itemId = request.getParameter("itemId");
            event = new CartEvent(CartEvent.DELETE_ITEM, itemId);
        }
        else if (actionType.equals("update")) {
            Map quantities = new HashMap();
            Map parameters = request.getParameterMap();
            for (Iterator it = parameters.keySet().iterator();
                 it.hasNext(); ) {
                String name = (String) it.next();
                String value = ((String[]) parameters.get(name))[0];

                final String ITEM_QTY = "itemQuantity_";
                if (name.startsWith(ITEM_QTY)) {
                    String itemID = name.substring(ITEM_QTY.length());
                    Integer quantity = null;
                    
                    quantity = internalGetQuantity(value);
                    
                    quantities.put(itemID, quantity);
                }
            }
            event = CartEvent.createUpdateItemEvent(quantities);
        }
        return event;
    }

    /**
     * Created during EH refactoring.
     * EH - Refactored to aspect PetstoreWebHandler.
     * Decided to separate the internal try-catch block from perform method  
     * into another method. The reason is that it is not possible 
     * to use an advice "around" with pointcut mixing within(method perform) 
     * and call(internal block code). 
     */          
    private Integer internalGetQuantity(String value) {
        return new Integer(value);
    }
    
    // prepare the cart
    public void doEnd(HttpServletRequest request, EventResponse eventResponse) {
    }
}

