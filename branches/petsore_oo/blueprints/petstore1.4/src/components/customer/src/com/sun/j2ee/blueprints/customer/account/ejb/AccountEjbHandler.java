package com.sun.j2ee.blueprints.customer.account.ejb;

import javax.ejb.CreateException;

public class AccountEjbHandler {
	
	public void ejbPostCreateHandler(Exception e) throws CreateException {
        throw new CreateException("ContactInfoEJB error: naming exception looking up contact info or credit card");
	}
}
