package com.atlassw.tools.eclipse.checkstyle.config.migration;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import java.io.FileNotFoundException;
import org.xml.sax.SAXException;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import java.text.DateFormat;
import java.util.Date;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import org.eclipse.osgi.util.NLS;
import com.atlassw.tools.eclipse.checkstyle.config.migration.CheckConfigurationMigrator;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

public aspect CheckConfigurationMigratorHandler
{
    declare soft: FileNotFoundException : ensureFileExistsHandler();
    declare soft: CheckstylePluginException : endElementHandler() || internalStartElementHandler() || startElementHandler();
    declare soft: SAXException : migrateHandler();
    declare soft: ParserConfigurationException : migrateHandler();
    declare soft: IOException : migrateHandler();
    
    pointcut ensureFileExistsHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.ensureFileExists(..));
    pointcut endElementHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.endElement(..));
    pointcut internalStartElementHandler() : execution(* CheckConfigurationMigrator.OldConfigurationHandler.internalStartElement(..));
    pointcut migrateHandler(): execution(* CheckConfigurationMigrator.migrate(..));
    pointcut startElementHandler(): execution(* CheckConfigurationMigrator.OldConfigurationHandler.startElement(..));
    
    after() throwing(Exception e) throws CheckstylePluginException : 
        ensureFileExistsHandler(){    
        CheckstylePluginException.rethrow(e);
    }
    
    after() throwing(Exception e) throws SAXException : 
        endElementHandler(){    
        throw new SAXException(e);
    }
    
    void around(String name) throws CheckstylePluginException : internalStartElementHandler() && args(name){
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
    
    void around() throws CheckstylePluginException : migrateHandler(){
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
    
    void around() throws SAXException : startElementHandler(){
        try{
            proceed();
        }catch (CheckstylePluginException e){
            throw new SAXException(e);
        }
    }
}
