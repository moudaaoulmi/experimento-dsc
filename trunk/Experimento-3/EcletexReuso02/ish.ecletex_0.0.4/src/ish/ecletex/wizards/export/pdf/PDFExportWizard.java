/*
 * Created on 20-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ish.ecletex.wizards.export.pdf;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.properties.ecletexProjectProperties;
import ish.ecletex.utils.ProjectUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * @author ish
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PDFExportWizard extends Wizard implements IExportWizard {

	private IConfigurationElement fConfigElement;

	private IStructuredSelection selection;

	private IProject project;

	private IPath sourceFile;

	private PDFWizardPageOne pageone;

	public PDFExportWizard() {
		super();
		setDialogSettings(ecletexPlugin.getDefault().getDialogSettings());
		setWindowTitle("PDF Export Wizard");
	}

	public void addPages() {
		super.addPages();
		pageone = new PDFWizardPageOne(project, sourceFile);
		this.addPage(pageone);
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

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		fConfigElement = config;

		initializeDefaultPageImageDescriptor();

	}

	private void handleException(Throwable target) {
		String title = "PDF Creation Failed."; //$NON-NLS-1$
		String message = "The PDF could not be created."; //$NON-NLS-1$
		if (target instanceof IOException) {
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
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Object o = selection.getFirstElement();
		if (o instanceof IProject) {
			project = (IProject) o;
			internalInit();
		}
	}

	private void internalInit() {
		if (!project.isNatureEnabled("ish.ecletex.ecletexNature")) {
			System.out.println("Project is not an ecleTex nature");
		}

		String projectDir = ProjectUtils.getProjectDir(project);
		sourceFile = new Path(projectDir);
		String mainFile = project.getPersistentProperty(new QualifiedName(
				"ish.ecletex", ecletexProjectProperties.MAINFILE_PROPERTY));
		sourceFile = sourceFile.append(mainFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {

		String export_method = pageone.GetExportMethod();
		IRunnableWithProgress runnable = null;
		if (export_method.equals(PDFExportMethods.PDFLATEX)) {

			runnable = new PDFLatexExportOperation(pageone.GetMainFile(),
					pageone.GetTarget());

		} else if (export_method.equals(PDFExportMethods.GS)) {
			runnable = new PDFGSExportOperation(pageone.GetMainFile(), pageone
					.GetTarget());

		}

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

}