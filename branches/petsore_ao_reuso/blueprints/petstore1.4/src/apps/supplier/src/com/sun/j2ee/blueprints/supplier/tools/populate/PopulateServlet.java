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

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


public class PopulateServlet extends HttpServlet {
  private static final String POPULATE_DATA_PATH_PARAM = "PopulateData";
  private static final String DEFAULT_POPULATE_DATA_PATH = "/populate/Populate-UTF8.xml";
  private static final String SUCCESS_PAGE_URL_PARAM = "success_page";
  private static final String ERROR_PAGE_URL_PARAM = "error_page";
  private static final String FORCEFULLY_MODE_PARAM = "forcefully";
  private static final String REFERER_HEADER = "Referer";
  private String populateDataPath;

  public void init(ServletConfig config) throws javax.servlet.ServletException {
    super.init(config);
    populateDataPath = config.getInitParameter(POPULATE_DATA_PATH_PARAM);
    if (populateDataPath == null) {
      populateDataPath = DEFAULT_POPULATE_DATA_PATH;
    }
    return;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    doPost(request, response);
    return;
  }

  /**
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */    
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String successPageURL = request.getParameter(SUCCESS_PAGE_URL_PARAM);
    String errorPageURL = request.getParameter(ERROR_PAGE_URL_PARAM);
    String forcefully = request.getParameter(FORCEFULLY_MODE_PARAM);
    String refererURL = request.getHeader(REFERER_HEADER);
    internalPopulate(forcefully, errorPageURL, request, response);
    if (successPageURL != null) {
      redirect(request, response, successPageURL);
    } else if (refererURL != null) {
      redirect(request, response, refererURL);
    }
    return;
  }

  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */      
  private void internalPopulate(String forcefully, String errorPageURL, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  	populate(forcefully != null && Boolean.valueOf(forcefully).booleanValue());
  }
  
  /**
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */        
  private boolean populate(boolean forcefully) throws PopulateException {
  	  XMLReader reader = null;
      boolean alreadyPopulated = false;
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();
      //parserFactory.setValidating(true);
      parserFactory.setNamespaceAware(true);
      reader = parserFactory.newSAXParser().getXMLReader();
      InventoryPopulator inventoryPopulator = new InventoryPopulator();
      if (!forcefully) {      	
      	alreadyPopulated = internalInventoryPopulatorCheck(inventoryPopulator);
        System.err.println("Already populated: " + alreadyPopulated);
      }
      if (forcefully || !alreadyPopulated) {
        //inventoryPopulator.setup(reader).parse(new InputSource(getResource(populateDataPath)));
      	internalInventoryPopulator(inventoryPopulator, reader);
      }
      return forcefully || !alreadyPopulated;
  }

  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */        
  private boolean internalInventoryPopulatorCheck(InventoryPopulator inventoryPopulator) {
    return inventoryPopulator.check();
  }
  
  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */          
  private void internalInventoryPopulator(InventoryPopulator inventoryPopulator, XMLReader reader) throws PopulateException {
  	inventoryPopulator.setup(reader).parse(new InputSource(getResource(populateDataPath)));
  }
  
  /**
   * EH - Refactored to aspect SupplierToolsPopulateHandler.
   */        
  private String getResource(String path) throws IOException {
  	String url;
    url = new URL(path).toString();
    //System.err.println("Made " + url + " from " + path);
    return url;
  }

  /**
   * Updated during EH refactoring.
   */          
  public void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
    String url;
    if (path.startsWith("//")) {
      url = new URL(new URL(HttpUtils.getRequestURL(request).toString()), path.substring(1)).toString();
    } else {
      url = request.getContextPath() + (path.startsWith("/") ? path : "/" + path);
    }
    //System.err.println("redirecting to: " + url);
    response.sendRedirect(url);
    return;
  }
}

