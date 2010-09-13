package net.sourceforge.metrics.ui;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import br.upe.dsc.reusable.exception.*;
import net.sourceforge.metrics.core.IExporter;
import net.sourceforge.metrics.core.Log;
import net.sourceforge.metrics.ui.EnableMetrics.MetricsNatureException;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.touchgraph.graphlayout.TGException;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect UiHandler extends PrintStackTraceAbstractExceptionHandler {
	
	public pointcut printStackTraceException(): showViewHandler() || setDependenciesHandler();

	pointcut internalRunHandler(): execution(private void EnableMetrics.internalRun(Shell, IRunnableWithProgress));

	pointcut selectionChangedHandler(): execution(public void EnableMetrics.selectionChanged(IAction, ISelection));

	pointcut runHandler(): execution(public void MetricsActionGroup.GraphAction.run());

	pointcut runHandler2(): execution(public void MetricsActionGroup.ExportAction.run());

	pointcut runHandler3(): execution(public void MetricsActionGroup.AbortAllAction.run());

	pointcut runHandler4(): execution(public void MetricsActionGroup.PauseAction.run());

	pointcut runHandler5(): execution(public void MetricsActionGroup.ResumeAction.run());

	pointcut setDependenciesHandler(): execution(public void DependencyGraphView.setDependencies(Map));

	pointcut selectionChangedHandler2(): execution(public void AbortMetrics.selectionChanged(IAction, ISelection));

	pointcut internalWidgetDefaultSelectedHandler(): execution(private void MetricsTable.internalWidgetDefaultSelected(IJavaElement,String));

	pointcut getWidthHandler(): execution(private int MetricsTable.getWidth(IMemento, String, int));

	pointcut showViewHandler(): call(public IViewPart IWorkbenchPage.showView(..)) && withincode(private void MetricsView.displayDependencyGraphSWT(..));

	pointcut doExportHandler(): execution(private void MetricsView.doExport(Shell, File, IExporter));

	declare soft: MetricsNatureException: internalRunHandler();
	declare soft: InterruptedException: internalRunHandler()||doExportHandler();
	declare soft: InvocationTargetException: internalRunHandler()||doExportHandler();
	declare soft: Throwable: selectionChangedHandler() || selectionChangedHandler2()||internalWidgetDefaultSelectedHandler()||getWidthHandler();
	declare soft: TGException: setDependenciesHandler();
	declare soft: PartInitException: internalWidgetDefaultSelectedHandler()||showViewHandler();
	declare soft: JavaModelException: internalWidgetDefaultSelectedHandler();

	void around(final File outputFile): doExportHandler() && args(*,outputFile,*) {
		try {
			proceed(outputFile);
		} catch (InvocationTargetException e) {
			Log.logError("MetricsView::doExport", e);
		} catch (InterruptedException e) {
			outputFile.delete();
		}
	}

//	IViewPart around(): showViewHandler() {
//		try {
//			return proceed();
//		} catch (PartInitException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	int around(int defaultVal): getWidthHandler() && args(..,defaultVal) {
		try {
			return proceed(defaultVal);
		} catch (Throwable e) {
			return defaultVal;
		}
	}

	void around(String handle): internalWidgetDefaultSelectedHandler() && args(*,handle) {
		try {
			proceed(handle);
		} catch (PartInitException x) {
			System.err.println("Error selecting " + handle);
			x.printStackTrace();
		} catch (JavaModelException x) {
			System.err.println("Error selecting " + handle);
			x.printStackTrace();
		} catch (Throwable t) {
			System.err.println("Error selecting " + handle);
			t.printStackTrace();
		}
	}
	
	
	void around(AbortMetrics ab): selectionChangedHandler2() && this(ab) {
		try {
			proceed(ab);
		} catch (Throwable e) {
			Log.logError("AbortMetrics: error getting project.", e);
			ab.project = null;
		}
	}

//	void around(): setDependenciesHandler() {
//		try {
//			proceed();
//		} catch (TGException e) {
//			e.printStackTrace();
//		}
//	}

	void around(): runHandler4() || runHandler5(){
		try {
			proceed();
		} catch (RuntimeException e) {
			//XXX LOG - não generalizar
			Log.logError("PauseAction::run", e);
		}
	}

	void around(): runHandler3() {
		try {
			proceed();
		} catch (RuntimeException e) {
			//XXX LOG - não generalizar
			Log.logError("AbortAllAction::run", e);
		}
	}

	void around(): runHandler2() {
		try {
			proceed();
		} catch (RuntimeException e) {
			//XXX LOG - não generalizar
			Log.logError("ExportAction::run", e);
		}
	}

	void around(): runHandler() {
		try {
			proceed();
		} catch (RuntimeException e) {
			//XXX LOG - não generalizar
			Log.logError("GraphAction::run", e);
		}
	}

	void around(EnableMetrics en): selectionChangedHandler()&& this(en) {
		try {
			proceed(en);
		} catch (Throwable e) {
			Log.logError("EnableMetrics: error getting project.", e);
			en.project = null;
		}
	}

	void around(Shell shell): internalRunHandler()&&args(shell,*) {
		try {
			proceed(shell);
		} catch (MetricsNatureException e) {
			Log.logError("Could not " + e.getTask() + " metrics", e);
			MessageDialog.openInformation(shell, "Metrics",
					"Could not enable metrics.");
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
			//XXX LOG - não generalizar
			Log.logError("Could not change metrics enablement", e);
		}
	}

}
