package com.sun.j2ee.blueprints.opc.mailer;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

//Reuso nível III
//@ExceptionHandler
public privileged aspect MailerHandler extends EmptyBlockAbstractExceptionHandling {

	pointcut internalConstrutorHandler(): execution(private void ByteArrayDataSource.internalConstrutor(String));

	pointcut createAndSendMailHandler(): execution(public void MailHelper.createAndSendMail(String, String ,String , Locale ));

	public pointcut emptyBlockException(): internalConstrutorHandler();
	
	
	declare soft: UnsupportedEncodingException : internalConstrutorHandler();
	declare soft: Exception :createAndSendMailHandler();

	void around() throws MailerException : createAndSendMailHandler()  {
		try {
			proceed();
		} catch (Exception e) {
			System.err.println("MailHelper caught: " + e);
			e.printStackTrace();
			throw new MailerException("Failure while sending mail:" + e);
		}
	}	


}
