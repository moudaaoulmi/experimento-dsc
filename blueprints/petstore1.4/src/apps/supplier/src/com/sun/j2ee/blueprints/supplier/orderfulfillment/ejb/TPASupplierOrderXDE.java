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


package com.sun.j2ee.blueprints.supplier.orderfulfillment.ejb;


import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import com.sun.j2ee.blueprints.supplierpo.ejb.SupplierOrder;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentEditor;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentUtils;


public class TPASupplierOrderXDE extends XMLDocumentEditor.DefaultXDE {
  public static final String DEFAULT_SCHEMA_URI
  = "-//Sun Microsystems, Inc. - J2EE Blueprints Group//DTD TPA-SupplierOrder 1.0//EN";
  public static final String STYLE_SHEET_CATALOG_PATH
  = "/com/sun/j2ee/blueprints/supplier/rsrc/SupplierOrderStyleSheetCatalog.properties";
  private String schemaURI;
  private Transformer transformer;
  private SupplierOrder supplierOrder = null;


  public TPASupplierOrderXDE() throws XMLDocumentException {
    this(null, true, DEFAULT_SCHEMA_URI);
    return;
  }

  /**
   * EH - Refactored to aspect SupplierOrderfulfillmentEjbHandler.
   */
  public TPASupplierOrderXDE(URL entityCatalogURL, boolean validating, String schemaURI)
    throws XMLDocumentException {
      setEntityCatalogURL(entityCatalogURL);
      setValidating(validating);
      this.schemaURI = schemaURI;
      Properties styleSheetCatalog = new Properties();
      InputStream stream = getClass().getResourceAsStream(STYLE_SHEET_CATALOG_PATH);
      if (stream != null) {
      	internalLoad(styleSheetCatalog, stream);
      } else {
        System.err.println("Can't access resource: " + STYLE_SHEET_CATALOG_PATH);
      }
      String styleSheetPath = styleSheetCatalog.getProperty(schemaURI);
      String supportingXSD = styleSheetCatalog.getProperty(schemaURI + ".XSDSupport");
      setSupportingXSD(supportingXSD != null && Boolean.getBoolean(supportingXSD));
      if (styleSheetPath != null && !styleSheetPath.trim().equals("")) {
        stream = getClass().getResourceAsStream(styleSheetPath);
        if (stream != null) {
        	transformer = internalGetTransformer(stream);
        } else {
          throw new XMLDocumentException("Can't access style sheet: " + styleSheetPath);
        }
      } else {
      	transformer = internalCreateTransformer();
      }
      return;
  }
  
  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect SupplierOrderfulfillmentEjbHandler.
   * Decided to separate the internal try-catch block from TPASupplierOrderXDE constructor  
   * into another method. The reason is that it is not possible 
   * to use an advice "around" with pointcut mixing within(TPASupplierOrderXDE) 
   * and call(internal block code). 
   */                
  private void internalLoad(Properties styleSheetCatalog, InputStream stream) {
    styleSheetCatalog.load(stream);
  }
  
  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect SupplierOrderfulfillmentEjbHandler.
   * Decided to separate the internal try-catch block from TPASupplierOrderXDE constructor  
   * into another method. The reason is that it catches Exception 
   * that is rather generic to move to a global try-catch block. 
   */                  
  private Transformer internalGetTransformer(InputStream stream) throws XMLDocumentException {
    return TransformerFactory.newInstance().newTransformer(new StreamSource(stream));
  }

  /**
   * Created during EH refactoring.
   * EH - Refactored to aspect SupplierOrderfulfillmentEjbHandler.
   * Decided to separate the internal try-catch block from TPASupplierOrderXDE constructor  
   * into another method. The reason is that it catches Exception 
   * that is rather generic to move to a global try-catch block. 
   */                    
  private Transformer internalCreateTransformer() throws XMLDocumentException {
    return XMLDocumentUtils.createTransformer();
  }
  
  public void setDocument(String buffer) throws XMLDocumentException {
    setDocument(new StreamSource(new StringReader(buffer)));
    return;
  }

  public void setDocument(Source source) throws XMLDocumentException {

    supplierOrder = null;
    supplierOrder
      = SupplierOrder.fromDOM(XMLDocumentUtils.transform(transformer, source, schemaURI,
                                                         getEntityCatalogURL(), isValidating(), isSupportingXSD()));
    return;
  }

  public SupplierOrder getSupplierOrder() {
    return supplierOrder;
  }
}
