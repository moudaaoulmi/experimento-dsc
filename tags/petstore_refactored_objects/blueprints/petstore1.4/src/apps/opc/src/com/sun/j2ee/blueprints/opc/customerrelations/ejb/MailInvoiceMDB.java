/*
 * Copyright 2004-2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

package com.sun.j2ee.blueprints.opc.customerrelations.ejb;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.Context;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import com.sun.j2ee.blueprints.mailer.ejb.Mail;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.purchaseorder.ejb.PurchaseOrderLocal;
import com.sun.j2ee.blueprints.purchaseorder.ejb.PurchaseOrderLocalHome;
import com.sun.j2ee.blueprints.opc.ejb.TPAInvoiceXDE;
import com.sun.j2ee.blueprints.opc.transitions.MailInvoiceTransitionDelegate;
import com.sun.j2ee.blueprints.processmanager.transitions.*;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator;

/**
 * MailInvoiceMDB receives a JMS message containing an Invoice for a user order.
 * It makes a mail message that it then sends to the customer by email
 */
public class MailInvoiceMDB implements MessageDrivenBean, MessageListener {

	private static final String MAIL_SUBJECT = "Java Pet Store Order Shipped: ";
	private static final String INVOICE_STYLE_SHEET = "/com/sun/j2ee/blueprints/opc/rsrc/xsl/PartialInvoice.xsl";

	private Context context;
	private MessageDrivenContext mdc = null;
	private boolean sendConfirmationMail = false;
	private MailContentXDE mailContentXDE;
	private TransitionDelegate transitionDelegate;
	private TPAInvoiceXDE invoiceXDE = null;
	private PurchaseOrderLocalHome poHome;

	private CustomerrelationsEjbHandler ejbHandler = new CustomerrelationsEjbHandler();

	public MailInvoiceMDB() {
	}

	public void ejbCreate() {
		try {
			ServiceLocator serviceLocator = new ServiceLocator();
			sendConfirmationMail = serviceLocator
					.getBoolean(JNDINames.SEND_CONFIRMATION_MAIL);
			invoiceXDE = new TPAInvoiceXDE(serviceLocator
					.getUrl(JNDINames.XML_ENTITY_CATALOG_URL), serviceLocator
					.getBoolean(JNDINames.XML_VALIDATION_INVOICE),
					serviceLocator.getBoolean(JNDINames.XML_XSD_VALIDATION));
			poHome = (PurchaseOrderLocalHome) serviceLocator
					.getLocalHome(JNDINames.PURCHASE_ORDER_EJB);
			transitionDelegate = new MailInvoiceTransitionDelegate();
			transitionDelegate.setup();
			mailContentXDE = new MailContentXDE(INVOICE_STYLE_SHEET);
		} catch (ServiceLocatorException se) {
			this.ejbHandler.throwEJBExceptionHandler(se);
		} catch (TransitionException te) {
			this.ejbHandler.throwEJBExceptionHandler(te);
		} catch (MailContentXDE.FormatterException exception) {
			this.ejbHandler.throwEJBExceptionHandler(exception);
		} catch (XMLDocumentException xde) {
			this.ejbHandler.throwEJBExceptionHandler(xde);
		}
	}

	/**
	 * Receive a JMS Message containing the Invoice xml to generate a Mail xml
	 * message for the customer. The Mail xml mesages contain html presentation
	 */
	public void onMessage(Message recvMsg) {
		TextMessage recdTM = null;
		String recdText = null;
		try {
			recdTM = (TextMessage) recvMsg;
			recdText = recdTM.getText();
			if (sendConfirmationMail) {
				String xmlMail = doWork(recdText);
				doTransition(xmlMail);
			}
		} catch (XMLDocumentException xde) {
			this.ejbHandler.throwEJBExceptionHandler(xde);
		} catch (TransitionException te) {
			this.ejbHandler.throwEJBExceptionHandler(te);
		} catch (JMSException je) {
			this.ejbHandler.throwEJBExceptionHandler(je);
		} catch (MailContentXDE.FormatterException mfe) {
			this.ejbHandler.throwEJBExceptionHandler(mfe);
		} catch (FinderException fe) {
			this.ejbHandler.throwEJBExceptionHandler(fe);
		}
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		this.mdc = mdc;
	}

	public void ejbRemove() {
	}

	/**
	 * @param xmlMessage
	 *            is the invoice for the customer order that has shipped
	 */
	private String doWork(String xmlMessage) throws JMSException,
			XMLDocumentException, MailContentXDE.FormatterException,
			FinderException {

		invoiceXDE.setDocument(xmlMessage);
		PurchaseOrderLocal po = poHome
				.findByPrimaryKey(invoiceXDE.getOrderId());
		// build message with html formatting
		String emailAddress = po.getPoEmailId();
		String subject = MAIL_SUBJECT + invoiceXDE.getOrderId();
		mailContentXDE.setDocument(invoiceXDE.getDocument());
		mailContentXDE.setLocale(LocaleUtil.getLocaleFromString(po
				.getPoLocale()));
		String message = mailContentXDE.getDocumentAsString();
		// build mail message as xml
		Mail mailMsg = new Mail(emailAddress, subject, message);
		return mailMsg.toXML();
	}

	/**
	 * send a Mail message to mailer service, so customer gets an email
	 */
	private void doTransition(String xmlMail) throws TransitionException {
		TransitionInfo info = new TransitionInfo(xmlMail);
		transitionDelegate.doTransition(info);
	}
}
