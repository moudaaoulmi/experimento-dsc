package com.atlassw.tools.eclipse.checkstyle.actions;

import org.eclipse.core.runtime.CoreException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public privileged aspect CheckSelectedFilesActionHandle

{
    declare soft: CoreException : runHandle();
    pointcut runHandle(): execution (* CheckSelectedFilesAction.run(..)) ;
    
    void around(): runHandle(){
        try{
            proceed();
        }catch (CoreException e){
            CheckSelectedFilesAction c = 
                (CheckSelectedFilesAction) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog( c.mPart.getSite().getShell(), e, true);
        }
    }
}
