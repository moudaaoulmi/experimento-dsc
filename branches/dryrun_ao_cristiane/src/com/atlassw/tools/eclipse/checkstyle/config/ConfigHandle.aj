
package com.atlassw.tools.eclipse.checkstyle.config;

import org.eclipse.osgi.util.NLS;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;

import com.puppycrawl.tools.checkstyle.api.SeverityLevel;

public privileged aspect ConfigHandle
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
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

    declare soft: SAXException: RetrowException_writeHandle();

    declare soft: CloneNotSupportedException: cloneHandle();

    declare soft: TransformerConfigurationException: RetrowException_writeHandle();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut CheckConfigurationWorkingCopy_setLocationHandle():
        execution (* CheckConfigurationWorkingCopy.setLocationHandle(..)) ;

    pointcut CheckConfigurationWorkingCopy_setModulesIterationHandle(): 
        call (* IResource.refreshLocal(..)) &&
        withincode (* CheckConfigurationWorkingCopy.setModules(..)) ;

    pointcut CheckstyleLogMessage_refreshHandle(): 
        execution (* CheckConfigurationFactory.refresh(..)) ;

    pointcut CheckstyleLogMessage_removeCheckConfigurationHandle():
        call(* ProjectConfigurationFactory.isCheckConfigInUse(..)) &&
        withincode(* GlobalCheckConfigurationWorkingSet.removeCheckConfiguration(..)) ;

    pointcut ConfigurationReaderHandle_getAdditionalConfigDataHandler(): 
        call(* Integer.parseInt(..)) &&
        withincode (* ConfigurationReader.getAdditionalConfigData(..)) ;

    pointcut ConfigurationReaderHandle_startElementHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.startElementHandle(..)) ;

    pointcut cloneHandle(): 
        execution (* CheckConfigurationWorkingCopy.clone(..)) || 
        execution (* Module.clone(..)) || 
        execution (* ConfigProperty.clone(..)) ||
        execution (* ResolvableProperty.clone(..)) ;

    pointcut RetrowException_endElementHandle(): 
        execution (* CheckConfigurationFactory.CheckConfigurationsFileHandler.endElement(..));

    pointcut RetrowException_resolveEntityHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.resolveEntityHandle(..)) ;

    // esse ta errado, ajeitar!!!
    pointcut RetrowException_exportConfigurationHandle(): 
        execution (* CheckConfigurationFactory.exportConfiguration(..)) ;

    pointcut RetrowException_loadFromPersistenceHandle(): 
        execution (* CheckConfigurationFactory.loadFromPersistence(..)) ;

    pointcut RetrowException_migrateHandle(): 
        execution (* CheckConfigurationFactory.migrate(..)) ;

    pointcut RetrowException_getUnresolvedPropertiesIterationHandle(): 
        execution (* CheckConfigurationTester.getUnresolvedPropertiesIteration(..)) ;

    pointcut RetrowException_setModulesHandle(): 
        execution (* CheckConfigurationWorkingCopy.setModules(..)) ;

    pointcut RetrowException_runHandle(): 
        execution (* ConfigurationReader.read(..)) ;

    pointcut RetrowException_writeHandle(): 
        execution (* ConfigurationWriter.write(..)) ;

    pointcut RetrowException_storeToPersistenceHandle(): 
        execution (* GlobalCheckConfigurationWorkingSet.storeToPersistence(..)) ;

    // ---------------------------
    // Advice's
    // ---------------------------
    void around(String location, String oldLocation) throws CheckstylePluginException: 
        CheckConfigurationWorkingCopy_setLocationHandle() 
            && args(location, oldLocation) {
        try
        {
            proceed(location, oldLocation);
        }
        catch (CheckstylePluginException e)
        {
            CheckConfigurationWorkingCopy c = (CheckConfigurationWorkingCopy) thisJoinPoint
                    .getThis();
            c.mEditedLocation = oldLocation;
            CheckstylePluginException.rethrow(e, NLS.bind(ErrorMessages.errorResolveConfigLocation,
                    location, e.getLocalizedMessage()));
        }
    }

    void around(): CheckConfigurationWorkingCopy_setModulesIterationHandle()  {
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            // NOOP - just ignore
        }
    }

    Object around(): CheckstyleLogMessage_removeCheckConfigurationHandle() || 
                     CheckstyleLogMessage_refreshHandle() {
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstyleException e)
        {
            CheckstyleLog.log(e);
        }
        return result;
    }

    int around(): ConfigurationReaderHandle_getAdditionalConfigDataHandler() {
        int result = 0;
        try
        {
            result = proceed();
        }
        catch (NumberFormatException se)
        {
            // ignore
        }
        return result;
    }

    void around(String value, Module module): ConfigurationReaderHandle_startElementHandleHandle() && args(value, module) {
        try
        {
            proceed(value, module);
        }
        catch (IllegalArgumentException e)
        {
            module.setSeverity(SeverityLevel.WARNING);
        }
    }

    Object around() throws InternalError: cloneHandle()  {
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            throw new InternalError(); // this should never happen
        }
        return result;
    }

    void around() throws SAXException :RetrowException_endElementHandle(){
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            throw new SAXException(e);
        }
    }

    InputSource around() throws SAXException: 
        RetrowException_resolveEntityHandleHandle(){
        InputSource result = null;
        try
        {
            result = proceed();
        }
        catch (IOException e)
        {
            throw new SAXException("" + e, e);
        }
        return result;
    }

    void around() throws CheckstylePluginException: RetrowException_exportConfigurationHandle()
                                                    || RetrowException_setModulesHandle() {
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }

    void around() throws CheckstylePluginException: RetrowException_loadFromPersistenceHandle() {
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorLoadingConfigFile);
        }
    }

    void around() throws CheckstylePluginException: RetrowException_migrateHandle() {
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorMigratingConfig);
        }
    }

    void around() throws CheckstylePluginException: RetrowException_storeToPersistenceHandle() {
        try
        {
            proceed();
        }
        catch (CheckstyleException e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorWritingConfigFile);
        }
    }

    void around() throws CheckstylePluginException: RetrowException_writeHandle() {
        try
        {
            proceed();
        }
        catch (TransformerConfigurationException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        catch (SAXException e)
        {
            Exception ex = e.getException() != null ? e.getException() : e;
            CheckstylePluginException.rethrow(ex);
        }
    }

}
