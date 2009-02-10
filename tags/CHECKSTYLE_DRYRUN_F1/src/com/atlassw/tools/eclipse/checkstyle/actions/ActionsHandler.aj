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
    declare soft: CoreException : CheckSelectedFilesActionHandle_runHandle();
    declare soft: CheckstylePluginException : ConfigureProjectFromBluePrintActionHandle_runInWorkspaceHandle();
    
    pointcut CheckSelectedFilesActionHandle_runHandle(): execution (* CheckSelectedFilesAction.run(..)) ;
    pointcut ConfigureProjectFromBluePrintActionHandle_runInWorkspaceHandle():  
        execution (* ConfigureProjectFromBluePrintAction.BulkConfigureJob.runInWorkspace(IProgressMonitor));
    
    Object around() : CheckSelectedFilesActionHandle_runHandle()
                      || ConfigureProjectFromBluePrintActionHandle_runInWorkspaceHandle() {
        Object result = null;
        try{
            result = proceed();    
        } catch(CheckstylePluginException e) {
            result = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK, e
                  .getMessage(), e);
        } catch (CoreException e) {
            CheckSelectedFilesAction c = 
                (CheckSelectedFilesAction) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog( c.mPart.getSite().getShell(), e, true);
        }
        return result;
    }
}