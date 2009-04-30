package petstore.exception;


import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;
import com.sun.j2ee.blueprints.opc.customerrelations.ejb.MailContentXDE;
import com.sun.j2ee.blueprints.opc.customerrelations.ejb.MailContentXDE.FormatterException;
import com.sun.j2ee.blueprints.supplier.inventory.web.RcvrRequestProcessor;
import com.sun.j2ee.blueprints.supplier.inventory.web.DisplayInventoryBean;
import com.sun.j2ee.blueprints.supplier.orderfulfillment.ejb.TPASupplierOrderXDE;
import com.sun.j2ee.blueprints.petstore.controller.ejb.actions.CustomerEJBAction;
import com.sun.j2ee.blueprints.petstore.controller.ejb.actions.OrderEJBAction;
import com.sun.j2ee.blueprints.petstore.controller.ejb.ShoppingControllerLocal;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.signon.ejb.SignOnEJB;
import com.sun.j2ee.blueprints.petstore.controller.ejb.actions.SignOnEJBAction;
import com.sun.j2ee.blueprints.customer.ejb.CustomerLocal;
import com.sun.j2ee.blueprints.waf.controller.ejb.EJBControllerLocal;
import com.sun.j2ee.blueprints.waf.event.EventResponse;
import com.sun.j2ee.blueprints.waf.event.Event;
import com.sun.j2ee.blueprints.uidgen.ejb.UniqueIdGeneratorLocal;
import com.sun.j2ee.blueprints.purchaseorder.ejb.PurchaseOrder;
import com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.controller.web.PetstoreComponentManager;
import com.sun.j2ee.blueprints.waf.controller.web.DefaultComponentManager;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.servlet.http.HttpSession;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;

import java.io.InputStream;
import java.util.Collection;


public aspect GeneralExceptionHandler {
	
	// ---------------------------
	// Declare Soft's
	// ---------------------------

	declare soft : FormatterException : getDocumentHandler() || 
										getDocumentAsStringHandler();
	
	declare soft : TransformerConfigurationException : internalGetTransformerHandler();

	declare soft : TransformerConfigurationException : internalGetTransformerHandler();

	declare soft : FinderException : signOnEJB_authenticateHandler() ||
									 internalGetCustomerHandler() || 
									 performSignOnHandler();
	
	declare soft : ServiceLocatorException : getInventoryHandler() || 
	 										 initHandler() ||
	 										 sendInvoicesHandler() ||
	 										 internalSendAMessageHandler() ||
	 										 getUniqueIdGeneratorHandler() ||
	 										 getShoppingControllerHandler() ||
	 										 defaultComponentManager_getEJBControllerHandler();
	
	declare soft : CreateException : getUniqueIdGeneratorHandler() ||
									 initHandler() ||
									 getShoppingControllerHandler() ||
									 defaultComponentManager_getEJBControllerHandler();
	
	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut getShoppingControllerHandler() :
		execution(public ShoppingControllerLocal PetstoreComponentManager.getShoppingController(HttpSession));
	
	pointcut defaultComponentManager_getEJBControllerHandler() : 
		execution(public EJBControllerLocal DefaultComponentManager.getEJBController(HttpSession));
	
	pointcut internalGetTransformerHandler() : 
		execution(private Transformer TPASupplierOrderXDE.internalGetTransformer(InputStream));

	pointcut internalCreateTransformerHandler() : 
		execution(private Transformer TPASupplierOrderXDE.internalCreateTransformer());

	pointcut getDocumentHandler() : 
		execution(public Source MailContentXDE.getDocument());

	pointcut getDocumentAsStringHandler() :
		execution(public String MailContentXDE.getDocumentAsString());
	
	pointcut signOnEJB_authenticateHandler() : 
		execution(public boolean SignOnEJB.authenticate(String, String));

	pointcut performSignOnHandler() : 
		execution(public EventResponse SignOnEJBAction.perform(Event));
	
	pointcut internalGetCustomerHandler() : 
		execution(private CustomerLocal CustomerEJBAction.internalGetCustomer());
	
	pointcut sendInvoicesHandler() : 
		execution(private void RcvrRequestProcessor.sendInvoices(Collection));
	
	pointcut initHandler() : 
		execution(public void RcvrRequestProcessor.init());
	
	pointcut getInventoryHandler() : 
		execution(public Collection DisplayInventoryBean.getInventory());
	
	pointcut getUniqueIdGeneratorHandler() : 
		execution(private UniqueIdGeneratorLocal OrderEJBAction.getUniqueIdGenerator());
	
	pointcut internalSendAMessageHandler() : 
		execution(private void OrderEJBAction.internalSendAMessage(PurchaseOrder));

	// ---------------------------
	// Advice's
	// ---------------------------
	Object around() throws GeneralFailureException : 
		getShoppingControllerHandler() ||
		defaultComponentManager_getEJBControllerHandler(){
		try{
			return proceed();
		}catch(CreateException ce){
			throw new GeneralFailureException(ce.getMessage());			
		}catch(ServiceLocatorException ne){
			throw new GeneralFailureException(ne.getMessage());			
		}
	}
	
	Object around() : getUniqueIdGeneratorHandler() ||
					  initHandler(){
		try {
			return proceed();
		} catch (CreateException cx) {
			cx.printStackTrace();
			return null;
		}
	}
	
	Object around() :  sendInvoicesHandler()|| 
					   initHandler() || 
					   getInventoryHandler() ||
					   internalSendAMessageHandler() ||
					   getUniqueIdGeneratorHandler(){
		Object result = null;
		try {
			result = proceed();
		} catch (ServiceLocatorException se) {
			se.printStackTrace();
		}
		return result;
	}
	
	Object around() throws XMLDocumentException : 
				getDocumentHandler() || 
				getDocumentAsStringHandler() ||
				internalGetTransformerHandler() ||
				internalCreateTransformerHandler(){
		try {
			return proceed();
		} catch (Exception exception) {
			throw new XMLDocumentException(exception);
		}
	}
	
	Object around() : signOnEJB_authenticateHandler() ||
					  internalGetCustomerHandler() || 
					  performSignOnHandler(){
		try {
			return proceed();
		} catch (FinderException fe) {
			return null; 
		}
	}
}