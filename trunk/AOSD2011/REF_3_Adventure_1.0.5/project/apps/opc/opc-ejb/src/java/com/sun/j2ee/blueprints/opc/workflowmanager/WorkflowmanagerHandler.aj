package com.sun.j2ee.blueprints.opc.workflowmanager;


import javax.ejb.EJBException;
import javax.ejb.Timer;
import javax.jms.JMSException;
import javax.jms.Message;
import com.sun.j2ee.blueprints.opc.workflowmanager.handlers.HandlerException;

//@ExceptionHandler
public privileged aspect WorkflowmanagerHandler {

	pointcut ejbCreateHandler(): execution(public void WorkFlowManagerBean.ejbCreate());

	pointcut onMessageHandler(): execution(public void WorkFlowManagerBean.onMessage(Message));

	pointcut createStatusUpdateTimerHandler(): execution(private void WorkFlowManagerBean.createStatusUpdateTimer());

	pointcut ejbTimeoutHandler(): execution(public void WorkFlowManagerBean.ejbTimeout(Timer));

	declare soft: HandlerException : ejbCreateHandler() || onMessageHandler();
	declare soft: Exception : ejbCreateHandler() || createStatusUpdateTimerHandler() || ejbTimeoutHandler();
	declare soft: JMSException : onMessageHandler();
	

	void around(): ejbTimeoutHandler() || createStatusUpdateTimerHandler() ||  ejbCreateHandler() {
		try {
			
			proceed();
		} catch (Exception exe) {
			throw new EJBException(exe);
		}
	}	
	
	// Nao foi reusado, pois pode alterar o comportamento da aplicacao;
	// Devido a precedencia dos aspectos envolvidos

	void around(): onMessageHandler() ||  ejbCreateHandler() {
		try {
			proceed();
		}catch (JMSException exe) {
			throw new EJBException(exe);
		}
	}

	void around(): ejbCreateHandler() {
		try {
			proceed();
		} catch (HandlerException he) {
			throw new EJBException(he.getMessage());
		} 
	}

}
