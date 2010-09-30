package com.sun.j2ee.blueprints.opc.webservicebroker.provider;

import java.io.IOException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import br.upe.dsc.reusable.exception.GenericAbstractHandler;

@ExceptionHandler
public privileged aspect ProviderHandler extends GenericAbstractHandler{

	pointcut getClassPathSourceHandler(): execution(private InputSource BrokerEntityResolver.getClassPathSource(String));	
	pointcut internalAddTransformerHandler(): execution(private void BrokerTransformer.internalAddTransformer(String, StreamSource) );
	pointcut internalTransformHandler(): execution(private void BrokerTransformer.internalTransform());
	
	declare soft: Exception: getClassPathSourceHandler();	
	declare soft: TransformerConfigurationException: internalAddTransformerHandler();
	declare soft: IOException:  internalTransformHandler();
	declare soft: SAXException: internalTransformHandler();

		
	
	public pointcut checkedExceptionLog():internalTransformHandler()||internalAddTransformerHandler();
	
	public void getMessageText(int pointcutId,Exception e){
		
		switch (pointcutId) {
		case 0:
			System.err.println("BrokerTransformer error: " + e);
			break;
		case 1:
			// Vazio
			break;
		default:
			break;
		}
		
	}

	InputSource around(String name): getClassPathSourceHandler() && args(name) {
		try {
			return proceed(name);
		} catch (Exception e) {
			System.err.println("BrokerEntityResolver error resolving: " + name);
			e.printStackTrace();
		}
		// default to the default entity resolver
		return null;
	}

}
