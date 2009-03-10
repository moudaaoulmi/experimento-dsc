package org.jhotdraw.exception;

import javax.swing.JApplet;

public class GeneralException {

	public Object abstractLocatorClone(){
		throw new InternalError();
	}
	
	public void errPrintln(Object o) {
		System.err.println(o);
	}
	
	public void printStackTraceException(Exception e){
		e.printStackTrace();
	}
	
	public void defaultValue(Object object){
		object = null;
	}
	
	public void showStatusApplet(JApplet a, String msg){
		a.showStatus(msg);
	}

}
