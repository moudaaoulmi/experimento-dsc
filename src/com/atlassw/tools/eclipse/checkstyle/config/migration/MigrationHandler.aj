
package com.atlassw.tools.eclipse.checkstyle.config.migration;

import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import java.text.DateFormat;
import java.util.Date;
import org.eclipse.osgi.util.NLS;

@ExceptionHandler
public aspect MigrationHandler
{
    // ---------------------------
    // Declare Soft
    // ---------------------------
    declare soft: CheckstylePluginException : checkConfigurationMigrator_internalStartElementHandler();

    // ---------------------------
    // Pointcut
    // ---------------------------
    pointcut checkConfigurationMigrator_internalStartElementHandler() : 
        execution(* CheckConfigurationMigrator.OldConfigurationHandler.internalStartElement(..));

    // ---------------------------
    // Advice
    // ---------------------------
    void around(String name) throws CheckstylePluginException : 
            checkConfigurationMigrator_internalStartElementHandler() 
            && args(name){
        try
        {
            proceed(name);
        }
        catch (CheckstylePluginException cpe)
        {
            CheckConfigurationMigrator.OldConfigurationHandler ccmh = ((CheckConfigurationMigrator.OldConfigurationHandler) thisJoinPoint
                    .getThis());
            CheckConfigurationWorkingCopy current = ccmh.getMCurrentConfiguration();
            // we probably got a name collision so we try to use a
            // unique name
            String nameAddition = NLS.bind(
                    Messages.CheckConfigurationMigrator_txtMigrationAddition, DateFormat
                            .getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(
                                    new Date()));
            current.setName(name + nameAddition);
        }
    }
}
