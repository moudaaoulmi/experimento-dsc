package ish.ecletex.wizards.project;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public privileged aspect ProjectHandler {

	pointcut internalCreateProjectHandler(): execution(private void ecletexProjectCreationOperation.internalCreateProject(IWorkspaceRoot,ecletexProjectCreationWizardPage, IProgressMonitor,String));

	pointcut internalCreateProject2Handler(): execution(private void ecletexProjectCreationOperation.internalCreateProject2(File));

	pointcut internalPerformFinishHandler(): execution(private boolean ecletexProjectCreationWizard.internalPerformFinish(IRunnableWithProgress));

	declare soft: CoreException: internalCreateProjectHandler();
	declare soft: IOException: internalCreateProject2Handler();
	declare soft: InvocationTargetException: internalPerformFinishHandler();
	declare soft: InterruptedException: internalPerformFinishHandler();

	boolean around(ecletexProjectCreationWizard ecle): internalPerformFinishHandler() && this(ecle) {
		try {
			return proceed(ecle);
		} catch (InvocationTargetException e) {
			ecle.handleException(e.getTargetException());
			return false;
		} catch (InterruptedException e) {
			return false;
		}
	}

	void around() throws InvocationTargetException : internalCreateProjectHandler(){
		try {
			proceed();
		} catch (CoreException e) {
			throw new InvocationTargetException(e);
		}
	}

	void around(): internalCreateProject2Handler() {
		try {
			proceed();
		} catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}

}
