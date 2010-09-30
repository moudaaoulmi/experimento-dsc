package com.sun.j2ee.blueprints.taglibs.smart;

import java.io.IOException;
import java.util.TreeMap;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import adventure.exception.SystemErroLog;
import br.upe.dsc.reusable.exception.ILogObject;
import br.upe.dsc.reusable.exception.LogAbstractHandler;

@ExceptionHandler
public privileged aspect SmartHandler extends LogAbstractHandler {

	pointcut doEndTagHandler2(): execution(public int SelectTag.doEndTag());

	pointcut doEndTagHandler1(): execution(public int InputTag.doEndTag());

	pointcut doTagHandler(): execution(public void CheckboxTag.doTag());

	pointcut internalDoEndTagHandler2(): execution(private void ClientStateTag.internalDoEndTag(StringBuffer));

	pointcut internalDoEndTagHandler1(): execution(private void CacheTag.internalDoEndTag(BodyContent , String));

	pointcut internalDoEndTagHandler(): execution(private void CacheTag.internalDoEndTag());

	pointcut internalDoEndTagHandler3(): execution(private void ClientStateTag.internalDoEndTag(StringBuffer, String, Object));

	pointcut internalDoEndTagHandler4(): execution(private void ClientStateTag.internalDoEndTag());

	pointcut doEndTagHandler(): execution(public int FormTag.doEndTag());

	declare soft: IOException :internalDoEndTagHandler() || internalDoEndTagHandler1() || doTagHandler() || internalDoEndTagHandler2() || internalDoEndTagHandler3() || doEndTagHandler() || doEndTagHandler1() || doEndTagHandler2();
	declare soft: ClassNotFoundException :internalDoEndTagHandler4();

	// Inserir o this.msg[thisEnclosingJoinPointStaticPart.getId()]

	Object around() throws JspTagException  : doEndTagHandler2() || doEndTagHandler1() || doTagHandler() {
		try {
			return proceed();
		} catch (IOException e) {
			throw new JspTagException("" + e.getMessage());
		}
	}

	int around(FormTag tag) throws JspTagException  : doEndTagHandler() && this(tag) {
		try {
			return proceed(tag);
		} catch (IOException e) {
			throw new JspTagException("FormTag: " + e.getMessage());
		} finally {
			// force the tag values to be reset
			tag.validatedFields = new TreeMap();
			tag.name = null;
			tag.action = null;
			tag.method = null;
			tag.formHTML = null;
		}
	}

	
	public pointcut checkedExceptionLog(): internalDoEndTagHandler2()|| internalDoEndTagHandler1()|| internalDoEndTagHandler()||internalDoEndTagHandler3()|| internalDoEndTagHandler4();
	
	public pointcut exceptionLog();

	public String getMessageText(int pointcutId) {
		switch (pointcutId) {
		case 0:
			return "SelectTag: ";
		case 1:
			return "InputTag: ";
		case 2:
			return "CheckboxTag: ";
		case 3:
			return "ClientStateTag: Problems with writing...";
		case 4:
		case 5:
			return "Problems with writing...";
		case 6:
		case 7:
			return "ClientStateTag caught: ";
		default:
			return "";
		}

	}

	public ILogObject getLogObject(){
		return new SystemErroLog();
	}

}
