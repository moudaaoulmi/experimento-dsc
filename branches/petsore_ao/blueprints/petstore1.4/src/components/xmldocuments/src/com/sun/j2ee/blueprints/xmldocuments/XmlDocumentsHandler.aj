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
	
	/*** CustomEntityResolver ***/
	pointcut CustomEntityResolverHandler() : 
		execution(public CustomEntityResolver.new(EntityResolver));
	pointcut CustomEntityResolverURLHandler() : 
		execution(public CustomEntityResolver.new(URL, EntityResolver));
	pointcut resolveEntityFromURL() : 
		withincode(private InputSource CustomEntityResolver.resolveEntityFromURL(String));
	pointcut resolveEntityFromURLURLHandler() :
		call(URL.new(String)) && resolveEntityFromURL();
	pointcut internalOpenStreamHandler() :
		execution(private InputStream CustomEntityResolver.internalOpenStream(String, URL));
	pointcut internalGetResourceAsStreamHandler() : 
		execution(private InputStream CustomEntityResolver.internalGetResourceAsStream(String, URL));
	pointcut internalResolveEntityHandler() : 
		execution(private InputSource CustomEntityResolver.internalResolveEntity(String, String));
	pointcut resolveEntityHandler() : 
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
	pointcut createXDEHandler() : 
		execution(public XMLDocumentEditor XMLDocumentEditorFactory.createXDE(String));
	
	/*** XMLDocumentUtils ***/
	pointcut getAttributeAsIntHandler() : 
		execution(public static int XMLDocumentUtils.getAttributeAsInt(Element, String, boolean));
	pointcut internalBufferAppendHandler() : 	
		execution(private static void XMLDocumentUtils.internalBufferAppend(StringBuffer, Node));
	pointcut getContentAsIntHandler() : 
		execution(public static int XMLDocumentUtils.getContentAsInt(Element, boolean));
	pointcut getContentAsFloatHandler() : 
		execution(public static float XMLDocumentUtils.getContentAsFloat(Element, boolean));
	pointcut getAttributeAsIntNSHandler() : 
		execution(public static int XMLDocumentUtils.getAttributeAsIntNS(Element, String, String, boolean));
	pointcut serializeHandler() : 
		execution(public static void XMLDocumentUtils.serialize(Transformer, Document, String, String , boolean, String, Result));
	pointcut toXMLUtilsHandler() : 
		execution(public static void XMLDocumentUtils.toXML(Document, String, URL, String, Result)) || 
		execution(public static void XMLDocumentUtils.toXML(Document, String, URL, boolean, String, Result));
	pointcut transformSetSystemIdHandler() : 
		call(void Source.setSystemId(String)) && 
		withincode(public static void XMLDocumentUtils.transform(Transformer, Source, Result, String, URL, boolean, boolean));
	pointcut transformSetXMLReaderHandler() : 
		call(void SAXSource.setXMLReader(XMLReader)) && 
		withincode(public static void XMLDocumentUtils.transform(Transformer, Source, Result, String, URL, boolean, boolean));
	pointcut transformHandler() : 
		execution(public static void XMLDocumentUtils.transform(Transformer, Source, Result, String, URL, boolean, boolean));
	pointcut fromXMLUtilsHandler() : 
		execution(public static Document XMLDocumentUtils.fromXML(InputSource, String, URL, boolean));
	pointcut createDocumentBuilderHandler() : 
		execution(public static DocumentBuilder XMLDocumentUtils.createDocumentBuilder());
	pointcut createParserSetPropertyHandler() : 
		call(void SAXParser.setProperty(String, Object)) && 
		withincode(public static SAXParser XMLDocumentUtils.createParser(boolean, boolean, CustomEntityResolver, String));
	pointcut createParserSetFeatureHandler() : 
		call(void XMLReader.setFeature(String, boolean)) && 
		withincode(public static SAXParser XMLDocumentUtils.createParser(boolean, boolean, CustomEntityResolver, String));
	pointcut createParserHandler() : 
		execution(public static SAXParser XMLDocumentUtils.createParser(boolean, boolean, CustomEntityResolver, String));
	pointcut createTransformerHandler() : 
		execution(public static Transformer XMLDocumentUtils.createTransformer());

	/*** com.sun.j2ee.blueprints.xmldocuments.tpa.TPAInvoiceXDE ***/
	pointcut internalTPAInvoiceXDEHandler() : 
		execution(public String com.sun.j2ee.blueprints.xmldocuments.tpa.TPAInvoiceXDE.internalTPAInvoiceXDE(URL));
	pointcut getDocumentAsStringHandler() : 
		execution(public String com.sun.j2ee.blueprints.xmldocuments.tpa.*.getDocumentAsString());
	
	/*** com.sun.j2ee.blueprints.xmldocuments.tpa.TPASupplierOrderXDE ***/
	pointcut internalTPASupplierOrderXDEHandler() : 
		execution(private String com.sun.j2ee.blueprints.xmldocuments.tpa.TPASupplierOrderXDE.internalTPASupplierOrderXDE(URL));
	
	
	
	
	declare soft : IOException : CustomEntityResolverHandler() || 
		CustomEntityResolverURLHandler() || 
		XMLDocumentEditorFactoryHandler() || 
		internalOpenStreamHandler() ||
		resolveEntityHandler() || 
		internalResolveEntityHandler() || 
		fromXMLUtilsHandler();
	declare soft : SAXNotSupportedException : createParserHandler();
	declare soft : SAXNotRecognizedException : createParserSetPropertyHandler() ||
		createParserHandler();
	declare soft : SAXException : createParserSetFeatureHandler() || 
		internalResolveEntityHandler() ||
		transformHandler() ||
		fromXMLUtilsHandler() || 
		createParserHandler();
	declare soft : UnsupportedEncodingException : toXMLHandler();
	declare soft : ClassNotFoundException : createXDEHandler();
	declare soft : IllegalAccessException : createXDEHandler();
	declare soft : InstantiationException : createXDEHandler();
	declare soft : TransformerException : serializeHandler() ||
		transformHandler();
	declare soft : ParserConfigurationException : fromXMLUtilsHandler() ||
		createDocumentBuilderHandler() || 
		createParserHandler();
	declare soft : TransformerConfigurationException : createTransformerHandler();
	declare soft : UnsupportedEncodingException : getDocumentAsStringHandler();
	
	
	
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
		resolveEntityFromURLURLHandler() && 
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
		internalOpenStreamHandler() && 
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
		internalGetResourceAsStreamHandler() {
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
		internalResolveEntityHandler() && 
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
		resolveEntityHandler() && 
		args(entityURI, entityURL) && target(cer){
		try {
			return proceed(cer, entityURI, entityURL);
		} catch (Exception exception) {
			System.err.println("Cannot resolve " + entityURI + " using: " + cer.entityCatalog + " " + exception);
			return null;
		}
    }	
	
	/*
	after() throwing(Exception exception) throws XMLDocumentException : 
		toXMLHandler() ||
		fromXMLUtilsHandler() ||
		getDocumentAsStringHandler() {
		throw new XMLDocumentException(exception);
	}
	*/

	/*
	after() throwing(XMLDocumentException exception) throws XMLDocumentException : 
		fromXMLHandler() {
	    System.err.println(exception.getRootCause().getMessage());
	    throw new XMLDocumentException(exception);
	}
	*/
	
	void around(URL catalogURL) throws XMLDocumentException: 
		XMLDocumentEditorFactoryHandler() && args(catalogURL){
		try{
			proceed(catalogURL);
		}catch(IOException exception){
			throw new XMLDocumentException("Can't load from resource: " + catalogURL, exception);
		}
	}
	
//	after(URL catalogURL) throwing(IOException exception) throws XMLDocumentException : 
//		XMLDocumentEditorFactoryHandler() && 
//		args(catalogURL) {
//		throw new XMLDocumentException("Can't load from resource: " + catalogURL, exception);
//	}
	
	after(String className) throwing(Exception exception) throws XMLDocumentException : 
		createXDEHandler() && args(className) {
		throw new XMLDocumentException("Can't instantiate XDE: " + className, exception);
	}
	
	after(Element element, String name, boolean optional) throwing(NumberFormatException exception) throws XMLDocumentException :
		getAttributeAsIntHandler() && 
		args(element, name, optional) {
		throw new XMLDocumentException(element.getTagName() + "/@" + name + " attribute: value format error.", exception);		
    }
	
	void around() : internalBufferAppendHandler() {
		try {
			proceed();
		} catch(DOMException e) {
			//DO NOTHING
		}
	}

	after(Element element, boolean optional) throwing(NumberFormatException exception) throws XMLDocumentException :
		(getContentAsIntHandler() || 
		 getContentAsFloatHandler()) && 
		args(element, optional) {
		throw new XMLDocumentException(element.getTagName() + " element: content format error.", exception);
    }
	
	after(Element element, String nsURI, String name, boolean optional) throwing(NumberFormatException exception) throws XMLDocumentException :
		getAttributeAsIntNSHandler() && 
		args(element, nsURI, name, optional) {
		throw new XMLDocumentException(element.getTagName() + "/@" + name + " attribute: value format error.", exception);		
    }
	
	void around() : transformSetSystemIdHandler() {
        try {
            proceed();
        } catch (Throwable e) {
            System.out.println("XMLDocuments: could not set schemas directory path to:" + XMLDocumentUtils.SCHEMAS_DIRECTORY_PATH);
        }				
	}	

	void around() : transformSetXMLReaderHandler() {
        try {
            proceed();
        } catch (DOMException dex) {
            System.out.println("XMLDocumentUtils:caught " + dex);
        }
	}
	//Need to be first
	void around() : createParserSetPropertyHandler() {
		try {
			proceed();
        } catch(SAXNotRecognizedException exception) {
            System.err.println(exception);
        }
	}
	//Need to be first	
	void around() : createParserSetFeatureHandler() {
		try {
			proceed();
        } catch (SAXException exception) {
        	//DO NOTHING
        }
	}	
	
	after() throwing(Exception exception) throws XMLDocumentException : 
		serializeHandler() ||
		toXMLUtilsHandler() || 
		transformHandler() || 
		createDocumentBuilderHandler() || 
		createParserHandler() ||
		createTransformerHandler() ||
		internalTPAInvoiceXDEHandler() || 
		internalTPASupplierOrderXDEHandler() {
        exception.printStackTrace(System.err);
        throw new XMLDocumentException(exception);		
	}
	
		
}
