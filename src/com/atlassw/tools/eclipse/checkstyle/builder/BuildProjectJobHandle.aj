package com.atlassw.tools.eclipse.checkstyle.builder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public aspect BuildProjectJobHandle
{
    declare soft: CoreException: runHandle();
    pointcut runHandle(): execution (* BuildProjectJob.run(..)) ;
    
    IStatus around(): runHandle() {
        IStatus result = null;
        try{
           result = proceed();
        } catch (CoreException e) {
            result = e.getStatus();
        }
        return result;
    }
}
