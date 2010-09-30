package com.sun.j2ee.blueprints.consumerwebsite;


import javax.servlet.http.HttpSession;
import com.sun.j2ee.blueprints.consumerwebsite.actions.CustomerHTMLAction;
import com.sun.j2ee.blueprints.customer.CustomerFacade;
import com.sun.j2ee.blueprints.util.tracer.Debug;

//@ExceptionHandler
public privileged aspect ConsumerwebsiteHandler {

	pointcut internalGetCartHandler(): execution(private Cart AdventureComponentManager.internalGetCart(HttpSession, Cart));

	pointcut internalProcessEventHandler(): execution(private CustomerBean SignOnNotifier.internalProcessEvent(HttpSession,	CustomerFacade , CustomerHTMLAction, CustomerBean));

	declare soft: Exception:  internalGetCartHandler() || internalProcessEventHandler();

	CustomerBean around(CustomerBean bean): internalProcessEventHandler() && args(..,bean) {
		try {
			return proceed(bean);
		} catch (Exception cex) {
			// log error message
			cex.printStackTrace();
		}
		return bean;
	}

	Cart around(Cart cart): internalGetCartHandler() && args(*, cart) {
		try {
			return proceed(cart);
		} catch (Exception ex) {
			ex.printStackTrace();
			Debug.print("Error instanciating Cart object: " + ex);
		}
		return cart;
	}

	

}
