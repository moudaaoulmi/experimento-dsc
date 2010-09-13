package net.sourceforge.metrics.properties;

import java.util.List;
import br.upe.dsc.reusable.exception.*;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect Properties extends EmptyBlockAbstractExceptionHandling {
	
	public pointcut emptyBlockException(): internalGetPackageFragmentRootsHandler();


	pointcut getExclusionPatternsHandler(): execution(private String[] MetricsPropertyPage.EnableMetricsTable.getExclusionPatterns(QualifiedName));

	pointcut internalIsEnabledHandler(): execution(private boolean MetricsPropertyPage.EnableMetricsTable.internalIsEnabled(String, String));

	pointcut isEnabledHandler(): execution(private boolean MetricsPropertyPage.EnableMetricsTable.isEnabled(String));

	pointcut internalGetPackageFragmentRootsHandler(): execution(private void MetricsPropertyPage.EnableMetricsTable.internalGetPackageFragmentRoots(IJavaProject, List));

	declare soft: CoreException: getExclusionPatternsHandler()||internalIsEnabledHandler()||isEnabledHandler();
	declare soft: JavaModelException: internalGetPackageFragmentRootsHandler();

//	void around(): internalGetPackageFragmentRootsHandler() {
//		try {
//			proceed();
//		} catch (JavaModelException e) {
//		}
//	}

	boolean around(): isEnabledHandler() {
		try {
			return proceed();
		} catch (CoreException e) {
			return true;
		}
	}

	boolean around(): internalIsEnabledHandler() {
		try {
			return proceed();
		} catch (CoreException e) {
			return true;
		}
	}

	String[] around(): getExclusionPatternsHandler() {
		try {
			return proceed();
		} catch (CoreException e) {
			return new String[] {};
		}
	}
	
}
