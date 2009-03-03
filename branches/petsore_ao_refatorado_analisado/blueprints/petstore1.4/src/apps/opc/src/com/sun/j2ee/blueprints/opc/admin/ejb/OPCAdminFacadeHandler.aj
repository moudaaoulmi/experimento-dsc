package com.sun.j2ee.blueprints.opc.admin.ejb;

import java.util.Date;
import java.util.Map;

import javax.ejb.FinderException;
import com.sun.j2ee.blueprints.util.aspect.AspectUtil;

public aspect OPCAdminFacadeHandler {
	
	pointcut getOrdersByStatusHandler() :
		execution(public OrdersTO OPCAdminFacadeEJB.getOrdersByStatus(String));	
	pointcut getChartInfoHandler() :
		execution(public Map OPCAdminFacadeEJB.getChartInfo(String,Date,Date,String));	
	declare soft: FinderException : getOrdersByStatusHandler() || getChartInfoHandler();		
	Object around() throws OPCAdminFacadeException : getOrdersByStatusHandler() || getChartInfoHandler(){
		try{
			return proceed();
		}catch(FinderException e){
			if (AspectUtil.verifyJointPoint(thisJoinPoint,"public OrdersTO OPCAdminFacadeEJB.getOrdersByStatus(String)")){
				System.err.println("finder Ex while getOrdByStat :" + e.getMessage());	
			}else if(AspectUtil.verifyJointPoint(thisJoinPoint,"public Map OPCAdminFacadeEJB.getChartInfo(String,Date,Date,String)")){
				System.err.println("finder Ex while getChart :" + e.getMessage());	
			}			 
		    throw new OPCAdminFacadeException("Unable to find PurchaseOrders of given status : " + e.getMessage());
		}		
	}        
}
