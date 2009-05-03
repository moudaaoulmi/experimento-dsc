package com.sun.j2ee.blueprints.opc.customerrelations.ejb;

import java.util.Locale;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.opc.customerrelations.ejb.MailContentXDE.FormatterException;
import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler
public class CustomerrelationsEjbHandler extends GeneralException {

	public void throwFormatterException(Exception e)
			throws MailContentXDE.FormatterException {
		throw new MailContentXDE.FormatterException(e);
	}

	public void getTransformerHandler(Locale locale) throws FormatterException {
		throw new FormatterException("No style sheet found for locale: "
				+ locale);
	}

	public void throwErrAndEjbExceptionHandler(Exception e) throws EJBException {
		System.err.println(e.toString());
		throw new EJBException(e);
	}

}
