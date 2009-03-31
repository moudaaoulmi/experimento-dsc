/*
 * Created on 24/09/2005
 */
package com.sun.j2ee.blueprints.supplier.transitions;

import java.util.HashMap;
import java.util.Map;

import javax.jms.*;

import org.aspectj.lang.SoftException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionInfo;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.aspect.TransitionExceptionGenericAspect;

/**
 * @author Raquel Maranhao
 */
public aspect SupplierTransitionsHandler extends TransitionExceptionGenericAspect {
	
	//TopicConnection topicConnect = null;
	private Map topicConnect = new HashMap();
	
	/*** SupplierOrderTD ***/
	pointcut setupHandler() : 
		execution(public void SupplierOrderTD.setup());
	pointcut doTransitionHandler() : 
		execution(public void SupplierOrderTD.doTransition(TransitionInfo));
	
	/*** TopicSender ***/
	pointcut createTopicConnectionHandler() : 
		call(TopicConnection TopicConnectionFactory.createTopicConnection()) && 
		withincode(public void TopicSender.sendMessage(String));
	pointcut sendMessageHandler() : 
		execution(public void TopicSender.sendMessage(String));

    public pointcut afterServiceLocatorExceptionHandler() : 
        setupHandler();
    public pointcut afterJMSExceptionHandler() : 
        doTransitionHandler();
	
    
	
	declare soft : ServiceLocatorException : setupHandler();
	declare soft : JMSException : doTransitionHandler();
	
	
		
	/*
	after() throwing(ServiceLocatorException se) throws TransitionException : 
		setupHandler() {
        throw new TransitionException(se);
	}
	
	after() throwing(JMSException je) throws TransitionException : 
		doTransitionHandler() {
        throw new TransitionException(je);		
	}
	*/

	after() returning(TopicConnection tc) : createTopicConnectionHandler() {
    	//Save inner method variable to local(multi-thread)
	    topicConnect.put(Thread.currentThread().getName(), tc);
		//topicConnect = tc; 
	}
		
	void around() : 
		sendMessageHandler() {
		try {
			proceed();
		} finally {
          //Uses aspect local topicConnect variable, fed above
		  TopicConnection topicConnectAux = (TopicConnection)topicConnect.get(Thread.currentThread().getName());	            		    
	      if(topicConnectAux != null ) {
	      	try {
	      	  topicConnectAux.close();
	      	} catch (JMSException ex) {
	      		throw new SoftException(ex);
	      	}      		
	      }
		}
	}

	after() throwing(SoftException exception) throws JMSException : 
		sendMessageHandler() {
	    throw new JMSException(exception.getMessage(), exception.getCause().getMessage());
	}
	
}
