/*
 * Created on 18-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.builders;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.builders.latexlogparser.Entry;
import ish.ecletex.builders.latexlogparser.Model;
import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexExternalToolsPreferencePage;
import ish.ecletex.properties.ecletexProjectProperties;

import java.util.LinkedList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class ecletexBuildVisitor implements IResourceVisitor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources
	 * .IResource)
	 */
	public boolean visit(IResource resource) throws CoreException {
		// System.out.println(
		// "Visiting [" + resource.getProjectRelativePath().toString() + "]");
		String resourceName = resource.getProjectRelativePath().toString();
		String targetName = resource.getProject().getPersistentProperty(
				new QualifiedName("ish.ecletex",
						ecletexProjectProperties.MAINFILE_PROPERTY));
		if (resourceName.equals(targetName)) {
			// System.out.println(
			// "Found Main [" + targetName + "] at [" + resourceName + "]");
			String TEX_BIN = ecletexPlugin.getDefault().getPreferenceStore()
					.getString(TexExternalToolsPreferencePage.TEX_BIN_DIR);
			String GS_BIN = ecletexPlugin.getDefault().getPreferenceStore()
					.getString(TexExternalToolsPreferencePage.GS_BIN_DIR);
			// System.out.println("Using [" + TEX_BIN + "] as bin directory.");

			ecletexLatexBuildManager builder = new ecletexLatexBuildManager(
					resource.getLocation().toString(), TEX_BIN, resource
							.getProject().getLocation().toString());
			Model results = builder.build();
			if (resource
					.getProject()
					.getPersistentProperty(
							new QualifiedName(
									"ish.ecletex",
									ecletexProjectProperties.BIBTEX_COMPLIER_ACTION_PROPERTY))
					.equals(ecletexProjectProperties.CHECKED)) {
				ecletexBibtexBuildManager bibbuilder = new ecletexBibtexBuildManager(
						resource.getLocation().toString(), TEX_BIN, resource
								.getProject().getLocation().toString());
				bibbuilder.build();
				results = builder.build();
				results = builder.build();
			} else {
				builder.build();
			}
			ProcessResults(results, resource);

			if (resource.getProject().getPersistentProperty(
					new QualifiedName("ish.ecletex",
							ecletexProjectProperties.PS_ACTION_PROPERTY))
					.equals(ecletexProjectProperties.CHECKED)) {
				ecletexPSBuildManager psbuilder = new ecletexPSBuildManager(
						resource.getLocation().toString(), TEX_BIN, resource
								.getProject().getLocation().toString());
				psbuilder.build();
			}

			// if (resource
			// .getProject()
			// .getPersistentProperty(
			// new QualifiedName(
			// "ish.ecletex",
			// ecletexProjectProperties.PDF_ACTION_PROPERTY))
			// .equals(ecletexProjectProperties.CHECKED)) {
			// ecletexPDFBuildManager pdfbuilder =
			// new ecletexPDFBuildManager(
			// resource.getLocation().toString(),
			// GS_BIN,
			// resource.getProject().getLocation().toString());
			// pdfbuilder.build();
			// }

			// Refresh project.
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IProject thisProject = workspace.getRoot().getProject(
					resource.getProject().getName());
			thisProject.refreshLocal(IProject.DEPTH_INFINITE, null);

		} else {
			// System.out.println(
			// "Not Main [" + targetName + "] at [" + resourceName + "]");
		}

		return true;
	}

	private void ProcessResults(Model results, IResource resource) {
		DoMarkers domarkers = new DoMarkers(results, resource);
		ResourcesPlugin.getWorkspace().run(domarkers, null);
	}

	class DoMarkers implements IWorkspaceRunnable {

		private Model results;

		private IResource resource;

		public DoMarkers(Model Results, IResource Resource) {
			this.resource = Resource;
			this.results = Results;
		}

		public void run(IProgressMonitor monitor) {

			IProject thisProject = resource.getProject();

			LinkedList problems = results.get();

			for (int i = 0; i < problems.size(); i++) {
				internalRun(thisProject, problems, i);
			}
		}

		private void internalRun(IProject thisProject, LinkedList problems,
				int i) {
			Entry e = (Entry) problems.get(i);
			IPath projectPath = thisProject.getLocation();
			IResource r = thisProject;
			if (e.filename != null) {
				IPath fullResource = new Path(e.filename);
				// System.out.println("Project Path
				// "+projectPath.toString());
				// System.out.println("Resource Path
				// "+fullResource.toString());
				IPath relativeResource = fullResource
						.removeFirstSegments(fullResource
								.matchingFirstSegments(projectPath));
				// System.out.println("Trying to find:
				// "+relativeResource.toString());

				r = thisProject.findMember(relativeResource);
			}
			// if (r != null)
			// System.out.println("Resource :" + r.toString());

			// System.out.println("Found Problem: " + e);
			if (r != null) {

				IMarker marker = null;
				if (!MarkerExists(r, e)) {
					if (e.type == Entry.VBOX) {

						marker = r.createMarker(MarkerTypes.VBOX);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_WARNING);
						// System.out.println("Added Problem Marker");
					} else if (e.type == Entry.FONT_ERROR) {
						marker = r.createMarker(MarkerTypes.FONT_ERROR);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_WARNING);
						// System.out.println("Added Problem Marker");
					} else if (e.type == Entry.UNDERFULL_HBOX) {
						marker = r.createMarker(MarkerTypes.UNDERFULL_HBOX);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_WARNING);
						// System.out.println("Added Problem Marker");
					} else if (e.type == Entry.OVERFULL_HBOX) {
						marker = r.createMarker(MarkerTypes.OVERFULL_HBOX);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_WARNING);

					} else if (e.type == Entry.FLOAT_WARNING) {
						marker = r.createMarker(MarkerTypes.FLOAT_WARNING);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_WARNING);
					} else if (e.type == Entry.NOFILE_ERROR) {
						marker = r.createMarker(MarkerTypes.NOFILE_ERROR);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_ERROR);
					} else if (e.type == Entry.REFERENCE_ERROR) {
						marker = r.createMarker(MarkerTypes.REFERENCE_ERROR);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_ERROR);
					} else if (e.type == Entry.LATEX_WARNING) {
						marker = r.createMarker(MarkerTypes.LATEX_WARNING);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_WARNING);
					} else if (e.type == Entry.ERRORMESSAGE) {
						marker = r.createMarker(MarkerTypes.ERRORMESSAGE);
						marker.setAttribute(IMarker.SEVERITY,
								IMarker.SEVERITY_ERROR);
					}
					if (marker != null) {
						marker.setAttribute(IMarker.MESSAGE, e.msg);
						marker.setAttribute(IMarker.LINE_NUMBER, e.lineno);

					}
				}
			}
		}

		private boolean MarkerExists(IResource resource, Entry e) {
			IMarker[] markers = resource.findMarkers(IMarker.PROBLEM, true,
					IResource.DEPTH_INFINITE);
			for (int i = 0; i < markers.length; i++) {
				Object LineNumber = markers[i]
						.getAttribute(IMarker.LINE_NUMBER);
				Object Message = markers[i].getAttribute(IMarker.MESSAGE);
				// System.out.println("Line Number: "+LineNumber);
				// System.out.println("Message: "+Message);
				if ((((Integer) LineNumber).intValue() == e.lineno)
						&& ((String) Message).equals(e.msg)) {
					// System.out.println("Found Duplicate");
					return true;
				}
			}
			return false;
		}

	}
}