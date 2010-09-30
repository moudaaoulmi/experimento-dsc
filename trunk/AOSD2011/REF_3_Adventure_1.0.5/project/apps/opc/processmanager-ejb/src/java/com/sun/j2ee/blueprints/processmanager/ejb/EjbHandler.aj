package com.sun.j2ee.blueprints.processmanager.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

//@ExceptionHandler
public privileged aspect EjbHandler {

	pointcut ejbCreateHandler(): execution(public void ProcessManagerBean.ejbCreate());

	void around() throws CreateException:  ejbCreateHandler()  {
		try {
			proceed();
		} catch (ServiceLocatorException se) {
			throw new EJBException("Got service locator exception! "
					+ se.getMessage());
		}
	}

}
