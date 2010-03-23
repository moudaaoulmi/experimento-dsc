/*
 * Created on 14-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.wizards.project;

import ish.ecletex.ecletexPlugin;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class ecletexProjectCreationWizard extends Wizard implements INewWizard,
		IExecutableExtension {

	private ecletexProjectCreationWizardPage[] fPages;
	private IConfigurationElement fConfigElement;

	public ecletexProjectCreationWizard() {
		super();
		setDialogSettings(ecletexPlugin.getDefault().getDialogSettings());
		setWindowTitle("Latex Project Wizard");
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

		//IConfigurationElement[] children = fConfigElement.getChildren("projectsetup"); //$NON-NLS-1$
		// if (children == null || children.length == 0) {
		//	//ExampleProjectsPlugin.log("descriptor must contain one ore more projectsetup tags"); //$NON-NLS-1$
		// return;
		// }

		// fPages= new ecletexProjectCreationWizardPage[children.length];
		fPages = new ecletexProjectCreationWizardPage[1];

		// for (int i= 0; i < children.length; i++) {
		// fPages[i]= new ecletexProjectCreationWizardPage(i, children[i]);
		// addPage(fPages[i]);
		// }
		fPages[0] = new ecletexProjectCreationWizardPage(0);
		addPage(fPages[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		ecletexProjectCreationOperation runnable = new ecletexProjectCreationOperation(
				fPages, new ImportOverwriteQuery());

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
		String title = "Project Creation Failed"; //$NON-NLS-1$
		String message = "Project could not be created."; //$NON-NLS-1$
		if (target instanceof CoreException) {
			IStatus status = ((CoreException) target).getStatus();
			ErrorDialog.openError(getShell(), title, message, status);
			// ExampleProjectsPlugin.log(status);
		} else {
			MessageDialog.openError(getShell(), title, target.getMessage());
			// ExampleProjectsPlugin.log(target);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		fConfigElement = config;
		initializeDefaultPageImageDescriptor();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	private class ImportOverwriteQuery implements IOverwriteQuery {
		public String queryOverwrite(String file) {
			String[] returnCodes = { YES, NO, ALL, CANCEL };
			int returnVal = openDialog(file);
			return returnVal < 0 ? CANCEL : returnCodes[returnVal];
		}

		private int openDialog(final String file) {
			final int[] result = { IDialogConstants.CANCEL_ID };
			getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					String title = "Overwite"; //$NON-NLS-1$
					String msg = "Do you want to overwrite " + file + "?";
					String[] options = { IDialogConstants.YES_LABEL,
							IDialogConstants.NO_LABEL,
							IDialogConstants.YES_TO_ALL_LABEL,
							IDialogConstants.CANCEL_LABEL };
					MessageDialog dialog = new MessageDialog(getShell(), title,
							null, msg, MessageDialog.QUESTION, options, 0);
					result[0] = dialog.open();
				}
			});
			return result[0];
		}
	}

}
