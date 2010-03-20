/*
 * Created on 31/08/2005
 */
package com.sun.j2ee.blueprints.admin.client;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.MissingResourceException;
import javax.swing.JPanel;
import org.w3c.dom.Node;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import petstore.exception.ExceptionHandler;
import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

/**
 * @author Raquel Maranhao
 */
@ExceptionHandler
public aspect AdminClientHandler extends PrintStackTraceAbstractExceptionHandler{

	// ---------------------------
    // Declare soft's
    // ---------------------------
	declare soft : ParseException : updateModelDatesBarChartPanelHandler() ||  
									updateModelDatesHandler();
	declare soft : ClassNotFoundException : dataSourceHandler();
	declare soft : InstantiationException : dataSourceHandler();
	declare soft : IllegalAccessException : dataSourceHandler();
	declare soft : MalformedURLException : internalGetUrlHandler();
	declare soft : InterruptedException : internalWaitQueueHandler();
	declare soft : ParserConfigurationException : internalGetDocumentBuilderHandler();
	declare soft : IOException : internalDoHttpPostHandler();
	declare soft : ProtocolException : internalDoHttpPostHandler();
	declare soft : SAXException : internalDoHttpPostHandler();

	
	// ---------------------------
    // Pointcut's
    // ---------------------------
	/*** BarChartPanel ***/
	// Not Necessary!!! Indicates whether execution in target must continue or not
	// public boolean BarChartPanel.bContinue = true;
	/*** BarChartPanel ***/
	pointcut updateModelDatesBarChartPanelHandler() : 		
		execution(private void BarChartPanel.updateModelDates());

	/*
	 * //First Attempt pointcut internalDateFormatHandler() : execution(Date
	 * BarChartPanel.internalDateFormat(..));
	 */

	/*** DataSource ***/
	pointcut dataSourceHandler() :
		execution(com.sun.j2ee.blueprints.admin.client.DataSource.new(JFrame, String, String, String, String));
	
	public pointcut printStackTraceException(): dataSourceHandler();
	
	/*** DataSource.OrdersViewTableModel ***/
	pointcut internalGetIdHandler() :
		execution(Integer DataSource.OrdersViewTableModel.internalGetId(..));

	/*** HttpPostPetStoreProxy ***/
	pointcut internalGetUrlHandler() :
		execution(URL HttpPostPetStoreProxy.internalGetUrl(..));

	pointcut internalGetDocumentBuilderHandler() :
		execution(DocumentBuilder HttpPostPetStoreProxy.internalGetDocumentBuilder(..));

	pointcut internalDoHttpPostHandler() :
		execution(Document HttpPostPetStoreProxy.internalDoHttpPost(..));

	/*
	 * //First attempt - not necessary! pointcut
	 * internalCloseOutputStreamHandler() : execution(void
	 * HttpPostPetStoreProxy.internalCloseOutputStream(..));
	 * 
	 * pointcut internalCloseInputStreamHandler() : execution(void
	 * HttpPostPetStoreProxy.internalCloseInputStream(..));
	 */
	pointcut getBodyHandler() : 
		execution(String HttpPostPetStoreProxy.getBody(Node));

	pointcut getDateHandler() : 
		execution(Date HttpPostPetStoreProxy.getDate(Node));

	pointcut getIntHandler() : 
		execution(int HttpPostPetStoreProxy.getInt(Node));

	pointcut getFloatHandler() : 
		execution(float HttpPostPetStoreProxy.getFloat(Node));

	/*** PetStoreAdminClient ***/
	pointcut getStringHandler() : 
		execution(public static String PetStoreAdminClient.getString(String));

	pointcut getIntegerHandler() : 
		execution(public static int PetStoreAdminClient.getInteger(String));

	/*** PieChartPanel ***/
	pointcut updateModelDatesHandler() :
		execution(private void PieChartPanel.updateModelDates());

	/*** ServerAction ***/
	pointcut internalSetValueHandler() : 
		execution(private void ServerAction.internalSetValue(Object));

	/*** WorkQueue ***/
	pointcut internalWaitQueueHandler() : 
		execution(private void WorkQueue.internalWaitQueue());

	/*
	 * //First Attempt //Create an "around" advice because the exception is
	 * catched but not throwed Date around(BarChartPanel bcp) :
	 * internalDateFormatHandler() && target(bcp) { try { bcp.bContinue = true;
	 * return proceed(bcp); } catch (ParseException e) {
	 * JOptionPane.showMessageDialog(bcp,
	 * PetStoreAdminClient.getString("DateFormatErrorDialog.message"),
	 * PetStoreAdminClient.getString("DateFormatErrorDialog.title"),
	 * JOptionPane.ERROR_MESSAGE); //Indicates that execution must stop
	 * bcp.bContinue = false; return null; } }
	 */

	// ---------------------------
    // Advice's
    // ---------------------------
	// Create an "around" advice because the exception is catched but not
	// throwed
//	void around() : dataSourceHandler() {
//		try {
//			proceed();
//		} catch (ClassNotFoundException cnfe) {
//			cnfe.printStackTrace();
//		} catch (InstantiationException ie) {
//			ie.printStackTrace();
//		} catch (IllegalAccessException iae) {
//			iae.printStackTrace();
//		}
//	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	Integer around() : internalGetIdHandler() {
		try {
			return proceed();
		} catch (NumberFormatException e) {
			return new Integer(-1);
		}
	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	URL around() : internalGetUrlHandler() {
		try {
			return proceed();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			// TBD deal with exception - rethrow to main()
			return null;
		}
	}


	/*
	 * //Create an "around" advice because the exception is catched but not
	 * throwed DocumentBuilder around() : internalGetDocumentBuilderHandler() {
	 * try { return proceed(); } catch (Exception e) { e.printStackTrace(); //
	 * TBD deal with exception - rethrow to main() return null; } }
	 * 
	 * //Create an "around" advice because the exception is catched but not
	 * throwed String around() : getBodyHandler() { try { return proceed(); }
	 * catch (Exception e) { e.printStackTrace(); return null; } }
	 * 
	 * //Create an "around" advice because the exception is catched but not
	 * throwed Date around() : getDateHandler() { try { return proceed(); }
	 * catch (Exception e) { e.printStackTrace(); return null; } }
	 */

	/*
	 * //First attempt - not necessary! //Create an "around" advice because the
	 * exception is catched but not throwed void around() :
	 * internalCloseOutputStreamHandler() || internalCloseInputStreamHandler() {
	 * try { proceed(); } catch (IOException ignore) { } }
	 */
	// Create an "around" advice because the exception is catched but not
	// throwed
	Document around(HttpPostPetStoreProxy hp, String request, InputStream ist,
			OutputStream ost, HttpURLConnection uc) : internalDoHttpPostHandler() && 
									target(hp) && 
									args(request, ist, ost, uc) {
		try {
			return proceed(hp, request, ist, ost, uc);
		} catch (Exception e) {
			if (uc != null) {
				uc.disconnect();
			}
			return null;
		} finally { // Carefully close the input and output streams
			if (ost != null) {
				try {
					ost.close();
				} catch (IOException ignore) {
				}
			}
			if (ist != null) {
				try {
					ist.close();
				} catch (IOException ignore) {
				}
			}
		}
	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	int around() : getFloatHandler() || getIntHandler() || getIntegerHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	// Reuse identicated: consirering return method as Object
	Object around() : internalGetDocumentBuilderHandler() ||
		getBodyHandler() || 
		getDateHandler()  || 
		getStringHandler(){
		try {
			return proceed();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	void around(JPanel jp) : (updateModelDatesHandler() ||
	updateModelDatesBarChartPanelHandler()) && target(jp) {
		try {
			proceed(jp);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(jp, PetStoreAdminClient
					.getString("DateFormatErrorDialog.message"),
					PetStoreAdminClient
							.getString("DateFormatErrorDialog.title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	void around(final ServerAction sa) : internalSetValueHandler() && target(sa) {
		try {
			// TODO: ServerAction is extended by another classes.
			// handleException method to be executed must be the extended one!
			// Test is needed!!!
			System.out
					.println("Aspect - AdminClientHandler. Pointcut - internalSetValueHandler. Class: "
							+ sa.getClass().getName());
			proceed(sa);
		} catch (final Exception e) {
			Runnable doHandleException = new Runnable() {
				public void run() {
					sa.handleException(e);
				}
			};
			EventQueue.invokeLater(doHandleException);
		}
	}

	// Create an "around" advice because the exception is catched but not
	// throwed
	void around(WorkQueue wq) : internalWaitQueueHandler() && target(wq) {
		try {
			proceed(wq);
		} catch (InterruptedException e) {
			wq.setStopped(true);
		}
	}

}
