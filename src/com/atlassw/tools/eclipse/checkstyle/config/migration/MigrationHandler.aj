
package com.atlassw.tools.eclipse.checkstyle.config.migration;

import java.io.File;
import java.io.FileNotFoundException;

import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import java.text.DateFormat;
import java.util.Date;
import org.eclipse.osgi.util.NLS;

public aspect MigrationHandler
{
    // ---------------------------
    // Declare Soft's
    // ---------------------------
    declare soft: CheckstylePluginException : checkConfigurationMigrator_endElementHandler() || checkConfigurationMigrator_internalStartElementHandler() || checkConfigurationMigrator_startElementHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut checkConfigurationMigrator_ensureFileExistsHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.internalEnsureFileExists(..));

    pointcut checkConfigurationMigrator_endElementHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.endElement(..));

    pointcut checkConfigurationMigrator_internalStartElementHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.internalStartElement(..));

    pointcut checkConfigurationMigrator_startElementHandler(): execution(* CheckConfigurationMigrator.OldConfigurationHandler.startElement(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    
    void around(String name) throws CheckstylePluginException : checkConfigurationMigrator_internalStartElementHandler() && args(name){
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

//    void around() throws CheckstylePluginException : checkConfigurationMigrator_migrateHandler(){
//        try
//        {
//            proceed();
//        }
//        catch (SAXException se)
//        {
//            Exception ex = se.getException() != null ? se.getException() : se;
//            CheckstylePluginException.rethrow(ex);
//        }
//        catch (ParserConfigurationException pe)
//        {
//            CheckstylePluginException.rethrow(pe);
//        }
//        catch (IOException ioe)
//        {
//            CheckstylePluginException.rethrow(ioe);
//        }
//    }

    void around() throws SAXException : checkConfigurationMigrator_startElementHandler() || checkConfigurationMigrator_endElementHandler() {
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            throw new SAXException(e);
        }
    }
}
