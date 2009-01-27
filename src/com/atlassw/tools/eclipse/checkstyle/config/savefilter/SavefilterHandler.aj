package com.atlassw.tools.eclipse.checkstyle.config.savefilter;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public aspect SavefilterHandler
{
    declare soft: Exception : saveFilters_internalHandler();
    
    pointcut saveFilters_internalHandler() : execution(* SaveFilters.internal(..));
    
    void around() : saveFilters_internalHandler(){
        try{
            proceed();
        }
        catch (Exception e){
            CheckstyleLog.log(e);
        }
    }
}
