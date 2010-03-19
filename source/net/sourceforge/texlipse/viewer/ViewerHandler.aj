package net.sourceforge.texlipse.viewer;

import java.io.File;
import java.io.IOException;
import net.sourceforge.texlipse.TexlipsePlugin;
import org.aspectj.lang.SoftException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

public privileged aspect ViewerHandler {

	pointcut internalLaunchHandler(): execution(private void TexLaunchConfigurationDelegate.internalLaunch());

	pointcut internalOkPressedHandler(): execution(private void ViewerConfigDialog.internalOkPressed(String));

	pointcut internalOkPressedHandlerSoft(): execution(protected void ViewerConfigDialog.okPressed());

	pointcut internalPreviewHandler(): execution(private static void ViewerManager.internalPreview(long));

	pointcut internalInitializeHandler(): execution(private void ViewerManager.internalInitialize());

	pointcut internalInitializeHandlerSoft(): execution(protected boolean ViewerManager.initialize() );

	pointcut internalRebuildIfNeededHandler(): execution(private void ViewerManager.internalRebuildIfNeeded());

	pointcut internalRebuildIfNeededHandlerSoft(): execution(protected boolean ViewerManager.rebuildIfNeeded());

	pointcut internalGetExistingHandler(): execution(private int ViewerManager.internalGetExisting(Process, int) );

	pointcut internalGetExistingHandler2(): execution(private Process ViewerManager.internalGetExisting(Process));

	pointcut internalExecuteHandler(): execution(private Process ViewerManager.internalExecute(File));

	pointcut internalCheckLineHandler(): execution(private int ViewerOutputScanner.internalCheckLine(String, int) );

	pointcut internalOpenFileFromLineNumberHandler(): execution(private IMarker ViewerOutputScanner.internalOpenFileFromLineNumber(int,
			IResource, IMarker) );

	pointcut internalOpenFileFromLineNumberHandler2(): execution(private void ViewerOutputScanner.internalOpenFileFromLineNumber(IMarker));

	pointcut internalRunHandler(): execution(private void ViewerOutputScanner.EditorOpener.internalRun(IWorkbenchPage));

	pointcut runHandler(): execution(public void ViewerOutputScanner.run());

	declare soft: InterruptedException: internalLaunchHandler()||internalPreviewHandler();
	declare soft: CoreException: internalOkPressedHandler() || internalInitializeHandler()||internalRebuildIfNeededHandler()||internalOpenFileFromLineNumberHandler()||internalOpenFileFromLineNumberHandler2();
	declare soft: IOException: internalGetExistingHandler2()||internalExecuteHandler()||runHandler();
	declare soft: PartInitException: internalRunHandler();

	void around(): runHandler() {
		try {
			proceed();
		} catch (IOException e) {
		}
	}

	void around(): internalRunHandler() {
		try {
			proceed();
		} catch (PartInitException e) {
		}
	}

	void around(): internalOpenFileFromLineNumberHandler2() {
		try {
			proceed();
		} catch (CoreException e) {
		}
	}

	IMarker around(IMarker mark):internalOpenFileFromLineNumberHandler() && args(..,mark) {
		try {
			return proceed(mark);
		} catch (CoreException e) {
		}
		return mark;
	}

	int around(int lineNumber): internalCheckLineHandler() && args(*,lineNumber) {
		try {
			return proceed(lineNumber);
		} catch (NumberFormatException e) {
		}
		return lineNumber;
	}

	Process around(ViewerManager view) throws CoreException : internalExecuteHandler()&&this(view){
		try {
			return proceed(view);
		} catch (IOException e) {
			throw new CoreException(
					TexlipsePlugin
							.stat(
									"Could not start previewer '"
											+ view.registry.getActiveViewer()
											+ "'. Please make sure you have entered "
											+ "the correct path and filename in the viewer preferences.",
									e));
		}
	}

	Process around():internalGetExistingHandler2() {
		try {
			return proceed();
		} catch (IOException e) {
			return null;
		}

	}

	int around(int code): internalGetExistingHandler()&& args(*,code) {
		try {
			return proceed(code);
		} catch (IllegalThreadStateException e) {
		}
		return code;
	}

	//XXX rethrow
	void around(): internalRebuildIfNeededHandler() {
		try {
			proceed();
		} catch (OperationCanceledException e) {
			// build failed, so no output file
			// return false;
			throw new SoftException(null);
		} catch (CoreException e) {
			// build failed, so no output file
			// return false;
			throw new SoftException(null);
		}
	}

	boolean around(): internalInitializeHandlerSoft() ||internalRebuildIfNeededHandlerSoft(){
		try {
			return proceed();
		} catch (SoftException e) {
			return false;
		}
	}

	void around(): internalInitializeHandler() {
		try { // Make sure it's a LaTeX project
			proceed();
		} catch (CoreException e) {
		}
	}

	void around(): internalOkPressedHandlerSoft() {
		try {
			proceed();
		} catch (SoftException e) {
			return;
		}
	}

	void around(): internalOkPressedHandler() {
		try {
			proceed();
		} catch (CoreException e) {
			// Something wrong with the config, or could not read attributes, so
			// swallow and skip
		}
	}

	void around(): internalLaunchHandler() ||internalPreviewHandler(){
		try {
			proceed();
		} catch (InterruptedException e) {
			// swallow
		}
	}

}
