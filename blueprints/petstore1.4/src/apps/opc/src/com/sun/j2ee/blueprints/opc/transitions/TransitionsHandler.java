package com.sun.j2ee.blueprints.opc.transitions;

import javax.jms.QueueConnection;

import com.sun.j2ee.blueprints.processmanager.transitions.TransitionDelegate;
import com.sun.j2ee.blueprints.processmanager.transitions.TransitionException;

public class TransitionsHandler {
	
	public void setupHandler(Exception e) throws TransitionException{
		throw new TransitionException(e);
	}
	
	public void sendMessageHandler(QueueConnection qC){
		try {
            if(qC != null) {
            	qC.close();
            }
        } catch(Exception e) {
        System.err.println("OPC.QueueHelper GOT EXCEPTION closing connection" + e);
      }
	}
	
}
