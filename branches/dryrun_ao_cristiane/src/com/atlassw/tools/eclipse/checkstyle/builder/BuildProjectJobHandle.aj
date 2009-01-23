package com.atlassw.tools.eclipse.checkstyle.builder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public aspect BuildProjectJobHandle
{
    declare soft: CoreException: buildProjectJob_runHandler();
    pointcut buildProjectJob_runHandler(): execution (* BuildProjectJob.run(..)) ;
    
    IStatus around(): buildProjectJob_runHandler() {
        IStatus result = null;
        try{
           result = proceed();
        } catch (CoreException e) {
            result = e.getStatus();
        }
        return result;
    }
}
