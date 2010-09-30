package com.sun.j2ee.blueprints.opc.workflowmanager.handlers;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.jms.Message;
import com.sun.j2ee.blueprints.opc.invoice.Invoice;
import com.sun.j2ee.blueprints.opc.invoice.XMLException;
import com.sun.j2ee.blueprints.opc.purchaseorder.PurchaseOrder;
import com.sun.j2ee.blueprints.processmanager.ejb.OrderStatusNames;

//@ExceptionHandler
public privileged aspect HandlersHandler {

	Map opcPoIDMap = new HashMap();
	Map poMap = new HashMap();
	Map mailMap = new HashMap();

	String[] msg = {
			"OPC Exception creating POHandler",
			"OPC Exception sending mail",
			"OPC Exception creating InvoiceHandler"
	};
	

	// Pointcuts	

	pointcut pOHandlerHandler(): execution(public POHandler.new());
	pointcut internalSendOrderCompletedMailHandler(): execution(private void InvoiceHandler.internalSendOrderCompletedMail(String, boolean));
	pointcut invoiceHandler(): execution(public InvoiceHandler.new());
	pointcut internalHandleHandler(): execution(private void InvoiceHandler.internalHandle(Message, Invoice, String ,	String));
	pointcut internalHandleHandler1(): execution(private void POHandler.internalHandle(Message, PurchaseOrder, String , String , boolean));
	pointcut getOpcPoIDHandler(): call(public String Invoice.getOpcPoId());
	pointcut getPoHandler(): call(String PurchaseOrder.getPoId());
	pointcut getMailHandler(): call(String PurchaseOrder.getEmailId());

	declare soft: Exception: invoiceHandler()|| internalHandleHandler() || internalSendOrderCompletedMailHandler() || pOHandlerHandler() || internalHandleHandler1();
	declare soft: CreateException: internalHandleHandler1();
	declare soft: RemoteException: internalHandleHandler1();

	void around(POHandler po, boolean sendMail) throws HandlerException  : internalHandleHandler1() && this(po) && args(.., sendMail){
		try {
			proceed(po, sendMail);
		} catch (CreateException ce) {
			// call CRM to notify the customer
			String emailID = (String)this.mailMap.get(Thread.currentThread().getName());
			if (sendMail) {
				String subject = "Problems processing your Adventure Builder order";
				String msg = "We had problems processing your Adventure Builder order.";
				msg += " Please resubmit the order";
				po.sendMail(emailID, subject, msg);
			}
		} catch (RemoteException re) {
			// call process manager and set error status
			String poID = (String) this.poMap.get(Thread.currentThread().getName());
			try {
				po.processManager.updateStatus(poID,
						OrderStatusNames.PAYMENT_PROCESSING_ERROR);
				po.processManager.updateOrderErrorStatus(poID, true);
			} catch (FinderException fe) {
				System.err.println(fe);
			}
		} catch (Exception exe) {
			System.err.println(exe);
			throw new HandlerException("OPC Exception handling PO");
		}
	}
	
	String around(): withincode(private void POHandler.internalHandle(Message, PurchaseOrder, String , String , boolean)) && getMailHandler(){
		String str = proceed();
		this.mailMap.put(Thread.currentThread().getName(), str);
		return str;
	}
	
	String around(): withincode(private void POHandler.internalHandle(Message, PurchaseOrder, String , String , boolean)) && getPoHandler(){
		String str = proceed();
		this.poMap.put(Thread.currentThread().getName(), str);
		return str;
	}

//Inserir o this.msg[thisEnclosingJoinPointStaticPart.getId()]
	Object around() throws HandlerException  : pOHandlerHandler() || internalSendOrderCompletedMailHandler() || invoiceHandler()  {
		try {
			return proceed();
		} catch (Exception exe) {
			System.err.println(exe);
			throw new HandlerException(this.msg[0]);
		}
	}	
	
	
	void around(InvoiceHandler inv) throws HandlerException  : internalHandleHandler() && this(inv) {
		try {
			proceed(inv);
		} catch (XMLException exe) {
			String opcPoID = (String) this.opcPoIDMap.get(Thread.currentThread()
					.getName());
			System.err.println(exe);
			// call process manager and set error status
			try {
				/*
				 * The status is set to invoice xml error, indicating that an
				 * error occurred while deserializing the invoice. Advanced
				 * error handling will be done for a later release, with the
				 * status having additional information about the
				 * supplier(lodging, activity, or airline)
				 */
				inv.processManager.updateStatus(opcPoID,
						OrderStatusNames.INVOICE_XML_ERROR);
				inv.processManager.updateOrderErrorStatus(opcPoID, true);
			} catch (Exception xe) {
				System.err.println(xe);
			}
		} catch (Exception exe) {
			System.err.println(exe);
			throw new HandlerException("OPC Exception handling invoice");
		}
	}
	
	

	String around(): withincode(private void InvoiceHandler.internalHandle(Message, Invoice, String ,	String)) && getOpcPoIDHandler(){
		String str = proceed();
		this.opcPoIDMap.put(Thread.currentThread().getName(), str);
		return str;
	}
	
	
}
