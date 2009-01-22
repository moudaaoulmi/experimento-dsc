package com.atlassw.tools.eclipse.checkstyle.config.migration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;


import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import java.text.DateFormat;
import java.util.Date;
import org.eclipse.osgi.util.NLS;


public aspect MigrationHandler
{
    declare soft: FileNotFoundException : checkConfigurationMigrator_ensureFileExistsHandler();
    declare soft: CheckstylePluginException : checkConfigurationMigrator_endElementHandler() || checkConfigurationMigrator_internalStartElementHandler() || checkConfigurationMigrator_startElementHandler();
    declare soft: SAXException : checkConfigurationMigrator_migrateHandler();
    declare soft: ParserConfigurationException : checkConfigurationMigrator_migrateHandler();
    declare soft: IOException : checkConfigurationMigrator_migrateHandler();
    
    pointcut checkConfigurationMigrator_ensureFileExistsHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.internalEnsureFileExists(..));
    pointcut checkConfigurationMigrator_endElementHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.endElement(..));
    pointcut checkConfigurationMigrator_internalStartElementHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.internalStartElement(..));
    pointcut checkConfigurationMigrator_migrateHandler(): execution(* CheckConfigurationMigrator.migrate(..));
    pointcut checkConfigurationMigrator_startElementHandler(): execution(* CheckConfigurationMigrator.OldConfigurationHandler.startElement(..));
    
    
    void around(File file, OutputStream out) throws CheckstylePluginException : checkConfigurationMigrator_ensureFileExistsHandler() && args(file, out){
        try
        {
            proceed(file, out);
        }
        catch (IOException ioe)
        {
            CheckstylePluginException.rethrow(ioe);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }
   
    
    void around() throws SAXException: checkConfigurationMigrator_endElementHandler() {
        try
        {
           proceed();
        }
        catch (CheckstylePluginException e)
        {
            throw new SAXException(e);
        }
    }
    
    void around(String name) throws CheckstylePluginException : checkConfigurationMigrator_internalStartElementHandler() && args(name){
        CheckConfigurationMigrator.OldConfigurationHandler ccmh = ((CheckConfigurationMigrator.OldConfigurationHandler)thisJoinPoint.getThis());
        CheckConfigurationWorkingCopy current = ccmh.getMCurrentConfiguration();
        try{
            proceed(name);
        }catch (CheckstylePluginException cpe){
            // we probably got a name collision so we try to use a
            // unique name
            String nameAddition = NLS.bind(
                    Messages.CheckConfigurationMigrator_txtMigrationAddition,
                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL)
                            .format(new Date()));
            current.setName(name + nameAddition);
        }
    }
    
    void around() throws CheckstylePluginException : checkConfigurationMigrator_migrateHandler(){
        try{
            proceed();
        }catch (SAXException se){
            Exception ex = se.getException() != null ? se.getException() : se;
            CheckstylePluginException.rethrow(ex);
        }catch (ParserConfigurationException pe) {
            CheckstylePluginException.rethrow(pe);
        }catch (IOException ioe){
            CheckstylePluginException.rethrow(ioe);
        }
    }
    
    void around() throws SAXException : checkConfigurationMigrator_startElementHandler(){
        try{
            proceed();
        }catch (CheckstylePluginException e){
            throw new SAXException(e);
        }
    }
}
