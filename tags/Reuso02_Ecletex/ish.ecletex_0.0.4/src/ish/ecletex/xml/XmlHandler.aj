package ish.ecletex.xml;

import java.io.InputStream;

public privileged aspect XmlHandler {

	pointcut getIntValueHandler() : execution(public int Attribute.getIntValue());

	pointcut internalPopElementHandler() : execution(private void Parser.internalPopElement());

	pointcut internalNewElementHandler() : execution(private void Parser.internalNewElement(String , String , int));

	pointcut internalParseHandler() : execution(private void Parser.internalParse(InputStream , StringBuffer));
	
	

	declare soft: Exception: getIntValueHandler() || internalPopElementHandler() || internalNewElementHandler() || internalParseHandler();
	
	

	void around() throws Error : internalParseHandler(){
		try {
			proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void around(String s2) throws Error : internalNewElementHandler() && args(s2,..){
		try {
			proceed(s2);
		} catch (Exception e) {
			throw new Error("Cannot parse element '" + s2 + "' - (" + e + ")");
		}
	}

	void around(Parser parser): internalPopElementHandler() && this(parser){
		try {
			proceed(parser);
		} catch (Exception e) {
			parser.topElement = null;
		}
	}

	int around(Attribute att): getIntValueHandler() && this(att){
		try {
			return proceed(att);
		} catch (Exception e) {
			throw new Error("Attribute '" + att.name + "' has value '"
					+ att.value + "' which is not an integer");
		}
	}

}
