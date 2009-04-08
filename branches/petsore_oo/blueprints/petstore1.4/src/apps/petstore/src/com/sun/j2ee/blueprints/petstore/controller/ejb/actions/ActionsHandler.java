package com.sun.j2ee.blueprints.petstore.controller.ejb.actions;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.petstore.controller.exceptions.DuplicateAccountException;
import com.sun.j2ee.blueprints.xmldocuments.XMLDocumentException;


public class ActionsHandler {
	public void perform1Handler(Exception e) throws DuplicateAccountException {
		throw new DuplicateAccountException("Failed to Create SignOn EJB: caught " + e);
	}
	
	public void perform2Handler() throws DuplicateAccountException {
		throw new DuplicateAccountException("Bad UserName or password");
	}
	
	public void perform3Handler(Exception e)  {
		e.printStackTrace();
	}
	
	public void perform4Handler(XMLDocumentException xde)  {
		xde.printStackTrace();
		System.err.println(xde.getRootCause().getMessage());
	}

	
	
}
