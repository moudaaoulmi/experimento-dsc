package org.maze.eimp.rendezvous;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.maze.eimp.im.Buddy;
import org.xml.sax.SAXException;

import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;


public privileged aspect RendezvousHandler extends EmptyBlockAbstractExceptionHandling{

	pointcut internalRendezVousConnectionHandler(): execution(private void RendezVousConnection.internalRendezVousConnection());

	pointcut loginHandler(): execution(public boolean RendezVousConnection.login());

	pointcut internalRendezVousSessionHandler(): execution(private void RendezVousSession.internalRendezVousSession(Buddy));

	pointcut startSessionHandler(): execution(protected void RendezVousSession.startSession(URL));

	pointcut internalRunHandler(): execution(private void RendezVousSession.MessageReaderThread.internalRun() );

	public pointcut emptyBlockException(): internalRendezVousConnectionHandler();
	
	declare soft: IOException: internalRendezVousConnectionHandler()|| loginHandler()||startSessionHandler()||internalRunHandler();

	declare soft: MalformedURLException: internalRendezVousSessionHandler();

	declare soft: UnknownHostException : startSessionHandler();

	declare soft: SAXException : internalRunHandler();

//	void around():internalRendezVousConnectionHandler() {
//		try {
//			proceed();
//		} catch (IOException ioe) {
//		}
//	}

	boolean around(RendezVousConnection rdvc):loginHandler()&& this(rdvc) {
		try {
			return proceed(rdvc);
		} catch (IOException ioe) {
			return false;
		} finally {
			rdvc.fireLoginComplete();
		}
	}

//	void around() : internalRendezVousSessionHandler() {
//		try {
//			proceed();
//		} catch (MalformedURLException e) {
//		}
//	}

	void around(): startSessionHandler() {
		try {
			proceed();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void around():internalRunHandler() {
		try {
			proceed();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException se) {
		}
	}	

}
