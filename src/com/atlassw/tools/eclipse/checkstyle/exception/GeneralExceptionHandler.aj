package com.atlassw.tools.eclipse.checkstyle.exception;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public abstract aspect GeneralExceptionHandler{

    public abstract pointcut exceptionPoints();
    
    Object around () throws CheckstylePluginException: exceptionPoints(){
    
        Object result = null;
        try{
            result = proceed();
            
        }catch (Exception e){
            CheckstylePluginException.rethrow(e);
        }
        return result;
    }

}
