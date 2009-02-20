package com.sun.j2ee.blueprints.admin.web;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.opc.admin.ejb.OPCAdminFacadeHome;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator;

public aspect AspectoExemplo {

	pointcut adminRequestBDHandler() : execution(public AdminRequestBD.new());
	
	declare soft : ServiceLocatorException : adminRequestBDHandler();
	declare soft : CreateException : adminRequestBDHandler();
	declare soft : RemoteException : adminRequestBDHandler();
	
	void around() throws AdminBDException : adminRequestBDHandler(){
		try {
			proceed();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
            throw new AdminBDException(sle.getMessage());
        } catch (CreateException ce) {
            ce.printStackTrace();
            throw new AdminBDException(ce.getMessage());
        } catch (RemoteException re) {
            re.printStackTrace();
            throw new AdminBDException(re.getMessage());
        }
	}
	
	
	
}
