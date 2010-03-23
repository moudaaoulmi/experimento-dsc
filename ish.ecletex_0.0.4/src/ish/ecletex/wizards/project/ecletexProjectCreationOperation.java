/*
 * Created on 14-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.wizards.project;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.properties.ecletexProjectProperties;
import ish.ecletex.properties.texFileProperties;
import ish.ecletex.utils.ProjectUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.dialogs.IOverwriteQuery;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class ecletexProjectCreationOperation implements IRunnableWithProgress {

	private ecletexProjectCreationWizardPage[] fPages;
	private IOverwriteQuery fOverwriteQuery;

	public ecletexProjectCreationOperation(
			ecletexProjectCreationWizardPage[] pages,
			IOverwriteQuery overwriteQuery) {
		fPages = pages;
		fOverwriteQuery = overwriteQuery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core
	 * .runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		try {
			monitor.beginTask("Creating Project", 2);
			IWorkspaceRoot root = ecletexPlugin.getWorkspace().getRoot();

			for (int i = 0; i < fPages.length; i++) {
				createProject(root, fPages[i], new SubProgressMonitor(monitor,
						1));
			}
		} finally {
			monitor.done();
		}
	}

	private void createProject(IWorkspaceRoot root,
			ecletexProjectCreationWizardPage page, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {

		monitor.beginTask("Creating Project Directory", 1);

		String name = page.getName();
		internalCreateProject(root, page, monitor, name);

	}

	private void internalCreateProject(IWorkspaceRoot root,
			ecletexProjectCreationWizardPage page, IProgressMonitor monitor,
			String name) throws InvocationTargetException {
		IProject project = root.getProject(name);
		if (!project.exists()) {
			project.create(null);
		}
		if (!project.isOpen()) {
			project.open(null);
		}
		IProjectDescription desc = project.getDescription();
		String[] natures = desc.getNatureIds();
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 1, natures.length);
		newNatures[0] = "ish.ecletex.ecletexNature";
		desc.setNatureIds(newNatures);

		ICommand[] commands = desc.getBuildSpec();
		ICommand command = desc.newCommand();
		command.setBuilderName("ish.ecletex.ecletexBuilder");

		ICommand[] newCommands = new ICommand[commands.length + 1];

		System.arraycopy(commands, 0, newCommands, 1, commands.length);
		newCommands[0] = command;
		desc.setBuildSpec(newCommands);

		project.setDescription(desc, new SubProgressMonitor(monitor, 1));
		project
				.setPersistentProperty(new QualifiedName("ish.ecletex",
						ecletexProjectProperties.MAINFILE_PROPERTY), page
						.getMainFile());
		project.setPersistentProperty(new QualifiedName("ish.ecletex",
				ecletexProjectProperties.BIBTEX_COMPLIER_ACTION_PROPERTY),
				ecletexProjectProperties.DEFAULT_BIBTEX_COMPLIER_ACTION);
		project.setPersistentProperty(new QualifiedName("ish.ecletex",
				ecletexProjectProperties.PS_ACTION_PROPERTY),
				ecletexProjectProperties.DEFAULT_PS_ACTION);
		if (ecletexProjectProperties.DEFAULT_PDF_ACTION
				.equals(ecletexProjectProperties.CHECKED))
			project.setPersistentProperty(new QualifiedName("ish.ecletex",
					ecletexProjectProperties.PS_ACTION_PROPERTY),
					ecletexProjectProperties.DEFAULT_PDF_ACTION);
		project.setPersistentProperty(new QualifiedName("ish.ecletex",
				ecletexProjectProperties.PDF_ACTION_PROPERTY),
				ecletexProjectProperties.DEFAULT_PDF_ACTION);
		// project.setPersistentProperty(
		// new QualifiedName("ish.ecletex",
		// ecletexProjectProperties.DICTIONARY_PROPERTY),
		// ecletexProjectProperties.DEFAULT_DICTIONARY);

		project.setPersistentProperty(new QualifiedName("ish.ecletex",
				texFileProperties.ALTERNATE_SUPPORT),
				texFileProperties.UNCHECKED);
		project.setPersistentProperty(new QualifiedName("ish.ecletex",
				texFileProperties.DICTIONARY_PROPERTY), page.getLanguage());

		IPath projectDir = new Path(ProjectUtils.getProjectDir(project));
		IPath mainFile = projectDir.append(page.getMainFile());

		System.out.println("Creating: " + mainFile.toOSString());
		File f = new File(mainFile.toOSString());
		internalCreateProject2(f);
		project.refreshLocal(IProject.DEPTH_INFINITE, null);
	}

	private void internalCreateProject2(File f) {
		f.createNewFile();
	}

}
