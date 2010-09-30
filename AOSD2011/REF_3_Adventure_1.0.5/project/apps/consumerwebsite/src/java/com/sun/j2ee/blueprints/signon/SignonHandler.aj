package com.sun.j2ee.blueprints.signon;

import com.sun.j2ee.blueprints.signon.dao.InvalidPasswordException;
import com.sun.j2ee.blueprints.signon.dao.SignOnDAODupKeyException;
import com.sun.j2ee.blueprints.signon.dao.SignOnDAOFinderException;

//@ExceptionHandler
public privileged aspect SignonHandler {

	pointcut authenticateHandler(): execution(public boolean SignOnFacade.authenticate(String, String));
	pointcut internalCreateSignOnHandler(): execution(private void SignOnFacade.internalCreateSignOn(String, String));

	declare soft: SignOnDAOFinderException : authenticateHandler();
	declare soft: SignOnDAODupKeyException : internalCreateSignOnHandler();

	void around(String userName) throws SignOnDupKeyException : internalCreateSignOnHandler() && args(userName,*)	{
		try {
			proceed(userName);
		} catch (SignOnDAODupKeyException sdke) {
			throw new SignOnDupKeyException("Duplicate User: " + userName);
		}
	}

	boolean around(): authenticateHandler() {
		try {
			return proceed();
		} catch (SignOnDAOFinderException sfx) {
			return false;
		} catch (InvalidPasswordException ix) {
			return false;
		}
	}

}
