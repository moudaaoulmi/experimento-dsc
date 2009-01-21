package com.atlassw.tools.eclipse.checkstyle.config;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public privileged aspect RetrowException
{
    
    declare soft: Exception: endElementHandle();
    pointcut endElementHandle(): execution (* CheckConfigurationFactory.endElement(..)) ;
    
    void around() throws SAXException: endElementHandle() {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            throw new SAXException(e);
        }
    }
    
    declare soft: IOException: resolveEntityHandleHandle();
    pointcut resolveEntityHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.resolveEntityHandle(..)) ;
    
    InputSource around() throws SAXException: 
            resolveEntityHandleHandle() {
        InputSource result = null;
        try{
           result = proceed();
        } catch (IOException e)
        {
            throw new SAXException("" + e, e); //$NON-NLS-1$
        }
        return result;
    }
    
    declare soft: Exception: exportConfigurationHandle();
    pointcut exportConfigurationHandle(): execution (* CheckConfigurationFactory.exportConfiguration(..)) ;
    
    void around() throws CheckstylePluginException: exportConfigurationHandle() {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            retrowException(e);
        }
    }
    
    declare soft: Exception: loadFromPersistenceHandle();
    pointcut loadFromPersistenceHandle(): execution (* CheckConfigurationFactory.loadFromPersistence(..)) ;
    
    void around() throws CheckstylePluginException: loadFromPersistenceHandle() {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            retrowException(e, ErrorMessages.errorLoadingConfigFile);
        }
    }
    
    declare soft: Exception: migrateHandle();
    pointcut migrateHandle(): execution (* CheckConfigurationFactory.migrate(..)) ;
    
    void around() throws CheckstylePluginException: migrateHandle() {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            retrowException(e, ErrorMessages.errorMigratingConfig);
        }
    }
    
    declare soft: CheckstyleException: getUnresolvedPropertiesIterationHandle();
    
    pointcut getUnresolvedPropertiesIterationHandle(): 
        execution (* CheckConfigurationTester.getUnresolvedPropertiesIteration(..)) ;
    
    void around() throws CheckstylePluginException: getUnresolvedPropertiesIterationHandle() {
        try{
           proceed();
        } catch (CheckstyleException e) {
            this.retrowException(e);
        }
    }
    
    declare soft: IOException: setModulesHandle();
    pointcut setModulesHandle(): execution (* CheckConfigurationWorkingCopy.setModules(..)) ;
    
    void around() throws CheckstylePluginException: setModulesHandle()  {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            this.retrowException(e);
        }
    }
    
    declare soft: SAXException: runHandle();
    declare soft: ParserConfigurationException: runHandle();
    declare soft: IOException: runHandle();
    pointcut runHandle(): execution (* ConfigurationReader.read(..)) ;
    
    List around() throws CheckstylePluginException: runHandle() {
        List result = null;
        try{
           result = proceed();
        } catch (SAXException se)
        {
            Exception ex = se.getException() != null ? se.getException() : se;
            this.retrowException(ex);
        }
        catch (ParserConfigurationException pe)
        {
            this.retrowException(pe);
        }
        catch (IOException ioe)
        {
            this.retrowException(ioe);
        }
        return result;
    }
    
    declare soft: TransformerConfigurationException: writeHandle();
    declare soft: SAXException: writeHandle();
    
    pointcut writeHandle(): 
        execution (* ConfigurationWriter.write(..)) ;
    
    void around() throws CheckstylePluginException: writeHandle() {
        try{
           proceed();
        } catch (TransformerConfigurationException e)
        {
            this.retrowException(e);
        }
        catch (SAXException e)
        {
            Exception ex = e.getException() != null ? e.getException() : e;
            this.retrowException(ex);
        }
    }
    
declare soft: Exception: storeToPersistenceHandle();
    
    pointcut storeToPersistenceHandle(): 
        execution (* GlobalCheckConfigurationWorkingSet.storeToPersistence(..)) ;
    
    void around() throws CheckstylePluginException: storeToPersistenceHandle() {
        try{
           proceed();
        } catch (CheckstyleException e) {
            this.retrowException(e, ErrorMessages.errorWritingConfigFile);
        }
    }
    
    private void retrowException(Exception e) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e);
    }
    
    private void retrowException(Exception e, String message) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e);
    }
}
