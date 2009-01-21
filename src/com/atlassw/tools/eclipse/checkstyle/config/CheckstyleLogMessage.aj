package com.atlassw.tools.eclipse.checkstyle.config;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public aspect CheckstyleLogMessage
{
    declare soft: CheckstylePluginException: refreshHandle();
    pointcut refreshHandle(): execution (* CheckConfigurationFactory.refresh(..)) ;
    
    void around(): refreshHandle() {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            checkstyleLogMessage(e);
        }
    }
    
    declare soft: CheckstylePluginException: removeCheckConfigurationHandle();
    
    pointcut removeCheckConfigurationHandle(): 
        execution (* GlobalCheckConfigurationWorkingSet.removeCheckConfiguration(..)) ;
    
    boolean around(): removeCheckConfigurationHandle() {
        boolean result = false;
        try{
            result = proceed();
        } catch (CheckstyleException e) {
            checkstyleLogMessage(e);
        }
        return result;
    }
    
    private void checkstyleLogMessage(Exception e) {
        CheckstyleLog.log(e);
    }
}
