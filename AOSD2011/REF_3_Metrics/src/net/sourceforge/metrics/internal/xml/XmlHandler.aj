package net.sourceforge.metrics.internal.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect XmlHandler {

	pointcut exportHandler(): execution(public void MetricsFirstExporter.export(IJavaElement, File, IProgressMonitor));

	pointcut exportHandler2(): execution(public void XMLSourceTreeExporter.export(IJavaElement, File, IProgressMonitor));

	declare soft: FileNotFoundException: exportHandler();
	declare soft: Throwable: exportHandler2();

	void around() throws InvocationTargetException: exportHandler2() {
		try {
			proceed();
		} catch (Throwable e) {
			throw new InvocationTargetException(e);
		}
	}

	void around() throws InvocationTargetException: exportHandler(){
		try {
			proceed();
		} catch (FileNotFoundException e) {
			throw new InvocationTargetException(e);
		}
	}
}
