package com.sun.j2ee.blueprints.waf.controller.web;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.controller.EventException;
import com.sun.j2ee.blueprints.waf.controller.GeneralFailureException;
import com.sun.j2ee.blueprints.waf.controller.ejb.EJBControllerLocal;
import com.sun.j2ee.blueprints.waf.controller.web.util.WebKeys;

@ExceptionHandler
public privileged aspect WebHandler {
	
	pointcut contextInitializedHandler(): execution(public void ApplicationComponentManager.contextInitialized(ServletContextEvent));

	pointcut internalGetEJBControllerHandler(): execution(private EJBControllerLocal DefaultComponentManager.internalGetEJBController(EJBControllerLocal));

	pointcut internalGetWebControllerHandler(): execution(private WebController DefaultComponentManager.internalGetWebController(HttpSession ,	WebController ));

	pointcut internalDoProcessHandler(): execution(private void MainServlet.internalDoProcess(HttpServletRequest,	HttpServletResponse , URLMapping ,	boolean , UserTransaction));

	pointcut internalDoProcessHandler1(): execution(private void MainServlet.internalDoProcess(HttpServletRequest,	HttpServletResponse));

	pointcut internalGetActionHandler(): execution(private Action RequestProcessor.internalGetAction(Action, String));
	
	pointcut internalForwardToNextScreenHandler(): execution(private String ScreenFlowManager.internalForwardToNextScreen(HttpServletRequest , String , URLMapping, String));

	pointcut loadDocumentHandler(): execution(public Element URLMappingsXmlDAO.loadDocument(String));

	declare soft : Throwable: contextInitializedHandler() || internalDoProcessHandler1();
	declare soft : MalformedURLException: loadDocumentHandler();
	declare soft: CreateException : internalGetEJBControllerHandler();
	declare soft: Exception : internalGetWebControllerHandler() || internalGetActionHandler() || internalForwardToNextScreenHandler() || loadDocumentHandler();
	
	declare soft: SAXParseException : loadDocumentHandler();
	declare soft: SAXException : loadDocumentHandler();
	declare soft: IOException : loadDocumentHandler();

	Element around(): loadDocumentHandler() {
		try {
			return proceed();
		} catch (SAXParseException err) {
			System.err.println("URLMappingsXmlDAO ** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.err.println("URLMappingsXmlDAO error: " + err.getMessage());
		} catch (SAXException e) {
			System.err.println("URLMappingsXmlDAO error: " + e);
		} catch (java.net.MalformedURLException mfx) {
			System.err.println("URLMappingsXmlDAO error: " + mfx);
		} catch (java.io.IOException e) {
			System.err.println("URLMappingsXmlDAO error: " + e);
		} catch (Exception pce) {
			System.err.println("URLMappingsXmlDAO error: " + pce);
		}
		return null;
	}

	String around(String currentScreen): internalForwardToNextScreenHandler() && args(*, currentScreen,..) {
		try {
			return proceed(currentScreen);
		} catch (Exception ex) {
			System.err.println("ScreenFlowManager caught loading handler: "
					+ ex);
		}
		return currentScreen;
	}
	
	Action around(Action handler): internalGetActionHandler() && args(handler, *) {
		try {
			return proceed(handler);
		} catch (Exception ex) {
			ex.printStackTrace();
			Debug.print("RequestProcessor caught loading action: " + ex);
		}
		return handler;
	}

	void around(HttpServletRequest request, HttpServletResponse response,
			MainServlet main) throws ServletException, IOException  : internalDoProcessHandler1() && args(request, response) && this(main){
		try {
			proceed(request, response, main);
		} catch (Throwable ex) {
			String className = ex.getClass().getName();
			String nextScreen = main.screenFlowManager.getExceptionScreen(ex);
			// put the exception in the request
			request.setAttribute(WebKeys.WAF_EXCEPTION, ex);
			if (nextScreen == null) {
				// send to general error screen
				ex.printStackTrace();
				throw new ServletException("MainServlet: unknown exception: "
						+ className);
			}
			main.context.getRequestDispatcher(nextScreen).forward(request,
					response);
		}
	}

	void around(boolean tx_started, UserTransaction ut) throws ActionException,
			EventException, ServletException, IOException,
			FlowHandlerException, SecurityException  : 
		internalDoProcessHandler() && args(.., tx_started , ut) {
		try {
			proceed(tx_started, ut);
		} finally {
			// commit the transaction if it was started earlier successfully.
			try {
				if (tx_started && ut != null) {
					ut.commit();
				}
			} catch (IllegalStateException re) {
				re.printStackTrace();
			} catch (RollbackException re) {
				re.printStackTrace();
			} catch (HeuristicMixedException hme) {
				hme.printStackTrace();
			} catch (HeuristicRollbackException hre) {
				hre.printStackTrace();
			} catch (SystemException se) {
				se.printStackTrace();
			}
		}
	}


	WebController around() throws RuntimeException : internalGetWebControllerHandler()  {
		try {
			return proceed();
		} catch (ServiceLocatorException slx) {
			throw new RuntimeException(
					"Cannot create bean of class WebController: " + slx);
		} catch (Exception exc) {
			throw new RuntimeException(
					"Cannot create bean of class WebController: " + exc);
		}
	}

	EJBControllerLocal around() throws ServiceLocatorException,
			GeneralFailureException :  internalGetEJBControllerHandler(){
		try {
			return proceed();
		} catch (CreateException ce) {
			throw new GeneralFailureException(ce.getMessage());
		}
	}	

	void around(): contextInitializedHandler(){
		try {
			proceed();
		} catch (Throwable ex) {
			System.out
					.println("WAF ApplicaitonComponentManager Error Initializing:"
							+ ex);
			throw new RuntimeException(ex);
		}
	}

}
