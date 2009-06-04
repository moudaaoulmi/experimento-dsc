package com.sun.j2ee.blueprints.waf.view.taglibs.smart;

import javax.servlet.jsp.JspTagException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler
public class SmartHandler extends GeneralException{

	public void doEndTag1Handler(String msg,Exception e) throws JspTagException{
		throw new JspTagException(msg + e);
	}
	
	public void doEndTag2Handler(String msg) {
		System.err.println(msg);
	}
	
	public void doEndTag3Handler(Exception e) {
		System.err.println("ClientStateTag caught: " + e);
	}
}
