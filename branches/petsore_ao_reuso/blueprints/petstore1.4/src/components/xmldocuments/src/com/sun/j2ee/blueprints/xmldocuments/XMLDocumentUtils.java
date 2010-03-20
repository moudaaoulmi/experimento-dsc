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

package com.sun.j2ee.blueprints.xmldocuments;

import java.net.URL;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;


public final class XMLDocumentUtils {
  public static final String DEFAULT_ENCODING = "UTF-8";
  public static final String SCHEMAS_DIRECTORY_PATH = "/com/sun/j2ee/blueprints/xmldocuments/rsrc/schemas/";
  public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
  public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
  public static final String W3C_XML_SCHEMA_LOCATION_QNAME = "xsi:schemaLocation";
  public static final String W3C_XML_SCHEMA_INSTANCE_NS = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String SAX_NS_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";

  private XMLDocumentUtils() {}

  public static String getAttribute(Element element, String name, boolean optional) throws XMLDocumentException {
        String value = element.getAttribute(name);
        if (value == null && !optional) {
      throw new XMLDocumentException("Attribute " + name + " of " + element.getTagName() + " expected.");
        }
        return value;
  }

  public static String getAttributeAsString(Element element, String name, boolean optional) throws XMLDocumentException {
        return getAttribute(element, name, optional);
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                   
  public static int getAttributeAsInt(Element element, String name, boolean optional) throws XMLDocumentException {
      return Integer.parseInt(getAttribute(element, name, optional));
  }

  public static Element getFirstChild(Element element, String name, boolean optional) throws XMLDocumentException {
      for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
      	if (child.getNodeType() == Node.ELEMENT_NODE) {
          if (((Element) child).getTagName().equals(name)) {
          	return (Element) child;
          }
          break;
      	}
      }
      if (!optional) {
      	throw new XMLDocumentException(name + " element expected as first child of " + element.getTagName() + ".");
      }
      return null;
  }

  public static Element getChild(Element element, String name, boolean optional) throws XMLDocumentException {
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (((Element) child).getTagName().equals(name)) {
          return (Element) child;
                }
      }
        }
        if (!optional) {
      throw new XMLDocumentException(name + " element expected as child of " + element.getTagName() + ".");
        }
        return null;
  }

  public static Element getSibling(Element element, boolean optional) throws XMLDocumentException {
        return getSibling(element, element.getTagName(), optional);
  }

  public static Element getSibling(Element element, String name, boolean optional) throws XMLDocumentException {
  		for (Node sibling = element.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {
  			if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (((Element) sibling).getTagName().equals(name)) {
                	return (Element) sibling;
                }
  			}
        }  		
        if (!optional) {
        	throw new XMLDocumentException(name + " element expected after " + element.getTagName() + ".");
        }
        return null;
  }

  public Element getNextSibling(Element element, boolean optional) throws XMLDocumentException {
        return getNextSibling(element, element.getTagName(), optional);
  }

  public static Element getNextSibling(Element element, String name, boolean optional) throws XMLDocumentException {
        for (Node sibling = element.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {
        	if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (((Element) sibling).getTagName().equals(name)) {
                	return (Element) sibling;
                }
                break;
        	}
        }
        if (!optional) {
        	throw new XMLDocumentException(name + " element expected after " + element.getTagName() + ".");
        }
        return null;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                   
  public static String getContent(Element element, boolean optional) throws XMLDocumentException {
     StringBuffer buffer = new StringBuffer();
     for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
     	if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
     		internalBufferAppend(buffer, child);
     	}
     }
     if (!optional && buffer.length() == 0) {
     	throw new XMLDocumentException(element.getTagName() + " element: content expected.");
     }
     return buffer.toString();
  }
  
  /**
   * Created during EH - Refactoring to aspect XmlDocumentsHandler.
   */                                     
  private static void internalBufferAppend(StringBuffer buffer, Node child) {
    	buffer.append(((Text) child).getData());
  }

  public static String getContentAsString(Element element, boolean optional) throws XMLDocumentException {
        return getContent(element, optional);
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                     
  public static int getContentAsInt(Element element, boolean optional) throws XMLDocumentException {
      return Integer.parseInt(getContent(element, optional));
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                   
  public static float getContentAsFloat(Element element, boolean optional) throws XMLDocumentException {
      return Float.parseFloat(getContent(element, optional));
  }

  public static String getAttributeNS(Element element, String nsURI, String name, boolean optional) throws XMLDocumentException {
        String value = element.getAttributeNS(nsURI, name);
        if (value == null && !optional) {
        	throw new XMLDocumentException("Attribute " + name + " of " + element.getTagName() + " expected.");
        }
        return value;
  }

  public static String getAttributeAsStringNS(Element element, String nsURI, String name, boolean optional) throws XMLDocumentException {
        return getAttributeNS(element, nsURI, name, optional);
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                   
  public static int getAttributeAsIntNS(Element element, String nsURI, String name, boolean optional) throws XMLDocumentException {
      return Integer.parseInt(getAttributeNS(element, nsURI, name, optional));
  }

  public static Element getFirstChildNS(Element element, String nsURI, String name, boolean optional) throws XMLDocumentException {
      for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
        if (child.getNodeType() == Node.ELEMENT_NODE) {
          if (((Element) child).getLocalName().equals(name) && ((Element) child).getNamespaceURI().equals(nsURI)) {
            return (Element) child;
          }
          break;
      }
        }
        if (!optional) {
      throw new XMLDocumentException(name + " element expected as first child of " + element.getTagName() + ".");
        }
        return null;
  }

  public static Element getChildNS(Element element, String nsURI, String name, boolean optional) throws XMLDocumentException {
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (((Element) child).getLocalName().equals(name) && ((Element) child).getNamespaceURI().equals(nsURI)) {
          return (Element) child;
                }
      }
        }
        if (!optional) {
      throw new XMLDocumentException(name + " element expected as child of " + element.getTagName() + ".");
        }
        return null;
  }

  public static Element getSiblingNS(Element element, boolean optional) throws XMLDocumentException {
        return getSiblingNS(element, element.getNamespaceURI(), element.getLocalName(), optional);
  }

  public static Element getSiblingNS(Element element, String nsURI, String name, boolean optional) throws XMLDocumentException {
  		for (Node sibling = element.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {
  			if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (((Element) sibling).getLocalName().equals(name) && ((Element) sibling).getNamespaceURI().equals(nsURI)) {
                	return (Element) sibling;
                }
  			}
        }
        if (!optional) {
        	throw new XMLDocumentException(name + " element expected after " + element.getTagName() + ".");
        }
        return null;
  }

  public Element getNextSiblingNS(Element element, boolean optional) throws XMLDocumentException {
        return getNextSiblingNS(element, element.getNamespaceURI(), element.getLocalName(), optional);
  }

  public static Element getNextSiblingNS(Element element, String nsURI, String name, boolean optional) throws XMLDocumentException {
        for (Node sibling = element.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {
        	if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (((Element) sibling).getLocalName().equals(name) && ((Element) sibling).getNamespaceURI().equals(nsURI)) {
                	return (Element) sibling;
                }
                break;
        	}
        }
        if (!optional) {
        	throw new XMLDocumentException(name + " element expected after " + element.getTagName() + ".");
        }
        return null;
  }

  public static Element createElement(Document document, String name, String value) {
    if (value != null) {
      Element element = (Element) document.createElement(name);
      element.appendChild(document.createTextNode(value));
      return element;
    }
    throw new IllegalArgumentException("XMLDocumentUtils.createElement: value of " + name + " element can't be null.");
  }

  public static Element createElement(Document document, String name, long value) {
        return createElement(document, name, Long.toString(value));
  }

  public static Element createElement(Document document, String name, float value) {
        return createElement(document, name, Float.toString(value));
  }

  public static Element createElement(Document document, String name, Element child) {
        Element element = (Element) document.createElement(name);
        element.appendChild(child);
        return element;
  }

  public static void appendChild(Document document, Node root, String name, String value) {
        Node node = document.createElement(name);
        node.appendChild(document.createTextNode(value != null ? value : ""));
        root.appendChild(node);
        return;
  }

  public static void appendChild(Document document, Node root, String name, long value) {
        appendChild(document, root, name, Long.toString(value));
        return;
  }

  public static void appendChild(Document document, Node root, String name, float value) {
        appendChild(document, root, name, Float.toString(value));
        return;
  }

  public static void appendChild(Node root, String name, String value) {
        appendChild(root.getOwnerDocument(), root, name, value);
        return;
  }

  public static Element createElement(Document document, String nsURI, String name, String value) {
    if (value != null) {
      Element element = (Element) document.createElementNS(nsURI, name);
      element.appendChild(document.createTextNode(value != null ? value : ""));
      return element;
    }
    throw new IllegalArgumentException("XMLDocumentUtils.createElement: value of " + name + " element can't be null.");
  }

  public static Element createElement(Document document, String nsURI, String name, long value) {
        return createElement(document, nsURI, name, Long.toString(value));
  }

  public static Element createElement(Document document, String nsURI, String name, float value) {
        return createElement(document, nsURI, name, Float.toString(value));
  }

  public static Element createElement(Document document, String nsURI, String name, Element child) {
        Element element = (Element) document.createElement(name);
        element.appendChild(child);
        return element;
  }

  public static void appendChild(Document document, Node root, String nsURI, String name, String value) {
        Node node = document.createElementNS(nsURI, name);
        node.appendChild(document.createTextNode(value != null ? value : ""));
        root.appendChild(node);
        return;
  }

  public static void appendChild(Document document, Node root, String nsURI, String name, long value) {
        appendChild(document, root, nsURI, name, Long.toString(value));
        return;
  }

  public static void appendChild(Document document, Node root, String nsURI, String name, float value) {
        appendChild(document, root, nsURI, name, Float.toString(value));
        return;
  }

  public static void appendChild(Node root, String name, String nsURI, String value) {
        appendChild(root.getOwnerDocument(), root, nsURI, name, value);
        return;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                     
  public static void serialize(Transformer transformer, Document document, String dtdPublicId,
                               String dtdSystemId, boolean xsdSupport, String encoding, Result result)
        throws XMLDocumentException {
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      if (!xsdSupport) {
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        if (dtdSystemId != null) {
        	transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdSystemId);
        }
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, dtdPublicId);
      }
      transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(document), result);
      return;
  }

  public static void toXML(Document document, String dtdPublicId, String dtdSystemId, String encoding, Result result)
        throws XMLDocumentException {
      serialize(createTransformer(), document, dtdPublicId, dtdSystemId, false, encoding, result);
      return;
  }

  public static void toXML(Document document, String dtdPublicId, String dtdSystemId, Result result)
        throws XMLDocumentException {
      toXML(document, dtdPublicId, dtdSystemId, DEFAULT_ENCODING, result);
      return;
  }

  public static void toXML(Document document, String dtdPublicId, URL entityCatalogURL, Result result)
        throws XMLDocumentException {
      toXML(document, dtdPublicId, entityCatalogURL, DEFAULT_ENCODING, result);
      return;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                   
  public static void toXML(Document document, String dtdPublicId, URL entityCatalogURL, String encoding, Result result)
        throws XMLDocumentException {
      CustomEntityResolver entityResolver = new CustomEntityResolver(entityCatalogURL);
      String dtdSystemId = entityResolver.mapEntityURI(dtdPublicId);
      toXML(document, dtdPublicId, dtdSystemId, encoding, result);
      return;
  }

  public static void toXML(Document document, String dtdPublicId, URL entityCatalogURL, boolean xsdSupport, Result result)
        throws XMLDocumentException {
      toXML(document, dtdPublicId, entityCatalogURL, xsdSupport, DEFAULT_ENCODING, result);
      return;
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                   
  public static void toXML(Document document, String dtdPublicId, URL entityCatalogURL, boolean xsdSupport, String encoding, Result result)
        throws XMLDocumentException {
      CustomEntityResolver entityResolver = new CustomEntityResolver(entityCatalogURL);
      String dtdSystemId = entityResolver.mapEntityURI(dtdPublicId);
      serialize(createTransformer(), document, dtdPublicId, dtdSystemId, xsdSupport,  encoding, result);
      return;
  }

  public static Document deserialize(Transformer transformer, Source source,
                                     String dtdPublicId, final URL entityCatalogURL, boolean validating, boolean xsdSupport)
        throws XMLDocumentException {
      Node node;
      if (source instanceof DOMSource) {
            node = ((DOMSource) source).getNode();
      } else {
            node = transform(transformer, source, dtdPublicId, entityCatalogURL, validating, xsdSupport);
      }
      Document document;
      if (node != null && node instanceof Document) {
            document = (Document) node;
      } else {
            throw new XMLDocumentException("Document node required.");
      }
      if (!xsdSupport) {
            if (!XMLDocumentUtils.checkDocumentType(document, dtdPublicId)) {
            	throw new XMLDocumentException("Document not of type: " + dtdPublicId);
            }
      }
      return document;
  }

  public static Document transform(Transformer transformer, Source source,
                                   String dtdPublicId, URL entityCatalogURL, boolean validating, boolean xsdSupport)
        throws XMLDocumentException {
      DOMResult result = new DOMResult();
      transform(transformer, source, result, dtdPublicId, entityCatalogURL, validating, xsdSupport);
      return (Document) result.getNode();
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                     
    public static void transform(Transformer transformer, Source source, Result result,
                                     String dtdPublicId, URL entityCatalogURL, boolean validating, boolean xsdSupport)
        throws XMLDocumentException {            
        if (!(source instanceof DOMSource)) {
          if (source.getSystemId() == null) { // Set the base URI to resolve relative URI              
             source.setSystemId(SCHEMAS_DIRECTORY_PATH);
          }
          CustomEntityResolver entityResolver = new CustomEntityResolver(entityCatalogURL);
          SAXSource saxSource = null;
          if (source instanceof StreamSource) {
          	InputSource inputSource = SAXSource.sourceToInputSource(source);
          	if (inputSource == null) {
          		throw new XMLDocumentException("Can't convert this source.");
            }
            saxSource = new SAXSource(inputSource);
          } else if (source instanceof SAXSource) {
            saxSource = (SAXSource) source;
          }
          XMLReader reader = saxSource.getXMLReader();
          if (reader == null) {
            reader = createParser(validating, xsdSupport, entityResolver, dtdPublicId).getXMLReader();
            saxSource.setXMLReader(reader);
          }
          source = saxSource;
        }
        transformer.transform(source, result);
        return;
  }

  public static Document fromXML(Source source, String dtdPublicId, final URL entityCatalogURL, boolean validating)
        throws XMLDocumentException {
      return deserialize(createTransformer(), source, dtdPublicId, entityCatalogURL, validating, false);
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                     
  public static Document fromXML(InputSource source, String dtdPublicId, final URL entityCatalogURL, boolean validating)
        throws XMLDocumentException {
      Document document;
      if (source.getSystemId() == null) { // Set the base URI to resolve relative URI
      	source.setSystemId(SCHEMAS_DIRECTORY_PATH);
      }
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setValidating(validating);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      builder.setErrorHandler(new ErrorHandler() {

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
      builder.setEntityResolver(new CustomEntityResolver(entityCatalogURL));
      document = builder.parse(source);
      if (!validating || XMLDocumentUtils.checkDocumentType(document, dtdPublicId)) {
        return document;
      }
      throw new XMLDocumentException("Document not of type: " + dtdPublicId);        
  }
  

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                     
  public static DocumentBuilder createDocumentBuilder() throws XMLDocumentException {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware(true);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      return builder;
  }

  public static SAXParser createParser(boolean validating, boolean xsdSupport, URL entityCatalogURL, String schemaURI)
    throws XMLDocumentException {
      return createParser(validating, xsdSupport, new CustomEntityResolver(entityCatalogURL), schemaURI);
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                     
  public static SAXParser createParser(boolean validating, boolean xsdSupport,
                                       CustomEntityResolver entityResolver, String schemaURI)
    throws XMLDocumentException {
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();
      parserFactory.setValidating(validating);
      parserFactory.setNamespaceAware(true);
      SAXParser parser = parserFactory.newSAXParser();
      if (xsdSupport) {
          parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
          parser.setProperty(JAXP_SCHEMA_SOURCE, entityResolver.mapEntityURI(schemaURI));
      }
      XMLReader reader = parser.getXMLReader();
      reader.setFeature(SAX_NS_PREFIXES, true);
      reader.setErrorHandler(new ErrorHandler() {
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
      reader.setEntityResolver(entityResolver);
      return parser;
  }

  public static Document createDocument() throws XMLDocumentException {
        return createDocumentBuilder().newDocument();
  }

  /**
   * EH - Refactored to aspect XmlDocumentsHandler.
   */                                     
  public static Transformer createTransformer() throws XMLDocumentException {
  	  TransformerFactory transformerFactory = TransformerFactory.newInstance();
      return transformerFactory.newTransformer();
  }

  public static boolean checkDocumentType(Document document, String dtdPublicId) {
      DocumentType documentType = document.getDoctype();
      if (documentType != null) {
      	String publicId = documentType.getPublicId();
      	return publicId != null && publicId.equals(dtdPublicId);
      }
      // System.err.println("Can't check document type: " + dtdPublicId);
      return true; // false; Due to problem of IdentityTransformer not creating the DocType nodes
  }

  /**
   * Convert a string based locale into a Locale Object
   * <br>
   * <br>Strings are formatted:
   * <br>
   * <br>language_contry_variant
   *
   **/
  public static Locale getLocaleFromString(String localeString) {
      if (localeString == null) {
      	return null;
      }
      if (localeString.toLowerCase().equals("default")) {
      	return Locale.getDefault();
      }
      int languageIndex = localeString.indexOf('_');
      if (languageIndex  == -1) {
      	return null;
      }
      int countryIndex = localeString.indexOf('_', languageIndex +1);
      String country = null;
      if (countryIndex  == -1) {
      	if (localeString.length() > languageIndex) {
      		country = localeString.substring(languageIndex +1, localeString.length());
      	} else {
      		return null;
      	}
      }
      int variantIndex = -1;
      if (countryIndex != -1) {
      	countryIndex = localeString.indexOf('_', countryIndex +1);
      }
      String language = localeString.substring(0, languageIndex);
      String variant = null;
      if (variantIndex  != -1) {
      	variant = localeString.substring(variantIndex +1, localeString.length());
      }
      if (variant != null) {
      	return new Locale(language, country, variant);
      } else {
      	return new Locale(language, country);
      }
  }
  
}
