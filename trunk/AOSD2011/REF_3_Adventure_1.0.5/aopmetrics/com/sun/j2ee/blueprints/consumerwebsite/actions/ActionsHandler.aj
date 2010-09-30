package com.sun.j2ee.blueprints.consumerwebsite.actions;

import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import com.sun.j2ee.blueprints.catalog.CatalogFacade;
import com.sun.j2ee.blueprints.consumerwebsite.Cart;
import com.sun.j2ee.blueprints.consumerwebsite.exceptions.CustomerException;
import com.sun.j2ee.blueprints.consumerwebsite.exceptions.OrderNotFoundException;
import com.sun.j2ee.blueprints.consumerwebsite.exceptions.SignOnException;
import com.sun.j2ee.blueprints.customer.Account;
import com.sun.j2ee.blueprints.customer.CustomerFacade;
import com.sun.j2ee.blueprints.waf.controller.web.html.HTMLActionException;

@ExceptionHandler
public privileged aspect ActionsHandler {

	pointcut internalPurchaseActivitiesHandler(): execution(private int CartHTMLAction.internalPurchaseActivities(String, int));

	pointcut internalBuildPurchaseOrderHandler(): execution(private void CheckoutHTMLAction.internalBuildPurchaseOrder(HttpServletRequest,	String , CatalogFacade , Cart));

	pointcut internalReadAccountHandler(): execution(private Account CustomerHTMLAction.internalReadAccount(CustomerFacade , String ,Account ));

	pointcut internalCreateAccountHandler(): execution(private void CustomerHTMLAction.internalCreateAccount(CustomerFacade,	com.sun.j2ee.blueprints.customer.Account));

	pointcut createSignOnHandler(): execution(private boolean CustomerHTMLAction.createSignOn(HttpServletRequest));

	pointcut internalPerformHandler1(): execution(private void OrderTrackingHTMLAction.internalPerform(HttpServletRequest, String ,	OrderDetails ));

	pointcut internalSearchTransportationHandler(): execution(private ArrayList TransportSearchHTMLAction.internalSearchTransportation(String , String , Locale , ArrayList ));

	declare soft: Exception: internalBuildPurchaseOrderHandler() || internalReadAccountHandler() || internalCreateAccountHandler() || createSignOnHandler() || internalPerformHandler1() || internalSearchTransportationHandler();
	declare soft: OrderNotFoundException: internalPerformHandler1();

	ArrayList around() throws HTMLActionException  : internalSearchTransportationHandler(){
		try {
			return proceed();
		} catch (Exception e) {
			throw new HTMLActionException(
					"Transportation Search Exception:: Catalog Exception accessing catalog component: "
							+ e);
		}
	}

	

	void around() throws OrderNotFoundException  : internalPerformHandler1() {
		try {
			proceed();
		} catch (OrderNotFoundException ex) {
			System.out
					.println("OrderTrackingHTMLAction caught the OrderNotFoundException Service Exception");
			throw new com.sun.j2ee.blueprints.consumerwebsite.exceptions.OrderNotFoundException(
					"Action error calling ordertracking endpoint " + ex);
		} catch (Exception ex) {
			System.out.println("OrderTrackingHTMLAction caught an Exception");
			throw new com.sun.j2ee.blueprints.consumerwebsite.exceptions.OrderNotFoundException(
					"Action error calling ordertracking endpoint " + ex);
		}
	}

	boolean around() throws SignOnException  : createSignOnHandler()	{
		try {
			return proceed();
		} catch (Exception e) {
			throw new SignOnException(
					"SignOnHTMLAction:: Exception creating new signon: ", e);
		}
	}

	void around() throws CustomerException  : internalCreateAccountHandler(){
		try {
			proceed();
		} catch (Exception e) {
			System.out.println("**** Customer Error");
			e.printStackTrace();
			throw new CustomerException(
					"CustomerBD:: CustomerAppException Error Creating Customer",
					e);
		}
	}

	Account around() throws CustomerException : internalReadAccountHandler()  {
		try {
			return proceed();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomerException(
					"CustomerHTMLAction:: CustomerAppException accessing Customer Component: ",
					e);
		}
	}

	void around() throws HTMLActionException  : internalBuildPurchaseOrderHandler()		{
		try {
			proceed();
		} catch (Exception e) {
			throw new HTMLActionException("CheckoutHTMLAction Exception : "
					+ e.getMessage(), e);
		}
	}

	int around(int qty): internalPurchaseActivitiesHandler() && args(*, qty) {
		try {
			return proceed(qty);
		} catch (NumberFormatException nex) {
		}
		return qty;
	}

	

}
