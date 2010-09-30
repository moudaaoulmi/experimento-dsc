package com.sun.j2ee.blueprints.opc.purchaseorder.ejb;

import javax.ejb.CreateException;
import com.sun.j2ee.blueprints.opc.purchaseorder.ContactInfo;
import com.sun.j2ee.blueprints.opc.purchaseorder.PurchaseOrder;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

@ExceptionHandler
public privileged aspect EjbHandler {

	pointcut ejbPostCreateHandler(): execution(public void ContactInfoBean.ejbPostCreate(ContactInfo) );

	pointcut ejbPostCreateHandler1(): execution(public void PurchaseOrderBean.ejbPostCreate(PurchaseOrder));

	void around() throws CreateException  : ejbPostCreateHandler1() || ejbPostCreateHandler() {
		try {
			proceed();
		} catch (ServiceLocatorException se) {
			throw new CreateException(" Exception saving PO:" + se.getMessage());
		}

	}
}
