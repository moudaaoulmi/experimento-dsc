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
	private Map topicConnect = new HashMap();
	
	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : ServiceLocatorException : setupHandler();
	
	declare soft : JMSException : doTransitionHandler();

	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** SupplierOrderTD ***/
	pointcut setupHandler() : 
		execution(public void SupplierOrderTD.setup());
	
	pointcut doTransitionHandler() : 
		execution(public void SupplierOrderTD.doTransition(TransitionInfo));
	
	/*** TopicSender ***/
	pointcut sendMessageHandler() : 
		execution(* TopicSender.sendMessage(..));
	
    public pointcut afterServiceLocatorExceptionHandler() : 
        setupHandler();
    
    public pointcut afterJMSExceptionHandler() : 
        doTransitionHandler();
    
    pointcut createTopicConnectionHandler() : 
		call(TopicConnection TopicConnectionFactory.createTopicConnection()) && 
		withincode(public void TopicSender.sendMessage(String));

	
	// ---------------------------
    // Advice's
    // ---------------------------
    TopicConnection around(): createTopicConnectionHandler(){
    	TopicConnection tc = proceed();
    	topicConnect.put(Thread.currentThread().getName(), tc);
    	return tc;
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

}
