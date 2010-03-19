package net.sourceforge.texlipse.properties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import net.sourceforge.texlipse.TexlipsePlugin;

import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public privileged aspect PropertiesHandler {

	pointcut internalPerformDefaultsHandler(): execution(private int TexlipseProjectPropertyPage.internalPerformDefaults(String, int));

	pointcut internalGetFileContentsHandler(): execution(private static String TexlipseProperties.internalGetFileContents(IResource,ByteArrayOutputStream, String));

	pointcut internalGetAllMemberFilesHandler(): execution(private static IResource[] TexlipseProperties.internalGetAllMemberFiles(IContainer,IResource[]));

	pointcut internalLoadProjectPropertiesHandler(): execution(private static void TexlipseProperties.internalLoadProjectProperties(IFile,Properties));

	pointcut internalLoadProjectPropertiesHandlerSoft(): execution(public static void TexlipseProperties.loadProjectProperties(IProject));

	pointcut internalSaveProjectPropertiesHandler(): execution(private static void TexlipseProperties.internalSaveProjectProperties(Properties,ByteArrayOutputStream));

	

	pointcut getProjectPropertyHandler(): execution(public static String TexlipseProperties.getProjectProperty(IResource, String));

	pointcut setProjectPropertyHandler(): execution(public static void TexlipseProperties.setProjectProperty(IResource, String,String));

	pointcut getSessionPropertyHandler(): execution(public static Object TexlipseProperties.getSessionProperty(IResource, String));

	pointcut setSessionPropertyHandler(): execution(public static void TexlipseProperties.setSessionProperty(IResource, String,Object));

	declare soft: UnsupportedEncodingException : internalGetFileContentsHandler();
	declare soft: CoreException : internalGetFileContentsHandler()||internalGetAllMemberFilesHandler()||internalLoadProjectPropertiesHandler()||getProjectPropertyHandler()||setProjectPropertyHandler()||getSessionPropertyHandler()||setSessionPropertyHandler();
	declare soft: IOException : internalLoadProjectPropertiesHandler()||internalSaveProjectPropertiesHandler();

	void around(): setSessionPropertyHandler() {
		try {
			proceed();
		} catch (CoreException e) {
			// do nothing
		}
	}

	Object around(): getSessionPropertyHandler() {
		try {
			return proceed();
		} catch (CoreException e) {
			// do nothing
		}
		return null;
	}

	void around(): setProjectPropertyHandler() {
		try {
			proceed();
		} catch (CoreException e) {
			// do nothing
		}
	}

	String around(): getProjectPropertyHandler() {
		try {
			return proceed();
		} catch (CoreException e) {
			// do nothing
		}
		return null;
	}

	void around(): internalSaveProjectPropertiesHandler() {
		try {
			proceed();
		} catch (IOException e) {
		}
	}

	void around(): internalLoadProjectPropertiesHandlerSoft() {
		try {
			proceed();
		} catch (SoftException e) {
			return;
		}
	}

	void around(): internalLoadProjectPropertiesHandler() {
		try {
			proceed();
		} catch (CoreException e) {
			TexlipsePlugin.log("Loading project property file", e);
			// return;
			throw new SoftException(e);
		} catch (IOException e) {
			TexlipsePlugin.log("Loading project property file", e);
			// return;
			throw new SoftException(e);
		}
	}

	IResource[] around(IResource[] arr): internalGetAllMemberFilesHandler() && args(*,arr) {
		try {
			return proceed(arr);
		} catch (CoreException e) {
		}
		return arr;
	}

	String around(ByteArrayOutputStream out, String contents): internalGetFileContentsHandler() && args(*,out,contents) {
		try {
			return proceed(out, contents);
		} catch (UnsupportedEncodingException e) {
			// if the correct encoding is not supported, try with default
			contents = out.toString();
		} catch (CoreException e) {
			// should not happen
		}
		return contents;
	}

	int around(int num): internalPerformDefaultsHandler() && args(*,num) {
		try {
			proceed(num);
		} catch (NumberFormatException e) {
		}
		return num;
	}

}
