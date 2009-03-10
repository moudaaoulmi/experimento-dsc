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
	declare soft: ClassNotFoundException: CollectionsFactory_isJDK12() ||
										  CollectionsFactory_createCollectionsFactory() ||
										  SerializationStorageFormat_restore() ||
										  StorableInput_makeInstance();

	declare soft: InstantiationException: CollectionsFactory_createCollectionsFactory() ||
										  StorableInput_makeInstance();

	declare soft: IllegalAccessException: CollectionsFactory_createCollectionsFactory() ||
										  StorableInput_makeInstance();

	declare soft: Exception: Iconkit_loadImageResource();



	declare soft: NoSuchMethodError: StorableInput_makeInstance();
	
	declare soft: IOException: VersionManagement_readVersionFromFile() ||
				  JDOStorageFormat_main();

	// ---------------------------
	// Pointcut's
	// ---------------------------
	pointcut CollectionsFactory_isJDK12(): 
		execution (* CollectionsFactory.isJDK12(..));

	pointcut CollectionsFactory_createCollectionsFactory(): 
		execution(* CollectionsFactory.createCollectionsFactory(..));

	pointcut Iconkit_loadImageResource():
		execution(* Iconkit.loadImageResource(..));

	pointcut JDOStorageFormat_storeInternal():
		execution(* JDOStorageFormat.storeInternal(..));

	pointcut JDOStorageFormat_restoreInternal():
		execution(* JDOStorageFormat.restoreInternal(..));

	pointcut SerializationStorageFormat_restore():
		execution(* SerializationStorageFormat.restore(..));

	pointcut StorableInput_makeInstance():
		execution(* StorableInput.makeInstance(..));

	pointcut VersionManagement_readVersionFromFile():
		execution(* VersionManagement.readVersionFromFile(..));

	pointcut JDOStorageFormat_main():
		execution(* JDOStorageFormat.main(..));

	// ---------------------------
	// Advice's
	// ---------------------------

	boolean around(): CollectionsFactory_isJDK12(){

		boolean result = false;
		try {
			result = proceed();
		} catch (ClassNotFoundException e) {
			// ignore
		}
		return result;
	}

	CollectionsFactory around(): CollectionsFactory_createCollectionsFactory(){
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


	Image around(): Iconkit_loadImageResource(){
		Image result = null;
		try {
			result = proceed();
		} catch (Exception ex) {
			result = null;
		}
		return result;
	}

	String around(Drawing storeDrawing, PersistenceManager pm,
			Drawing txnDrawing, String drawingName): 
		JDOStorageFormat_storeInternal() &&
		args(storeDrawing, pm, txnDrawing, drawingName){

		String result = null;

		try {
			result = proceed(storeDrawing, pm, txnDrawing, drawingName);
		} finally {
			JDOStorageFormat jDSF = (JDOStorageFormat) thisJoinPoint.getThis();
			jDSF.endTransaction(pm, (drawingName != null));
		}
		return result;
	}

	Drawing around(PersistenceManager pm, Drawing restoredDrawing): 
		JDOStorageFormat_restoreInternal() &&
			args( pm, restoredDrawing){

		Drawing result = null;
		try {
			result = proceed(pm, restoredDrawing);
		} finally {
			JDOStorageFormat jDSF = (JDOStorageFormat) thisJoinPoint.getThis();
			jDSF.endTransaction(pm, false);
		}
		return result;
	}



	Drawing around(String fileName) throws IOException:
		SerializationStorageFormat_restore() &&
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
		StorableInput_makeInstance() &&
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
	
	Object around(): VersionManagement_readVersionFromFile() ||
	JDOStorageFormat_main(){
		Object result = null;
		try {
			result = proceed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}