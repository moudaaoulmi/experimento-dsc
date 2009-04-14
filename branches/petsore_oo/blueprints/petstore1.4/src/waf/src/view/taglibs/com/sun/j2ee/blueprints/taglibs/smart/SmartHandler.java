package com.sun.j2ee.blueprints.waf.view.taglibs.smart;

import javax.servlet.jsp.JspTagException;


public class SmartHandler {

	public void doEndTag1Handler(Exception e) throws JspTagException{
		throw new JspTagException("LinkTag: " + e.getMessage());
	}
	
	public void doEndTag2Handler(Exception e) throws JspTagException{
		throw new JspTagException("InputTag: " + e.getMessage());
	}
	
	public void doEndTag3Handler(Exception e) throws JspTagException{
		throw new JspTagException("FormTag: " + e.getMessage());
	}
	
	public void doEndTag4Handler(Exception e) throws JspTagException{
		System.err.println("ClientStateTag caught: " + e);
	}
	
	public void doEndTag5Handler() {
		System.err.println("ClientStateTag: Problems with writing...");
	}
	
	public void doEndTag6Handler(Exception e) throws JspTagException{
		throw new JspTagException("CheckboxTag: " + e.getMessage());
	}
	
	public void doEndTag7Handler() {
		System.err.println("ChacheTag: Problems with writing...");
	}
}
