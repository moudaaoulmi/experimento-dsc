
package com.atlassw.tools.eclipse.checkstyle.config.migration;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.osgi.util.NLS;

import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class MigrationHandler extends GeneralException
{

    public void checkConfigurationMigratorStartElement(
            CheckConfigurationWorkingCopy mCurrentConfiguration, String name)
        throws CheckstylePluginException
    {
        // we probably got a name collision so we try to use a
        // unique name
        String nameAddition = NLS
                .bind(Messages.CheckConfigurationMigrator_txtMigrationAddition, DateFormat
                        .getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(new Date()));
        mCurrentConfiguration.setName(name + nameAddition);
    }

}
