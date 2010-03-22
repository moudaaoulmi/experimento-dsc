package org.maze.eimp.exception;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.MissingResourceException;
import org.eclipse.jface.preference.PreferenceStore;
import org.maze.eimp.eimpPlugin;
import org.maze.eimp.app.AboutDialog;
import org.maze.eimp.app.Main;
import org.maze.eimp.app.Messages;
import org.maze.eimp.im.Buddy;
import rath.msnm.msg.MimeMessage;
import org.maze.eimp.msn.MSNConnection;
import org.maze.eimp.perf.PreferenceMessages;
import org.maze.eimp.rendezvous.XMLMessage;
import org.maze.eimp.rendezvous.XMLStream;
import org.maze.eimp.script.ImCommandServer;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

public privileged aspect GeneralExceptionHandler extends PrintStackTraceAbstractExceptionHandler{

	pointcut internalLoadFileHandler(): execution(private void AboutDialog.internalLoadFile(FileReader, char[], StringBuffer));

	pointcut internalMainHander(): execution(private static void  Main.internalMain(..));

	pointcut loadPrefHander(): execution(public static void Main.loadPref(PreferenceStore));

	pointcut savePrefHander(): execution(public static void Main.savePref(PreferenceStore));

	pointcut getStringHandler(): execution(public static String Messages.getString(String));

	pointcut getStringHandler2(): execution(public static String org.maze.eimp.app.action.Messages.getString(String));

	// MsnHandler

	pointcut internalLogoutHandler(): execution(private void MSNConnection.internalLogout());

	pointcut internalSendInstantMessageHandler(): execution(private void MSNConnection.internalSendInstantMessage(Buddy, MimeMessage));

	pointcut addBuddyHandler(): execution(public void MSNConnection.addBuddy(Buddy));

	pointcut callBuddyHandler(): execution(public void MSNConnection.callBuddy(Buddy));

	pointcut removeBuddyHandler(): execution(public void MSNConnection.removeBuddy(Buddy));

	pointcut internalSetStatusHandler(): execution(private void MSNConnection.internalSetStatus(String));

	// perf package

	pointcut getStringHandler3(): execution( public static String PreferenceMessages.getString(String));

	// RendezvouzHandler

	pointcut internalSetToHandler(): execution(private void XMLStream.internalSetTo(String) );

	pointcut startMessageHandler(): execution(public void XMLStream.startMessage(XMLMessage) );

	// ScriptHandler

	pointcut internalGetOut2Handler(): execution(private void ImCommandServer.RespCmd.internalGetOut2(StringBuffer, int,
			InputStreamReader));

	// eimpHandler

	pointcut internalStartupHandler(): execution(private void eimpPlugin.internalStartup());
	
	public pointcut printStackTraceException() : internalLoadFileHandler() || 
												 internalMainHander() ||
												 loadPrefHander() || 
												 savePrefHander() || 
												 internalLogoutHandler() || 
												 internalSendInstantMessageHandler() || 
												 addBuddyHandler()|| 
												 callBuddyHandler() || 
												 removeBuddyHandler() || 
												 internalSetStatusHandler() || 
												 internalSetToHandler() || 
												 startMessageHandler() || 
												 internalGetOut2Handler() || 
												 internalStartupHandler();

	declare soft: IOException: printStackTraceException();
	
	after():internalLoadFileHandler()||internalMainHander()||loadPrefHander()|| savePrefHander() || internalLogoutHandler() || internalSendInstantMessageHandler() || 
	  addBuddyHandler()|| callBuddyHandler() || removeBuddyHandler()|| internalSetStatusHandler()||internalSetToHandler() || startMessageHandler()||internalGetOut2Handler()||internalStartupHandler(){
		System.out.println(thisEnclosingJoinPointStaticPart.getId());
	}
	
	

//	declare soft: IOException: internalLoadFileHandler() || 
//							   internalMainHander() || 
//							   loadPrefHander() || 
//							   savePrefHander() || 
//							   internalLogoutHandler() || 
//							   internalSendInstantMessageHandler() || 
//							   addBuddyHandler() || 
//							   callBuddyHandler() || 
//							   removeBuddyHandler()|| 
//							   internalSetStatusHandler() ||
//							   internalSetToHandler() || 
//							   startMessageHandler() || 
//							   internalGetOut2Handler() || 
//							   internalStartupHandler();

//	void around(): internalLoadFileHandler()||internalMainHander()||loadPrefHander()|| savePrefHander() || internalLogoutHandler() || internalSendInstantMessageHandler() || 
//	  addBuddyHandler()|| callBuddyHandler() || removeBuddyHandler()|| internalSetStatusHandler()||internalSetToHandler() || startMessageHandler()||internalGetOut2Handler()||internalStartupHandler(){
//		try {
//			proceed();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	String around(String key): (getStringHandler()|| getStringHandler2()||getStringHandler3() )&& args(key){
		try {
			return proceed(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
