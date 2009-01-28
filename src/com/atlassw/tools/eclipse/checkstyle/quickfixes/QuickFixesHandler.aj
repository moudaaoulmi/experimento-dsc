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

/**
 * @author julianasaraiva
 *
 */
public privileged aspect QuickFixesHandler {
    
    declare soft : BadLocationException : getLineInformationOfOffsetHandler();
    declare soft : CoreException : getLineInformationOfOffsetHandler()
                    || runInternalHandler()
                    || runInternalHandler2()
                    || runInUIThreadHandler();
    
    pointcut getLineInformationOfOffsetHandler(): execution(* AbstractASTResolution.run(..));
    pointcut runInternalHandler(): execution(* AbstractASTResolution.runInternal(..));
    pointcut runInternalHandler2(): execution(* FixCheckstyleMarkersAction.runInternal(..));
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
    void around(): runInternalHandler2(){
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
