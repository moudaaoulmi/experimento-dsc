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
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.controller.web.DefaultComponentManager;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.servlet.http.HttpSession;
import com.sun.j2ee.blueprints.petstore.tools.populate.PopulateServlet;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import  com.sun.j2ee.blueprints.supplier.tools.populate.InventoryPopulator;
import java.io.InputStream;
import java.util.Collection;
import javax.naming.NamingException;
import com.sun.j2ee.blueprints.signon.user.ejb.UserLocal;
import  com.sun.j2ee.blueprints.petstore.tools.populate.UserPopulator;
import com.sun.j2ee.blueprints.petstore.controller.web.ShoppingWebController;
import com.sun.j2ee.blueprints.petstore.tools.populate.CustomerPopulator;
import com.sun.j2ee.blueprints.catalog.dao.GenericCatalogDAO.ParsingDoneException;
import com.sun.j2ee.blueprints.catalog.dao.GenericCatalogDAO;
import javax.xml.parsers.SAXParser;
import org.xml.sax.InputSource;

public privileged aspect GeneralExceptionHandler {

	// ---------------------------
	// Declare Soft's
	// ---------------------------
	declare soft : FormatterException : getDocumentHandler() || 
										getDocumentAsStringHandler();

	declare soft : TransformerConfigurationException : internalGetTransformerHandler();

	declare soft : FinderException : signOnEJB_authenticateHandler() ||
									 internalGetCustomerHandler() || 
									 performSignOnHandler() ||
									 checkHandler() ||
									 internalRemoveExistingUserHandler() ||
									 checkHandler2() ||
									 userPopulatorCheckHandler(); 
	
	declare soft : NamingException : checkHandler() ||
									 checkHandler2() ||
									 userPopulatorCheckHandler();
	
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

	declare soft : RemoveException : destroyHandler() ||
									 defaultWebController_destroyHandler() ||
									 internalRemoveExistingUserHandler();

    declare soft : ParsingDoneException : loadSQLStatementsHandler() ||
    									  loadSQLStatementsHandler2();
	// ---------------------------
	// Pointcut's
	// ---------------------------
    pointcut loadSQLStatementsHandler2() : 
		execution(private void PopulateServlet.loadSQLStatements(SAXParser, String, InputSource));
	
	pointcut userPopulatorCheckHandler() : 
		execution(public boolean UserPopulator.check());
	
	pointcut internalRemoveExistingUserHandler() :
		execution(private UserLocal UserPopulator.internalRemoveExistingUser(String, UserLocal));
	
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

	pointcut destroyHandler() : 
		execution(public void ShoppingWebController.destroy(HttpSession));

	pointcut defaultWebController_destroyHandler() : 
		execution(public synchronized void destroy(HttpSession));

	pointcut checkHandler() : 
		execution(public boolean InventoryPopulator.check());
	
	pointcut checkHandler2() : 
		execution(public boolean CustomerPopulator.check());
	
	pointcut loadSQLStatementsHandler() : 
		execution(private void GenericCatalogDAO.loadSQLStatements(SAXParser, String, InputSource));
	
	// ---------------------------
	// Advice's
	// ---------------------------
	void around() :  loadSQLStatementsHandler() ||
					 loadSQLStatementsHandler2(){
		try {
			proceed();
		} catch(ParsingDoneException exception) {
			// Ignored		
		}
	}
	
	Object around() : checkHandler() ||
					  checkHandler2() ||
					  internalRemoveExistingUserHandler() ||
					  userPopulatorCheckHandler(){
		try {
			return proceed();
	    } catch (Exception e) {
	        return null;
	    }
	}

	void around() :  defaultWebController_destroyHandler() ||
    				 destroyHandler(){
		try {
			proceed();
		} catch (RemoveException re) {
			// ignore, after all its only a remove() call!
			Debug.print(re);
		}
	}

	Object around() throws GeneralFailureException : 
		getShoppingControllerHandler() ||
		defaultComponentManager_getEJBControllerHandler(){
		try {
			return proceed();
		} catch (CreateException ce) {
			throw new GeneralFailureException(ce.getMessage());
		} catch (ServiceLocatorException ne) {
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
