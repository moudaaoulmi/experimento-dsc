/*
 * Created on 01/09/2005
 */
package com.sun.j2ee.blueprints.opc.transitions;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionInfo;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.aspect.TransitionExceptionGenericAspect;

/**
 * @author Raquel Maranhao
 */
public aspect OPCTransitionsHandler extends TransitionExceptionGenericAspect {
	
	/*** QueueHelper (Solution2) ***/
	//private QueueConnection qConnect;
	private Map qConnect = new HashMap();
	
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
	
	/*
	after() throwing(ServiceLocatorException se) throws TransitionException : 
		setupHandler() || 
		mailCompletedOrderTDSetupHandler() || 
		mailInvoiceTransitionDelegateSetupHandler() ||
		mailOrderApprovalTransitionDelegateSetupHandler() ||
		orderApprovalTDSetupHandler() || 
		purchaseOrderTDSetupHandler() {
		throw new TransitionException(se);
	}
	*/
	
	/*
	after() throwing(JMSException je) throws TransitionException : 
		doTransitionHandler() || 
		mailCompletedOrderTDDoTransitionHandler() ||
		mailInvoiceTransitionDelegateDoTransitionHandler() || 
		mailOrderApprovalTransitionDelegateDoTransitionHandler() ||
		orderApprovalTDDoTransitionHandler() ||
		purchaseOrderTDDoTransitionHandler() {
		throw new TransitionException(je);
	}
	*/
	
/*	//Solution1: /It was necessary to refactor the local variable  QueueConnection qConnect to be a class variable
 	//Similar to a try-finally block around the method QueueHelper.sendMessage
	after(QueueHelper qh) : 
		sendMessageHandler() && target(qh) {
        try {
            if(qh.getQConnect() != null) {
                qh.getQConnect().close();
            }
        } catch(Exception e) {
        	System.err.println("OPC.QueueHelper GOT EXCEPTION closing connection" + e);
        }		
	}
*/	
	
	//Solution2: It was necessary to create an aspect  local variable QueueConnection qConnect to save temporarily the method local variable value
	//Auxiliary: to save temporarily QueueConnection value into aspect local qConnect variable
	after() returning(QueueConnection q) : 
		createQueueConnectionHandler() {
    	//Save inner method variable to local(multi-thread)
	    qConnect.put(Thread.currentThread().getName(), q);
		//qConnect = q;
	}
	
	//Similar to a try-finally block around the method QueueHelper.sendMessage
	void around() : 
		sendMessageHandler() {
		try {
			proceed();
		} finally {
	        try {
	            //Uses aspect local qConnect variable, fed above
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
