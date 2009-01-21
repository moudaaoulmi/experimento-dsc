package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public aspect FileMatchPatternHandler
{
    declare soft : CloneNotSupportedException : cloneHandler() || cloneFileSetHandler() || cloneProjectHandler()
                    || cloneWorkingCopyHandler();
    
    pointcut internalSetMatchPatternHandler() : execution (* FileMatchPattern.internalSetMatchPattern(..));
    pointcut cloneHandler(): execution(* FileMatchPattern.clone(..));
    pointcut cloneFileSetHandler(): execution(* FileSet.clone(..));
    pointcut cloneProjectHandler(): execution(* ProjectConfiguration.clone(..));
    pointcut cloneWorkingCopyHandler(): execution(* ProjectConfigurationWorkingCopy.clone(..));
    
    after() throwing(Exception e) throws CheckstylePluginException : 
        internalSetMatchPatternHandler(){    
        CheckstylePluginException.rethrow(e); // wrap the exception
    }
    
    Object around() : cloneHandler() || cloneFileSetHandler() || cloneProjectHandler()
                || cloneWorkingCopyHandler(){
        try{
            return proceed();
        }catch (CloneNotSupportedException e){
            throw new InternalError(); // should never happen
        }
    }

}
