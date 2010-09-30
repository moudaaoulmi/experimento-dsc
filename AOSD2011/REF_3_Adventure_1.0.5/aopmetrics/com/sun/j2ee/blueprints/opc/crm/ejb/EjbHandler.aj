package com.sun.j2ee.blueprints.opc.crm.ejb;


import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Message;
import com.sun.j2ee.blueprints.opc.mailer.MailerException;

//@ExceptionHandler
public privileged aspect EjbHandler {

	pointcut internalOnMessageHandler(): execution(private void CrmBean.internalOnMessage(Message, String));
	pointcut internalDoWorkHandler(): execution(private void CrmBean.internalDoWork(String, String , String));	

	declare soft: JMSException: internalOnMessageHandler();
	declare soft: MailerException: internalDoWorkHandler();
	declare soft: Exception: internalDoWorkHandler();	
	
	void around(): internalDoWorkHandler() {
		try {
			proceed();
		} catch (MailerException mx) {
			System.err.println("CrmBean: caught=" + mx);
			mx.printStackTrace();
		} catch (Exception ex) {
			System.err.println("CrmBean: caught=" + ex);
			ex.printStackTrace();
		}
	}

	void around() throws EJBException : internalOnMessageHandler()	{
		try {
			proceed();
		} catch (JMSException je) {
			throw new EJBException(je);
		}
	}

}
