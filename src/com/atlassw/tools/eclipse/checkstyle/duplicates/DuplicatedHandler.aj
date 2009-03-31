
package com.atlassw.tools.eclipse.checkstyle.duplicates;


import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;


import java.util.MissingResourceException;
import java.lang.NumberFormatException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.PartInitException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.TreeViewer;

import org.eclipse.osgi.util.NLS;
import org.eclipse.core.runtime.Status;
import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import org.eclipse.core.runtime.IProgressMonitor;

@ExceptionHandler
public privileged aspect DuplicatedHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: CheckstyleException : DuplicatedCodeAction_createCheckerHandler();

    declare soft: CoreException : DuplicatedCodeAction_addJavaFilesToSetHandler() 
                                || DuplicatedCodeView_selectHandler() 
                                || DuplicatedCodeView_internalGetChildrenHandler() 
                                || DuplicatedCodeView_internalRunHandler() 
                                || DuplicatedCodeView_runHandler() 
                                || DuplicatedCodeView_internal2Handler() 
                                || DuplicatedCodeView_internalGetChildren2Handler();

    declare soft: PartInitException: DuplicatedCodeAction_findDuplicatedCodeViewHandler() 
                                     || DuplicatedCodeView_runHandler() 
                                     || DuplicatedCodeView_internal2Handler();

    declare soft : BadLocationException : DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut DuplicatedCodeAction_createCheckerHandler() : 
        call(Checker.new()) &&
        withincode(* DuplicatedCodeAction.createChecker(..));

    pointcut DuplicatedCodeAction_addJavaFilesToSetHandler() : 
        execution(* DuplicatedCodeAction.addJavaFilesToSet(..));

    pointcut DuplicatedCodeAction_findDuplicatedCodeViewHandler() : 
        execution(* DuplicatedCodeAction.findDuplicatedCodeView(..));

    pointcut DuplicatedCode_internalHandler() : 
        staticinitialization(DuplicatedCode);

    pointcut DuplicatedCode_internalGetNumberOfDuplicatedLinesHandler() : 
        execution(* DuplicatedCode.internalGetNumberOfDuplicatedLines(..));

    pointcut DuplicatedCodeView_selectHandler() : 
        execution (* DuplicatedCodeView.DuplicatesFilter.select(..));

    pointcut DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler(): 
        execution (* DuplicatedCodeView.selectAndRevealDuplicatedLines(..));

    pointcut DuplicatedCodeView_internalGetChildrenHandler(): 
        execution (* DuplicatedCodeView.ViewContentProvider.internalGetChildren(..));

    pointcut DuplicatedCodeView_internalRunHandler() : 
        execution (* DuplicatedCodeView.internalRun(..));

    pointcut DuplicatedCodeView_runHandler(): 
         execution(* DuplicatedCodeView.internal(..));

//     pointcut DuplicatedCodeView_runHandler():
//     withincode(* DuplicatedCodeView.createOpenSourceFileAction()) &&
//     within(Action+) &&
//     execution(* run(..));

    pointcut DuplicatedCodeView_internal2Handler() :
        execution (* DuplicatedCodeView.internal2(..));

    pointcut DuplicatedCodeView_internalGetChildren2Handler(): 
        execution (* DuplicatedCodeView.ViewContentProvider.internalGetChildren2(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    Object around(): DuplicatedCodeAction_addJavaFilesToSetHandler() 
                    || DuplicatedCode_internalHandler() 
                    || DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler() 
                    || DuplicatedCodeAction_findDuplicatedCodeViewHandler() {
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (PartInitException e)
        {
            DuplicatedCodeAction duplicate = ((DuplicatedCodeAction) thisJoinPoint.getThis());
            CheckstyleLog.errorDialog(duplicate.mWorkbenchPart.getSite().getShell(),
                    "Error opening the duplicated code view '" + DuplicatedCodeView.VIEW_ID + "'.",
                    e, true);
        }
        catch (CoreException e)
        {
            // we can't do anything : just log the pbm...
            CheckstyleLog.log(e, "Error while scanning files for the duplication code analysis.");
        }
        catch (MissingResourceException e)
        {
            String DUPLICATES_MESSAGE_BUNDLE = "com.puppycrawl.tools.checkstyle.checks.duplicates.messages";
            CheckstyleLog.log(e, "Unable to get the resource bundle " //$NON-NLS-1$
                    + DUPLICATES_MESSAGE_BUNDLE + "."); //$NON-NLS-1$
        }
        catch (BadLocationException e)
        {
            DuplicatedCodeView dcv = (DuplicatedCodeView) thisJoinPoint.getThis();
            TreeViewer tv = dcv.getMViewer();
            CheckstyleLog.errorDialog(tv.getControl().getShell(),
                    ErrorMessages.errorWhileDisplayingDuplicates, e, true);
        }
        return result;
    }

    boolean around() : DuplicatedCodeView_selectHandler(){
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            return false;
        }
        return true;
    }

    Object[] around() : DuplicatedCodeView_internalGetChildrenHandler() 
                        || DuplicatedCodeView_internalGetChildren2Handler(){
        Object[] obj = null;
        try
        {
            return obj = proceed();
        }
        catch (CoreException e)
        {
            obj = new Object[0];
        }
        return obj;
    }

    void around() : DuplicatedCodeView_runHandler() 
                || DuplicatedCodeView_internal2Handler(){
        try
        {
            proceed();
        }
        catch (PartInitException e)
        {
            DuplicatedCodeView dcv = (DuplicatedCodeView) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(dcv.mViewer.getControl().getShell(),
                    ErrorMessages.errorWhileOpeningEditor, e, true);
        }
        catch (CoreException e)
        {
            DuplicatedCodeView dcv = (DuplicatedCodeView) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(dcv.mViewer.getControl().getShell(),
                    ErrorMessages.errorWhileOpeningEditor, e, true);
        }
    }

    Checker around(): DuplicatedCodeAction_createCheckerHandler(){
        Checker checker = null;
        try
        {
            checker = proceed();
        }
        catch (CheckstyleException e)
        {
            DuplicatedCodeAction duplicate = ((DuplicatedCodeAction) thisJoinPoint.getThis());
            CheckstyleLog.errorDialog(duplicate.mWorkbenchPart.getSite().getShell(),
                    "Unable to launch the duplicated code analyser.", e, true);
        }
        return checker;
    }

    int around(): DuplicatedCode_internalGetNumberOfDuplicatedLinesHandler() {
        int result = 0;
        try
        {
            result = proceed();
        }
        catch (NumberFormatException e)
        {
            result = 0;
        }
        return result;
    }

    IStatus around(IProgressMonitor monitor, IProject project) : 
            DuplicatedCodeView_internalRunHandler() && 
            args(monitor, project){
        IStatus result = null;
        try
        {
            return result = proceed(monitor, project);
        }
        catch (CoreException e)
        {
            CheckstyleLog.log(e, NLS.bind(ErrorMessages.errorWhileBuildingProject, project
                    .getName()));
            result = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK, NLS.bind(
                    ErrorMessages.errorWhileBuildingProject, project.getName()), e);
        }
        return result;
    }
}
