package com.sun.j2ee.blueprints.aspectos.reuso;

import javax.ejb.CreateException;
import com.sun.j2ee.blueprints.admin.web.AdminBDException;

public abstract aspect CreateExceptionGenericHandler{
	
	public abstract pointcut afterCreateException();
	public abstract void handlerCreateException(CreateException e) throws AdminBDException;
	
	declare soft : CreateException : afterCreateException();
	
//	after() throwing(CreateException ce) throws AdminBDException : afterCreateException() {
//		handlerCreateException(ce);
//	}	
	
	void around() throws AdminBDException : afterCreateException(){
		try{
			proceed();
		}catch(CreateException e){
			handlerCreateException(e);
		}
	}	
}
