/*
 * 26th, November, 2008 
 */
package com.atlassw.tools.eclipse.checkstyle.quickfixes;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

/**
 * @author juliana
 *
 */
public privileged aspect CheckstryleMessageHandler {
    
    declare soft : BadLocationException : getLineInformationOfOffsetHandler();
    declare soft : CoreException : getLineInformationOfOffsetHandler() ;
    
    pointcut getLineInformationOfOffsetHandler(): //call(* IDocument.getLineInformationOfOffset(..)) &&
                execution(* AbstractASTResolution.run(..));
    
    
    void around(): getLineInformationOfOffsetHandler() {
        try{
            proceed();
        } 
        catch (CoreException e)
        {
            CheckstyleLog.log(e, ErrorMessages.AbstractASTResolution_msgErrorQuickfix);
        }
        catch (MalformedTreeException e)
        {
            CheckstyleLog.log(e, "Error processing quickfix");
        }
        catch (BadLocationException e)
        {
            CheckstyleLog.log(e, "Error processing quickfix");
        }
    }
    
    declare soft : CoreException : runInternalHandler() ;
    
    pointcut runInternalHandler(): execution(* AbstractASTResolution.runInternal(..));
   
    void around(): runInternalHandler(){
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            CheckstyleLog.log(e, "Error processing quickfix"); //$NON-NLS-1$
        }
    }
    
 declare soft : CoreException : runInternalHandler2() ;
    
    pointcut runInternalHandler2(): execution(* FixCheckstyleMarkersAction.runInternal(..));
   
    void around(): runInternalHandler2(){
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            FixCheckstyleMarkersAction obj = (FixCheckstyleMarkersAction) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(obj.mWorkBenchPart.getSite().getShell(), e, true);
        }
    }
}