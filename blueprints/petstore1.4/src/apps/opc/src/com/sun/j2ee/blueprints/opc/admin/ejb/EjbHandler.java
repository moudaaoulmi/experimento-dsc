package com.sun.j2ee.blueprints.opc.admin.ejb;

import java.util.Map;

import javax.ejb.EJBException;

public class EjbHandler {

	// private EjbHandler ejbHandler = new EjbHandler();

	public void ejbCreateHanlder(Exception e) throws EJBException {
		throw new EJBException(e);
	}

	public void getOrdersByStatusHandler(Exception e)
			throws OPCAdminFacadeException {
		System.err.println("finder Ex while getOrdByStat :" + e.getMessage());
		throw new OPCAdminFacadeException("Unable to find PurchaseOrders"
				+ " of given status : " + e.getMessage());
	}

	public void getChartInfo(Exception e) throws OPCAdminFacadeException {
		System.err.println("finder Ex while getChart :" + e.getMessage());
		throw new OPCAdminFacadeException("Unable to find PurchaseOrders"
				+ " in given period : " + e.getMessage());
	}
}
