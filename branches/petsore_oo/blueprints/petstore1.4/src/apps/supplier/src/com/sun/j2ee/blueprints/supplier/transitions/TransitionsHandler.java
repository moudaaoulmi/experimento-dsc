package com.sun.j2ee.blueprints.supplier.transitions;

import javax.jms.JMSException;
import javax.jms.TopicConnection;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;

public class TransitionsHandler {
	
	/**
	 *O nome do método é genérico pois há reuso de código!
	 */
	public void transitionExceptionHanlder(Exception e) throws TransitionException{
		 throw new TransitionException(e);		
	}
	
	public void sendMessage(TopicConnection topicConnect) throws JMSException{
		if( topicConnect != null )
	        topicConnect.close();
	}

}
