package com.sun.j2ee.blueprints.waf.util;

import com.sun.j2ee.blueprints.util.tracer.Debug;

public class UtilHandler {
	
	public void parseKeywordsHandler(Throwable e) {
		Debug.print(e, "Error while parsing search string");
	}

}
