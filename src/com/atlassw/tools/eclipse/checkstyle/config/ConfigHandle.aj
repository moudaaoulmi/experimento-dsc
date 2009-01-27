package com.atlassw.tools.eclipse.checkstyle.config;

import org.eclipse.osgi.util.NLS;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import com.puppycrawl.tools.checkstyle.api.SeverityLevel;


public privileged aspect ConfigHandle
{
    declare soft: Exception: CheckConfigurationWorkingCopy_setLocationHandle() 
                            || RetrowException_endElementHandle()
                            || RetrowException_exportConfigurationHandle()
                            || RetrowException_loadFromPersistenceHandle()
                            || RetrowException_migrateHandle()
                            || RetrowException_storeToPersistenceHandle();
    
    declare soft: CoreException: CheckConfigurationWorkingCopy_setModulesIterationHandle();
    
    declare soft: CheckstylePluginException: CheckstyleLogMessage_refreshHandle()
                            || CheckstyleLogMessage_removeCheckConfigurationHandle();   
    
    declare soft: IOException: ConfigurationReaderHandle_startElementHandleHandle()
                            || RetrowException_resolveEntityHandleHandle()
                            || RetrowException_setModulesHandle()
                            || RetrowException_runHandle();
    
    declare soft: SAXException: RetrowException_runHandle()
                            || RetrowException_writeHandle();
    
    declare soft: CloneNotSupportedException: cloneHandle();
    declare soft: CheckstyleException: RetrowException_getUnresolvedPropertiesIterationHandle();
    declare soft: ParserConfigurationException: RetrowException_runHandle();
    declare soft: TransformerConfigurationException: RetrowException_writeHandle();
    
    //CheckConfigurationWorkingCopy
    pointcut CheckConfigurationWorkingCopy_setLocationHandle(): execution (* CheckConfigurationWorkingCopy.setLocationHandle(..)) ;
    pointcut CheckConfigurationWorkingCopy_setModulesIterationHandle(): execution (* CheckConfigurationWorkingCopy.setModulesIteration(..)) ;
    //CheckstyleLogMessage
    pointcut CheckstyleLogMessage_refreshHandle(): execution (* CheckConfigurationFactory.refresh(..)) ;
    pointcut CheckstyleLogMessage_removeCheckConfigurationHandle(): 
        execution (* GlobalCheckConfigurationWorkingSet.removeCheckConfiguration(..)) ;
    //ConfigurationReaderHandle
    pointcut ConfigurationReaderHandle_getAdditionalConfigDataHandleHandle(): 
        execution (* ConfigurationReader.getAdditionalConfigDataHandle(..)) ;
    pointcut ConfigurationReaderHandle_startElementHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.startElementHandle(..)) ;
    
    pointcut cloneHandle(): execution (* CheckConfigurationWorkingCopy.clone(..)) || 
        execution (* Module.clone(..)) || execution (* ConfigProperty.clone(..)) ||
        execution (* ResolvableProperty.clone(..)) ;
    
    pointcut RetrowException_endElementHandle(): execution (* CheckConfigurationFactory.CheckConfigurationsFileHandler.endElement(..)) ;
    
    pointcut RetrowException_resolveEntityHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.resolveEntityHandle(..)) ;
    
    pointcut RetrowException_exportConfigurationHandle(): execution (* CheckConfigurationFactory.exportConfiguration(..)) ;
    
    pointcut RetrowException_loadFromPersistenceHandle(): execution (* CheckConfigurationFactory.loadFromPersistence(..)) ;
    
    pointcut RetrowException_migrateHandle(): execution (* CheckConfigurationFactory.migrate(..)) ;
    
    pointcut RetrowException_getUnresolvedPropertiesIterationHandle(): 
        execution (* CheckConfigurationTester.getUnresolvedPropertiesIteration(..)) ;
    
    pointcut RetrowException_setModulesHandle(): execution (* CheckConfigurationWorkingCopy.setModules(..)) ;
    
    pointcut RetrowException_runHandle(): execution (* ConfigurationReader.read(..)) ;
    
    pointcut RetrowException_writeHandle(): execution (* ConfigurationWriter.write(..)) ;
    
    pointcut RetrowException_storeToPersistenceHandle(): execution (* GlobalCheckConfigurationWorkingSet.storeToPersistence(..)) ;
    
    void around(String location, String oldLocation) throws CheckstylePluginException: CheckConfigurationWorkingCopy_setLocationHandle() 
            && args(location, oldLocation) {
        try{
           proceed(location, oldLocation);
        } catch (CheckstylePluginException e) {
            CheckConfigurationWorkingCopy c = (CheckConfigurationWorkingCopy) thisJoinPoint.getThis();
            c.mEditedLocation = oldLocation;
            CheckstylePluginException.rethrow(e, NLS
                    .bind(ErrorMessages.errorResolveConfigLocation, location, e
                            .getLocalizedMessage()));
        }
    }
    
    void around(): CheckConfigurationWorkingCopy_setModulesIterationHandle()  {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            // NOOP - just ignore
        }
    }
    
    Object around(): CheckstyleLogMessage_removeCheckConfigurationHandle() || CheckstyleLogMessage_refreshHandle() {
        Object result = null;
        try{
            result = proceed();
        } catch (CheckstyleException e) {
            checkstyleLogMessage(e);
        }
        return result;
    }
    
    
    int around(int tabWidth,String tabWidthProp): ConfigurationReaderHandle_getAdditionalConfigDataHandleHandle() && args(tabWidth, tabWidthProp)  {
        int result = tabWidth;
        try{
           result = proceed(tabWidth, tabWidthProp);
        } catch (NumberFormatException se)
        {
            // ignore
        }
        return result;
    }
    
    void around(String value, Module module): ConfigurationReaderHandle_startElementHandleHandle() && args(value, module) {
        try{
           proceed(value, module);
        } catch (IllegalArgumentException e)
        {
           module.setSeverity(SeverityLevel.WARNING);
        }
    }
    
    Object around() throws InternalError: cloneHandle()  {
        Object result = null;
        try{
            result = proceed();
        } catch (CheckstylePluginException e) {
            throw new InternalError(); // this should never happen
        }
        return result;
    }
       
    Object around() throws SAXException: RetrowException_resolveEntityHandleHandle() 
            || RetrowException_endElementHandle() {
        Object result = null;
        try {
           result = proceed();
        } catch (IOException e) {
            throw new SAXException("" + e, e); //$NON-NLS-1$
        } catch (CheckstylePluginException e) {
            throw new SAXException(e);
        }
        return result;
    }
    
    void around() throws CheckstylePluginException: RetrowException_exportConfigurationHandle() 
                                                    || RetrowException_getUnresolvedPropertiesIterationHandle()
                                                    || RetrowException_setModulesHandle() {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            retrowException(e);
        } catch (CheckstyleException e) {
            this.retrowException(e);
        }
    }
    
    void around() throws CheckstylePluginException: RetrowException_loadFromPersistenceHandle() {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            retrowException(e, ErrorMessages.errorLoadingConfigFile);
        }
    }
    
    void around() throws CheckstylePluginException: RetrowException_migrateHandle() 
                                    || RetrowException_writeHandle()
                                    || RetrowException_storeToPersistenceHandle() {
        try{
           proceed();
        } catch (CheckstylePluginException e) {
            this.retrowException(e, ErrorMessages.errorMigratingConfig);
        } catch (CheckstyleException e) {
            this.retrowException(e, ErrorMessages.errorWritingConfigFile);
        } catch (TransformerConfigurationException e){
            this.retrowException(e);
        } catch (SAXException e) {
            Exception ex = e.getException() != null ? e.getException() : e;
            this.retrowException(ex);
        }
    }     
    
    List around() throws CheckstylePluginException: RetrowException_runHandle() {
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
    
    private void retrowException(Exception e) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e);        
    }
    
    private void retrowException(Exception e, String message) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e,message);
    }
    
    private void checkstyleLogMessage(Exception e) {
        CheckstyleLog.log(e);
    }
}
