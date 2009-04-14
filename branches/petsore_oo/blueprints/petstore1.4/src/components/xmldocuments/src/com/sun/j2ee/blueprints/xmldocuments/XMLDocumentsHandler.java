package com.sun.j2ee.blueprints.xmldocuments;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.xml.sax.SAXNotRecognizedException;

public class XMLDocumentsHandler {

	public void getAttributeAsIntHandler(Element element, String name,
			NumberFormatException exception) throws XMLDocumentException {
		throw new XMLDocumentException(element.getTagName() + "/@" + name
				+ " attribute: value format error.", exception);
	}

	public void getContentAsIntHandler(Element element,
			NumberFormatException exception) throws XMLDocumentException {
		throw new XMLDocumentException(element.getTagName()
				+ " element: content format error.", exception);
	}

	public void serializeHandler(Exception exception)
			throws XMLDocumentException {
		exception.printStackTrace(System.err);
		throw new XMLDocumentException(exception);
	}

	public void transformHandler(String path) {
		System.out
				.println("XMLDocuments: could not set schemas directory path to:"
						+ path);
	}

	public void transformHandler(DOMException dex) {
		System.out.println("XMLDocumentUtils:caught " + dex);
	}

	public void fromXMLHandler(Exception exception) throws XMLDocumentException {
		throw new XMLDocumentException(exception);
	}

	public void createParserHandler(SAXNotRecognizedException exception) {
		System.err.println(exception);
	}

	public void xmlDocumentEditorFactoryHandler(URL catalogURL,
			IOException exception) throws XMLDocumentException {
		throw new XMLDocumentException("Can't load from resource: "
				+ catalogURL, exception);
	}

	public void createXDEHandler(String className, Exception exception)
			throws XMLDocumentException {
		throw new XMLDocumentException("Can't instantiate XDE: " + className,
				exception);
	}

	public void toXMLHandler(Exception exception) throws XMLDocumentException {
		throw new XMLDocumentException(exception);
	}

	public void fromXMLHandler(XMLDocumentException exception)
			throws XMLDocumentException {
		System.err.println(exception.getRootCause().getMessage());
		throw new XMLDocumentException(exception);
	}

	public void customEntityResolverHandler(IOException exception,
			String message) {
		System.err.println("CustomEntityResolver: Can't load from resource: "
				+ message + ": " + exception);
	}

	public void customEntityResolverHandler(URL entityCatalogURL,
			IOException exception) {
		System.err.println("Can't load from resource: " + entityCatalogURL
				+ ": " + exception);
	}

	public void resolveEntityFromURL1Handler(String entityURL, boolean trace) {
		if (trace) {
			System.err.println("entityURL: " + entityURL + ": not a URL");
		}
	}

	public void resolveEntityFromURL2Handler(String entityURL, boolean trace) {
		if (trace) {
			System.err.println("entityURL: " + entityURL
					+ ": not a readable URL");
		}
	}

	public void resolveEntityFromURL3Handler(Exception exception1, boolean trace) {
		if (trace) {
			System.err.println("No");
			System.err.println(exception1.getMessage());
			exception1.printStackTrace(System.err);
		}
	}

	public void resolveEntity1Handler(String entityURI, String entityURL,
			Exception exception, boolean trace) {
		if (trace) {
			System.err.println("Parent resolver failed to resolve: "
					+ entityURI + " " + entityURL + ": " + exception);
		}
	}
	
	public void resolveEntity2Handler(String entityURI, Exception exception, Properties entityCatalog) {
		System.err.println("Cannot resolve " + entityURI + " using: " + entityCatalog + " " + exception);
	}
}
