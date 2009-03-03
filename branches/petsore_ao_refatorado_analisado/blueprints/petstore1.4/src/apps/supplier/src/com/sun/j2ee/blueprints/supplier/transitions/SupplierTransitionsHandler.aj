package com.sun.j2ee.blueprints.supplier.transitions;

import java.util.HashMap;
import java.util.Map;

import javax.jms.*;

import org.aspectj.lang.SoftException;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionInfo;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;
import com.sun.j2ee.blueprints.util.aspect.TransitionExceptionGenericAspect;

public aspect SupplierTransitionsHandler extends TransitionExceptionGenericAspect {

	private Map topicConnect = new HashMap();	
	pointcut setupHandler() : 
		execution(public void SupplierOrderTD.setup());
	pointcut doTransitionHandler() : 
		execution(public void SupplierOrderTD.doTransition(TransitionInfo));	
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