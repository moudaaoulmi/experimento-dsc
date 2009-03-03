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


package com.sun.j2ee.blueprints.opc.ejb;


import java.io.*;
import java.util.*;
import java.net.*;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import org.xml.sax.*;

import com.sun.j2ee.blueprints.xmldocuments.*;


public class TPAInvoiceXDE extends XMLDocumentEditor.DefaultXDE {
  public static final String DTD_PUBLIC_ID = "-//Sun Microsystems, Inc. - J2EE Blueprints Group//DTD TPA-Invoice 1.0//EN";
  public static final String XSD_PUBLIC_ID = "http://blueprints.j2ee.sun.com/TPAInvoice";
  public static final String INVOICE_NS = "http://blueprints.j2ee.sun.com/TPAInvoice";
  public static final String LINEITEM_NS = "http://blueprints.j2ee.sun.com/TPALineItem";
  public static final String DTD_SYSTEM_ID = "/com/sun/j2ee/blueprints/xmldocuments/rsrc/schemas/TPAInvoice.dtd";
  public static final String XSD_SYSTEM_ID = "/com/sun/j2ee/blueprints/xmldocuments/rsrc/schemas/TPAInvoice.xsd";
  public static final String XML_INVOICE = "tpai:Invoice";
  public static final String XML_ORDERID = "tpai:OrderId";
  public static final String XML_LINEITEMS = "tpai:LineItems";
  public static final String XML_LINEITEM = "tpali:LineItem";
  public static final String XML_ITEMID = "itemId";
  public static final String XML_QUANTITY = "quantity";
  private Transformer transformer;
  private String orderId = null;
  private Map lineItemIds = null;
  private Document invoiceDocument = null;


  public TPAInvoiceXDE(URL entityCatalogURL, boolean validating) throws XMLDocumentException {
        this(entityCatalogURL, validating, false);
        return;
  }

  public TPAInvoiceXDE() throws XMLDocumentException {
        this(null, true, false);
        return;
  }

  public TPAInvoiceXDE(URL entityCatalogURL, boolean validating, boolean xsdValidation) throws XMLDocumentException {
    setEntityCatalogURL(entityCatalogURL);
    setValidating(validating);
    setSupportingXSD(xsdValidation);
        return;
  }

  public void setDocument(InputSource source) throws XMLDocumentException {
        invoiceDocument = getDocument(source);
        lineItemIds = new HashMap();
        orderId = null;
        extractData();
        return;
  }

  public void setDocument(String text) throws XMLDocumentException {
        setDocument(new InputSource(new StringReader(text)));
        return;
  }

  public Source getDocument() throws XMLDocumentException {
      if (invoiceDocument != null) {
          return new DOMSource(invoiceDocument);
        }
        throw new XMLDocumentException("No document source previously set.");
  }

  public String getOrderId() {
        return orderId;
  }

  public Map getLineItemIds() {
        return lineItemIds;
  }

  private void extractData() throws XMLDocumentException {
      Element element = invoiceDocument.getDocumentElement();
      if (element.getTagName().equals(XML_INVOICE)) {
        Element child;
        child = XMLDocumentUtils.getFirstChild(element, XML_ORDERID, true);
        orderId = XMLDocumentUtils.getContentAsString(child, true);
        child = XMLDocumentUtils.getSibling(child, XML_LINEITEMS, true);
        for (child = XMLDocumentUtils.getFirstChild(child, XML_LINEITEM, true);
            child != null;
            child = XMLDocumentUtils.getNextSibling(child, XML_LINEITEM, true)) {
            lineItemIds.put(XMLDocumentUtils.getAttribute(child, XML_ITEMID, true),
                        new Integer(XMLDocumentUtils.getAttributeAsInt(child, XML_QUANTITY, true)));
        }
        return;
      }
      throw new XMLDocumentException(XML_INVOICE + " element expected.");
  }
  
  /**
   * EH - Refactored to aspect OPCHandler. 
   */
  private Document getDocument(InputSource src) {
	    Document doc = null;
	    DocumentBuilder db = null;
	    db = getDocumentBuilder();
	  //InputSource is =  new InputSource(new StringReader(xmlText));
	    doc = db.parse(src);
	    return doc;
  }
  
  /**
   * EH - Refactored to aspect OPCHandler.
   * Decided to separate the internal try-catch block from getDocument  
   * into another method. The reason is that it is not possible 
   * to use an advice "around" with pointcut mixing 
   * within(method getDocument) and call(internal block code). 
   */  
  private DocumentBuilder getDocumentBuilder() {
		DocumentBuilderFactory dbf = null;
		DocumentBuilder db = null;
	    dbf = DocumentBuilderFactory.newInstance();
	    dbf.setValidating(false);
	    if (dbf != null){
	       db = dbf.newDocumentBuilder();
	    }
	    db.setErrorHandler(new ErrorHandler() {
	
	  public void warning(SAXParseException exception) {
	                System.err.println("[Warning]: " + exception.getMessage());
	                return;
	  }
	
	  public void error(SAXParseException exception) {
	                System.err.println("[Error]: " + exception.getMessage());
	                return;
	  }
	
	  public void fatalError(SAXParseException exception) throws SAXException {
	                System.err.println("[Fatal Error]: " + exception.getMessage());
	                throw exception;
	  }
	
	        });
	    return db;  	
	  }
}
