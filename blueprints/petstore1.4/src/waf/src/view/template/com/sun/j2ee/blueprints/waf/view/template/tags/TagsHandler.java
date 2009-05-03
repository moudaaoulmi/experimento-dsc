package com.sun.j2ee.blueprints.waf.view.template.tags;

import javax.servlet.jsp.JspTagException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler



public class TagsHandler extends GeneralException{

	public void doStartTagHandler(NullPointerException e)
			throws JspTagException {
		throw new JspTagException("Error extracting Screen from session: " + e);
	}

	public void doEndTagHandler(Exception ex) {
		System.err.println("InsertTag:doEndTag caught: " + ex);
		ex.printStackTrace();
	}

}
