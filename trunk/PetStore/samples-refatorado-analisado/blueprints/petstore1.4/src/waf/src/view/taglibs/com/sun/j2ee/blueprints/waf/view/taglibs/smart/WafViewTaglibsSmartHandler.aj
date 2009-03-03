/*
 * Created on 02/10/2005
 */
package com.sun.j2ee.blueprints.waf.view.taglibs.smart;

import java.io.IOException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * @author Raquel Maranhao
 */
public aspect WafViewTaglibsSmartHandler {

	
	pointcut doEndTagHandler() : 
		execution(public int FormTag.doEndTag());
	pointcut internalDoEndTag1Handler() : 
		execution(private void CacheTag.internalDoEndTag1(BodyContent, String));
	pointcut internalDoEndTag2Handler() : 
		execution(private void CacheTag.internalDoEndTag2());
	pointcut checkboxTagDoEndTagHandler() : 
		execution(public int CheckboxTag.doEndTag());
	pointcut clientStateTagInternalDoEndTag1Handler() : 
		execution(private Class ClientStateTag.internalDoEndTag1());
	pointcut clientStateTagInternalDoEndTag2Handler() : 
		execution(private void ClientStateTag.internalDoEndTag2(StringBuffer, Object, String));
	pointcut clientStateTagInternalDoEndTag3Handler() : 
		execution(private void ClientStateTag.internalDoEndTag3(StringBuffer));
	pointcut inputTagDoEndTagHandler() : 
		execution(public int InputTag.doEndTag());
	pointcut selectTagDoEndTag() : 
		execution(public int SelectTag.doEndTag());
	declare soft : IOException : doEndTagHandler() || 
		internalDoEndTag1Handler() ||
		internalDoEndTag2Handler() || 
		checkboxTagDoEndTagHandler() || 
		clientStateTagInternalDoEndTag2Handler() ||
		clientStateTagInternalDoEndTag3Handler() || 
		inputTagDoEndTagHandler() || 
		selectTagDoEndTag();
	declare soft : ClassNotFoundException : clientStateTagInternalDoEndTag1Handler();
	void around() : internalDoEndTag1Handler() ||
		internalDoEndTag2Handler() {
		try {
			proceed();
		} catch (IOException ioe) {
		  System.err.println("ChacheTag: Problems with writing...");
		}    			
	}
	Class around() : clientStateTagInternalDoEndTag1Handler() {
		try {
			return proceed();
		} catch (ClassNotFoundException cnf) {
			System.err.println("ClientStateTag caught: " + cnf);
			return null;
		}    	
	}
	void around() : clientStateTagInternalDoEndTag2Handler() {
		try {
			proceed();
		} catch (IOException iox) {
		    System.err.println("ClientStateTag caught: " + iox);
		}
	}	
	void around() : clientStateTagInternalDoEndTag3Handler() {
		try {
			proceed();
        } catch (IOException ioe) {
        	System.err.println("ClientStateTag: Problems with writing...");
        }
	}
	after() throwing(IOException e) throws JspTagException : 
		doEndTagHandler() ||
		checkboxTagDoEndTagHandler() ||
		inputTagDoEndTagHandler() || 
		selectTagDoEndTag() {
		throw new JspTagException((!thisJoinPoint.getClass().getName().equals("SelectTag")?thisJoinPoint.getClass().getName():"LinkTag") + ": " + e.getMessage());		
    }	
}
