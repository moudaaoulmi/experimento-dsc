package com.atlassw.tools.eclipse.checkstyle.duplicates;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.PartInitException;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import org.eclipse.jface.viewers.TreeViewer;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.util.NLS;
import org.eclipse.core.runtime.Status;
import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import org.eclipse.core.runtime.IProgressMonitor;

public privileged aspect DuplicatedCodeViewHandler
{
    /*declare soft: CoreException : selectHandler() || internalGetChildrenHandler() || internalRunHandler() 
                    || internalHandler() || internal2Handler() || internalGetChildren2Handler();
    declare soft : BadLocationException : selectAndRevealDuplicatedLinesHandler();
    declare soft : PartInitException : internalHandler() || internal2Handler(); 
    
    pointcut selectHandler() : execution (* DuplicatedCodeView.DuplicatesFilter.select(..));
    pointcut selectAndRevealDuplicatedLinesHandler(): execution (* DuplicatedCodeView.selectAndRevealDuplicatedLines(..));
    pointcut internalGetChildrenHandler(): execution (* DuplicatedCodeView.ViewContentProvider.internalGetChildren(..));
    pointcut internalRunHandler() : execution (* DuplicatedCodeView.internalRun(..));
    pointcut internalHandler() : execution (* DuplicatedCodeView.internal(..));
    pointcut internal2Handler() : execution (* DuplicatedCodeView.internal2(..));
    pointcut internalGetChildren2Handler(): execution (* DuplicatedCodeView.ViewContentProvider.internalGetChildren2(..));
   
    boolean around() : selectHandler(){
        try{
            proceed();
        }catch (CoreException e){
            return false;
        }
        return true;
    }
    
    void around() : selectAndRevealDuplicatedLinesHandler(){
        DuplicatedCodeView dcv = (DuplicatedCodeView) thisJoinPoint.getThis();
        TreeViewer tv = dcv.getMViewer();
        try{
            proceed();
        }catch (BadLocationException e){
            CheckstyleLog.errorDialog(tv.getControl().getShell(),
                    ErrorMessages.errorWhileDisplayingDuplicates, e, true);
        }
    }
    Object[] around() : internalGetChildrenHandler() || internalGetChildren2Handler(){
        Object[] obj = null;
        try{
            return obj = proceed();
        }catch (CoreException e){
            obj = new Object[0];
        }
        return obj;
    }
    IStatus around(IProgressMonitor monitor, IProject project) : internalRunHandler() && args(monitor, project){
        IStatus result = null;
        try{
            return result = proceed(monitor, project);
        }catch (CoreException e)
        {
            CheckstyleLog
                    .log(e, NLS.bind(ErrorMessages.errorWhileBuildingProject,
                            project.getName()));
            result = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID,
                    IStatus.OK, NLS.bind(
                            ErrorMessages.errorWhileBuildingProject, project
                                    .getName()), e);
        }
        return result;
    }
    void around() : internalHandler() || internal2Handler(){
        DuplicatedCodeView dcv = (DuplicatedCodeView) thisJoinPoint.getThis();
        try{
            proceed();
        }catch (PartInitException e){
            CheckstyleLog.errorDialog(dcv.mViewer.getControl().getShell(),
                    ErrorMessages.errorWhileOpeningEditor, e, true);
        }catch (CoreException e){
            CheckstyleLog.errorDialog(dcv.mViewer.getControl().getShell(),
                    ErrorMessages.errorWhileOpeningEditor, e, true);
        }
    }*/
}
