
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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.File;
import javax.xml.transform.TransformerConfigurationException;
import java.io.InputStream;
import java.io.OutputStream;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import org.apache.commons.io.IOUtils;
import com.puppycrawl.tools.checkstyle.PropertyResolver;
import java.io.ByteArrayOutputStream;
import java.util.List;

public privileged aspect ConfigHandle
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: CheckstyleException: RetrowException_getUnresolvedPropertiesIterationHandle();

    declare soft: Exception: CheckConfigurationWorkingCopy_setLocationHandle() 
                            || RetrowException_endElementHandle()
                            || RetrowException_exportConfigurationHandle()
                            || RetrowException_loadFromPersistenceHandle()
                            || RetrowException_migrateHandle()
                            || RetrowException_storeToPersistenceHandle();

    declare soft: CoreException: CheckConfigurationWorkingCopy_setModulesIterationHandle();

    declare soft: CheckstylePluginException: CheckstyleLogMessage_refreshHandle()
                            || CheckstyleLogMessage_removeCheckConfigurationHandle()
                            || CheckConfigurationWorkingCopy_internalGetModules();

    declare soft: IOException: ConfigurationReaderHandle_startElementHandleHandle()
                            || RetrowException_resolveEntityHandleHandle()
                            || RetrowException_setModulesHandle();

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
        withincode (* CheckConfigurationWorkingCopy.internalSetModules(..)) ;

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

    pointcut RetrowException_exportConfigurationHandle(): 
        execution (* CheckConfigurationFactory.internalExportConfiguration(..)) ;

    pointcut RetrowException_loadFromPersistenceHandle(): 
        execution (* CheckConfigurationFactory.internalLoadFromPersistence(..)) ;

    pointcut RetrowException_migrateHandle(): 
        execution (* CheckConfigurationFactory.internalMigrate(..)) ;

    pointcut RetrowException_getUnresolvedPropertiesIterationHandle(): 
        execution (* CheckConfigurationTester.getUnresolvedPropertiesIteration(..));

    pointcut RetrowException_setModulesHandle(): 
        execution (* CheckConfigurationWorkingCopy.internalSetModules(..)) ;

    pointcut RetrowException_writeHandle(): 
        execution (* ConfigurationWriter.write(..)) ;

    pointcut RetrowException_storeToPersistenceHandle(): 
        execution (* GlobalCheckConfigurationWorkingSet.internalStoreToPersistence(..)) ;

    pointcut CheckConfigurationWorkingCopy_internalGetModules():
        execution(* CheckConfigurationWorkingCopy.internalGetModules(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    void around(InputStream in, Object result) throws CheckstylePluginException:
                CheckConfigurationWorkingCopy_internalGetModules() &&
                args(in, result){
        
        try
        {
            proceed(in, result);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

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
        catch (CheckstylePluginException e)
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
        catch (Exception se)
        {
            // ignore
        }
        return result;
    }

    void around(String value, Module module): 
            ConfigurationReaderHandle_startElementHandleHandle() &&
            args(value, module) {
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
        catch (CloneNotSupportedException e)
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

    InputSource around() throws SAXException: RetrowException_resolveEntityHandleHandle(){
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

    void around(ICheckConfiguration config, File file, InputStream in, OutputStream out)
        throws CheckstylePluginException: RetrowException_exportConfigurationHandle()
        && args(config, file, in, out)
    {
        try
        {
            proceed(config, file, in, out);
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e);
        }
        finally
        {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    void around(Object modules, OutputStream out, ByteArrayOutputStream byteOut)
        throws CheckstylePluginException: RetrowException_setModulesHandle() &&
        args(modules, out, byteOut){
        try
        {
            proceed(modules, out, byteOut);
        }
        catch (IOException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        finally
        {
            IOUtils.closeQuietly(byteOut);
            IOUtils.closeQuietly(out);
        }

    }

    void around(InputStream inStream) throws CheckstylePluginException: RetrowException_loadFromPersistenceHandle() 
        && args(inStream){
        try
        {
            proceed(inStream);
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorLoadingConfigFile);
        }
        finally
        {
            IOUtils.closeQuietly(inStream);
        }
    }

    void around(InputStream inStream, InputStream defaultConfigStream) throws CheckstylePluginException: 
        RetrowException_migrateHandle()  && 
        args(inStream, defaultConfigStream){
        try
        {
            proceed(inStream, defaultConfigStream);
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorMigratingConfig);
        }
        finally
        {
            IOUtils.closeQuietly(inStream);
            IOUtils.closeQuietly(defaultConfigStream);
        }
    }

    void around(CheckstyleConfigurationFile configFile, PropertyResolver resolver,
            ClassLoader contextClassloader, InputStream in) throws CheckstylePluginException:
            RetrowException_getUnresolvedPropertiesIterationHandle() &&
            args(configFile, resolver, contextClassloader, in)
    {
        try
        {
            proceed(configFile, resolver, contextClassloader, in);
        }
        catch (CheckstyleException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        finally
        {
            IOUtils.closeQuietly(in);
            // restore the original classloader
            Thread.currentThread().setContextClassLoader(contextClassloader);
        }

    }

    void around(BufferedOutputStream out, ByteArrayOutputStream byteOut)
        throws CheckstylePluginException: RetrowException_storeToPersistenceHandle() &&
        args( out,  byteOut){
        try
        {
            proceed(out, byteOut);
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e, ErrorMessages.errorWritingConfigFile);
        }
        finally
        {
            IOUtils.closeQuietly(byteOut);
            IOUtils.closeQuietly(out);
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
