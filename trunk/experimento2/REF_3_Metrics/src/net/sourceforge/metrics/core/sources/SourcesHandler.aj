package net.sourceforge.metrics.core.sources;

import java.util.Iterator;
import java.util.Set;

import jdbm.helper.IterationException;
import jdbm.htree.HTree;
import net.sourceforge.metrics.core.ICalculator;
import net.sourceforge.metrics.core.Log;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect SourcesHandler {

	pointcut calculateHandler(): call(public void ICalculator.calculate(..)) && withincode(protected void AbstractMetricSource.invokeCalculators());

	pointcut internalGetHashtableForProjectHandler(): execution(private HTree Cache.internalGetHashtableForProject(String,HTree));

	pointcut putHandler(): execution(public void Cache.put(AbstractMetricSource));

	pointcut internalGetKeysHandler(): execution(private void Cache.internalGetKeys(HTree, Set));

	pointcut getHandler(): execution(public AbstractMetricSource Cache.get(String));

	pointcut removeHandler(): execution(public void Cache.remove(String));

	pointcut internalRemoveSubtreeHandler(): execution(private void Cache.internalRemoveSubtree(HTree, Iterator, String));


	pointcut getASTHandler(): execution(private CompilationUnit CompilationUnitMetrics.getAST());
	
	pointcut internalInitializeChildrenHandler3(): execution(private void ProjectMetrics.internalInitializeChildren(IJavaProject) );

	pointcut internalGetHierarchyHandler(): execution(private void TypeMetrics.internalGetHierarchy(IType));

	pointcut clearHandler(): execution(public void Cache.clear(String));
	
	declare soft: Throwable :calculateHandler()||internalGetHashtableForProjectHandler()||putHandler()||internalGetKeysHandler()||getHandler()||removeHandler()||internalRemoveSubtreeHandler()|| internalGetHierarchyHandler() || clearHandler(); 
	
	declare soft: JavaModelException :internalInitializeChildrenHandler3();
	

	void around(TypeMetrics tp): internalGetHierarchyHandler() && this(tp) {
		try {
			proceed(tp);
		} catch (Throwable e) {
			Log.logError("Could not get type hierarchy for " + tp.getHandle(),
					e);
		}
	}

	void around(): internalInitializeChildrenHandler3() {
		try {
			proceed();
		} catch (JavaModelException e) {
		}
	}

	CompilationUnit around(): getASTHandler() {
		try {
			return proceed();
		} catch (RuntimeException e) {
			//XXX LOG - não generalizar
			Log.logError("No AST obtained!", e);
			// occurs when the compilation unit gets deleted at a bad time
		}
		return null;
	}

	void around(String projectName): clearHandler() && args(projectName) {
		try {
			proceed(projectName);
		} catch (Throwable e) {
			//XXX LOG - não generalizar
			Log.logError("Could not clear project " + projectName, e);
		}
	}


	void around(String next): internalRemoveSubtreeHandler() && args(.., next) {
		try {
			proceed(next);
		} catch (Throwable e) {
			//XXX LOG - não generalizar
			// doesn't seem to be a severe problem, don't log
			Log.logError("Could not remove " + next, e);
		}
	}

	void around(String handle): removeHandler() && args(handle) {
		try {
			proceed(handle);
		} catch (Throwable e) {
			//XXX LOG - não generalizar
			Log.logError("Could not remove " + handle, e);
		}
	}

	AbstractMetricSource around(String handle): getHandler() && args(handle) {
		try {
			return proceed(handle);
		} catch (Throwable e) {
			//XXX LOG - não generalizar
			Log.logError("Error fetching data for " + handle, e);
			return null;
		}
	}

	void around(): internalGetKeysHandler() {
		try {
			proceed();
		} catch (IterationException e) {
			// ok
		} catch (Throwable e) {
			//XXX LOG - não generalizar
			Log.logError("Error iterating over database keys", e);
		}
	}

	void around(AbstractMetricSource source): putHandler() && args(source) {
		try {
			proceed(source);
		} catch (Throwable e) {
			//XXX LOG - não generalizar
			Log.logError("Could not store " + source.getHandle(), e);
		}
	}

	HTree around(String projectName, HTree hashtable): internalGetHashtableForProjectHandler() && args(projectName,hashtable) {
		try {
			return proceed(projectName, hashtable);
		} catch (Throwable e) {
			//XXX LOG - não generalizar
			Log.logError("Could not get/create HTree for " + projectName, e);
		}
		return hashtable;
	}

	void around(AbstractMetricSource ab): calculateHandler() && this(ab){
		try {
			proceed(ab);
		} catch (OutOfMemoryError m) {
			throw m;
		} catch (Throwable e) {
			Log.logError("Error running calculators for "
					+ ab.getJavaElement().getHandleIdentifier(), e);
		}
	}

}
