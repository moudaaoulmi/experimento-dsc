/*
 * Created on 27/09/2005
 */
package com.sun.j2ee.blueprints.servicelocator;

import javax.naming.NamingException;
import org.aspectj.lang.SoftException;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import petstore.exception.ExceptionHandler;
/**
 * @author rmaranhao
 */
@ExceptionHandler
public aspect ServiceLocatorHandler {
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : NamingException : ServiceLocatorEjbHandler() ||
		ServiceLocatorWebHandler() || 
		serviceLocator_getLocalHomeHandler() ||
		serviceLocator_getRemoteHomeHandler() || 
		serviceLocator_getQueueConnectionFactoryHandler() ||
		serviceLocator_getQueueHandler() ||
		serviceLocator_getTopicConnectionFactoryHandler() || 
		serviceLocator_getTopicHandler() ||
		serviceLocator_getDataSourceHandler() ||
		serviceLocator_getUrlHandler() ||
		serviceLocator_getBooleanHandler() || 
		serviceLocator_getStringHandler();
	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** ServiceLocator ***/
	pointcut ServiceLocatorEjbHandler() : 
		execution(com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.new()) ;
	pointcut ServiceLocatorWebHandler() : 
		execution(com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator.new()) ;
	pointcut serviceLocator_staticInitHandler() : 
		set(com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator.me) && 
		within(com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator);
	pointcut serviceLocator_getLocalHomeHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getLocalHome(String));
	pointcut serviceLocator_getRemoteHomeHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getRemoteHome(String, Class));
	pointcut serviceLocator_getQueueConnectionFactoryHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getQueueConnectionFactory(String));
	pointcut serviceLocator_getQueueHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getQueue(String));
	pointcut serviceLocator_getTopicConnectionFactoryHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getTopicConnectionFactory(String));
	pointcut serviceLocator_getTopicHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getTopic(String));
	pointcut serviceLocator_getDataSourceHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getDataSource(String)); 
	pointcut serviceLocator_getUrlHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getUrl(String));
	pointcut serviceLocator_getBooleanHandler() : 
		execution(public boolean com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getBoolean(String));
	pointcut serviceLocator_getStringHandler() : 
		execution(public String com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getString(String));

	// ---------------------------
    // Advice's
    // ---------------------------			
	Object around() throws ServiceLocatorException : 
		ServiceLocatorEjbHandler() ||
		serviceLocator_getLocalHomeHandler() ||
		serviceLocator_getRemoteHomeHandler() ||
		serviceLocator_getQueueConnectionFactoryHandler() ||
		serviceLocator_getQueueHandler() ||
		serviceLocator_getTopicConnectionFactoryHandler() ||
		serviceLocator_getTopicHandler() ||
		serviceLocator_getDataSourceHandler() ||
		serviceLocator_getUrlHandler() ||
		serviceLocator_getBooleanHandler() ||
		serviceLocator_getStringHandler(){
		Object result = null;
		try{
			result = proceed();
		} catch (NamingException ne) {
            throw new ServiceLocatorException(ne);
       } catch (Exception e) {
            throw new ServiceLocatorException(e);
       }
		return result;
	}
	
	void around() : ServiceLocatorWebHandler(){
		try{
			proceed();
		}catch(Exception e){
			throw new SoftException(e);
		}
	}
	

	void around() : 
		serviceLocator_staticInitHandler() {
		try {
			proceed();
		} catch(SoftException se) {		
			System.err.println(se);
			se.printStackTrace(System.err);			
		}
	}	
	

}
