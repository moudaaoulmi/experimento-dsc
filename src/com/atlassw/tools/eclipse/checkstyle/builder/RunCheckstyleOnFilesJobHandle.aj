package com.atlassw.tools.eclipse.checkstyle.builder;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public aspect RunCheckstyleOnFilesJobHandle
{
    declare soft: CheckstylePluginException: runCheckstyleOnFilesJob_runInWorkspaceHandle();
    
    pointcut runCheckstyleOnFilesJob_runInWorkspaceHandle(): 
        execution (* RunCheckstyleOnFilesJob.runInWorkspace(..)) ;
    
    IStatus around() throws CoreException: runCheckstyleOnFilesJob_runInWorkspaceHandle() {
        try{
            proceed();
        } catch (CheckstylePluginException e) { 
            Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.ERROR, e
                    .getLocalizedMessage(), e);
            throw new CoreException(status);
        }
        return null;
    }
}
