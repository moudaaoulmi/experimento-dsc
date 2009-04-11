package com.sun.j2ee.blueprints.waf.util;

import java.util.Locale;
import java.util.Vector;
import com.sun.j2ee.blueprints.util.tracer.Debug;


public aspect UtilHandler {
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** StateMachine ***/
	pointcut i18nUtil_parseKeywordsHandler() : 
		execution(public static Vector I18nUtil.parseKeywords(String));
	pointcut i18nUtil_parseKeywords2Handler() : 
		execution(public static Vector I18nUtil.parseKeywords(String,Locale));

	// ---------------------------
    // Advice's
    // ---------------------------	
	Vector around(String keywordString) : 
		i18nUtil_parseKeywordsHandler() && 
		args(keywordString){
		Vector keywords = new Vector();
		try {
			keywords = proceed(keywordString);
		} catch (Throwable e){
            Debug.print(e, "Error while parsing search string");
        }
		return keywords;
	}
	
	Vector around(String keywordString, Locale locale) : 
		i18nUtil_parseKeywords2Handler() && 
		args(keywordString, locale){
		Vector keywords = new Vector();
		try {
			keywords = proceed(keywordString, locale);
		} catch (Throwable e){
            Debug.print(e, "Error while parsing search string");
        }
		return keywords;
	}
	
}
