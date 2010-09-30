package adventure.exception;

import javax.jms.JMSException;
import javax.jms.Message;
import com.sun.j2ee.blueprints.opc.crm.ejb.CrmBean;
import com.sun.j2ee.blueprints.opc.webservicebroker.requestor.BrokerRequestorBean;

import br.upe.dsc.reusable.exception.EJBExceptionGenericAspect;

@ExceptionHandler
public aspect EJBExceptionHandler extends EJBExceptionGenericAspect {

	pointcut internalOnMessageHandler(): execution(private void CrmBean.internalOnMessage(Message, String));

	pointcut onMessageHandler(): execution(public void BrokerRequestorBean.onMessage(Message));

	declare soft: JMSException: internalOnMessageHandler() || onMessageHandler();

	public pointcut afterEJBExceptionHandler(): internalOnMessageHandler() || onMessageHandler();


}
