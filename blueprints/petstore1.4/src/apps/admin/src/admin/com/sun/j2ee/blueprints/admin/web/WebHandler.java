package com.sun.j2ee.blueprints.admin.web;

import javax.ejb.CreateException;

public class WebHandler {

	public void throwAdminBDExceptionHandler(Exception e) throws AdminBDException {
		e.printStackTrace();
		throw new AdminBDException(e.getMessage());
	}

	public void updateOrdersHandler(CreateException ce) throws AdminBDException {
		throw new AdminBDException(ce.getMessage());
	}

	public String messageExeptionHandler(Exception e, String replyHeader) {
		return replyHeader + "<Error>Exception while processing :  "
				+ e.getMessage() + ". Please try again</Error>\n</Response>\n";
	}
	
	public String printStackTraceHandler(Exception e){
		 e.printStackTrace();
         return null;
	}

}
