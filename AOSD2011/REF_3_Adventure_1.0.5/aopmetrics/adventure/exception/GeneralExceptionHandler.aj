package adventure.exception;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.sun.j2ee.blueprints.activitysupplier.pomessagebean.ActivityMessageBean;
import com.sun.j2ee.blueprints.activitysupplier.powebservice.ActivityOrder;
import com.sun.j2ee.blueprints.airlinesupplier.pomessagebean.AirlineMessageBean;
import com.sun.j2ee.blueprints.airlinesupplier.powebservice.AirlineOrder;
import com.sun.j2ee.blueprints.catalog.CatalogFacade;
import com.sun.j2ee.blueprints.consumerwebsite.AdventureComponentManager;
import com.sun.j2ee.blueprints.consumerwebsite.Cart;
import com.sun.j2ee.blueprints.consumerwebsite.actions.CartHTMLAction;
import com.sun.j2ee.blueprints.consumerwebsite.actions.SignOffHTMLAction;
import com.sun.j2ee.blueprints.lodgingsupplier.pomessagebean.Invoice;
import com.sun.j2ee.blueprints.lodgingsupplier.pomessagebean.LodgingMessageBean;
import com.sun.j2ee.blueprints.lodgingsupplier.powebservice.LodgingOrder;
import com.sun.j2ee.blueprints.opc.crm.ejb.CrmBean;
import com.sun.j2ee.blueprints.opc.financial.CreditCardVerifier;
import com.sun.j2ee.blueprints.opc.mailer.Mail;
import com.sun.j2ee.blueprints.opc.orderfiller.OrderFillerBean;
import com.sun.j2ee.blueprints.opc.purchaseorder.Activity;
import com.sun.j2ee.blueprints.opc.purchaseorder.CreditCard;
import com.sun.j2ee.blueprints.opc.purchaseorder.Lodging;
import com.sun.j2ee.blueprints.opc.purchaseorder.Transportation;
import com.sun.j2ee.blueprints.opc.purchaseorder.XMLException;
import com.sun.j2ee.blueprints.opc.webservicebroker.provider.BrokerServiceBean;
import com.sun.j2ee.blueprints.opc.webservicebroker.provider.BrokerTransformer;
import com.sun.j2ee.blueprints.opc.webservicebroker.requestor.ActivitySupplierClient;
import com.sun.j2ee.blueprints.opc.webservicebroker.requestor.AirlineSupplierClient;
import com.sun.j2ee.blueprints.opc.webservicebroker.requestor.LodgingSupplierClient;
import com.sun.j2ee.blueprints.opc.webservicebroker.requestor.WSClient;
import com.sun.j2ee.blueprints.opc.webservicebroker.requestor.WSClientFactory;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator;
import com.sun.j2ee.blueprints.waf.controller.Event;
import com.sun.j2ee.blueprints.waf.controller.web.ApplicationComponentManager;
import com.sun.j2ee.blueprints.waf.controller.web.ScreenFlowManager;
import com.sun.j2ee.blueprints.waf.view.template.InsertTag;
import com.sun.j2ee.blueprints.waf.view.template.TemplateServlet;

@ExceptionHandler
public privileged aspect GeneralExceptionHandler {

	String[] msg = { "CrmBean::getDocument error loading XML Document ",
			"BrokerTransformer initizalization error: ",
			"BrokerServiceBean::getDocument error loading XML Document ",
			"TemplateServlet: malformed URL exception: ",
			"ScreenFlowManager: initializing ScreenFlowManager malformed URL exception: ",
			"ApplicationComponentManager: initializing ScreenFlowManager malformed URL exception: "						
	};

	pointcut getDocumentHandler(): execution(private Document CrmBean.getDocument(String));
	pointcut internalBrokerTransformerHandler(): execution(private void BrokerTransformer.internalBrokerTransformer(SAXParserFactory));
	pointcut internalValidateHandler(): execution(private boolean BrokerServiceBean.internalValidate(String));
		
	
	pointcut internalInitScreensHandler(): execution(private URL TemplateServlet.internalInitScreens(ServletContext, String ,URL ));
	pointcut internalInitHandler(): execution(private String ScreenFlowManager.internalInit(ServletContext,	String));

	pointcut internalDoInitHandler(): execution(private String ApplicationComponentManager.internalDoInit(ServletContext,String));

		
	// /Pointcuts

	pointcut internalSendInvoiceHandler(): execution(private void LodgingMessageBean.internalSendInvoice(Invoice));

	pointcut internalDoWorkHandler(): execution(private void LodgingMessageBean.internalDoWork(LodgingOrder));

	pointcut internalDoWorkHandler1(): execution(private void ActivityMessageBean.internalDoWork(ActivityOrder));

	pointcut internalSendInvoiceHandler1(): execution(private void ActivityMessageBean.internalSendInvoice(com.sun.j2ee.blueprints.activitysupplier.pomessagebean.Invoice));

	pointcut internalSendInvoiceHandler2(): execution(private void AirlineMessageBean.internalSendInvoice(com.sun.j2ee.blueprints.airlinesupplier.pomessagebean.Invoice));

	pointcut internalDoWorkHandler2(): execution(private void AirlineMessageBean.internalDoWork(AirlineOrder));

	pointcut internalPerformHandler(): execution(private void CartHTMLAction.internalPerform(String , Cart , CatalogFacade));

	pointcut serviceLocatorHandler(): execution( private ServiceLocator.new());

	pointcut internalGetLocalHomeHandler(): execution( private EJBLocalHome ServiceLocator.internalGetLocalHome(String , EJBLocalHome));

	pointcut internalGetRemoteHomeHandler(): execution( private EJBHome ServiceLocator.internalGetRemoteHome(String , Class ,EJBHome));

	pointcut internalGetJMSConnectionFactoryHandler(): execution(private ConnectionFactory ServiceLocator.internalGetJMSConnectionFactory(String , ConnectionFactory ));

	pointcut internalGetJMSDestinationHandler(): execution(private javax.jms.Destination ServiceLocator.internalGetJMSDestination(String ,javax.jms.Destination));

	pointcut internalGetDataSourceHandler(): execution(private DataSource ServiceLocator.internalGetDataSource(String ,DataSource ) );

	pointcut getUserTransactionHandler(): execution(public UserTransaction ServiceLocator.getUserTransaction(String));

	pointcut getUrlHandler(): execution(public URL ServiceLocator.getUrl(String));
	pointcut getBooleanHandler(): execution(public boolean ServiceLocator.getBoolean(String));
	pointcut getStringHandler(): execution(public String ServiceLocator.getString(String));
	
	pointcut serviceLocatorHandler1(): execution(public com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.new());
	pointcut getLocalHomeHandler(): execution(public EJBLocalHome com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.getLocalHome(String));
	pointcut getRemoteHomeHandler(): execution(public EJBHome com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.getRemoteHome(String, Class));
	pointcut getJMSConnectionFactoryHandler(): execution(public ConnectionFactory com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.getJMSConnectionFactory(String));
	pointcut getJMSDestinationHandler(): execution(public Destination com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.getJMSDestination(String));
	pointcut getDataSourceHandler(): execution(public DataSource com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.getDataSource(String));
	pointcut getUrlHandler1(): execution(public URL com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.getUrl(String));
	pointcut getBooleanHandler1(): execution(public boolean com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.getBoolean(String));
	pointcut getStringHandler1(): execution(public String com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.getString(String));
	
	
	
	pointcut internalDoXSLTTransformationHandler(): execution(private void BrokerTransformer.internalDoXSLTTransformation(Transformer,StreamSource , StreamResult));
	pointcut internalOnMessageHandler(): execution(private void ActivityMessageBean.internalOnMessage(ActivityOrder));
	pointcut internalOnMessageHandler1(): execution(private void LodgingMessageBean.internalOnMessage(LodgingOrder));
	pointcut internalOnMessageHandler2(): execution(private ActivityOrder ActivityMessageBean.internalOnMessage(Message, ActivityOrder));
	pointcut internalOnMessageHandler3(): execution(private LodgingOrder LodgingMessageBean.internalOnMessage(Message, LodgingOrder));
	pointcut internalOnMessageHandler4(): execution(private void AirlineMessageBean.internalOnMessage(AirlineOrder));
	pointcut sessionDestroyedHandler(): execution(public void AdventureComponentManager.sessionDestroyed(HttpSessionEvent));
	pointcut performHandler(): execution(public Event SignOffHTMLAction.perform(HttpServletRequest));
	pointcut internalDoStartTagHandler1(): execution(private void InsertTag.internalDoStartTag());
	pointcut initHandler(): execution(private void BrokerTransformer.init(InputSource));
	pointcut onMessageHandler2(): execution(public String LodgingSupplierClient.sendRequest(String));
	pointcut sendRequestHandler(): execution(public String ActivitySupplierClient.sendRequest(String));
	pointcut sendRequestHandler1(): execution(public String AirlineSupplierClient.sendRequest(String) );
	pointcut creditCardVerifierHandler(): execution(public CreditCardVerifier.new());
	pointcut toXMLHandler(): execution(public String Mail.toXML());	
	pointcut internalValidateHandler1(): execution(private DocumentBuilder BrokerServiceBean.internalValidate(DocumentBuilder));
	pointcut internalGetDocumentHandler(): execution(private DocumentBuilder CrmBean.internalGetDocument(DocumentBuilder));
	pointcut getActivityPOHandler() : execution(private String OrderFillerBean.getActivityPO(Activity[], String));
	pointcut getTransportationPOHandler() : execution(private String OrderFillerBean.getTransportationPO(Transportation, Transportation, String));
	pointcut toXMLHandler4(): execution(public String Activity.toXML(String));
	pointcut toXMLHandler1(): execution(public String CreditCard.toXML());
	pointcut toXMLHandler2(): execution(public String Lodging.toXML(String));
	pointcut toXMLHandler3(): execution(public String Transportation.toXML(String));
	pointcut getWSClientHandler(): execution(public WSClient WSClientFactory.getWSClient(String));

	declare soft: Exception : getWSClientHandler() || internalPerformHandler() || internalSendInvoiceHandler()  ||
	internalDoWorkHandler() || internalSendInvoiceHandler1() || internalDoWorkHandler1() 
	|| internalSendInvoiceHandler2() || internalDoWorkHandler2() || serviceLocatorHandler() 
	|| internalGetLocalHomeHandler() || internalGetRemoteHomeHandler() 
	|| internalGetJMSConnectionFactoryHandler() || internalGetJMSDestinationHandler()
	|| internalGetDataSourceHandler() || getUserTransactionHandler() || getUrlHandler() 
	|| getBooleanHandler() || getStringHandler() || serviceLocatorHandler1() 
	|| getLocalHomeHandler() || getRemoteHomeHandler() 
	|| getJMSConnectionFactoryHandler() || getJMSDestinationHandler() 
	|| getDataSourceHandler() || getUrlHandler1() || getBooleanHandler1() 
	|| getStringHandler1() || sessionDestroyedHandler() || performHandler() 
	|| internalDoStartTagHandler1() || onMessageHandler2() ||  sendRequestHandler1() 
	|| sendRequestHandler() || creditCardVerifierHandler() || toXMLHandler() 
	|| getDocumentHandler() || internalValidateHandler() || internalBrokerTransformerHandler()
	|| internalDoXSLTTransformationHandler() || toXMLHandler4() || toXMLHandler1()
	|| toXMLHandler2() || toXMLHandler3() || getActivityPOHandler() || getTransportationPOHandler();

	declare soft: com.sun.j2ee.blueprints.activitysupplier.powebservice.OrderSubmissionException : internalOnMessageHandler();
	declare soft: com.sun.j2ee.blueprints.lodgingsupplier.powebservice.OrderSubmissionException: internalOnMessageHandler1();
	declare soft: com.sun.j2ee.blueprints.airlinesupplier.powebservice.OrderSubmissionException: internalOnMessageHandler4();
	declare soft: JMSException : internalOnMessageHandler2() || internalOnMessageHandler3();
	declare soft: IOException : initHandler();
	declare soft: ParserConfigurationException: internalValidateHandler1() || internalGetDocumentHandler();
	declare soft: MalformedURLException : internalInitScreensHandler() || internalInitHandler() || internalDoInitHandler() ;
		
	

	String around() throws XMLException  : toXMLHandler4() || toXMLHandler1() || toXMLHandler2() || toXMLHandler3()|| toXMLHandler() || toXMLHandler1() || toXMLHandler2() || toXMLHandler3(){
		try {
			return proceed();
		} catch (Exception exe) {
			throw new XMLException(exe);
		}
	}
	
	
	
	String around() throws XMLException : getTransportationPOHandler() ||  getActivityPOHandler() {
		try {
			return proceed();
		} catch (Exception exe) {
			throw new XMLException(exe);
		}
	}	
	
	//Inserir o this.msg[thisEnclosingJoinPointStaticPart.getId()]
	
	Object around(Object args): (internalInitScreensHandler() || internalInitHandler() || internalDoInitHandler() )&& args(.. , args) {
		try {
			return proceed(args);
		} catch (java.net.MalformedURLException ex) {
			System.err
					.println(this.msg[0]
							+ ex);
		}
		return args;
	}		
	
	DocumentBuilder around(DocumentBuilder db): ( internalValidateHandler1() || internalGetDocumentHandler() ) && args(db){
		try {
			return proceed(db);
		} catch (ParserConfigurationException pce) {
			System.err.println(pce);
		}
		return db;
	}
	
	//Inserir o this.msg[thisEnclosingJoinPointStaticPart.getId()]

	Object around() : getDocumentHandler() || internalBrokerTransformerHandler() || internalValidateHandler(){
		try {
			return proceed();
		} catch (Exception ex) {
			System.err.println(this.msg[0]
					+ ex);
		}
		return null;
	}
	

	Object around(): getWSClientHandler() || onMessageHandler2() ||  sendRequestHandler1() || sendRequestHandler() || creditCardVerifierHandler() || toXMLHandler(){
		try {
			return proceed();
		} catch (Exception ex) {
			System.err.println(ex);
		}
		return null;
	}

	Object around(): sessionDestroyedHandler() || performHandler() || internalDoStartTagHandler1() {
		try {
			proceed();
		} catch (Exception e) {
			// swallow the session listener exception for now
		}
		return null;
	}

	Object around(Object ao): (internalOnMessageHandler2() || internalOnMessageHandler3()) && args(* , ao) {
		try {
			return proceed(ao);
		} catch (JMSException e) {
			// Proper exception handling as in OPC module has to be
			// implemented here later
			e.printStackTrace();
		}
		return ao;
	}

	void around(): internalOnMessageHandler() || internalOnMessageHandler1() || internalOnMessageHandler4() || initHandler()  {
		try {
			proceed();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception oe) {
			// Proper exception handling as in OPC module has to be
			// implemented here later
			oe.printStackTrace();
		}
	}

	Object around() throws ServiceLocatorException : serviceLocatorHandler() 
	|| internalGetLocalHomeHandler() 
	|| internalGetRemoteHomeHandler() 
	|| internalGetJMSConnectionFactoryHandler() 
	|| internalGetJMSDestinationHandler() || internalGetDataSourceHandler()
	|| getUserTransactionHandler() 
	|| getUrlHandler() || getBooleanHandler() || getStringHandler() 
	|| serviceLocatorHandler1() 
	|| getLocalHomeHandler() || getRemoteHomeHandler() 
	|| getJMSConnectionFactoryHandler() || getJMSDestinationHandler() 
	|| getDataSourceHandler() || getUrlHandler1() || getBooleanHandler1() 
	|| getStringHandler1(){
		try {
			return proceed();
		} catch (Exception e) {
			throw new ServiceLocatorException(e);
		}
	}

	void around(): internalSendInvoiceHandler()  || internalDoWorkHandler() 
	|| internalSendInvoiceHandler1() || internalDoWorkHandler1() 
	|| internalSendInvoiceHandler2() || internalDoWorkHandler2() 
	|| internalPerformHandler() || internalDoXSLTTransformationHandler(){
		try {
			proceed();
		} catch (Exception ne) {
			// Proper exception handling as in OPC module has to be
			// implemented here later
			ne.printStackTrace();
		}
	}
}
