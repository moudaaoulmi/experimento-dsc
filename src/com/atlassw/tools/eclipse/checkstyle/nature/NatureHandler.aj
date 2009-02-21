package com.atlassw.tools.eclipse.checkstyle.nature;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public aspect NatureHandler
{
    // ---------------------------
    // Declare Soft's
    // ---------------------------
    declare soft : CoreException : nature_runInWorkspace();
    
    // ---------------------------
    // Pointcuts
    // ---------------------------
    pointcut nature_runInWorkspace() : execution(* ConfigureDeconfigureNatureJob.runInWorkspace(..));
    
    // ---------------------------
    // Advice's
    // ---------------------------
    IStatus around(IProgressMonitor monitor) throws CoreException : nature_runInWorkspace() && args(monitor) {
        IStatus status = null;
        try {
            status = proceed(monitor);
        } finally {
            monitor.done();
        }
        return status;
    }

}
