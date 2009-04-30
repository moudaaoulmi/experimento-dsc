package com.sun.j2ee.blueprints.waf.util;

import com.sun.j2ee.blueprints.util.tracer.Debug;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;
@ExceptionHandler


public class UtilHandler extends GeneralException{
	
	public void parseKeywordsHandler(Throwable e) {
		Debug.print(e, "Error while parsing search string");
	}

}
