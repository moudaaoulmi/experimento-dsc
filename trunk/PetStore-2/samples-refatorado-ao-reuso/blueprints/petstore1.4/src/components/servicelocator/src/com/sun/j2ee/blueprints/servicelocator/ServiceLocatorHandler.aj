/*
 * Created on 27/09/2005
 */
package com.sun.j2ee.blueprints.servicelocator;

import javax.naming.NamingException;

import org.aspectj.lang.SoftException;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

/**
 * @author rmaranhao
 */
public aspect ServiceLocatorHandler {
	
	/*** ServiceLocator ***/
	pointcut ServiceLocatorEjbHandler() : 
		execution(com.sun.j2ee.blueprints.servicelocator.ejb.ServiceLocator.new()) ;
	pointcut ServiceLocatorWebHandler() : 
		execution(com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator.new()) ;
	pointcut staticInitHandler() : 
		set(com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator.me) && 
		within(com.sun.j2ee.blueprints.servicelocator.web.ServiceLocator);
	pointcut getLocalHomeHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getLocalHome(String));
	pointcut getRemoteHomeHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getRemoteHome(String, Class));
	pointcut getQueueConnectionFactoryHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getQueueConnectionFactory(String));
	pointcut getQueueHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getQueue(String));
	pointcut getTopicConnectionFactoryHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getTopicConnectionFactory(String));
	pointcut getTopicHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getTopic(String));
	pointcut getDataSourceHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getDataSource(String)); 
	pointcut getUrlHandler() : 
		execution(* com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getUrl(String));
	pointcut getBooleanHandler() : 
		execution(public boolean com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getBoolean(String));
	pointcut getStringHandler() : 
		execution(public String com.sun.j2ee.blueprints.servicelocator.*.ServiceLocator.getString(String));
	
	
	
	declare soft : NamingException : ServiceLocatorEjbHandler() ||
		ServiceLocatorWebHandler() || 
		getLocalHomeHandler() ||
		getRemoteHomeHandler() || 
		getQueueConnectionFactoryHandler() ||
		getQueueHandler() ||
		getTopicConnectionFactoryHandler() || 
		getTopicHandler() ||
		getDataSourceHandler() ||
		getUrlHandler() ||
		getBooleanHandler() || 
		getStringHandler();
		

				
	after() throwing(Exception e) throws ServiceLocatorException : 
		ServiceLocatorEjbHandler() || 
		getLocalHomeHandler() || 
		getRemoteHomeHandler() || 
		getQueueConnectionFactoryHandler() || 
		getQueueHandler() ||
		getTopicConnectionFactoryHandler() || 
		getTopicHandler() ||
		getDataSourceHandler() ||
		getUrlHandler() ||
		getBooleanHandler() || 
		getStringHandler() {	
		throw new ServiceLocatorException(e);
	}
	
	void around() : ServiceLocatorWebHandler(){
		try{
			proceed();
		}catch(Exception e){
			throw new SoftException(e);
		}
	}
	

	void around() : 
		staticInitHandler() {
		try {
			proceed();
		} catch(SoftException se) {		
			System.err.println(se);
			se.printStackTrace(System.err);			
		}
	}	
	

}
