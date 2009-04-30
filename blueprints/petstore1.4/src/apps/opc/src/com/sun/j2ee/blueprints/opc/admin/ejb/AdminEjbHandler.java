package com.sun.j2ee.blueprints.opc.admin.ejb;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler
public class AdminEjbHandler extends GeneralException {

	public void ejbCreateHandler(Exception e) throws EJBException {
		throw new EJBException(e);
	}

	public void getOrdersHandler(Exception e, String mensagem, String mensagem2)
			throws OPCAdminFacadeException {
		System.err.println(mensagem + e.getMessage());
		throw new OPCAdminFacadeException("Unable to find PurchaseOrders"
				+ mensagem2 + e.getMessage());
	}

}
