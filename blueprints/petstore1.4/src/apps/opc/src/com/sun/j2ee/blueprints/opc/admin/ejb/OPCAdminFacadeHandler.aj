/*
 * Created on 01/09/2005
 */
package com.sun.j2ee.blueprints.opc.admin.ejb;

import java.util.Date;
import java.util.Map;

import javax.ejb.FinderException;

/**
 * @author Raquel Maranhao
 */
public aspect OPCAdminFacadeHandler {
	
	declare soft: FinderException : getOrdersByStatusHandler() || getChartInfoHandler();
	
	
	/*** OPCAdminFacadeEJB ***/
	pointcut getOrdersByStatusHandler() :
		execution(public OrdersTO OPCAdminFacadeEJB.getOrdersByStatus(String));
	
	pointcut getChartInfoHandler() :
		execution(public Map OPCAdminFacadeEJB.getChartInfo(String,Date,Date,String));	
	

	
	Object around() throws OPCAdminFacadeException : 
		getOrdersByStatusHandler() ||
		getChartInfoHandler(){
		try{
			return proceed();
		}catch(FinderException fe){
			 System.err.println("finder Ex while getOrdByStat :" + fe.getMessage());
		     throw new OPCAdminFacadeException("Unable to find PurchaseOrders"+
	                                           " of given status : " +
	                                           fe.getMessage());	  	
		}
		
	}
        
}
