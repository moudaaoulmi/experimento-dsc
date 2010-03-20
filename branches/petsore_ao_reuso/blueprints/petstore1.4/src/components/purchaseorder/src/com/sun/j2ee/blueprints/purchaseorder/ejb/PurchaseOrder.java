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
 * FITNESS FOR A PARTICULAR PURPurchaseOrderSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE PurchaseOrderSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package com.sun.j2ee.blueprints.purchaseorder.ejb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import com.sun.j2ee.blueprints.contactinfo.ejb.ContactInfo;
import com.sun.j2ee.blueprints.creditcard.ejb.CreditCard;
import com.sun.j2ee.blueprints.lineitem.ejb.LineItem;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentUtils;


public class PurchaseOrder {
  public static final boolean TRACE = false;
  public static final String DTD_PUBLIC_ID = "-//Sun Microsystems, Inc. - J2EE Blueprints Group//DTD PurchaseOrder 1.1//EN";
  public static final String DTD_SYSTEM_ID = "/com/sun/j2ee/blueprints/purchaseorder/rsrc/schemas/PurchaseOrder.dtd";
  public static final boolean VALIDATING = true;
  public static final String XML_PURCHASEORDER = "PurchaseOrder";
  public static final String XML_LOCALE = "locale";
  public static final String XML_ORDERID = "OrderId";
  public static final String XML_USERID = "UserId";
  public static final String XML_EMAILID = "EmailId";
  public static final String XML_ORDERDATE = "OrderDate";
  public static final String XML_SHIPPINGINFO = "ShippingInfo";
  public static final String XML_BILLINGINFO = "BillingInfo";
  public static final String XML_TOTALPRICE = "TotalPrice";
  public static final String XML_CREDITCARD = "CreditCard";
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private Locale locale = Locale.US;
  private String orderId;
  private String userId;
  private String emailId;
  private Date orderDate;
  private ContactInfo shippingInfo;
  private ContactInfo billingInfo;
  private float totalPrice;
  private CreditCard creditCard;
  private ArrayList lineItems = null;


  // Constructor to be used when creating PurchaseOrder from data

  public PurchaseOrder() {}

  // getter methods

  public Locale getLocale() {
    return locale;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getUserId() {
    return userId;
  }

  public String getEmailId() {
    return emailId;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public ContactInfo getShippingInfo() {
    return shippingInfo;
  }

  public ContactInfo getBillingInfo() {
    return billingInfo;
  }

  public float getTotalPrice() {
    return totalPrice;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }

  public Collection getLineItems() {
    return lineItems;
  }

  // setter methods

  public void setLocale(Locale locale) {
    this.locale = locale;
    return;
  }

  public void setLocale(String locale) {
    this.locale = XMLDocumentUtils.getLocaleFromString(locale);
    return;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
    return;
  }

  public void setUserId(String userId) {
    this.userId = userId;
    return;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
    return;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
    return;
  }

  public void setShippingInfo(ContactInfo contactInfo) {
    this.shippingInfo = contactInfo;
    return;
  }

  public void setBillingInfo(ContactInfo contactInfo) {
    this.billingInfo = contactInfo;
    return;
  }

  public void setTotalPrice(float totalPrice) {
    this.totalPrice = totalPrice;
    return;
  }

  public void setCreditCard(CreditCard creditCard) {
    this.creditCard = creditCard;
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

  public void toXML(Result result, URL entityCatalogURL) throws XMLDocumentException {
    if (entityCatalogURL != null) {
      XMLDocumentUtils.toXML(toDOM(), DTD_PUBLIC_ID, entityCatalogURL, XMLDocumentUtils.DEFAULT_ENCODING, result);
    } else {
      XMLDocumentUtils.toXML(toDOM(), DTD_PUBLIC_ID, DTD_SYSTEM_ID, XMLDocumentUtils.DEFAULT_ENCODING, result);
    }
    return;
  }

  /**
   * EH - Refactored to aspect PurchaseOrderEjbHandler.
   */        
  public String toXML(URL entityCatalogURL) throws XMLDocumentException {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      toXML(new StreamResult(stream), entityCatalogURL);
      if (TRACE) {
        System.err.println("PurchaseOrder.toXML: " + stream.toString(XMLDocumentUtils.DEFAULT_ENCODING));
      }
      return stream.toString(XMLDocumentUtils.DEFAULT_ENCODING);
  }

  public static PurchaseOrder fromXML(Source source) throws XMLDocumentException {
    return fromXML(source, null, VALIDATING);
  }

  public static PurchaseOrder fromXML(String buffer) throws XMLDocumentException {
    return fromXML(buffer, null, VALIDATING);
  }

  public static PurchaseOrder fromXML(Source source, URL entityCatalogURL, boolean validating) throws XMLDocumentException {
    return fromDOM(XMLDocumentUtils.fromXML(source, DTD_PUBLIC_ID, entityCatalogURL, validating));
  }

  /**
   * EH - Refactored to aspect PurchaseOrderEjbHandler.
   */          
  public static PurchaseOrder fromXML(String buffer, URL entityCatalogURL, boolean validating) throws XMLDocumentException {
    if (TRACE) {
      System.err.println(buffer);
    }
    return fromXML(new StreamSource(new StringReader(buffer)), entityCatalogURL, validating);
  }

  public Document toDOM() throws XMLDocumentException {
    Document document = XMLDocumentUtils.createDocument();
    Element root = (Element) toDOM(document);
    document.appendChild(root);
    return document;
  }

  public Node toDOM(Document document) {
    Element root = document.createElement(XML_PURCHASEORDER);
    root.setAttribute(XML_LOCALE, locale.toString());
    XMLDocumentUtils.appendChild(document, root, XML_ORDERID, orderId);
    XMLDocumentUtils.appendChild(document, root, XML_USERID, userId);
    XMLDocumentUtils.appendChild(document, root, XML_EMAILID, emailId);
    XMLDocumentUtils.appendChild(document, root, XML_ORDERDATE, dateFormat.format(orderDate));
    Element element = (Element) document.createElement(XML_SHIPPINGINFO);
    element.appendChild(shippingInfo.toDOM(document));
    root.appendChild(element);
    element = (Element) document.createElement(XML_BILLINGINFO);
    element.appendChild(billingInfo.toDOM(document));
    root.appendChild(element);
    XMLDocumentUtils.appendChild(document, root, XML_TOTALPRICE, totalPrice);
    root.appendChild(creditCard.toDOM(document));
    for (Iterator i = lineItems.iterator(); i.hasNext();) {
      LineItem lineItem = (LineItem ) i.next();
      root.appendChild(lineItem.toDOM(document));
    }
    return root;
  }

  public static PurchaseOrder fromDOM(Document document) throws XMLDocumentException {
    return fromDOM(document.getDocumentElement());
  }

  /**
   * EH - Refactored to aspect PurchaseOrderEjbHandler.
   */            
  public static PurchaseOrder fromDOM(Node node) throws XMLDocumentException {
  	Element element;
    if (node.getNodeType() == Node.ELEMENT_NODE && (element = ((Element) node)).getTagName().equals(XML_PURCHASEORDER)) {
      Element child;
      PurchaseOrder purchaseOrder = new PurchaseOrder();
      String value = element.getAttribute(XML_LOCALE);
      if (value != null) {
        purchaseOrder.setLocale(value);
      }
      child = XMLDocumentUtils.getFirstChild(element, XML_ORDERID, false);
      purchaseOrder.setOrderId(XMLDocumentUtils.getContentAsString(child, false));
      child = XMLDocumentUtils.getNextSibling(child, XML_USERID, false);
      purchaseOrder.setUserId(XMLDocumentUtils.getContentAsString(child, false));
      child = XMLDocumentUtils.getNextSibling(child, XML_EMAILID, false);
      purchaseOrder.setEmailId(XMLDocumentUtils.getContentAsString(child, false));
      child = internalSetOrderDate(child, purchaseOrder);
      child = XMLDocumentUtils.getNextSibling(child, XML_SHIPPINGINFO, false);
      purchaseOrder.shippingInfo = ContactInfo.fromDOM(XMLDocumentUtils.getFirstChild(child, ContactInfo.XML_CONTACTINFO, false));
      child = XMLDocumentUtils.getNextSibling(child, XML_BILLINGINFO, false);
      purchaseOrder.billingInfo = ContactInfo.fromDOM(XMLDocumentUtils.getFirstChild(child, ContactInfo.XML_CONTACTINFO, false));
      child = XMLDocumentUtils.getNextSibling(child, XML_TOTALPRICE, false);
      purchaseOrder.setTotalPrice(XMLDocumentUtils.getContentAsFloat(child, false));
      child = XMLDocumentUtils.getNextSibling(child, XML_CREDITCARD, false);
      purchaseOrder.creditCard = CreditCard.fromDOM(child);
      for (child = XMLDocumentUtils.getNextSibling(child, LineItem.XML_LINEITEM, false);
           child != null;
           child = XMLDocumentUtils.getNextSibling(child, LineItem.XML_LINEITEM, true)) {
        purchaseOrder.addLineItem(LineItem.fromDOM(child));
      }
      return purchaseOrder;
    }
    throw new XMLDocumentException(XML_PURCHASEORDER + " element expected.");
  }

  /**
   * Created durig EH - Refactored to aspect PurchaseOrderEjbHandler.
   * Atention: its necessary to return Element child, 
   * since it is replaced for a new object
   */              
  private static Element internalSetOrderDate(Element child, PurchaseOrder purchaseOrder) {
    child = XMLDocumentUtils.getNextSibling(child, XML_ORDERDATE, false);
    purchaseOrder.setOrderDate(purchaseOrder.dateFormat.parse(XMLDocumentUtils.getContentAsString(child, false)));
    return child;
  }
  
  
  /**
   * EH - Refactored to aspect PurchaseOrderEjbHandler.
   */              
  public static void main(String[] args) {
      if (args.length <= 1) {
        String fileName = args.length > 0 ? args[0] : "PurchaseOrder.xml";
        PurchaseOrder purchaseOrder = PurchaseOrder.fromXML(new StreamSource(new FileInputStream(new File(fileName)), fileName));
        purchaseOrder.toXML(new StreamResult(System.out));
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(true);
        XMLReader reader = parserFactory.newSAXParser().getXMLReader();
        XMLFilter filter = new XMLFilterImpl(reader) {
          public void startDocument() throws SAXException {
            System.err.println("StartDocument");
            getContentHandler().startDocument();
            return;
          }
        };
        SAXSource saxSource = new SAXSource(filter, new InputSource(fileName));
        purchaseOrder = PurchaseOrder.fromXML(saxSource);
        purchaseOrder.toXML(new StreamResult(System.out));
        System.exit(0);
      }
      System.err.println("Usage: " + PurchaseOrder.class.getName() + " [file-name]");
      System.exit(1);
  }
}
