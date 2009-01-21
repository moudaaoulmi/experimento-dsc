package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

public aspect PluginFiltersHandler
{
    declare soft: Exception : internalHandler();

    pointcut internalHandler() : execution(* PluginFilters.internal(..));
    
    void around() : internalHandler(){
        try{
            proceed();
        }
        catch (Exception e){
            CheckstyleLog.log(e);
        }
    }
}
