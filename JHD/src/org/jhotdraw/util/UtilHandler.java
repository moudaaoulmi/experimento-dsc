package org.jhotdraw.util;

import java.io.IOException;

import org.jhotdraw.exception.GeneralExeption;
import org.jhotdraw.framework.JHotDrawRuntimeException;

public class UtilHandler extends GeneralExeption {
	
	public void createCollectionsFactoryHandler(Exception e){
			throw new JHotDrawRuntimeException(e);
	}
	
	public Object loadImageResourceHandler (Exception ex) {
		return null;
	}
	
	public void printStackTraceHandler (IOException e) {
		e.printStackTrace();
	}
	
	public void restoreHandler (ClassNotFoundException exception, String fileName) throws IOException {
		throw new IOException("Could not restore drawing '" + fileName +"': class not found!");
	}
	
	public void makeInstanceHandler(NoSuchMethodError e,  String className) throws IOException{
		throw new IOException("Class " + className
				+ " does not seem to have a no-arg constructor");
	}
	
	public void makeInstanceHandler1(ClassNotFoundException e,  String className) throws IOException{
		throw new IOException("No class: " + className);
	}
	
	public void makeInstanceHandler2(InstantiationException e,  String className) throws IOException{
		throw new IOException("Cannot instantiate: " + className);
	}
	
	public void makeInstanceHandler3(IllegalAccessException e,  String className) throws IOException{
		throw new IOException("Class (" + className + ") not accessible");
	}

}//UtilHandler{}
