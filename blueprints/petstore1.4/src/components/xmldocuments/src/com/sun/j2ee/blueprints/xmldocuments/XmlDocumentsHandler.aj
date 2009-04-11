/*
 * Created on 29/09/2005
 */
package com.sun.j2ee.blueprints.xmldocuments;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import com.sun.j2ee.blueprints.util.aspect.XMLDocumentExceptionGenericAspect;

/**
 * @author Raquel Maranhao
 */
public aspect XmlDocumentsHandler extends XMLDocumentExceptionGenericAspect {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : IOException : CustomEntityResolverHandler() || 
		CustomEntityResolverURLHandler() || 
		XMLDocumentEditorFactoryHandler() || 
		customEntityResolver_internalOpenStreamHandler() ||
		customEntityResolver_resolveEntityHandler() || 
		customEntityResolver_internalResolveEntityHandler() || 
		fromXMLUtilsHandler();
	declare soft : SAXNotSupportedException : xMLDocumentUtils_createParserHandler();
	declare soft : SAXNotRecognizedException : xMLDocumentUtils_createParserSetPropertyHandler() ||
		xMLDocumentUtils_createParserHandler();
	declare soft : SAXException : xMLDocumentUtils_createParserSetFeatureHandler() || 
		customEntityResolver_internalResolveEntityHandler() ||
		xMLDocumentUtils_transformHandler() ||
		fromXMLUtilsHandler() || 
		xMLDocumentUtils_createParserHandler();
	declare soft : UnsupportedEncodingException : toXMLHandler();
	declare soft : ClassNotFoundException : xMLDocumentEditorFactory_createXDEHandler();
	declare soft : IllegalAccessException : xMLDocumentEditorFactory_createXDEHandler();
	declare soft : InstantiationException : xMLDocumentEditorFactory_createXDEHandler();
	declare soft : TransformerException : xMLDocumentUtils_serializeHandler() ||
		xMLDocumentUtils_transformHandler();
	declare soft : ParserConfigurationException : fromXMLUtilsHandler() ||
		xMLDocumentUtils_createDocumentBuilderHandler() || 
		xMLDocumentUtils_createParserHandler();
	declare soft : TransformerConfigurationException : xMLDocumentUtils_createTransformerHandler();
	declare soft : UnsupportedEncodingException : getDocumentAsStringHandler();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** CustomEntityResolver ***/
	pointcut CustomEntityResolverHandler() : 
		execution(public CustomEntityResolver.new(EntityResolver));
	pointcut CustomEntityResolverURLHandler() : 
		execution(public CustomEntityResolver.new(URL, EntityResolver));
	pointcut customEntityResolver_resolveEntityFromURL() : 
		withincode(private InputSource CustomEntityResolver.resolveEntityFromURL(String));
	pointcut customEntityResolver_resolveEntityFromURLURLHandler() :
		call(URL.new(String)) && customEntityResolver_resolveEntityFromURL();
	pointcut customEntityResolver_internalOpenStreamHandler() :
		execution(private InputStream CustomEntityResolver.internalOpenStream(String, URL));
	pointcut customEntityResolver_internalGetResourceAsStreamHandler() : 
		execution(private InputStream CustomEntityResolver.internalGetResourceAsStream(String, URL));
	pointcut customEntityResolver_internalResolveEntityHandler() : 
		execution(private InputSource CustomEntityResolver.internalResolveEntity(String, String));
	pointcut customEntityResolver_resolveEntityHandler() : 
		execution(public InputSource CustomEntityResolver.resolveEntity(String, String));

	/*** OrderApproval ***/
	pointcut toXMLHandler() : 
		execution(public String OrderApproval.toXML(URL));
	public pointcut afterXMLDocumentExceptionHandler() : 
	    toXMLHandler() ||
		fromXMLUtilsHandler() ||
		getDocumentAsStringHandler();	
	public pointcut afterWithPrintXMLDocumentExceptionHandler() : 
	    execution(public static OrderApproval OrderApproval.fromXML(String, URL, boolean));
	
	/*** XMLDocumentEditorFactory ***/
	pointcut XMLDocumentEditorFactoryHandler() : 
		execution(public XMLDocumentEditorFactory.new(URL));
	pointcut xMLDocumentEditorFactory_createXDEHandler() : 
		execution(public XMLDocumentEditor XMLDocumentEditorFactory.createXDE(String));
	
	/*** XMLDocumentUtils ***/
	pointcut xMLDocumentUtils_getAttributeAsIntHandler() : 
		execution(public static int XMLDocumentUtils.getAttributeAsInt(Element, String, boolean));
	pointcut xMLDocumentUtils_internalBufferAppendHandler() : 	
		execution(private static void XMLDocumentUtils.internalBufferAppend(StringBuffer, Node));
	pointcut xMLDocumentUtils_getContentAsIntHandler() : 
		execution(public static int XMLDocumentUtils.getContentAsInt(Element, boolean));
	pointcut xMLDocumentUtils_getContentAsFloatHandler() : 
		execution(public static float XMLDocumentUtils.getContentAsFloat(Element, boolean));
	pointcut xMLDocumentUtils_getAttributeAsIntNSHandler() : 
		execution(public static int XMLDocumentUtils.getAttributeAsIntNS(Element, String, String, boolean));
	pointcut xMLDocumentUtils_serializeHandler() : 
		execution(public static void XMLDocumentUtils.serialize(Transformer, Document, String, String , boolean, String, Result));
	pointcut xMLDocumentUtils_toXMLUtilsHandler() : 
		execution(public static void XMLDocumentUtils.toXML(Document, String, URL, String, Result)) || 
		execution(public static void XMLDocumentUtils.toXML(Document, String, URL, boolean, String, Result));
	pointcut xMLDocumentUtils_transformSetSystemIdHandler() : 
		call(void Source.setSystemId(String)) && 
		withincode(public static void XMLDocumentUtils.transform(Transformer, Source, Result, String, URL, boolean, boolean));
	pointcut xMLDocumentUtils_transformSetXMLReaderHandler() : 
		call(void SAXSource.setXMLReader(XMLReader)) && 
		withincode(public static void XMLDocumentUtils.transform(Transformer, Source, Result, String, URL, boolean, boolean));
	pointcut xMLDocumentUtils_transformHandler() : 
		execution(public static void XMLDocumentUtils.transform(Transformer, Source, Result, String, URL, boolean, boolean));
	pointcut fromXMLUtilsHandler() : 
		execution(public static Document XMLDocumentUtils.fromXML(InputSource, String, URL, boolean));
	pointcut xMLDocumentUtils_createDocumentBuilderHandler() : 
		execution(public static DocumentBuilder XMLDocumentUtils.createDocumentBuilder());
	pointcut xMLDocumentUtils_createParserSetPropertyHandler() : 
		call(void SAXParser.setProperty(String, Object)) && 
		withincode(public static SAXParser XMLDocumentUtils.createParser(boolean, boolean, CustomEntityResolver, String));
	pointcut xMLDocumentUtils_createParserSetFeatureHandler() : 
		call(void XMLReader.setFeature(String, boolean)) && 
		withincode(public static SAXParser XMLDocumentUtils.createParser(boolean, boolean, CustomEntityResolver, String));
	pointcut xMLDocumentUtils_createParserHandler() : 
		execution(public static SAXParser XMLDocumentUtils.createParser(boolean, boolean, CustomEntityResolver, String));
	pointcut xMLDocumentUtils_createTransformerHandler() : 
		execution(public static Transformer XMLDocumentUtils.createTransformer());

	/*** com.sun.j2ee.blueprints.xmldocuments.tpa.TPAInvoiceXDE ***/
	pointcut tPAInvoiceXDE_internalTPAInvoiceXDEHandler() : 
		execution(public String com.sun.j2ee.blueprints.xmldocuments.tpa.TPAInvoiceXDE.internalTPAInvoiceXDE(URL));
	pointcut getDocumentAsStringHandler() : 
		execution(public String com.sun.j2ee.blueprints.xmldocuments.tpa.*.getDocumentAsString());
	
	/*** com.sun.j2ee.blueprints.xmldocuments.tpa.TPASupplierOrderXDE ***/
	pointcut tPASupplierOrderXDE_internalTPASupplierOrderXDEHandler() : 
		execution(private String com.sun.j2ee.blueprints.xmldocuments.tpa.TPASupplierOrderXDE.internalTPASupplierOrderXDE(URL));
	
	// ---------------------------
    // Advice's
    // ---------------------------	
	void around() : CustomEntityResolverHandler() {
		try {
			proceed();
		} catch (IOException exception) {
		    System.err.println("CustomEntityResolver: Can't load from resource: " + CustomEntityResolver.ENTITY_CATALOG + ": " + exception);
		}	
	}	

	void around(URL entityCatalogURL, EntityResolver parentResolver) : 
		CustomEntityResolverURLHandler() && 
		args(entityCatalogURL,parentResolver) {
		try {
			proceed(entityCatalogURL,parentResolver);
		} catch (IOException exception) {
			System.err.println("Can't load from resource: " + entityCatalogURL + ": " + exception);
		}	
	}	
	
	URL around(String entityURL) : 
		customEntityResolver_resolveEntityFromURLURLHandler() && 
		args(entityURL) {
		try{
			return proceed(entityURL);
		} catch (Exception exception) {
	      if (CustomEntityResolver.TRACE) {
	        System.err.println("entityURL: " + entityURL + ": not a URL");
	      }
	      return null;
		}
	}

	InputStream around(String entityURL, URL entityURLURL) : 
		customEntityResolver_internalOpenStreamHandler() && 
		args(entityURL,entityURLURL) {
		try{
			return proceed(entityURL,entityURLURL);
	    } catch (Exception exception) {
	        if (CustomEntityResolver.TRACE) {
	          System.err.println("entityURL: " + entityURL + ": not a readable URL");
	        }
	        return null;
	    }
	}

	InputStream around() : 
		customEntityResolver_internalGetResourceAsStreamHandler() {
		try{
			return proceed();
	    } catch (Exception exception1) {
	        if (CustomEntityResolver.TRACE) {
	          System.err.println("No");
	          System.err.println(exception1.getMessage());
	          exception1.printStackTrace(System.err);
	        }
	        return null;
	    }  
	}
    // Need to be first
	InputSource around(String entityURI, String entityURL) : 
		customEntityResolver_internalResolveEntityHandler() && 
		args(entityURI, entityURL) {
		try {
			return proceed(entityURI, entityURL);
		} catch (Exception exception) {
			if (CustomEntityResolver.TRACE) {
				System.err.println("Parent resolver failed to resolve: " + entityURI + " " + entityURL + ": " + exception);
			}
			return null;
		}
    }	
	
	InputSource around(CustomEntityResolver cer, String entityURI, String entityURL) : 
		customEntityResolver_resolveEntityHandler() && 
		args(entityURI, entityURL) && target(cer){
		try {
			return proceed(cer, entityURI, entityURL);
		} catch (Exception exception) {
			System.err.println("Cannot resolve " + entityURI + " using: " + cer.entityCatalog + " " + exception);
			return null;
		}
    }	
	
	void around(URL catalogURL) throws XMLDocumentException: 
		XMLDocumentEditorFactoryHandler() && args(catalogURL){
		try{
			proceed(catalogURL);
		}catch(IOException exception){
			throw new XMLDocumentException("Can't load from resource: " + catalogURL, exception);
		}
	}
	
	XMLDocumentEditor around(String className) throws XMLDocumentException : 
		xMLDocumentEditorFactory_createXDEHandler() && args(className){
		try{
			return proceed(className);
		} catch(Exception exception) {
		      throw new XMLDocumentException("Can't instantiate XDE: " + className, exception);
	    }
	}
	
	int around(Element element, String name, boolean optional) throws XMLDocumentException : 
		xMLDocumentUtils_getAttributeAsIntHandler() && 
		args(element, name, optional) {
		try{
			return proceed(element, name, optional);
		} catch (NumberFormatException exception) {
		      throw new XMLDocumentException(element.getTagName() + "/@" + name + " attribute: value format error.", exception);
		}
	}
	
	int around(Element element, String nsURI, String name, boolean optional) throws XMLDocumentException :
		xMLDocumentUtils_getAttributeAsIntNSHandler() && 
		args(element, nsURI, name, optional) {
		try {
			return proceed(element, nsURI, name, optional);
		} catch (NumberFormatException exception) {
			throw new XMLDocumentException(element.getTagName() + "/@" + name + " attribute: value format error.", exception);
        }
	}
	
	void around() : xMLDocumentUtils_internalBufferAppendHandler() {
		try {
			proceed();
		} catch(DOMException e) {
			//DO NOTHING
		}
	}
	
	int around(Element element, boolean optional) throws XMLDocumentException :
		(xMLDocumentUtils_getContentAsIntHandler() || 
		 xMLDocumentUtils_getContentAsFloatHandler()) && 
		args(element, optional) {
		try{
			return proceed(element, optional);
		} catch (NumberFormatException exception) {
		      throw new XMLDocumentException(element.getTagName() + " element: content format error.", exception);
		}
	}
	
	void around() : xMLDocumentUtils_transformSetSystemIdHandler() {
        try {
            proceed();
        } catch (Throwable e) {
            System.out.println("XMLDocuments: could not set schemas directory path to:" + XMLDocumentUtils.SCHEMAS_DIRECTORY_PATH);
        }				
	}	

	void around() : xMLDocumentUtils_transformSetXMLReaderHandler() {
        try {
            proceed();
        } catch (DOMException dex) {
            System.out.println("XMLDocumentUtils:caught " + dex);
        }
	}
	//Need to be first
	void around() : xMLDocumentUtils_createParserSetPropertyHandler() {
		try {
			proceed();
        } catch(SAXNotRecognizedException exception) {
            System.err.println(exception);
        }
	}
	//Need to be first	
	void around() : xMLDocumentUtils_createParserSetFeatureHandler() {
		try {
			proceed();
        } catch (SAXException exception) {
        	//DO NOTHING
        }
	}	
	
	Object around() throws XMLDocumentException :
		xMLDocumentUtils_serializeHandler() ||
		xMLDocumentUtils_toXMLUtilsHandler() || 
		xMLDocumentUtils_transformHandler() || 
		xMLDocumentUtils_createDocumentBuilderHandler() || 
		xMLDocumentUtils_createParserHandler() ||
		xMLDocumentUtils_createTransformerHandler() ||
		tPAInvoiceXDE_internalTPAInvoiceXDEHandler() || 
		tPASupplierOrderXDE_internalTPASupplierOrderXDEHandler() {
		try{
			return proceed();
		}catch (Exception exception){
			exception.printStackTrace(System.err);
	        throw new XMLDocumentException(exception);
		}
	}
	
}
