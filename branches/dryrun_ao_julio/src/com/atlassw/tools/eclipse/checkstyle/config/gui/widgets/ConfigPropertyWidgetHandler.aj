package com.atlassw.tools.eclipse.checkstyle.config.gui.widgets;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * @author Romulo
 */
public aspect ConfigPropertyWidgetHandler{

    pointcut validateIntegerHandler() : execution(* ConfigPropertyWidgetInteger.validate(..));
    pointcut validateRegexHandler() : execution(* ConfigPropertyWidgetRegex.validate(..));
    
    after() throwing(Exception e) throws CheckstylePluginException : 
        validateIntegerHandler()|| 
        validateRegexHandler(){    
        CheckstylePluginException.rethrow(e, e.getLocalizedMessage());
    }

}
