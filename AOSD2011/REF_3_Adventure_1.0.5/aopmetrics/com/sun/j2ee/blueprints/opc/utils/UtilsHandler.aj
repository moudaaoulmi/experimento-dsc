package com.sun.j2ee.blueprints.opc.utils;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import com.sun.j2ee.blueprints.servicelocator.ServiceLocatorException;

@ExceptionHandler
public privileged aspect UtilsHandler {

	Map jmsConMap = new HashMap();

	pointcut getConnectionHandler(): call(public Connection ConnectionFactory.createConnection());
	
	// Pointcuts
	
	pointcut sendMessageHandler(): execution(public  boolean JMSUtils.sendMessage(String, String, String, String));

	pointcut sendMessageHandler1(): execution(public  boolean JMSUtils.sendMessage(String, String ,	String , Object ));

	declare soft: JMSException: sendMessageHandler() || sendMessageHandler1();
	declare soft: Exception: sendMessageHandler() || sendMessageHandler1();

	boolean around(): sendMessageHandler1() || sendMessageHandler(){
		try {
			return proceed();
		} catch (ServiceLocatorException se) {
			System.err.println("JMSUtil exception" + se.getMessage());
			return false;
		} catch (JMSException exe) {
			System.err.println("JMSUtil exception " + exe.getMessage());
			return false;
		} catch (Exception ge) {
			System.err.println("JMSUtil exception " + ge.getMessage());
			return false;
		} finally {
			Connection jmsCon = (Connection)this.jmsConMap.get(Thread.currentThread()
					.getName());
			if (jmsCon != null) {
				try {
					jmsCon.close();
				} catch (JMSException exe) {
					System.err.println("JMSUtil exception " + exe.getMessage());
					return false;
				}
			}
		}
	}	

	Connection around(): (
			withincode(public  boolean JMSUtils.sendMessage(String, String, String, String))
			|| withincode(public  boolean JMSUtils.sendMessage(String, String ,	String , Object ))			
		) && getConnectionHandler(){
		Connection con = proceed();
		this.jmsConMap.put(Thread.currentThread().getName(), con);
		return con;
	}

}
