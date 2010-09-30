/* Copyright 2004 Sun Microsystems, Inc.  All rights reserved.  You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: 
 http://adventurebuilder.dev.java.net/LICENSE.txt
 $Id: InsertTag.java,v 1.2 2004/05/26 00:07:35 inder Exp $ */

package com.sun.j2ee.blueprints.waf.view.template;

// J2EE Imports
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.sun.j2ee.blueprints.waf.view.template.Parameter;
import com.sun.j2ee.blueprints.waf.view.template.Screen;

// WAF imports
import com.sun.j2ee.blueprints.waf.controller.web.util.WebKeys;

/**
 * This class is works with a template jsp page to build a composite view of a
 * page.
 */

public class InsertTag extends TagSupport {

	private boolean directInclude = false;
	private String parameter = null;
	private Parameter parameterRef = null;

	/**
	 * default constructor
	 */
	public InsertTag() {
		super();
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public int doStartTag() throws JspTagException {
		internalDoStartTag();
		Screen screen = null;
		// load the ScreenFlow
		screen = internalDoStartTag(screen);
		if ((screen != null) && (parameter != null)) {
			parameterRef = (Parameter) screen.getParameter(parameter);
		} else {
			System.err.println("InsertTag: screenManager is null");
		}
		if (parameterRef != null)
			directInclude = parameterRef.isDirect();
		return SKIP_BODY;
	}

	private Screen internalDoStartTag(Screen screen) throws JspTagException {
		screen = (Screen) pageContext.getRequest().getAttribute(
				WebKeys.CURRENT_SCREEN);
		return screen;
	}

	private void internalDoStartTag() {
		pageContext.getOut().flush();
	}

	public int doEndTag() throws JspTagException {
		internalDoEndTag();
		// reset everything in that this tag may be pooled
		parameterRef = null;
		parameter = null;
		directInclude = false;
		return EVAL_PAGE;
	}

	private void internalDoEndTag() {
		if (directInclude && parameterRef != null) {
			pageContext.getOut().print(parameterRef.getValue());
		} else if (parameterRef != null) {
			if (parameterRef.getValue() != null)
				pageContext.getRequest().getRequestDispatcher(
						parameterRef.getValue()).include(
						pageContext.getRequest(), pageContext.getResponse());
		}
	}
}
