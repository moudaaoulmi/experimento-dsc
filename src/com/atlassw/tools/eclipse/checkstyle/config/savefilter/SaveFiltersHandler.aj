package com.atlassw.tools.eclipse.checkstyle.config.savefilter;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public aspect SaveFiltersHandler
{
    declare soft: Exception : internalHandler();
    
    pointcut internalHandler() : execution(* SaveFilters.internal(..));
    
    void around() : internalHandler(){
        try{
            proceed();
        }
        catch (Exception e){
            CheckstyleLog.log(e);
        }
    }
}
