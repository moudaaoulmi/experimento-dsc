package org.jhotdraw.util;

import java.awt.Image;
import java.io.IOException;

import org.jhotdraw.framework.Drawing;
import org.jhotdraw.framework.JHotDrawRuntimeException;

public privileged aspect  UtilExceptionHandler{

	// ---------------------------
	// Declare soft's
	// ---------------------------
	declare soft: ClassNotFoundException: CollectionsFactory_isJDK12Handler() ||
										  CollectionsFactory_createCollectionsFactoryHandler() ||
										  SerializationStorageFormat_restoreHandler() ||
  										  StorableInput_makeInstanceHandler();

	declare soft: InstantiationException: CollectionsFactory_createCollectionsFactoryHandler() ||
										  StorableInput_makeInstanceHandler();

	declare soft: IllegalAccessException: CollectionsFactory_createCollectionsFactoryHandler() ||
										  StorableInput_makeInstanceHandler();

	declare soft: Exception: Iconkit_loadImageResourceHandler();



	declare soft: NoSuchMethodError: StorableInput_makeInstanceHandler();
	
	declare soft: IOException: VersionManagement_readVersionFromFileHandler() ||
				  JDOStorageFormat_mainHandler();

	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut CollectionsFactory_isJDK12Handler(): 
		execution (boolean CollectionsFactory.isJDK12(..));

	pointcut CollectionsFactory_createCollectionsFactoryHandler(): 
		execution(CollectionsFactory CollectionsFactory.createCollectionsFactory(..));

	pointcut Iconkit_loadImageResourceHandler():
		execution(Image Iconkit.loadImageResource(..));

	/**
	pointcut storeInternalHandler():
		execution(* JDOStorageFormat.storeInternal(..));

	 pointcut restoreInternalHandler():
		execution(* JDOStorageFormat.restoreInternal(..)); */

	pointcut SerializationStorageFormat_restoreHandler():
		execution(Drawing SerializationStorageFormat.restore(..)); 

	pointcut StorableInput_makeInstanceHandler():
		execution(Object StorableInput.makeInstance(..));

	pointcut VersionManagement_readVersionFromFileHandler():
		execution(String VersionManagement.readVersionFromFile(..));

	pointcut JDOStorageFormat_mainHandler():
		execution(void JDOStorageFormat.main(..));

	// ---------------------------
	// Advice's
	// ---------------------------

	boolean around(): CollectionsFactory_isJDK12Handler(){

		boolean result = false;
		try {
			result = proceed();
		} catch (ClassNotFoundException e) {
			// ignore
		}
		return result;
	}

	CollectionsFactory around(): CollectionsFactory_createCollectionsFactoryHandler(){
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


	Image around(): Iconkit_loadImageResourceHandler(){
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
		SerializationStorageFormat_restoreHandler() &&
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
		StorableInput_makeInstanceHandler() &&
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
	
	Object around(): VersionManagement_readVersionFromFileHandler() ||
	JDOStorageFormat_mainHandler(){
		Object result = null;
		try {
			result = proceed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}