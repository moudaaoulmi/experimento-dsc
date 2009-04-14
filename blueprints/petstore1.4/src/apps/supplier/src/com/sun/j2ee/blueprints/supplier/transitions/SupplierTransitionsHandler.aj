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

	declare soft : ServiceLocatorException : setupHandler();
	declare soft : JMSException : doTransitionHandler();

	/*** SupplierOrderTD ***/
	pointcut setupHandler() : 
		execution(public void SupplierOrderTD.setup());
	
	pointcut doTransitionHandler() : 
		execution(public void SupplierOrderTD.doTransition(TransitionInfo));
	
	/*** TopicSender ***/
	pointcut internalSendMessageHandler() : 
		execution(* TopicSender.internalSendMessage(..));
	
    public pointcut afterServiceLocatorExceptionHandler() : 
        setupHandler();
    public pointcut afterJMSExceptionHandler() : 
        doTransitionHandler();
	
    void around( String xmlMessage, TopicConnection topicConnect, 
    		TopicSession pubSession,TopicPublisher topicPublisher) throws JMSException: 
    			internalSendMessageHandler() &&
    	args(xmlMessage,  topicConnect, pubSession,  topicPublisher) {
    	try{
    		 proceed(xmlMessage,  topicConnect, pubSession,  topicPublisher);
		  } finally {
			    if( topicConnect != null )
			      topicConnect.close();
		  }
    }

}
