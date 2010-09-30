package com.sun.j2ee.blueprints.airlinesupplier.pomessagebean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.CreateException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import com.sun.j2ee.blueprints.airlinesupplier.powebservice.AirlineOrder;
import com.sun.j2ee.blueprints.airlinesupplier.powebservice.OrderSubmissionException;
import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

//@ExceptionHandler
public privileged aspect PomessagebeanHandler {

	Map aloMap = new HashMap();

	pointcut internalOnMessageHandler1(): execution(private AirlineOrder AirlineMessageBean.internalOnMessage(Message , AirlineOrder));

	pointcut getAirlineOrderHandler(): call(public Serializable ObjectMessage.getObject()) && withincode(private AirlineOrder AirlineMessageBean.internalOnMessage(Message , AirlineOrder));

	pointcut persistOrderHandler(): execution(public void AirlineMessageBean.persistOrder(AirlineOrder));

	declare soft: JMSException : internalOnMessageHandler1();
	declare soft: CreateException: persistOrderHandler();

	void around() throws OrderSubmissionException : persistOrderHandler(){
		try {
			proceed();
		} catch (ServiceLocatorException je) {
			throw new OrderSubmissionException("Error AL persisting order:"
					+ je.getMessage());
		} catch (CreateException ce) {
			throw new OrderSubmissionException("Error AL persisting order:"
					+ ce.getMessage());
		}
	}	

	AirlineOrder around(): internalOnMessageHandler1(){
		try {
			proceed();
		} catch (JMSException e) {
			// Proper exception handling as in OPC module has to be
			// implemented here later
			e.printStackTrace();
		}
		AirlineOrder alo = (AirlineOrder) this.aloMap.get(Thread.currentThread().getName());
		return alo;
	}

	AirlineOrder around(): getAirlineOrderHandler() {
		AirlineOrder alo = (AirlineOrder) proceed();
		this.aloMap.put(Thread.currentThread().getName(), alo);
		return alo;
	}

}
