package com.sun.j2ee.blueprints.supplier.inventory.web;

import com.sun.j2ee.blueprints.util.aspect.AspectUtil;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

public aspect SupplierInventoryWebHandler {

	pointcut getInventoryHandler() : 
		execution(public Collection DisplayInventoryBean.getInventory());
	pointcut initHandler() : 
		execution(public void RcvrRequestProcessor.init());
	pointcut updateInventoryHandler() : 
		execution(private void RcvrRequestProcessor.updateInventory(HttpServletRequest));
	pointcut sendInvoicesHandler() : 
		execution(private void RcvrRequestProcessor.sendInvoices(Collection));
	pointcut doPostHandler() : 
		execution(public void RcvrRequestProcessor.doPost(HttpServletRequest, HttpServletResponse));		
	declare soft : ServiceLocatorException : getInventoryHandler() || 
		initHandler() ||
		sendInvoicesHandler();
	declare soft : FinderException : getInventoryHandler() || 
		updateInventoryHandler() ||
		doPostHandler();
	declare soft : CreateException : initHandler();
	declare soft : TransitionException : sendInvoicesHandler();
	declare soft : NamingException : doPostHandler();
	declare soft : NotSupportedException : doPostHandler();	
	declare soft : RollbackException : doPostHandler();
	declare soft : HeuristicMixedException : doPostHandler();
	declare soft : HeuristicRollbackException : doPostHandler();
	declare soft : SystemException : doPostHandler();	
	void around() : initHandler()					
					|| updateInventoryHandler()
					|| sendInvoicesHandler(){
		try{
			proceed();
		}catch(RuntimeException e){
			throw e;
		}catch(Exception e){			
			e.printStackTrace();					
		}
	}		
	Collection around() :  
		getInventoryHandler() {
		try {
			return proceed();
		} catch(FinderException ne) {
			ne.printStackTrace();
			return null;
		} catch(ServiceLocatorException se) {
			se.printStackTrace();
			return null;
		}
	}	
	void around() :  
		doPostHandler() {
		try {
			proceed();			
		} catch(FinderException fe) {
			fe.printStackTrace();
		} catch(NamingException ne) {
			ne.printStackTrace();
		} catch(NotSupportedException nse) {
			nse.printStackTrace();
		} catch(IllegalStateException ie) {
			ie.printStackTrace();
		} catch(RollbackException re) {
			re.printStackTrace();
		} catch(HeuristicMixedException hme) {
			hme.printStackTrace();
		} catch(HeuristicRollbackException hre) {
			hre.printStackTrace();
		} catch(SystemException se) {
			se.printStackTrace();
		}
	}	
}