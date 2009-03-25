/**
 * 
 */
package com.atlassw.tools.eclipse.checkstyle.quickfixes;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralExceptionHandler;
import org.eclipse.core.filebuffers.ITextFileBufferManager;

public privileged aspect QuickFixesHandler {
    
    declare soft : BadLocationException : getLineInformationOfOffsetHandler();
    declare soft : CoreException : getLineInformationOfOffsetHandler()
                    || runInternalHandler()
                    || runHandler2()
                    || runInUIThreadHandler();
    
    pointcut getLineInformationOfOffsetHandler(): execution(* AbstractASTResolution.run(..));
    /*pointcut runInternalHandler(): 
        call(* ITextFileBufferManager.disconnect(..) ) && 
        withincode(* AbstractASTResolution.run(..) );*/
    pointcut runInternalHandler(): 
        execution(* AbstractASTResolution.runInternal(..) );
    
    pointcut runHandler2(): execution(* FixCheckstyleMarkersAction.run(..));
    pointcut runInUIThreadHandler(): execution(* FixCheckstyleMarkersJob.runInUIThread(..));

    void around(): getLineInformationOfOffsetHandler() {
        try{
            proceed();
        } catch (CoreException e) {
            CheckstyleLog.log(e, ErrorMessages.AbstractASTResolution_msgErrorQuickfix);
        } catch (MalformedTreeException e) {
            CheckstyleLog.log(e, "Error processing quickfix");
        } catch (BadLocationException e) {
            CheckstyleLog.log(e, "Error processing quickfix");
        }
    }
    
    void around(): runInternalHandler(){
        try {
            proceed();
        } catch (CoreException e) {
            CheckstyleLog.log(e, "Error processing quickfix"); //$NON-NLS-1$
        }
    }
   
    /**
     * O advice atinge o método, mas não o afeta porque ele é private
     */
    void around(): runHandler2(){
        try {
            proceed();
        } catch (CoreException e) {
            FixCheckstyleMarkersAction obj = (FixCheckstyleMarkersAction) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(obj.mWorkBenchPart.getSite().getShell(), e, true);
        }
    }
    
    IStatus around() : runInUIThreadHandler() {
        IStatus c = null;
        try {
            c = proceed();
        } catch (CoreException e) {
            return new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK,
                    e.getMessage(), e);
        }
        return c;
    }//around()

}//QuickFixesHandler
