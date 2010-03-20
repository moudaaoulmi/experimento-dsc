package net.sourceforge.metrics.builder;

import java.util.Map;
import br.upe.dsc.reusable.exception.*;
import java.util.Stack;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import exception.ExceptionHandler;
import net.sourceforge.metrics.builder.MetricsBuilder.CalculatorThread;
import net.sourceforge.metrics.builder.MetricsBuilder.Command;
import net.sourceforge.metrics.builder.MetricsBuilder.FilterResult;
import net.sourceforge.metrics.builder.MetricsBuilder.Queue;
import net.sourceforge.metrics.core.Log;
@ExceptionHandler
public privileged aspect BuilderHandler extends EmptyBlockAbstractExceptionHandling {
	
	public pointcut emptyBlockException(): runHandler() || internalFilterHandler();

	pointcut buildHandler(): execution(protected IProject[] MetricsBuilder.build(int, Map, IProgressMonitor));

	pointcut internalFilterHandler(): execution(private void MetricsBuilder.MetricsBuildVisitor.internalFilter(FilterResult,IPackageFragment));

	pointcut internalFilter2Handler(): execution(private boolean MetricsBuilder.MetricsBuildVisitor.internalFilter2(IJavaElement));

	pointcut internalFilter3Handler(): execution(private boolean MetricsBuilder.MetricsBuildVisitor.internalFilter3(FilterResult,IPackageFragment));

	pointcut internalExecuteHeadlessHandler(): execution(private void MetricsBuilder.MetricsBuildVisitor.internalExecuteHeadless(Command));

	pointcut internalExecuteUIHandler(): execution(private void MetricsBuilder.MetricsBuildVisitor.internalExecuteUI(int, Stack, Queue));

	pointcut internalRunHandler(): execution(private static void MetricsBuilder.internalRun(CalculatorThread));

	pointcut runHandler(): execution(public void ProgressQueue.NotifierThread.run());

	

	declare soft: Throwable: buildHandler()||internalExecuteHeadlessHandler()||internalRunHandler();

	declare soft: JavaModelException: internalFilterHandler()||internalFilter2Handler()||internalFilter3Handler();
	declare soft: InterruptedException: internalRunHandler()||runHandler();


//	void around(): runHandler() {
//		try {
//			proceed();
//		} catch (InterruptedException e) {
//		}
//	}

	void around(): internalRunHandler(){
		try {
			proceed();
		} catch (InterruptedException e) {
			// Log.logMessage("Interrupted!");
		} catch (Throwable t) {
			//XXX LOG - não generalizado
			Log.logError("CalculatorThread terminated.", t);
		} finally {
			// make sure a new thread is created next time around
			MetricsBuilder.thread = null;
		}
	}

	void around(Stack stack, Queue queue): internalExecuteUIHandler() && args(*,stack,queue) {
		try {
			proceed(stack, queue);
		} catch (OperationCanceledException e) {
			Log.logMessage("Metrics queuing aborted by user.");
			stack.clear();
			queue.clear();
			throw e;
		}
	}

	void around(Command next): internalExecuteHeadlessHandler() && args(next) {
		try {
			proceed(next);
		} catch (Throwable t) {
			//XXX LOG - não generalizado
			Log.logError("(headless) error calculating metrics for "
					+ next.getHandleIdentifier(), t);
		}
	}

	boolean around(): internalFilter2Handler()||internalFilter3Handler() {
		try {
			return proceed();
		} catch (JavaModelException e) {
			return true;
		}
	}

//	void around(): internalFilterHandler() {
//		try {
//			proceed();
//		} catch (JavaModelException e) {
//		}
//	}

	IProject[] around(): buildHandler(){
		try {
			return proceed();
		} catch (OperationCanceledException x) {
			throw x;
		} catch (Throwable e) {
			//XXX LOG - não generalizado
			Log.logError("Error in MetricsBuilder", e);
		}
		return null;
	}

}
