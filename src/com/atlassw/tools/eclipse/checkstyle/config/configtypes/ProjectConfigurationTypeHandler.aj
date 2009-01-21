
package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public aspect ProjectConfigurationTypeHandler
{
    declare soft: CheckstylePluginException: internalIsConfigurableHandler();

    pointcut internalIsConfigurableHandler():
        execution(* ProjectConfigurationType.internalIsConfigurable(..));

    boolean around(ICheckConfiguration checkConfiguration, boolean isConfigurable): internalIsConfigurableHandler() && 
        args(checkConfiguration, isConfigurable){
        boolean result = false;
        try
        {
            result = proceed(checkConfiguration, isConfigurable);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
            isConfigurable = false;
        }
        return result;
    }
}
