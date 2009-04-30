package petstore.exception;


import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.opc.customerrelations.ejb.MailContentXDE;
import com.sun.j2ee.blueprints.opc.customerrelations.ejb.MailContentXDE.FormatterException;
import com.sun.j2ee.blueprints.supplier.orderfulfillment.ejb.TPASupplierOrderXDE;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;

import java.io.InputStream;

public aspect GeneralExceptionHandler {
	
	// ---------------------------
	// Declare Soft's
	// ---------------------------
	declare soft : FormatterException : getDocumentHandler() || 
										getDocumentAsStringHandler();
	declare soft : TransformerConfigurationException : internalGetTransformerHandler();

	declare soft : TransformerConfigurationException : internalGetTransformerHandler();

	// ---------------------------
	// Pointcut's
	// ---------------------------

	pointcut internalGetTransformerHandler() : 
		execution(private Transformer TPASupplierOrderXDE.internalGetTransformer(InputStream));

	pointcut internalCreateTransformerHandler() : 
		execution(private Transformer TPASupplierOrderXDE.internalCreateTransformer());

	pointcut getDocumentHandler() : 
		execution(public Source MailContentXDE.getDocument());

	pointcut getDocumentAsStringHandler() :
		execution(public String MailContentXDE.getDocumentAsString());

	// ---------------------------
	// Advice's
	// ---------------------------

    
	Object around() throws XMLDocumentException : 
				getDocumentHandler() || 
				getDocumentAsStringHandler() ||
				internalGetTransformerHandler() ||
				internalCreateTransformerHandler(){
		try {
			return proceed();
		} catch (Exception exception) {
			throw new XMLDocumentException(exception);
		}
	}
}
