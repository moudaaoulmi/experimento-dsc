package com.atlassw.tools.eclipse.checkstyle.duplicates;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPart;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.checks.duplicates.StrictDuplicateCodeCheck;
import org.eclipse.ui.PartInitException;

public privileged aspect DuplicatedCodeActionHandler
{
    declare soft: CheckstyleException : internalCreateCheckerHandler();
    declare soft: CoreException : addJavaFilesToSetHandler();
    declare soft: PartInitException: findDuplicatedCodeViewHandler();
    
    pointcut internalCreateCheckerHandler() : execution(* DuplicatedCodeAction.internalCreateChecker(..));
    pointcut addJavaFilesToSetHandler() : execution(* DuplicatedCodeAction.addJavaFilesToSet(..));
    pointcut findDuplicatedCodeViewHandler() : execution(* DuplicatedCodeAction.findDuplicatedCodeView(..));
    
    Checker around(Checker checker, StrictDuplicateCodeCheck check,IWorkbenchPart mWorkbenchPart): 
        internalCreateCheckerHandler() && args(checker,check,mWorkbenchPart){
        try {
            return proceed(checker,check,mWorkbenchPart);
        }catch (CheckstyleException e){
            CheckstyleLog.errorDialog(mWorkbenchPart.getSite().getShell(),
                    "Unable to launch the duplicated code analyser.", e, true);
        }
        return checker;
    }
    
    void around(): addJavaFilesToSetHandler(){
        try {
            proceed();
        }catch (CoreException e){
            // we can't do anything : just log the pbm...
            CheckstyleLog.log(e, "Error while scanning files for the duplication code analysis.");
        }
    }
    DuplicatedCodeView around(): findDuplicatedCodeViewHandler(){
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
}
