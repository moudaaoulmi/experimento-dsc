package ish.ecletex.wordnet.jwnl;

import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Document;

public aspect JWNLHandler {
	
	
	pointcut internalInitializeHandler(): execution(private static Document JWNL.internalInitialize(InputStream,Document));
	pointcut internalInitializeHandler2(): execution(private static void JWNL.internalInitialize(InputStream));
	
	declare soft: IOException :  internalInitializeHandler2();
	declare soft: Exception : internalInitializeHandler();
	
	

	Document around() throws JWNLException  : internalInitializeHandler() {
		try {
			return proceed();
		} catch (Exception ex) {
			throw new JWNLException("JWNL_EXCEPTION_002", ex);
		}		
	}

	void around() throws JWNLException : internalInitializeHandler2() {
		try {
			proceed();
		} catch (IOException ex) {
			throw new JWNLException("JWNL_EXCEPTION_001", ex);
		}
	}

}
