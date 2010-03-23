package ish.ecletex.editors.tex.spelling.engine;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public privileged aspect EngineHandler {

	Map reader = new HashMap();

	pointcut internalGetConfigurationHandler(): execution(private static Configuration Configuration.internalGetConfiguration(String , Configuration));
	pointcut internalPropertyConfigurationHandler(): execution(private void PropertyConfiguration.internalPropertyConfiguration());

	pointcut internalDictReadLineHandler(): execution(private int SpellDictionaryDichoDisk.internalDictReadLine(int, byte, byte[], int));

	pointcut getWordsHandler(): execution(public List SpellDictionaryDichoDisk.getWords(String));

	pointcut internalHandler(): execution(private void SpellDictionaryDisk.internal());

	pointcut internalGetWordsHandler(): execution(private void SpellDictionaryDisk.internalGetWords(String, Vector, int[]));

	pointcut internalNewDictionaryFilesHandler(): execution(private void SpellDictionaryDisk.internalNewDictionaryFiles(List, File, BufferedReader));

	pointcut bufferedReaderHandler(): call(BufferedReader.new(..)) && withincode(private void SpellDictionaryDisk.internalNewDictionaryFiles(List, File, BufferedReader));

	
	declare soft: InstantiationException : internalGetConfigurationHandler();
	declare soft: IllegalAccessException : internalGetConfigurationHandler();
	declare soft: ClassNotFoundException : internalGetConfigurationHandler();
	declare soft: IOException : getWordsHandler() || internalNewDictionaryFilesHandler() ;
	declare soft: Exception : internalPropertyConfigurationHandler() || internalHandler() || internalGetWordsHandler();
	declare soft: FileNotFoundException : internalNewDictionaryFilesHandler();
	declare soft: EOFException : internalDictReadLineHandler();

	

	void around() throws FileNotFoundException, IOException  : internalNewDictionaryFilesHandler() {
		try {
			proceed();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			BufferedReader read = (BufferedReader) reader.get(Thread
					.currentThread().getName());
			if (read != null)
				read.close();
		}
	}

	BufferedReader around(): bufferedReaderHandler(){
		BufferedReader r = proceed();
		reader.put(Thread.currentThread().getName(), r);
		return r;
	}

	void around(): internalGetWordsHandler() || internalHandler() {
		try {
			proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	List around(): getWordsHandler() {
		try {
			return proceed();
		} catch (IOException ex) {
			System.err.println("IOException: " + ex.getMessage());
			return new LinkedList();
		}
	}

	int around(int i): internalDictReadLineHandler() && args(.., i)
			 {
		try {
			proceed(i);
		} catch (EOFException ex) {
		}
		return i;
	}

	

	void around(): internalPropertyConfigurationHandler() {
		try {
			proceed();
		} catch (Exception e) {
			System.out.println("Could not load Properties file :\n" + e);
		}
	}

	Configuration around(Configuration result): internalGetConfigurationHandler() && args(* , result) {
		try {
			return proceed(result);
		} catch (InstantiationException e) {
			result = new PropertyConfiguration();
		} catch (IllegalAccessException e) {
			result = new PropertyConfiguration();
		} catch (ClassNotFoundException e) {
			result = new PropertyConfiguration();
		}
		return result;
	}

}
