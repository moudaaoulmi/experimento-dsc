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

package com.sun.j2ee.blueprints.supplierpo.ejb;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.j2ee.blueprints.contactinfo.ejb.ContactInfo;
import com.sun.j2ee.blueprints.lineitem.ejb.LineItem;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentUtils;


public class SupplierOrder {
  public static final boolean TRACE = false;
  public static final String DTD_PUBLIC_ID = "-//Sun Microsystems, Inc. - J2EE Blueprints Group//DTD SupplierOrder 1.1//EN";
  public static final String DTD_SYSTEM_ID = "/com/sun/j2ee/blueprints/supplierpo/rsrc/schemas/SupplierOrder.dtd";
  public static final boolean VALIDATING = false;
  public static final String TPASO_XML_NS = "http://blueprints.j2ee.sun.com/TPASupplierOrder";
  public static final String TAPLI_XML_NS = "http://blueprints.j2ee.sun.com/TPALineItem";
  public static final String XML_SUPPLIERORDER = "SupplierOrder";
  public static final String XML_ORDERID = "OrderId";
  public static final String XML_ORDERDATE = "OrderDate";
  public static final String XML_SHIPPINGINFO = "ShippingInfo";
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private String orderId;
  private Date orderDate;
  private ContactInfo shippingInfo;
  private ArrayList lineItems = null;


  // Constructor to be used when creating SupplierOrder from data

  public SupplierOrder() {}

  // getter methods

  public String getOrderId() {
    return orderId;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public ContactInfo getShippingInfo() {
    return shippingInfo;
  }

  public Collection getLineItems() {
    return lineItems;
  }

  // setter methods

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public void setShippingInfo(ContactInfo contactInfo) {
    this.shippingInfo = contactInfo;
    return;
  }

  public void addLineItem(LineItem lineItem) {
    if(lineItems == null) {
      lineItems = new ArrayList();
    }
    lineItems.add(lineItem);
    return;
  }

  // XML (de)serialization methods

  public void toXML(Result result) throws XMLDocumentException {
    toXML(result, null);
    return;
  }

  public String toXML() throws XMLDocumentException {
    return toXML((URL) null);
  }

  public void toXML(Result result, URL entityCatalogURL)
    throws XMLDocumentException {
      if (entityCatalogURL != null) {
        XMLDocumentUtils.toXML(toDOM(), DTD_PUBLIC_ID, entityCatalogURL,
                               XMLDocumentUtils.DEFAULT_ENCODING, result);
      } else {
        XMLDocumentUtils.toXML(toDOM(), DTD_PUBLIC_ID, DTD_SYSTEM_ID,
                               XMLDocumentUtils.DEFAULT_ENCODING, result);
      }
      return;
  }

  /**
    * EH - Refactored to aspect SupplierpoEjbHandler.
	*/               
  public String toXML(URL entityCatalogURL) throws XMLDocumentException {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      toXML(new StreamResult(stream), entityCatalogURL);
      if (TRACE) {
        System.err.println("SupplierOrder.toXML: " + stream.toString(XMLDocumentUtils.DEFAULT_ENCODING));
      }
      return stream.toString(XMLDocumentUtils.DEFAULT_ENCODING);
  }

  public static SupplierOrder fromXML(Source source)
    throws XMLDocumentException {
      return fromXML(source, null, VALIDATING);
  }

  public static SupplierOrder fromXML(String buffer)
    throws XMLDocumentException {
      return fromXML(buffer, null, VALIDATING);
  }

  public static SupplierOrder fromXML(Source source, URL entityCatalogURL, boolean validating)
    throws XMLDocumentException {
      return fromDOM(XMLDocumentUtils.fromXML(source, DTD_PUBLIC_ID,
                                              entityCatalogURL, validating));
  }

  /**
	* EH - Refactored to aspect SupplierpoEjbHandler.
	*/                 
  public static SupplierOrder fromXML(String buffer, URL entityCatalogURL, boolean validating)
    throws XMLDocumentException {
      System.err.println(buffer);
      return fromXML(new StreamSource(new StringReader(buffer)),
                     entityCatalogURL, validating);
  }

  public Document toDOM() throws XMLDocumentException {
    Document document = XMLDocumentUtils.createDocument();
    Element root = (Element) toDOM(document);
    document.appendChild(root);
    return document;
  }

  public Node toDOM(Document document) {
    Element root = document.createElement(XML_SUPPLIERORDER);
    XMLDocumentUtils.appendChild(document, root, XML_ORDERID, orderId);
    XMLDocumentUtils.appendChild(document, root, XML_ORDERDATE,
                                 dateFormat.format(orderDate));
    Element element = (Element) document.createElement(XML_SHIPPINGINFO);
    element.appendChild(shippingInfo.toDOM(document));
    root.appendChild(element);
    for (Iterator i = lineItems.iterator(); i.hasNext();) {
      LineItem lineItem = (LineItem) i.next();
      root.appendChild(lineItem.toDOM(document));
    }
    return root;
  }

  public static SupplierOrder fromDOM(Document document)
    throws XMLDocumentException {
      return fromDOM(document.getDocumentElement());
  }

  /**
	* EH - Refactored to aspect SupplierpoEjbHandler.
	*/                   
  public static SupplierOrder fromDOM(Node node) throws XMLDocumentException {
      Element element;
    if (node.getNodeType() == Node.ELEMENT_NODE &&
        (element = ((Element) node)).getTagName().equals(("tpaso:" + XML_SUPPLIERORDER))) {
      Element child;
      SupplierOrder supplierOrder = new SupplierOrder();
      child = XMLDocumentUtils.getFirstChildNS(element, TPASO_XML_NS, XML_ORDERID, true);
      supplierOrder.setOrderId(XMLDocumentUtils.getContentAsString(child, false));
      child = internalSetOrderDate(child, supplierOrder);
      child = XMLDocumentUtils.getNextSiblingNS(child,TPASO_XML_NS, XML_SHIPPINGINFO, false);
      supplierOrder.shippingInfo
        = ContactInfo.fromDOM("tpaso", XMLDocumentUtils.getFirstChildNS(child, TPASO_XML_NS, ContactInfo.XML_CONTACTINFO, true));
      for (child = XMLDocumentUtils.getNextSibling(child, "tpali:" +
                                                   LineItem.XML_LINEITEM, true);
           child != null;
           child = XMLDocumentUtils.getNextSibling(child, "tpali:" +
                                                   LineItem.XML_LINEITEM, true)) {
        supplierOrder.addLineItem(LineItem.fromDOM("tpali", child));
      }
      return supplierOrder;
    }
    throw new XMLDocumentException("tpaso:" + XML_SUPPLIERORDER + " element expected. Instead got " + ((Element) node).getTagName());
  }
  
  /**
	* Created during EH - Refactoring to aspect SupplierpoEjbHandler.
    * Atention: its necessary to return Element child, 
    * since it is replaced for a new object
	*/                     
  private static Element internalSetOrderDate(Element child, SupplierOrder supplierOrder) {
  	child = XMLDocumentUtils.getNextSiblingNS(child, TPASO_XML_NS, XML_ORDERDATE, true);
    supplierOrder.setOrderDate(supplierOrder.dateFormat.parse(XMLDocumentUtils.getContentAsString(child, false)));
    return child;
  }
  
}

