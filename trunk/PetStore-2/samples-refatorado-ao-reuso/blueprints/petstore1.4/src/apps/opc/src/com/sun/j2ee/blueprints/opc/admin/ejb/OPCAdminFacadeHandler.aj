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

	/*** OPCAdminFacadeEJB ***/
	pointcut getOrdersByStatusHandler() :
		execution(public OrdersTO OPCAdminFacadeEJB.getOrdersByStatus(String));
	
	pointcut getChartInfoHandler() :
		execution(public Map OPCAdminFacadeEJB.getChartInfo(String,Date,Date,String));	
	
	declare soft: FinderException : getOrdersByStatusHandler() || getChartInfoHandler();
	
	
	
	OrdersTO around() throws OPCAdminFacadeException : 
		getOrdersByStatusHandler(){
		try{
			return proceed();
		}catch(FinderException fe){
			 System.err.println("finder Ex while getOrdByStat :" + fe.getMessage());
		     throw new OPCAdminFacadeException("Unable to find PurchaseOrders"+
	                                           " of given status : " +
	                                           fe.getMessage());	  	
		}
		
	}
	
	Map around() throws OPCAdminFacadeException : 
		getChartInfoHandler(){
		try{
			return proceed();
		}catch(FinderException fe){
			System.err.println("finder Ex while getChart :" + fe.getMessage());
	        throw new OPCAdminFacadeException("Unable to find PurchaseOrders"+
	                                          " in given period : " +
	                                          fe.getMessage());  	
		}
		
	}
     
        
}
