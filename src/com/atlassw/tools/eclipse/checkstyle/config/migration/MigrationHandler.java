
package com.atlassw.tools.eclipse.checkstyle.config.migration;

import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.eclipse.osgi.util.NLS;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public class MigrationHandler
{
    public void checkConfigurationMigratorMigrate(SAXException se) throws CheckstylePluginException
    {
        Exception ex = se.getException() != null ? se.getException() : se;
        CheckstylePluginException.rethrow(ex);
    }

    public void checkConfigurationMigratorCheckstylePluginException(Exception e)
        throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e);
    }

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

    public void checkConfigurationMigratorSAXException(CheckstylePluginException e)
        throws SAXException
    {
        throw new SAXException(e);
    }

    public void checkConfigurationMigratorEnsureFileExists(OutputStream out)
    {
        IOUtils.closeQuietly(out);
    }

}
