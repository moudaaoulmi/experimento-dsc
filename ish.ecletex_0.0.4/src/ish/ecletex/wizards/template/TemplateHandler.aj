package ish.ecletex.wizards.template;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public privileged aspect TemplateHandler {

	pointcut internalRunHandler(): execution(private void templateCreationOperation.internalRun(IProgressMonitor));
	
	pointcut internalPerformFinishHandler(): execution(private boolean templateCreationWizard.internalPerformFinish(IRunnableWithProgress));

	declare soft: IOException: internalRunHandler();
	declare soft: InvocationTargetException: internalPerformFinishHandler();
	declare soft: InterruptedException: internalPerformFinishHandler();
	
	boolean around(templateCreationWizard temp): internalPerformFinishHandler() && this(temp){
		try {
			return proceed(temp);
		} catch (InvocationTargetException e) {
			temp.handleException(e.getTargetException());
			return false;
		} catch (InterruptedException e) {
			return false;
		}
	}

	void around(IProgressMonitor monitor) throws InvocationTargetException : internalRunHandler() && args(monitor) {
		try {
			proceed(monitor);
		} catch (IOException ex) {
			throw new InvocationTargetException(ex);
		} finally {
			monitor.done();
		}
	}

}
