package com.atlassw.tools.eclipse.checkstyle.duplicates;



import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IWorkbenchPart;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;


/**
 * @author juliana
 *
 */
public class DuplicatesHandler {
    
    //catch (MissingResourceException x)
    public void logHandler(RuntimeException x, String s){
        CheckstyleLog.log(x, s); 
    }
    
    //catch (NumberFormatException e)
    public void getNumberOfDuplicatedLinesHandler(int result){
        result = 0;
    }
    
    //catch (CoreException e)
    public Object[] getChildrenHandler(){
        return new Object[0];
    }

    //catch (CoreException e)
    public boolean selectHandler(){
        return false;
    }
    
    //catch (CoreException e)
    public Status contributeToActionBarsHandler(Exception e, IProject project){
                CheckstyleLog.log(e, NLS.bind(ErrorMessages.errorWhileBuildingProject,project.getName()));
                
        return new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID,IStatus.OK, NLS.bind(ErrorMessages.errorWhileBuildingProject, project.getName()), e);
    }
    
     //catch (PartInitException e)
    //catch (CoreException e)
    //catch (BadLocationException e)
    public void runHandler( TreeViewer mViewer, Exception e){
        CheckstyleLog.errorDialog(mViewer.getControl().getShell(),
                ErrorMessages.errorWhileOpeningEditor, e, true);
    }
    
    //catch (CheckstyleException e)
    public void createCheckerHandler(Exception e, IWorkbenchPart mWorkbenchPart){
        CheckstyleLog.errorDialog(mWorkbenchPart.getSite().getShell(),
                "Unable to launch the duplicated code analyser.", e, true);
    }
    
    //catch (PartInitException e)
    public void findDuplicatedCodeViewHandler(Exception e, IWorkbenchPart mWorkbenchPart){
        CheckstyleLog.errorDialog(mWorkbenchPart.getSite().getShell(),
                "Error opening the duplicated code view '" + DuplicatedCodeView.VIEW_ID + "'.",
                e, true);
    }
    
  //catch (PartInitException e)
    public void addJavaFilesToSetHandler(Exception e){
        CheckstyleLog.log(e, "Error while scanning files for the duplication code analysis.");
    }
    
    
}//DuplicatesHandler{}
