package com.sun.j2ee.blueprints.waf.view.template;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.jsp.JspTagException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import adventure.exception.ExceptionHandler;

@ExceptionHandler
public privileged aspect TemplateHandler {

	pointcut internalDoEndTagHandler(): execution(private void InsertTag.internalDoEndTag());
	pointcut internalDoStartTagHandler(): execution(private Screen InsertTag.internalDoStartTag(Screen));
	pointcut loadDocumentHandler(): execution(public static Element ScreenDefinitionDAO.loadDocument(URL));
	
	declare soft: Exception : internalDoEndTagHandler() || loadDocumentHandler();
	declare soft: SAXParseException : loadDocumentHandler();
	declare soft: SAXException : loadDocumentHandler();
	declare soft: MalformedURLException : loadDocumentHandler();
	declare soft: IOException : loadDocumentHandler();
	
	Element around(): loadDocumentHandler() {
        try {
           return proceed();
        } catch (SAXParseException err) {
            System.err.println ("ScreenFlowXmlDAO ** Parsing error" + ", line " +
                        err.getLineNumber () + ", uri " + err.getSystemId ());
            System.err.println("ScreenFlowXmlDAO error: " + err.getMessage ());
        } catch (SAXException e) {
            System.err.println("ScreenFlowXmlDAO error: " + e);
        } catch (MalformedURLException mfx) {
            System.err.println("ScreenFlowXmlDAO error: " + mfx);
        } catch (IOException e) {
            System.err.println("ScreenFlowXmlDAO error: " + e);
        } catch (Exception pce) {
            System.err.println("ScreenFlowXmlDAO error: " + pce);
        }
        return null;
    }

	void around(): internalDoEndTagHandler() {
		try {
			proceed();
		} catch (Exception ex) {
			System.err.println("InsertTag:doEndTag caught: " + ex);
			ex.printStackTrace();
		}
	}

	Screen around() throws JspTagException  : internalDoStartTagHandler() {
		try {
			return proceed();
		} catch (NullPointerException e) {
			throw new JspTagException("Error extracting Screen from session: "
					+ e);
		}
	}

	

}
