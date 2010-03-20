package net.sourceforge.texlipse.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import net.sourceforge.texlipse.TexlipsePlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public privileged aspect WizardsHandler {

	pointcut internalRunHandler(): execution(private void TexlipseProjectCreationOperation.internalRun(IProgressMonitor) );

	pointcut internalGetTemplateHandler(): execution(private byte[] TexlipseProjectCreationOperation.internalGetTemplate(String, byte[]) );

	pointcut internalGetTemplate2Handler(): execution(private byte[] TexlipseProjectCreationOperation.internalGetTemplate2(String, byte[]) );

	pointcut internalPerformFinishHandler(): execution(private boolean TexlipseProjectCreationWizard.internalPerformFinish(IRunnableWithProgress, boolean) );

	pointcut internalReadProjectTemplateDescriptionHandler(): execution(private void TexlipseProjectCreationWizardPage.internalReadProjectTemplateDescription(URL,	String) );

	declare soft: CoreException: internalRunHandler();

	declare soft: IOException: internalGetTemplateHandler()|| internalGetTemplate2Handler()||internalReadProjectTemplateDescriptionHandler();

	declare soft: InterruptedException: internalPerformFinishHandler();

	declare soft: InvocationTargetException: internalPerformFinishHandler();

	void around(TexlipseProjectCreationWizardPage tex): internalReadProjectTemplateDescriptionHandler() && this(tex) {
		try {
			proceed(tex);
		} catch (IOException e) {
			TexlipsePlugin.log("Reading a description of template file:", e);
			tex.descriptionField.setText("");
		}
	}

	boolean around(IRunnableWithProgress op, boolean result,
			TexlipseProjectCreationWizard tex):internalPerformFinishHandler()&& args(op,result) && this(tex) {
		try {
			proceed(op, result, tex);
		} catch (InterruptedException e) {
			result = false;
		} catch (InvocationTargetException e) {
			tex.handleTargetException(e.getTargetException());
			result = false;
		}
		return result;
	}

	byte[] around(byte[] content): (internalGetTemplate2Handler()||internalGetTemplate2Handler())&&args(*, content) {
		try {
			return proceed(content);
		} catch (IOException e) {
		}
		return content;
	}

	void around(IProgressMonitor monitor): internalRunHandler() && args(monitor) {
		try {
			proceed(monitor);
		} catch (CoreException e) {
			TexlipsePlugin.log(TexlipsePlugin
					.getResourceString("projectWizardCreateError"), e);
		} finally {
			monitor.done();
		}
	}

}
