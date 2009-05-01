/*
 * Created on 01/09/2005
 */
package com.sun.j2ee.blueprints.opc.transitions;

import java.util.HashMap;
import java.util.Map;
import com.sun.j2ee.blueprints.util.aspect.TransitionExceptionGenericAspect;
import javax.jms.QueueConnection;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import com.sun.j2ee.blueprints.processmanager.transitions.TransitionInfo;
import petstore.exception.ExceptionHandler;
/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect OPCTransitionsHandler extends TransitionExceptionGenericAspect {
	 //private QueueConnection qConnect;
	private Map qConnect = new HashMap();
	   
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
	pointcut createQueueConnectionHandler() : 
		call(* QueueConnectionFactory.createQueueConnection()) && within(QueueHelper);
	
	pointcut sendMessageHandler() : 
		execution(public void QueueHelper.sendMessage(String));


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
		QueueConnection around(): createQueueConnectionHandler(){
			QueueConnection q = proceed();
		    qConnect.put(Thread.currentThread().getName(), q);
		    return q;
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
		
	


