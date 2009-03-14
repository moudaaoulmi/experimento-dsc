
package com.atlassw.tools.eclipse.checkstyle.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IProgressMonitor;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public privileged aspect ActionsHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: CoreException : CheckSelectedFilesActionHandle_runHandle();

    declare soft: CheckstylePluginException : ConfigureProjectFromBluePrintActionHandle_runInWorkspaceHandle();
   
    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut CheckSelectedFilesActionHandle_runHandle(): 
        execution (* CheckSelectedFilesAction.run(..)) ;

    pointcut ConfigureProjectFromBluePrintActionHandle_runInWorkspaceHandle():  
        execution (* ConfigureProjectFromBluePrintAction.BulkConfigureJob.runInWorkspace(IProgressMonitor));
   
    // ---------------------------
    // Advice's
    // ---------------------------
    void around(): CheckSelectedFilesActionHandle_runHandle(){
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            CheckSelectedFilesAction c = (CheckSelectedFilesAction) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(c.mPart.getSite().getShell(), e, true);
        }
    }

    IStatus around() : ConfigureProjectFromBluePrintActionHandle_runInWorkspaceHandle(){
        IStatus result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            result = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK, e
                    .getMessage(), e);
        }

        return result;
    }
}
