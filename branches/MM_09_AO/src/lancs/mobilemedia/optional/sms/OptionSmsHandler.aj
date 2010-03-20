package lancs.mobilemedia.optional.sms;

import javax.microedition.lcdui.Alert;
import javax.wireless.messaging.MessageConnection;
import org.aspectj.lang.SoftException;


import java.io.IOException;
import java.io.InterruptedIOException;

//import lancs.mobilemedia.exception.ExceptionHandler;
import lancs.mobilemedia.optional.sms.SmsMessaging;
import lancs.mobilemedia.optional.sms.SmsReceiverThread;
import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

//@ExceptionHandler
public privileged aspect OptionSmsHandler extends PrintStackTraceAbstractExceptionHandler {
	
	pointcut internalSendImageHandler(): execution(MessageConnection SmsMessaging.internalSendImage(byte[], String, MessageConnection));
	pointcut sendImageHandler(): execution(boolean SmsMessaging.sendImage(byte[]));
	pointcut internalReceiveMessageHandler(): execution(MessageConnection internalReceiveImage(String));
	pointcut internalCleanUpConnectionsHandler(): execution(void SmsMessaging.internalCleanUpConnections(MessageConnection));
	pointcut internalCleanUpReceiverConnectionsHandler(): execution(void SmsMessaging.internalCleanUpReceiverConnections());
	pointcut internalRunHandler(): execution(void SmsReceiverThread.internalRun(SmsMessaging));
	pointcut internalRun2Handler(): execution(byte[] SmsReceiverThread.internalRun2(SmsMessaging));
	
	public pointcut printStackTraceException() : internalReceiveMessageHandler();
	
	declare soft: Throwable: internalSendImageHandler();
	declare soft: IOException: internalReceiveMessageHandler() || internalCleanUpConnectionsHandler() 
							   || internalCleanUpReceiverConnectionsHandler() || internalRun2Handler();
	declare soft: InterruptedIOException: internalRun2Handler();
	
	MessageConnection around(): internalSendImageHandler() {
		try {
			return proceed();
		} catch (Throwable t) {
			System.out.println("Send caught: ");
			t.printStackTrace();
			throw new SoftException(t);
			//return false;
		}
	}
	
	boolean around(): sendImageHandler() {
		try {
			return proceed();
		} catch(SoftException e) {
			return false;
		}
	}
	
//	MessageConnection around(): internalReceiveMessageHandler() {
//		try {
//			return proceed();
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
//		return null;
//	}
	
	void around(): internalCleanUpConnectionsHandler() || internalCleanUpReceiverConnectionsHandler(){
		try {
			proceed();
		} catch (IOException ioe) {
			System.out.println("Closing connection caught: ");
			ioe.printStackTrace();
		}
	}
	
	byte[] around(SmsReceiverThread receiver, SmsMessaging smsMessenger) : internalRun2Handler() && this(receiver) && args(smsMessenger) {
		try {
			return proceed(receiver, smsMessenger);
		} catch (InterruptedIOException e) {
			Alert alert = new Alert("Error Incoming Photo");
			alert.setString("You have just received a bad fragmentated photo which was not possible to recovery.");
			alert.addCommand(receiver.errorNotice);
			alert.setCommandListener(receiver.controller);
			receiver.controller.setCurrentScreen(alert);
			smsMessenger.cleanUpReceiverConnections();
			throw new SoftException(e);
		} catch (IOException e) {
			Alert alert = new Alert("Error Incoming Photo");
			alert.setString("You have just received a bad fragmentated photo which was not possible to recovery.");
			alert.addCommand(receiver.errorNotice);
			alert.setCommandListener(receiver.controller);
			receiver.controller.setCurrentScreen(alert);
			smsMessenger.cleanUpReceiverConnections();
			throw new SoftException(e);
		}
	}
	
	void around() : internalRunHandler() {
		try {
			proceed();
		} catch(SoftException e) {
			// no code, to simulate the continue construction
		}
	}
	
	
}
