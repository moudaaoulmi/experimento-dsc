/*
 * Created on 19/09/2005
 */
package com.sun.j2ee.blueprints.supplier.inventory.web;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import petstore.exception.ExceptionHandler;
import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;

/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect SupplierInventoryWebHandler extends PrintStackTraceAbstractExceptionHandler{


	declare soft : FinderException : getInventoryHandler() || 
									 updateInventoryHandler() ||
									 doPostHandler();
	declare soft : TransitionException : sendInvoicesHandler();
	declare soft : NamingException : doPostHandler();
	declare soft : NotSupportedException : doPostHandler();
	// IllegalStateException is a RuntimeException
	// declare soft : IllegalStateException : doPostHandler();
	declare soft : RollbackException : doPostHandler();
	declare soft : HeuristicMixedException : doPostHandler();
	declare soft : HeuristicRollbackException : doPostHandler();
	declare soft : SystemException : doPostHandler();
	

	/*** DisplayInventoryBean ***/
	pointcut getInventoryHandler() : 
		execution(public Collection DisplayInventoryBean.getInventory());

	/*** RcvrRequestProcessor ***/
	pointcut updateInventoryHandler() : 
		execution(private void RcvrRequestProcessor.updateInventory(HttpServletRequest));

	pointcut sendInvoicesHandler() : 
		execution(private void RcvrRequestProcessor.sendInvoices(Collection));

	pointcut doPostHandler() : 
		execution(public void RcvrRequestProcessor.doPost(HttpServletRequest, HttpServletResponse));
	
	public pointcut printStackTraceException(): (sendInvoicesHandler()) || (doPostHandler());

	Object around() :  
		updateInventoryHandler() || getInventoryHandler()  || doPostHandler() {
		Object result = null;
		try {
			result = proceed();
		} catch (FinderException ne) {
			ne.printStackTrace();
		}
		return result;
	}
	
//	void around() : sendInvoicesHandler() {
//		try {
//			proceed();
//		} catch (TransitionException te) {
//			te.printStackTrace();
//		}
//	}

//	void around() : doPostHandler() {
//		try {
//			proceed();
//		} catch (NamingException ne) {
//			ne.printStackTrace();
//		} catch (NotSupportedException nse) {
//			nse.printStackTrace();
//		} catch (IllegalStateException ie) {
//			ie.printStackTrace();
//		} catch (RollbackException re) {
//			re.printStackTrace();
//		} catch (HeuristicMixedException hme) {
//			hme.printStackTrace();
//		} catch (HeuristicRollbackException hre) {
//			hre.printStackTrace();
//		} catch (SystemException se) {
//			se.printStackTrace();
//		}
//	}

}
