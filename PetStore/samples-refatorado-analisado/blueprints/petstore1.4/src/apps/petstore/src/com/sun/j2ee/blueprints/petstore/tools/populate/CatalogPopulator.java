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

package com.sun.j2ee.blueprints.petstore.tools.populate;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.sql.*;
import java.util.*;
import java.io.*;


public class CatalogPopulator {
  public static final String XML_CATALOG = "Catalog";
  private static final String USER = "PBPUBLIC";
  private static final String PASSWORD = "PBPUBLIC";
  private CategoryPopulator categoryPopulator;
  private ProductPopulator productPopulator;
  private ItemPopulator itemPopulator;


  public CatalogPopulator(Map sqlStatements) throws PopulateException {
    categoryPopulator = new CategoryPopulator(sqlStatements);
    productPopulator = new ProductPopulator(sqlStatements);
    itemPopulator = new ItemPopulator(sqlStatements);
    return;
  }

  public XMLFilter setup(XMLReader reader, Connection connection) {
    XMLFilter filter = categoryPopulator.setup(reader, connection);
    filter = productPopulator.setup(filter, connection);
    filter = itemPopulator.setup(filter, connection);
    return filter;
  }

  public boolean check(Connection connection) throws PopulateException {
    return categoryPopulator.check(connection) && productPopulator.check(connection) && itemPopulator.check(connection);
  }

  /**
   * EH - Refactored to aspect PetstoreToolsHandler.
   */
  public void dropTables(Connection connection) {
  	internalItemPopulatorDropTables(itemPopulator, connection);
  	internalProductPopulatorDropTables(productPopulator, connection);
  	internalCategoryPopulatorDropTables(categoryPopulator, connection);
    return;
  }
  
  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect PetstoreToolsHandler.
   * Decided to separate the 3 internals try-catch block from dropTables method  
   * into another method. The reason is that it is not possible 
   * to use an advice "around" with pointcut mixing within(method dropTables) 
   * and call(internal block code). 
   */            
  private void internalItemPopulatorDropTables(ItemPopulator itemPopulator, Connection connection) {
  	itemPopulator.dropTables(connection);
  }

  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect PetstoreToolsHandler.
   * Decided to separate the 3 internals try-catch block from dropTables method  
   * into another method. The reason is that it is not possible 
   * to use an advice "around" with pointcut mixing within(method dropTables) 
   * and call(internal block code). 
   */            
  private void internalProductPopulatorDropTables(ProductPopulator productPopulator, Connection connection) {
  	productPopulator.dropTables(connection);
  }

  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect PetstoreToolsHandler.
   * Decided to separate the 3 internals try-catch block from dropTables method  
   * into another method. The reason is that it is not possible 
   * to use an advice "around" with pointcut mixing within(method dropTables) 
   * and call(internal block code). 
   */            
  private void internalCategoryPopulatorDropTables(CategoryPopulator categoryPopulator, Connection connection) {
  	categoryPopulator.dropTables(connection);
  }
  

  public void createTables(Connection connection) throws PopulateException {
    categoryPopulator.createTables(connection);
    productPopulator.createTables(connection);
    itemPopulator.createTables(connection);
    return;
  }
}

