package com.sun.j2ee.blueprints.waf.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import adventure.exception.ExceptionHandler;

//@ExceptionHandler
public privileged aspect UtilHandler {

	pointcut internalConvertJISEncodingHandler(): execution(private static String I18nUtil.internalConvertJISEncoding(ByteArrayOutputStream ,	String ));
	declare soft: UnsupportedEncodingException :  internalConvertJISEncodingHandler();
	
	String around(String convertedString): internalConvertJISEncodingHandler() && args(* , convertedString) {
		try {
			return proceed(convertedString);
		} catch (UnsupportedEncodingException uex) {
		}
		return convertedString;
	}

	

}
