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

import org.w3c.dom.Node;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public privileged aspect AdminClientHandler {

	pointcut updateModelDatesBarChartPanelHandler() : 		
		execution(private void BarChartPanel.updateModelDates());
	pointcut dataSourceHandler() :
		execution(com.sun.j2ee.blueprints.admin.client.DataSource.new(JFrame, String, String, String, String));
	pointcut internalGetIdHandler() :
		execution(Integer DataSource.OrdersViewTableModel.internalGetId(..));
	pointcut internalGetUrlHandler() :
		execution(URL HttpPostPetStoreProxy.internalGetUrl(..));
	pointcut internalGetDocumentBuilderHandler() :
		execution(DocumentBuilder HttpPostPetStoreProxy.internalGetDocumentBuilder(..));
	pointcut internalDoHttpPostHandler() :
		execution(Document HttpPostPetStoreProxy.internalDoHttpPost(..));
	pointcut getBodyHandler() : 
		execution(String HttpPostPetStoreProxy.getBody(Node));
	pointcut getDateHandler() : 
		execution(Date HttpPostPetStoreProxy.getDate(Node));
	pointcut getIntHandler() : 
		execution(int HttpPostPetStoreProxy.getInt(Node));
	pointcut getFloatHandler() : 
		execution(float HttpPostPetStoreProxy.getFloat(Node));
	pointcut getStringHandler() : 
		execution(public static String PetStoreAdminClient.getString(String));
	pointcut getIntegerHandler() : 
		execution(public static int PetStoreAdminClient.getInteger(String));
	pointcut updateModelDatesHandler() :
		execution(private void PieChartPanel.updateModelDates());
	pointcut internalSetValueHandler() : 
		execution(private void ServerAction.internalSetValue(Object));
	pointcut internalWaitQueueHandler() : 
		execution(private void WorkQueue.internalWaitQueue());
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
	void around(javax.swing.JPanel panel) : 
		(updateModelDatesBarChartPanelHandler() || updateModelDatesHandler()) && target(panel) {
		try {
			proceed(panel);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(panel, PetStoreAdminClient
					.getString("DateFormatErrorDialog.message"),
					PetStoreAdminClient
							.getString("DateFormatErrorDialog.title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	Object around() :  dataSourceHandler() 
					|| internalGetUrlHandler()
					|| internalGetDocumentBuilderHandler() 
					|| getBodyHandler() 
					|| getDateHandler()
					|| getStringHandler(){
		try {
			return proceed();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	Integer around() : internalGetIdHandler() {
		try {
			return proceed();
		} catch (NumberFormatException e) {
			return new Integer(-1);
		}
	}
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
	int around() :   getIntHandler()					
					|| getIntegerHandler(){
		try {
			return proceed();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	float around() : getFloatHandler() {
		try {
			return proceed();
		} catch (Exception e) {
			e.printStackTrace();
			return -1.0f;
		}
	}
	void around(final ServerAction sa) : internalSetValueHandler() && target(sa) {
		try {

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
	void around(WorkQueue wq) : internalWaitQueueHandler() && target(wq) {
		try {
			proceed(wq);
		} catch (InterruptedException e) {
			wq.setStopped(true);
		}
	}
}
