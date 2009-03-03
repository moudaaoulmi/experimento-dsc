package aspectos;

import org.aspectj.lang.reflect.AjType;
import org.aspectj.lang.reflect.AjTypeSystem;
import org.aspectj.lang.reflect.Pointcut;

public aspect ServiceLocatorExceptionHandler {
	
//	private static final int POINTCUT_ADMIN_REQUEST_BD_HANDLER = 0;
//	private static final int POINTCUT_ADMIN_UPDATE_ORDERS_HANDLER = 1;
//	private static final int POINTCUT_ADMIN_GET_CUSTOMER_HANDLER = 2;
//	
//	pointcut adminRequestBDHandler() :  execution(public AdminRequestBD.new(..));
//	pointcut updateOrdersHandler(): execution(public void AdminRequestBD.updateOrders(..));	
//	pointcut getCustomerHandler() : execution(public CustomerLocal ShoppingClientFacadeLocalEJB.getCustomer());
//	
//	declare soft : ServiceLocatorException : adminRequestBDHandler() || updateOrdersHandler() || getCustomerHandler();
//		
//	after() throwing (ServiceLocatorException sle) throws AdminBDException, FinderException : 
//	    adminRequestBDHandler() || updateOrdersHandler() || getCustomerHandler() {
//	    
//		AjType obj = AjTypeSystem.getAjType(this.getClass());
//		Pointcut[] points = obj.getDeclaredPointcuts();
//		
//		if (points[POINTCUT_ADMIN_REQUEST_BD_HANDLER].getPointcutExpression().asString().equals(thisJoinPoint.toLongString())
//		 || points[POINTCUT_ADMIN_UPDATE_ORDERS_HANDLER].getPointcutExpression().asString().equals(thisJoinPoint.toLongString())){
//			
//			sle.printStackTrace();
//	        throw new AdminBDException(sle.getMessage());
//	        
//		}else if(points[POINTCUT_ADMIN_GET_CUSTOMER_HANDLER].getPointcutExpression().asString().equals(thisJoinPoint.toLongString())){
//			
//	        throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of customer: caught " + sle);
//	        
//	    }  
//	        
//	}
//	
//	after() throwing (ServiceLocatorException slx) throws FinderException : 
//		  getCustomerHandler() {
//		  throw new GeneralFailureException("ShoppingClientFacade: failed to look up name of customer: caught " + slx);
//	}

}
