package com.sun.j2ee.blueprints.customer;

import com.sun.j2ee.blueprints.customer.dao.AccountDAOFinderException;

//@ExceptionHandler
public privileged aspect CustomerHandler {

	pointcut createAccountHandler(): execution(public void CustomerFacade.createAccount(Account));

	pointcut getAccountHandler(): execution(public Account CustomerFacade.getAccount(String));

	declare soft: Exception: createAccountHandler();
	declare soft: AccountDAOFinderException: getAccountHandler();

	void around() throws CustomerException  : createAccountHandler(){
		try {
			proceed();
		} catch (Exception e) {
			throw new CustomerException(
					"Exception in CustomerFacade while creating account.", e);
		}
	}

	Account around() throws CustomerException : getAccountHandler() {
		try {
			return proceed();
		} catch (AccountDAOFinderException fe) {
			throw new CustomerException(
					"Exception in CustomerFacade while creating account.", fe);
		}
	}

}
