package com.sun.j2ee.blueprints.waf.view.template.tags;

import javax.servlet.jsp.JspTagException;

public class TagsHandler {

	public void doStartTagHandler(NullPointerException e)
			throws JspTagException {
		throw new JspTagException("Error extracting Screen from session: " + e);
	}

	public void doEndTagHandler(Exception ex) {
		System.err.println("InsertTag:doEndTag caught: " + ex);
		ex.printStackTrace();
	}

}
