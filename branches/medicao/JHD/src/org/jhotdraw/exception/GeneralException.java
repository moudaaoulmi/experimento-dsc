package org.jhotdraw.exception;

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

}
