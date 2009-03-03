package com.sun.j2ee.blueprints.opc.transitions;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionInfo;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.aspect.TransitionExceptionGenericAspect;

public aspect OPCTransitionsHandler extends TransitionExceptionGenericAspect {

	private Map qConnect = new HashMap();	
	pointcut setupHandler() : 
		execution(public void InvoiceTD.setup());	
	pointcut doTransitionHandler() : 
		execution(public void InvoiceTD.doTransition(TransitionInfo));
	pointcut mailCompletedOrderTDSetupHandler() : 
		execution(public void MailCompletedOrderTD.setup());		
	pointcut mailCompletedOrderTDDoTransitionHandler() : 
		execution(public void MailCompletedOrderTD.doTransition(TransitionInfo));	
	pointcut mailInvoiceTransitionDelegateSetupHandler() :
		execution(public void MailInvoiceTransitionDelegate.setup());	
	pointcut mailInvoiceTransitionDelegateDoTransitionHandler() : 	
		execution(public void MailInvoiceTransitionDelegate.doTransition(TransitionInfo));	
	pointcut mailOrderApprovalTransitionDelegateSetupHandler() : 
		execution(public void MailOrderApprovalTransitionDelegate.setup());	
	pointcut mailOrderApprovalTransitionDelegateDoTransitionHandler() : 
		execution(public void MailOrderApprovalTransitionDelegate.doTransition(TransitionInfo));		
	pointcut orderApprovalTDSetupHandler() : 
		execution(public void OrderApprovalTD.setup());	
	pointcut orderApprovalTDDoTransitionHandler() : 
		execution(public void OrderApprovalTD.doTransition(TransitionInfo));	
	pointcut purchaseOrderTDSetupHandler() : 
		execution(public void PurchaseOrderTD.setup());	
	pointcut purchaseOrderTDDoTransitionHandler() : 
		execution(public void PurchaseOrderTD.doTransition(TransitionInfo));	
	pointcut sendMessageHandler() : 
		execution(public void QueueHelper.sendMessage(String));
	pointcut createQueueConnectionHandler() : 
		call(* QueueConnectionFactory.createQueueConnection()) && within(QueueHelper);	
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
	after() returning(QueueConnection q) : 
		createQueueConnectionHandler() {    
	    qConnect.put(Thread.currentThread().getName(), q);
	}	
	void around() : 
		sendMessageHandler() {
		try {
			proceed();
		} finally {
	        try {	           
	            QueueConnection qConnectAux = (QueueConnection)qConnect.get(Thread.currentThread().getName());	            
	        	if(qConnectAux != null) {
	        	    qConnectAux.close();
	            }
	        } catch(Exception e) {
	        	System.err.println("OPC.QueueHelper GOT EXCEPTION closing connection" + e);
	        }		
		}
	}
}