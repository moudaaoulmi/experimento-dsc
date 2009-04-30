package com.sun.j2ee.blueprints.petstore.controller.ejb.actions;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.petstore.controller.exceptions.DuplicateAccountException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler
public class ActionsHandler extends GeneralException {
	public void perform1Handler(Exception e) throws DuplicateAccountException {
		throw new DuplicateAccountException(
				"Failed to Create SignOn EJB: caught " + e);
	}

	public void perform2Handler() throws DuplicateAccountException {
		throw new DuplicateAccountException("Bad UserName or password");
	}

	public void perform4Handler(XMLDocumentException xde) {
		xde.printStackTrace();
		System.err.println(xde.getRootCause().getMessage());
	}

}
