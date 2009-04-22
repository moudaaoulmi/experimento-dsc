/*
 * Created on 01/09/2005
 */
package com.sun.j2ee.blueprints.opc.transitions;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionInfo;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.aspect.TransitionExceptionGenericAspect;

/**
 * @author Raquel Maranhao
 */
public aspect OPCTransitionsHandler extends TransitionExceptionGenericAspect {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : ServiceLocatorException : setupHandler() || 
											 mailCompletedOrderTDSetupHandler() || 
											 mailInvoiceTransitionDelegateSetupHandler() || 
											 mailOrderApprovalTransitionDelegateSetupHandler() || 
											 orderApprovalTDSetupHandler() || 
											 purchaseOrderTDSetupHandler();
	declare soft : JMSException : doTransitionHandler() || 
								  mailCompletedOrderTDDoTransitionHandler() || 
								  mailInvoiceTransitionDelegateDoTransitionHandler() || 
								  mailOrderApprovalTransitionDelegateDoTransitionHandler() || 
								  orderApprovalTDDoTransitionHandler() || 
								  purchaseOrderTDDoTransitionHandler();
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** InvoiceTD ***/
	pointcut setupHandler() : 
		execution(public void InvoiceTD.setup());
	
	pointcut doTransitionHandler() : 
		execution(public void InvoiceTD.doTransition(TransitionInfo));
	
	/*** MailCompletedOrderTD ***/
	pointcut mailCompletedOrderTDSetupHandler() : 
		execution(public void MailCompletedOrderTD.setup());
		
	pointcut mailCompletedOrderTDDoTransitionHandler() : 
		execution(public void MailCompletedOrderTD.doTransition(TransitionInfo));
	
	/*** MailInvoiceTransitionDelegate ***/
	pointcut mailInvoiceTransitionDelegateSetupHandler() :
		execution(public void MailInvoiceTransitionDelegate.setup());
	
	pointcut mailInvoiceTransitionDelegateDoTransitionHandler() : 	
		execution(public void MailInvoiceTransitionDelegate.doTransition(TransitionInfo));
	
	/*** MailOrderApprovalTransitionDelegate ***/
	pointcut mailOrderApprovalTransitionDelegateSetupHandler() : 
		execution(public void MailOrderApprovalTransitionDelegate.setup());
	
	pointcut mailOrderApprovalTransitionDelegateDoTransitionHandler() : 
		execution(public void MailOrderApprovalTransitionDelegate.doTransition(TransitionInfo));
	
	/*** OrderApprovalTD ***/
	pointcut orderApprovalTDSetupHandler() : 
		execution(public void OrderApprovalTD.setup());
	
	pointcut orderApprovalTDDoTransitionHandler() : 
		execution(public void OrderApprovalTD.doTransition(TransitionInfo));
	
	/*** PurchaseOrderTD ***/
	pointcut purchaseOrderTDSetupHandler() : 
		execution(public void PurchaseOrderTD.setup());
	
	pointcut purchaseOrderTDDoTransitionHandler() : 
		execution(public void PurchaseOrderTD.doTransition(TransitionInfo));
	
	/*** QueueHelper ***/
	pointcut sendMessageHandler() : 
		execution(* QueueHelper.internalSendMessage(..));

    public pointcut afterServiceLocatorExceptionHandler() : 
        setupHandler() || 
		mailCompletedOrderTDSetupHandler() || 
		mailInvoiceTransitionDelegateSetupHandler() ||
		mailOrderApprovalTransitionDelegateSetupHandler() ||
		orderApprovalTDSetupHandler() || 
		purchaseOrderTDSetupHandler();
    
    public pointcut afterJMSExceptionHandler() : 
        doTransitionHandler() || 
		mailCompletedOrderTDDoTransitionHandler() ||
		mailInvoiceTransitionDelegateDoTransitionHandler() || 
		mailOrderApprovalTransitionDelegateDoTransitionHandler() ||
		orderApprovalTDDoTransitionHandler() ||
		purchaseOrderTDDoTransitionHandler();
	
	// ---------------------------
    // Advice's
    // --------------------------- 
	void around(String xmlMessage,
			QueueConnection qConnect, QueueSession session, QueueSender qSender) 
	throws JMSException : 
		sendMessageHandler()  &&
		args(xmlMessage, qConnect, session, qSender){
		try {
			proceed(xmlMessage, qConnect, session, qSender);
		} finally {
	            try {
	                if(qConnect != null) {
	                    qConnect.close();
	                }
	            } catch(Exception e) {
	            System.err.println("OPC.QueueHelper GOT EXCEPTION closing connection" + e);
	          }
	   }	
	}
}
		
	


