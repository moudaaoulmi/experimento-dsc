package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import org.eclipse.osgi.util.NLS;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.eclipse.osgi.util.NLS;

public aspect ProjectConfigurationWorkingCopyHandler
{
    declare soft : Exception: internalStoreToPersistenceHandler();
    
    pointcut internalStoreToPersistenceHandler(): execution(* ProjectConfigurationWorkingCopy.internalStoreToPersistence(..));
    
    after() throwing(Exception e) throws CheckstylePluginException : 
        internalStoreToPersistenceHandler(){    
        CheckstylePluginException.rethrow(e, NLS.bind(
                ErrorMessages.errorWritingCheckConfigurations, e.getLocalizedMessage()));
    }
}
