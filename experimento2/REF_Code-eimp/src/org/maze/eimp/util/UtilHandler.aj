package org.maze.eimp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.maze.eimp.im.BuddyGroup;
import org.maze.eimp.im.BuddyList;
import org.maze.eimp.im.GroupList;

import br.upe.dsc.reusable.exception.PrintStackTraceAbstractExceptionHandler;

public privileged aspect UtilHandler extends PrintStackTraceAbstractExceptionHandler{

	pointcut internalRunHandler(): execution( private void internalRun())&& within(AllwaysOnlineHelper);

	pointcut internalRun2Handler(): execution(private void internalRun2())&& within(AllwaysOnlineHelper);

	pointcut newURLHandler(): execution(public static URL ImageManager.newURL(String) );

	pointcut internalLoadInformationHandler(): execution(private void LocalStore.internalLoadInformation(File, FileInputStream));

	pointcut internalStoreInformationHandler(): execution(private void LocalStore.internalStoreInformation(FileOutputStream));

	pointcut storeBuddiesHandler(): execution(public void LocalStore.storeBuddies(BuddyGroup));

	pointcut loadBuddiesHandler(): execution(public void LocalStore.loadBuddies(BuddyGroup));

	pointcut internalReadListHandler(): execution(private void LocalStore.internalReadList(BuddyList, File, FileInputStream));

	pointcut internalWriteListHandler(): execution(private void LocalStore.internalWriteList(BuddyList, File,FileOutputStream));

	pointcut internalWriteGroupsHandler(): execution(private void LocalStore.internalWriteGroups(File, Properties,FileOutputStream));

	pointcut internalReadGroupsHandler(): execution(private void LocalStore.internalReadGroups(GroupList, File, Properties,FileInputStream));
	
	public pointcut printStackTraceException(): internalRun2Handler();

	declare soft: InterruptedException:internalRunHandler();
	declare soft: Exception: printStackTraceException() || internalRun2Handler() || storeBuddiesHandler()||loadBuddiesHandler();
	declare soft: MalformedURLException: newURLHandler();
	declare soft: IOException: internalLoadInformationHandler()||internalStoreInformationHandler();

	void around(FileOutputStream fos) throws IOException: (internalWriteListHandler()||internalWriteGroupsHandler()) && args(..,fos) {
		try {
			proceed(fos);
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	void around(FileInputStream fis) throws IOException: (internalReadListHandler()||internalReadGroupsHandler()) && args(..,fis){
		try {
			proceed(fis);
		} finally {
			if (fis != null)
				fis.close();
		}
	}

	void around(): loadBuddiesHandler() {
		try {
			proceed();
		} catch (Exception e) {
			System.err.println("cannot read buddy list cache: " + e);
		}
	}

	void around(): storeBuddiesHandler() {
		try {
			proceed();
		} catch (Exception e) {
			System.err.println("cannot write buddy list cache: " + e);
		}
	}

	void around(FileOutputStream fos): internalStoreInformationHandler()&&args(fos)  {
		try {
			proceed(fos);
		} catch (IOException e) {
			System.err.println("cannot write serial infomration file: " + e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	void around(FileInputStream fis): internalLoadInformationHandler() && args(*, fis) {
		try {
			proceed(fis);
		} catch (IOException e) {
			System.err.println("cannot read local setting file: " + e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}

	void around():internalRunHandler(){
		try {
			proceed();
		} catch (InterruptedException e) {
		}
	}

//	void around():internalRun2Handler(){
//		try {
//			proceed();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	URL around(String url_name) : newURLHandler() && args(url_name) {
		try {
			return proceed(url_name);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL " + url_name + e);
		}
	}
}
