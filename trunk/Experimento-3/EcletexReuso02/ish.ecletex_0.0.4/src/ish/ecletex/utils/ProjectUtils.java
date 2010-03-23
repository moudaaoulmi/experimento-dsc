/*
 * Created on 05-May-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.utils;

import ish.ecletex.ecletexPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * @author ish
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ProjectUtils {
	
	public static IProject getCurrentProject(){
		IEditorPart editor = ecletexPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IEditorInput input =  editor.getEditorInput();
		((TextEditor)editor).getDocumentProvider().getDocument(editor.getEditorInput());
		IFile file = null;
		if(input instanceof IFileEditorInput){
			file = ((IFileEditorInput)input).getFile();
		}
		if(file==null)
			return null;
		
		//IPath filepath = file.getRawLocation();
		
		IProject project = file.getProject();
		
		//IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		//int projectIndex = -1;
		//for(int i=0;i<projects.length;i++){
		//	IResource resource = projects[i].findMember(file.ge);
		//	if(resource!=null){
		//		projectIndex=i;
		//		break;
		//	}
		//}
		//if(projectIndex==-1)
		//	return null;
		return project;	
	
	}
	
	public static String getCurrentTempDir(){
		IProject project = getCurrentProject();
		return getProjectTempDir(project);
	}
	
	
	public static String getProjectTempDir(IProject project){
		IPath projecttempfolder = project.getLocation().makeAbsolute();
		projecttempfolder = projecttempfolder.append(".ecletex");
		if(!projecttempfolder.toFile().exists()){
			projecttempfolder.toFile().mkdirs();
		}
		return projecttempfolder.toOSString();
		
	}
	
	public static String getProjectDir(IProject project){
		IPath projectfolder = project.getLocation().makeAbsolute();;
		if(!projectfolder.toFile().exists()){
			projectfolder.toFile().mkdirs();
		}
		return projectfolder.toOSString();
		
	}
}
