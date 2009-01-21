package com.atlassw.tools.eclipse.checkstyle.config;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public aspect CloneHandle
{
    //Clone Problems detection
    declare soft: CloneNotSupportedException: cloneHandle();
    pointcut cloneHandle(): execution (* CheckConfigurationWorkingCopy.clone(..)) || 
                execution (* Module.clone(..)) || execution (* ConfigProperty.clone(..)) ||
                execution (* ResolvableProperty.clone(..)) ;
    
    
    Object around() throws InternalError: cloneHandle()  {
        Object result = null;
        try{
            result = proceed();
        } catch (CheckstylePluginException e) {
            throw new InternalError(); // this should never happen
        }
        return result;
    }
}
