package org.jhotdraw.util;

import org.jhotdraw.framework.Drawing;
import org.jhotdraw.framework.JHotDrawRuntimeException;
import java.awt.Image;
import java.io.IOException;
import javax.jdo.PersistenceManager;

public privileged aspect  UtilHandler{

	// ---------------------------
	// Declare soft's
	// ---------------------------
	declare soft: ClassNotFoundException: isJDK12Handler() ||
  										  createCollectionsFactoryHandler() ||
  										  restoreHandler() ||
  										  makeInstanceHandler();

	declare soft: InstantiationException: createCollectionsFactoryHandler() ||
										  makeInstanceHandler();

	declare soft: IllegalAccessException: createCollectionsFactoryHandler() ||
										  makeInstanceHandler();

	declare soft: Exception: loadImageResourceHandler();



	declare soft: NoSuchMethodError: makeInstanceHandler();
	
	declare soft: IOException: readVersionFromFileHandler() ||
				  mainHandler();

	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut isJDK12Handler(): 
		execution (* CollectionsFactory.isJDK12(..));

	pointcut createCollectionsFactoryHandler(): 
		execution(* CollectionsFactory.createCollectionsFactory(..));

	pointcut loadImageResourceHandler():
		execution(* Iconkit.loadImageResource(..));

	/**
	pointcut storeInternalHandler():
		execution(* JDOStorageFormat.storeInternal(..));

	 pointcut restoreInternalHandler():
		execution(* JDOStorageFormat.restoreInternal(..)); */

	pointcut restoreHandler():
		execution(* SerializationStorageFormat.restore(..)); 

	pointcut makeInstanceHandler():
		execution(* StorableInput.makeInstance(..));

	pointcut readVersionFromFileHandler():
		execution(* VersionManagement.readVersionFromFile(..));

	pointcut mainHandler():
		execution(* JDOStorageFormat.main(..));

	// ---------------------------
	// Advice's
	// ---------------------------

	boolean around(): isJDK12Handler(){

		boolean result = false;
		try {
			result = proceed();
		} catch (ClassNotFoundException e) {
			// ignore
		}
		return result;
	}

	CollectionsFactory around(): createCollectionsFactoryHandler(){
		CollectionsFactory result = null;
		try {
			result = proceed();
		} catch (ClassNotFoundException e) {
			throw new JHotDrawRuntimeException(e);
		} catch (InstantiationException e) {
			throw new JHotDrawRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new JHotDrawRuntimeException(e);
		}
		return result;
	}


	Image around(): loadImageResourceHandler(){
		Image result = null;
		try {
			result = proceed();
		} catch (Exception ex) {
			result = null;
		}
		return result;
	}

	/**
	 * Cenário em que não dah pra refatorar pois tenta 
	 * chamar um método privado da classe JDOStorageFormat
	 * 
	 * 
	String around(Drawing storeDrawing, PersistenceManager pm,
			Drawing txnDrawing, String drawingName): 
		storeInternalHandler() &&
		args(storeDrawing, pm, txnDrawing, drawingName){

		String result = null;

		try {
			result = proceed(storeDrawing, pm, txnDrawing, drawingName);
		} finally {
			JDOStorageFormat jDSF = (JDOStorageFormat) thisJoinPoint.getThis();
			jDSF.endTransaction(pm, (drawingName != null));
		}
		return result;
	} */

	/**
	 * 
	 * Cenário em que não dah pra refatorar pois tenta 
	 * chamar um método privado da classe JDOStorageFormat
	 * 
	 * 
	 * 
	Drawing around(PersistenceManager pm, Drawing restoredDrawing): 
		restoreInternalHandler() &&
			args( pm, restoredDrawing){

		Drawing result = null;
		try {
			result = proceed(pm, restoredDrawing);
		} finally {
			JDOStorageFormat jDSF = (JDOStorageFormat) thisJoinPoint.getThis();
			jDSF.endTransaction(pm, false);
		}
		return result;
	}*/



	Drawing around(String fileName) throws IOException:
		restoreHandler() &&
		args(fileName){

		Drawing result = null;
		try {
			result = proceed(fileName);
		} catch (ClassNotFoundException exception) {
			throw new IOException("Could not restore drawing '" + fileName
					+ "': class not found!");
		}
		return result;
	}

	Object around(String className) throws IOException: 
		makeInstanceHandler() &&
		args(className){

		Object result = null;
		try {
			result = proceed(className);
		} catch (NoSuchMethodError e) {
			throw new IOException("Class " + className
					+ " does not seem to have a no-arg constructor");
		} catch (ClassNotFoundException e) {
			throw new IOException("No class: " + className);
		} catch (InstantiationException e) {
			throw new IOException("Cannot instantiate: " + className);
		} catch (IllegalAccessException e) {
			throw new IOException("Class (" + className + ") not accessible");
		}
		return result;
	}
	
	Object around(): readVersionFromFileHandler() ||
	mainHandler(){
		Object result = null;
		try {
			result = proceed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}