package com.atlassw.tools.eclipse.checkstyle.actions;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;

public privileged aspect ConfigureProjectFromBluePrintActionHandle
{
    declare soft: CheckstylePluginException : runInWorkspaceHandle();
    pointcut runInWorkspaceHandle():  
        execution (* ConfigureProjectFromBluePrintAction.BulkConfigureJob.runInWorkspace(IProgressMonitor)); 
    
    IStatus around() : runInWorkspaceHandle(){
        IStatus result = null;
        try{
            result = proceed();    
        }catch(CheckstylePluginException e){
            result = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.OK, e
                  .getMessage(), e);
        }
        
        return result;
    }
}
