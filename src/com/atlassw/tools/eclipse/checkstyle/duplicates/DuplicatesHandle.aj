package com.atlassw.tools.eclipse.checkstyle.duplicates;

import java.util.MissingResourceException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.checks.duplicates.StrictDuplicateCodeCheck;

public privileged aspect DuplicatesHandle
{
    declare soft: CheckstyleException : DuplicatedCodeAction_internalCreateCheckerHandler();
    
    declare soft: PartInitException: DuplicatedCodeAction_findDuplicatedCodeViewHandler() 
                                     || DuplicatedCodeView_internalHandler() 
                                     || DuplicatedCodeView_internal2Handler();
    
    declare soft: CoreException : DuplicatedCodeView_DuplicatesFilter_selectHandler() 
                                  || DuplicatedCodeView_ViewContentProvider_internalGetChildrenHandler() 
                                  || DuplicatedCodeView_internalRunHandler() 
                                  || DuplicatedCodeView_internalHandler() 
                                  || DuplicatedCodeView_internal2Handler() 
                                  || DuplicatedCodeView_ViewContentProvider_internalGetChildren2Handler()
                                  || DuplicatedCodeAction_addJavaFilesToSetHandler();

    declare soft : BadLocationException : DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler();
    ;

    pointcut DuplicatedCodeView_ViewContentProvider_internalGetChildren2Handler(): execution (* DuplicatedCodeView.ViewContentProvider.internalGetChildren2(..));
    
    pointcut DuplicatedCodeAction_internalCreateCheckerHandler() : execution(* DuplicatedCodeAction.internalCreateChecker(..));
    
    pointcut DuplicatedCodeAction_addJavaFilesToSetHandler() : execution(* DuplicatedCodeAction.addJavaFilesToSet(..));
    
    pointcut DuplicatedCodeAction_findDuplicatedCodeViewHandler() : execution(* DuplicatedCodeAction.findDuplicatedCodeView(..));
    
    pointcut DuplicatedCode_internalHandler() : execution(* DuplicatedCode.internal(..));
    
    pointcut DuplicatedCode_internalGetNumberOfDuplicatedLinesHandler() : execution(* DuplicatedCode.internalGetNumberOfDuplicatedLines(..));
    
    pointcut DuplicatedCodeView_DuplicatesFilter_selectHandler() : execution (* DuplicatedCodeView.DuplicatesFilter.select(..));

    pointcut DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler(): execution (* DuplicatedCodeView.selectAndRevealDuplicatedLines(..));

    pointcut DuplicatedCodeView_ViewContentProvider_internalGetChildrenHandler(): execution (* DuplicatedCodeView.ViewContentProvider.internalGetChildren(..));

    pointcut DuplicatedCodeView_internalRunHandler() : execution (* DuplicatedCodeView.internalRun(..));

    pointcut DuplicatedCodeView_internalHandler() : execution (* DuplicatedCodeView.internal(..));

    pointcut DuplicatedCodeView_internal2Handler() : execution (* DuplicatedCodeView.internal2(..));
    
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
    
    void around(): DuplicatedCodeAction_addJavaFilesToSetHandler(){
        try {
            proceed();
        } catch (CoreException e) {
            // we can't do anything : just log the pbm...
            CheckstyleLog.log(e, "Error while scanning files for the duplication code analysis.");
        }
    }
    
    DuplicatedCodeView around(): DuplicatedCodeAction_findDuplicatedCodeViewHandler(){
        DuplicatedCodeAction duplicate = ((DuplicatedCodeAction) thisJoinPoint.getThis());
        DuplicatedCodeView resultado = null;
        try{
            return resultado = proceed();
        }catch (PartInitException e){
            CheckstyleLog.errorDialog(duplicate.mWorkbenchPart.getSite().getShell(),
                    "Error opening the duplicated code view '" + DuplicatedCodeView.VIEW_ID + "'.",
                    e, true);
        }
        return resultado;
    }
    
    void around() : DuplicatedCode_internalHandler(){
        String DUPLICATES_MESSAGE_BUNDLE = "com.puppycrawl.tools.checkstyle.checks.duplicates.messages";
        try{
            proceed();
        }
        catch (MissingResourceException e){
            CheckstyleLog.log(e, "Unable to get the resource bundle " //$NON-NLS-1$
                    + DUPLICATES_MESSAGE_BUNDLE + "."); //$NON-NLS-1$
        }
    }
    
    int around(String number, int result): DuplicatedCode_internalGetNumberOfDuplicatedLinesHandler() && args(number,result){
        try {
            return proceed(number,result);
        }catch (NumberFormatException e){
            result = 0;
        }
        return result;
    }
    

    
    
    boolean around() : DuplicatedCodeView_DuplicatesFilter_selectHandler(){
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

    void around() : DuplicatedCodeView_selectAndRevealDuplicatedLinesHandler(){
        DuplicatedCodeView dcv = (DuplicatedCodeView) thisJoinPoint.getThis();
        TreeViewer tv = dcv.getMViewer();
        try
        {
            proceed();
        }
        catch (BadLocationException e)
        {
            CheckstyleLog.errorDialog(tv.getControl().getShell(),
                    ErrorMessages.errorWhileDisplayingDuplicates, e, true);
        }
    }

    Object[] around() : DuplicatedCodeView_ViewContentProvider_internalGetChildrenHandler() 
                        || DuplicatedCodeView_ViewContentProvider_internalGetChildren2Handler(){
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

    IStatus around(IProgressMonitor monitor, IProject project) : DuplicatedCodeView_internalRunHandler() && args(monitor, project){
        IStatus result = null;
        try
        {
            return result = proceed(monitor, project);
        }
        catch (CoreException e)
        {
            CheckstyleLog.log(e, NLS.bind(ErrorMessages.errorWhileBuildingProject, project.getName()));
            result = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK, NLS.bind(
                    ErrorMessages.errorWhileBuildingProject, project.getName()), e);
        }
        return result;
    }

    void around() : DuplicatedCodeView_internalHandler() 
                    || DuplicatedCodeView_internal2Handler(){
        DuplicatedCodeView dcv = (DuplicatedCodeView) thisJoinPoint.getThis();
        try
        {
            proceed();
        }
        catch (PartInitException e)
        {
            CheckstyleLog.errorDialog(dcv.mViewer.getControl().getShell(),
                    ErrorMessages.errorWhileOpeningEditor, e, true);
        }
        catch (CoreException e)
        {
            CheckstyleLog.errorDialog(dcv.mViewer.getControl().getShell(),
                    ErrorMessages.errorWhileOpeningEditor, e, true);
        }
    }
}
