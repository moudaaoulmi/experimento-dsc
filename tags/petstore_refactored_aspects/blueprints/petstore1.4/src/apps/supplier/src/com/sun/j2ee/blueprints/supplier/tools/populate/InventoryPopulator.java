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

package com.sun.j2ee.blueprints.supplier.tools.populate;

import javax.naming.*;
import java.util.*;
import org.xml.sax.*;
import com.sun.j2ee.blueprints.supplier.inventory.ejb.InventoryLocalHome;
import com.sun.j2ee.blueprints.supplier.inventory.ejb.InventoryLocal;


public class InventoryPopulator {
  public static final String JNDI_INVENTORY_HOME = "java:comp/env/ejb/Inventory";
  private static final String XML_INVENTORYLIST = "InventoryList";
  private static final String XML_INVENTORY = "Inventory";
  private static final String XML_ID = "Inventory/@id";
  private static final String XML_QUANTITY = "Inventory/@quantity";
  private InventoryLocalHome inventoryHome = null;
  private String rootTag;


  public InventoryPopulator() {
    this(XML_INVENTORYLIST);
    return;
  }

  public InventoryPopulator(String rootTag) {
    this.rootTag = rootTag;
    return;
  }

  public XMLFilter setup(XMLReader reader) throws PopulateException {
    return new XMLDBHandler(reader, rootTag, XML_INVENTORY) {

      public void update() throws PopulateException {}

      public void create() throws PopulateException {
        createInventory(getValue(XML_ID), getValue(XML_QUANTITY, 0));
        return;
      }
    };

  }

  /**
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */
  public boolean check() throws PopulateException {
      InitialContext context = new InitialContext();
      inventoryHome = (InventoryLocalHome) context.lookup(JNDI_INVENTORY_HOME);
      Collection inventoryItems = inventoryHome.findAllInventoryItems();
      if ((inventoryItems == null) || (inventoryItems.size() == 0)) {
      	return false;
      }
      return true;
  }

  /**
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */  
  private InventoryLocal createInventory(String id, int quantity) throws PopulateException {
  	  if (inventoryHome == null) {
        InitialContext context = new InitialContext();
        inventoryHome = (InventoryLocalHome) context.lookup(JNDI_INVENTORY_HOME);
      }
      InventoryLocal inventory;
      
      internalRemoveExistingInventory(id);
      
      inventory = inventoryHome.create(id, quantity);
      
      return inventory;
  }
  
  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */    
  private void internalRemoveExistingInventory(String id) {
  	InventoryLocal inventory = inventoryHome.findByPrimaryKey(id);
    inventory.remove();
  }
}
