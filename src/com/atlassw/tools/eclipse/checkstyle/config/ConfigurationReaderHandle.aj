package com.atlassw.tools.eclipse.checkstyle.config;

import java.io.IOException;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;


public privileged aspect ConfigurationReaderHandle
{
    
    pointcut getAdditionalConfigDataHandleHandle(): 
        execution (* ConfigurationReader.getAdditionalConfigDataHandle(..)) ;
    
    int around(int tabWidth,String tabWidthProp): getAdditionalConfigDataHandleHandle() && args(tabWidth, tabWidthProp)  {
        int result = tabWidth;
        try{
           result = proceed(tabWidth, tabWidthProp);
        } catch (NumberFormatException se)
        {
            // ignore
        }
        return result;
    }
    
    
    declare soft: IOException: startElementHandleHandle();
    pointcut startElementHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.startElementHandle(..)) ;
    
    void around(String value, Module module): startElementHandleHandle() && args(value, module) {
        try{
           proceed(value, module);
        } catch (IllegalArgumentException e)
        {
           module.setSeverity(SeverityLevel.WARNING);
        }
    }
}
