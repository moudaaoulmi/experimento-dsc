package com.sun.j2ee.blueprints.opc.transitions;

import javax.jms.QueueConnection;


import com.sun.j2ee.blueprints.admin.exception.ExceptionHandler;
import com.sun.j2ee.blueprints.admin.exception.GeneralException;

@ExceptionHandler
public class TransitionsHandler extends GeneralException {

	public void sendMessageFinallyHandler(QueueConnection qC) {
		try {
			if (qC != null) {
				qC.close();
			}
		} catch (Exception e) {
			System.err
					.println("OPC.QueueHelper GOT EXCEPTION closing connection"
							+ e);
		}
	}

}
