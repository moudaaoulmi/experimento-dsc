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


package com.sun.j2ee.blueprints.xmldocuments.tpa;


import java.io.*;
import java.util.*;
import java.net.*;
import java.text.SimpleDateFormat;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import com.sun.j2ee.blueprints.xmldocuments.*;


public class TPAInvoiceXDE extends XMLDocumentEditor.DefaultXDE {
  public static final String DTD_PUBLIC_ID = "-//Sun Microsystems, Inc. - J2EE Blueprints Group//DTD TPA-Invoice 1.0//EN";
  public static final String XSD_PUBLIC_ID = "http://blueprints.j2ee.sun.com/TPAInvoice";
  public static final String XML_NAMESPACE = "http://blueprints.j2ee.sun.com/TPAInvoice";
  public static final String XML_PREFIX = "tpai";
  public static final String LINE_ITEM_PREFIX = "tpali";
  public static final String LINE_ITEM_NAMESPACE = "http://blueprints.j2ee.sun.com/TPALineItem"; 
  public static final String DTD_SYSTEM_ID = "/com/sun/j2ee/blueprints/xmldocuments/rsrc/schemas/TPAInvoice.dtd";
  public static final String XSD_SYSTEM_ID = "/com/sun/j2ee/blueprints/xmldocuments/rsrc/schemas/TPAInvoice.xsd";
  public static final String XML_INVOICE = XML_PREFIX + ":" + "Invoice";
  public static final String XML_ORDERID = XML_PREFIX + ":" + "OrderId";
  public static final String XML_USERID = XML_PREFIX + ":" + "UserId";
  public static final String XML_ORDERDATE = XML_PREFIX + ":" + "OrderDate";
  public static final String XML_SHIPPINGDATE = XML_PREFIX + ":" + "ShippingDate";
  public static final String XML_LINEITEMS = XML_PREFIX + ":" + "LineItems";
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private final DocumentBuilder builder;
  private final Transformer transformer;
  private final String systemId;
  private Document invoiceDocument = null;
  private Element orderIdElement = null;
  private Element userIdElement = null;
  private Element orderDateElement = null;
  private Element shippingDateElement = null;
  private Element lineItemsElement = null;


  public TPAInvoiceXDE() throws XMLDocumentException {
    this(null, false);
    return;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                     
  public TPAInvoiceXDE(URL entityCatalogURL, boolean xsdValidation) throws XMLDocumentException {
    setEntityCatalogURL(entityCatalogURL);
    setSupportingXSD(xsdValidation);    
    this.systemId  = internalTPAInvoiceXDE(entityCatalogURL);
    builder = XMLDocumentUtils.createDocumentBuilder();
    transformer = XMLDocumentUtils.createTransformer();
    return;
  }

  /**
   * Created during EH - Refactoring to aspect XmlDocumentsHandler.
   */                                       
  public String internalTPAInvoiceXDE(URL entityCatalogURL) throws XMLDocumentException {
    CustomEntityResolver entityResolver = new CustomEntityResolver(entityCatalogURL);
    String systemId = entityResolver.mapEntityURI(isSupportingXSD() ? XSD_PUBLIC_ID : DTD_PUBLIC_ID);
    return (systemId != null) ? systemId : (isSupportingXSD() ? XSD_SYSTEM_ID : DTD_SYSTEM_ID);  	
  }
  
  public void newDocument() {
    invoiceDocument = builder.newDocument();
    orderIdElement = null;
    userIdElement = null;
    orderDateElement = null;
    shippingDateElement = null;
    lineItemsElement = null;
    return;
  }

  public void copyDocument(Result result) throws XMLDocumentException {
    Element invoiceElement = invoiceDocument.createElementNS(XML_NAMESPACE, XML_INVOICE);
    invoiceElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + XML_PREFIX, XML_NAMESPACE);
    invoiceElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + LINE_ITEM_PREFIX, LINE_ITEM_NAMESPACE);
    invoiceElement.appendChild(orderIdElement);
    invoiceElement.appendChild(userIdElement);
    invoiceElement.appendChild(orderDateElement);
    invoiceElement.appendChild(shippingDateElement);
    invoiceElement.appendChild(lineItemsElement);
    invoiceDocument.appendChild(invoiceElement);
    
    XMLDocumentUtils.serialize(XMLDocumentUtils.createTransformer(), invoiceDocument,
                               (isSupportingXSD() ? XSD_PUBLIC_ID : DTD_PUBLIC_ID),
                               systemId, isSupportingXSD(), XMLDocumentUtils.DEFAULT_ENCODING, result);
    return;
  }

  public Source getDocument() throws XMLDocumentException {
    DOMResult result = new DOMResult();
    copyDocument(result);
    return new DOMSource(result.getNode(), result.getSystemId());
  }

  public String getDocumentAsString() throws XMLDocumentException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    copyDocument(new StreamResult(stream));
    return stream.toString(XMLDocumentUtils.DEFAULT_ENCODING);
  }

  public void setOrderId(String orderId) {
    orderIdElement = XMLDocumentUtils.createElement(invoiceDocument, XML_NAMESPACE, XML_ORDERID, orderId);
    return;
  }

  public void setUserId(String userId) {
    userIdElement = XMLDocumentUtils.createElement(invoiceDocument, XML_NAMESPACE, XML_USERID, userId);
    return;
  }

  public void setOrderDate(Date orderDate) {
    orderDateElement = XMLDocumentUtils.createElement(invoiceDocument, XML_NAMESPACE, XML_ORDERDATE,
                                                      dateFormat.format(orderDate));
    return;
  }

  public void setShippingDate(Date shippingDate) {
    shippingDateElement = XMLDocumentUtils.createElement(invoiceDocument, XML_NAMESPACE, XML_SHIPPINGDATE,
                                                         dateFormat.format(shippingDate));
    return;
  }

  public void addLineItem(String categoryId, String productId, String itemId, String lineNo,
                          int quantity, float unitPrice) {
    if (lineItemsElement == null) {
      lineItemsElement = invoiceDocument.createElementNS(XML_NAMESPACE, XML_LINEITEMS);
    }
    TPALineItemUtils.addLineItem(invoiceDocument, lineItemsElement,
                                 categoryId, productId, itemId, lineNo, quantity, unitPrice);
    return;
  }
}
