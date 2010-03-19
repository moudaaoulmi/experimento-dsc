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
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralExceptionHandler;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.ui.IEditorPart;

@ExceptionHandler
public privileged aspect QuickFixesExceptionHandler {
    
    declare soft : BadLocationException : abstractASTResolution_getLineInformationOfOffsetHandler();
    declare soft : CoreException : abstractASTResolution_getLineInformationOfOffsetHandler() ||
                                   fixCheckstyleMarkersAction_runHandler2() ||
                                   fixCheckstyleMarkersJob_runInUIThreadHandler();
    
    pointcut abstractASTResolution_getLineInformationOfOffsetHandler(): 
        execution(* AbstractASTResolution.internalRun(..));
    
    pointcut fixCheckstyleMarkersAction_runHandler2(): 
        call(* JavaUI.openInEditor(..)) &&
        withincode(void FixCheckstyleMarkersAction.run(..));
    
    pointcut fixCheckstyleMarkersJob_runInUIThreadHandler(): 
        execution(* FixCheckstyleMarkersJob.runInUIThread(..));

    void around(IMarker marker, ICompilationUnit compilationUnit,
            ITextFileBufferManager bufferManager, IPath path): 
            abstractASTResolution_getLineInformationOfOffsetHandler() &&
            args (marker, compilationUnit, bufferManager, path){
        try{
            proceed(marker, compilationUnit, bufferManager, path);
        } catch (CoreException e) {
            CheckstyleLog.log(e, ErrorMessages.AbstractASTResolution_msgErrorQuickfix);
        } catch (MalformedTreeException e) {
            CheckstyleLog.log(e, "Error processing quickfix");
        } catch (BadLocationException e) {
            CheckstyleLog.log(e, "Error processing quickfix");
        } finally
        {

            if (bufferManager != null)
            {
                try
                {
                    bufferManager.disconnect(path, null);
                }
                catch (CoreException e)
                {
                    CheckstyleLog.log(e, "Error processing quickfix"); //$NON-NLS-1$
                }
            }
        }
    }
   
    IEditorPart around(IJavaElement javaElement): 
            fixCheckstyleMarkersAction_runHandler2() && 
            args (javaElement){
        IEditorPart ep = null;
        try {
            proceed(javaElement);
        } catch (CoreException e) {
            FixCheckstyleMarkersAction obj = (FixCheckstyleMarkersAction) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(obj.mWorkBenchPart.getSite().getShell(), e, true);
        }
        return ep;
    }
    
    IStatus around() : fixCheckstyleMarkersJob_runInUIThreadHandler() {
        try {
            proceed();
        } catch (CoreException e) {
            return new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK,
                    e.getMessage(), e);
        }
        return Status.OK_STATUS;
    }//around()

}//QuickFixesHandler
