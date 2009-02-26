package com.sun.j2ee.blueprints.aspectos.reuso;

import com.sun.j2ee.blueprints.admin.web.AdminRequestBD;
import com.sun.j2ee.blueprints.admin.web.AdminBDException;
import javax.ejb.CreateException;

public aspect CreateExceptionHandlerScene1_3 extends CreateExceptionGenericHandler{

	pointcut updateOrdersHandler(): execution(public void AdminRequestBD.updateOrders(..) throws AdminBDException);
	pointcut adminRequestBDHandler() : execution(public AdminRequestBD.new() throws AdminBDException);
	
	public pointcut afterCreateException() : 
								updateOrdersHandler()
							|| adminRequestBDHandler()
							&& within(AdminRequestBD);
	
	public void handlerCreateException(CreateException ce) throws AdminBDException{
		 ce.printStackTrace();
	     throw new AdminBDException(ce.getMessage());
	}
}
