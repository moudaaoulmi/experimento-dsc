package com.sun.j2ee.blueprints.supplier.transitions;

import javax.jms.JMSException;
import javax.jms.TopicConnection;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;

import exception.ExceptionHandler;
import exception.GeneralException;

@ExceptionHandler
public class TransitionsHandler extends GeneralException {

	public void sendMessage(TopicConnection topicConnect) throws JMSException {
		if (topicConnect != null)
			topicConnect.close();
	}

}
