package com.atlassw.tools.eclipse.checkstyle.duplicates;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPart;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.checks.duplicates.StrictDuplicateCodeCheck;
import org.eclipse.ui.PartInitException;

import java.util.MissingResourceException;
import java.lang.NumberFormatException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.util.NLS;
import org.eclipse.core.runtime.Status;
import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import org.eclipse.core.runtime.IProgressMonitor;

public privileged aspect DuplicatedHandler {
    
    declare soft: CheckstyleException : DuplicatedCodeAction_internalCreateCheckerHandler();
    declare soft: CoreException : DuplicatedCodeAction_addJavaFilesToSetHandler() || DuplicatedCodeView_selectHandler() 
                        || DuplicatedCodeView_internalGetChildrenHandler() || DuplicatedCodeView_internalRunHandler() 
                        || DuplicatedCodeView_internalHandler() || DuplicatedCodeView_internal2Handler() || DuplicatedCodeView_internalGetChildren2Handler();
    
    declare soft: PartInitException: DuplicatedCodeAction_findDuplicatedCodeViewHandler() || DuplicatedCodeView_internalHandler() 
                        || DuplicatedCodeView_internal2Handler();
    declare soft : BadLocationException : DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler();

    pointcut DuplicatedCodeAction_internalCreateCheckerHandler() : execution(* DuplicatedCodeAction.internalCreateChecker(..));
    pointcut DuplicatedCodeAction_addJavaFilesToSetHandler() : execution(* DuplicatedCodeAction.addJavaFilesToSet(..));
    pointcut DuplicatedCodeAction_findDuplicatedCodeViewHandler() : execution(* DuplicatedCodeAction.findDuplicatedCodeView(..));
    pointcut DuplicatedCode_internalHandler() : execution(* DuplicatedCode.internal(..));
    pointcut DuplicatedCode_internalGetNumberOfDuplicatedLinesHandler() : execution(* DuplicatedCode.internalGetNumberOfDuplicatedLines(..));
    pointcut DuplicatedCodeView_selectHandler() : execution (* DuplicatedCodeView.DuplicatesFilter.select(..));
    pointcut DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler(): execution (* DuplicatedCodeView.selectAndRevealDuplicatedLines(..));
    pointcut DuplicatedCodeView_internalGetChildrenHandler(): execution (* DuplicatedCodeView.ViewContentProvider.internalGetChildren(..));
    pointcut DuplicatedCodeView_internalRunHandler() : execution (* DuplicatedCodeView.internalRun(..));
    pointcut DuplicatedCodeView_internalHandler() : execution (* DuplicatedCodeView.internal(..));
    pointcut DuplicatedCodeView_internal2Handler() : execution (* DuplicatedCodeView.internal2(..));
    pointcut DuplicatedCodeView_internalGetChildren2Handler(): execution (* DuplicatedCodeView.ViewContentProvider.internalGetChildren2(..));

    Object around(): DuplicatedCodeAction_addJavaFilesToSetHandler() || DuplicatedCode_internalHandler() 
                  || DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler()  || DuplicatedCodeAction_findDuplicatedCodeViewHandler() {
        Object result = null;
        try {
            result = proceed();
        }catch (PartInitException e) {
                DuplicatedCodeAction duplicate = ((DuplicatedCodeAction) thisJoinPoint.getThis());
                CheckstyleLog.errorDialog(duplicate.mWorkbenchPart.getSite().getShell(),
                        "Error opening the duplicated code view '" + DuplicatedCodeView.VIEW_ID + "'.",
                        e, true);
            
        }catch (CoreException e){
            // we can't do anything : just log the pbm...
            CheckstyleLog.log(e, "Error while scanning files for the duplication code analysis.");
        } catch (MissingResourceException e){
            String DUPLICATES_MESSAGE_BUNDLE = "com.puppycrawl.tools.checkstyle.checks.duplicates.messages";
            CheckstyleLog.log(e, "Unable to get the resource bundle " //$NON-NLS-1$
                    + DUPLICATES_MESSAGE_BUNDLE + "."); //$NON-NLS-1$
        } catch (BadLocationException e){
            DuplicatedCodeView dcv = (DuplicatedCodeView) thisJoinPoint.getThis();
            TreeViewer tv = dcv.getMViewer();
            CheckstyleLog.errorDialog(tv.getControl().getShell(),
                    ErrorMessages.errorWhileDisplayingDuplicates, e, true);
        }
        return result;
    }
    
    boolean around() : DuplicatedCodeView_selectHandler(){
        try{
            proceed();
        }catch (CoreException e){
            return false;
        }
        return true;
    }

    Object[] around() : DuplicatedCodeView_internalGetChildrenHandler() || DuplicatedCodeView_internalGetChildren2Handler(){
        Object[] obj = null;
        try{
            return obj = proceed();
        }catch (CoreException e){
            obj = new Object[0];
        }
        return obj;
    }
    
    
    void around() : DuplicatedCodeView_internalHandler() || DuplicatedCodeView_internal2Handler(){ 
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
    }
    
    Checker around(Checker checker, StrictDuplicateCodeCheck check,IWorkbenchPart mWorkbenchPart): 
        DuplicatedCodeAction_internalCreateCheckerHandler() && args(checker,check,mWorkbenchPart){
        try {
            return proceed(checker,check,mWorkbenchPart);
        }catch (CheckstyleException e){
            CheckstyleLog.errorDialog(mWorkbenchPart.getSite().getShell(),
                    "Unable to launch the duplicated code analyser.", e, true);
        }
        return checker;
    }
    
    int around(String number, int result): DuplicatedCode_internalGetNumberOfDuplicatedLinesHandler() && args(number,result){
        try {
            return proceed(number,result);
        }catch (NumberFormatException e){
            result = 0;
        }
        return result;
    }
    
    IStatus around(IProgressMonitor monitor, IProject project) : DuplicatedCodeView_internalRunHandler() && args(monitor, project){
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
}
