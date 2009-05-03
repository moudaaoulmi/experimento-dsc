package com.sun.j2ee.blueprints.admin.client;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import exception.ExceptionHandler;
import exception.GeneralException;


@ExceptionHandler

public class ClientHandler extends GeneralException{

	public void updateModelDatesHandler(JPanel panel) {
		JOptionPane.showMessageDialog(panel, PetStoreAdminClient
				.getString("DateFormatErrorDialog.message"),
				PetStoreAdminClient.getString("DateFormatErrorDialog.title"),
				JOptionPane.ERROR_MESSAGE);
	}


	public Object getValueAtHandler() {
		return new Integer(-1);
	}

	public void doHttpPostHandler(HttpURLConnection uc) {
		if (uc != null) {
			uc.disconnect();
		}
	}

	public void doHttpPostFinallyHandler(OutputStream ost, InputStream ist) {
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

	public String getBodyHandler(Exception e) {
		e.printStackTrace();
		return null;
	}

	public Date getDateHandler(Exception e) {
		e.printStackTrace();
		return null;
	}

	public int getIntHandler(Exception e) {
		e.printStackTrace();
		return -1;
	}

	public float getFloatHandler(Exception e) {
		e.printStackTrace();
		return -1.0f;
	}


	public void actionPerformedHandler(final Exception e, final ServerAction serverAction) {
		Runnable doHandleException = new Runnable() {
			public void run() {
				serverAction.handleException(e);
			}
		};
		EventQueue.invokeLater(doHandleException);
	}
	
	public static void staticPrintStackTraceHandler(Exception e) {
		e.printStackTrace();
	}
	
	public boolean workerThreadHandler(){
		return true;
	}

}
