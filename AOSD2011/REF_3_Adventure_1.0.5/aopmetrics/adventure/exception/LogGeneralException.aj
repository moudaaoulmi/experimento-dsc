package adventure.exception;

import java.io.IOException;
import java.io.OptionalDataException;
import java.text.BreakIterator;
import java.util.Vector;
import javax.ejb.RemoveException;
import javax.servlet.http.HttpServletRequest;
import br.upe.dsc.reusable.exception.GenericAbstractHandler;
import com.sun.j2ee.blueprints.opc.crm.ejb.CrmBean;
import com.sun.j2ee.blueprints.opc.mailer.MailerException;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.waf.controller.ejb.EJBControllerLocal;
import com.sun.j2ee.blueprints.waf.controller.impl.ClientStateFlowHandler;
import com.sun.j2ee.blueprints.waf.controller.web.DefaultComponentManager;
import com.sun.j2ee.blueprints.waf.controller.web.EJBProxyWebController;
import com.sun.j2ee.blueprints.waf.util.I18nUtil;

@ExceptionHandler
public privileged aspect LogGeneralException extends GenericAbstractHandler{
	
	pointcut internalSessionDestroyedHandler(): execution(private void DefaultComponentManager.internalSessionDestroyed(EJBControllerLocal));
	pointcut internalDestroyHandler(): execution(private void EJBProxyWebController.internalDestroy(EJBControllerLocal));
	pointcut internalParseKeywordsHandler(): execution(private static Vector I18nUtil.internalParseKeywords(String,
			Vector, BreakIterator, int));
	pointcut internalProcessFlowHandler(): execution(private void ClientStateFlowHandler.internalProcessFlow(HttpServletRequest , String ,	String , byte[]));
	pointcut internalDoWorkHandler(): execution(private void CrmBean.internalDoWork(String, String , String));
	
	
	declare soft: RemoveException : internalSessionDestroyedHandler() || internalDestroyHandler();
	declare soft: Throwable :  internalParseKeywordsHandler();
	declare soft: OptionalDataException :internalProcessFlowHandler();
	declare soft: ClassNotFoundException :internalProcessFlowHandler();
	declare soft: IOException :internalProcessFlowHandler();	
	declare soft: MailerException: internalDoWorkHandler();
	declare soft: Exception: internalDoWorkHandler();
	
	public pointcut checkedExceptionLog(): internalSessionDestroyedHandler() || 
	internalDestroyHandler() || internalParseKeywordsHandler() || 
	internalProcessFlowHandler() || internalDoWorkHandler();
	
	
	
	public void getMessageText(int pointcutId, Exception e){
		switch (pointcutId) {
		case 0:
			System.err.println("CrmBean: caught=" + e);
			e.printStackTrace();
			break;
		case 1:	
			Debug.print("ClientCacheLinkFlowHandler caught: " + e);
			break;	
		case 2:
			Debug.print(e);
			break;
		case 3:
			Debug.print(e, "Error while parsing search string");
			break;		
		default:
			break;
		}
	}

	
	


}
