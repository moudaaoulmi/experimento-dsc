/*
 * Created on 09-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.wizards.template;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import ish.ecletex.ecletexPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class templateCreationWizard extends Wizard implements INewWizard,
		IExecutableExtension {

	private IConfigurationElement fConfigElement;
	private templateWizardPage page;

	public templateCreationWizard() {
		super();
		setDialogSettings(ecletexPlugin.getDefault().getDialogSettings());
		setWindowTitle("New Template Wizard");
	}

	private void initializeDefaultPageImageDescriptor() {
		if (fConfigElement != null) {
			String banner = fConfigElement.getAttribute("banner"); //$NON-NLS-1$
			if (banner != null) {
				ImageDescriptor desc = ecletexPlugin.getDefault()
						.getImageDescriptor(banner);
				setDefaultPageImageDescriptor(desc);
			}
		}
	}

	public void addPages() {
		super.addPages();
		page = new templateWizardPage(0);
		this.addPage(page);
	}

	public boolean performFinish() {
		templateCreationOperation runnable = new templateCreationOperation(page);

		IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(
				runnable);
		if (!internalPerformFinish(op))
			return false;
		BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
		return true;
	}

	private boolean internalPerformFinish(IRunnableWithProgress op) {
		getContainer().run(false, true, op);
		return true;
	}

	private void handleException(Throwable target) {
		String title = "Template Creation Failed."; //$NON-NLS-1$
		String message = "The template could not be created."; //$NON-NLS-1$
		if (target instanceof IOException) {
			IStatus status = ((CoreException) target).getStatus();
			ErrorDialog.openError(getShell(), title, message, status);
			// ExampleProjectsPlugin.log(status);
		} else {
			MessageDialog.openError(getShell(), title, target.getMessage());
			// ExampleProjectsPlugin.log(target);
		}
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		fConfigElement = config;

		initializeDefaultPageImageDescriptor();

	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

}
