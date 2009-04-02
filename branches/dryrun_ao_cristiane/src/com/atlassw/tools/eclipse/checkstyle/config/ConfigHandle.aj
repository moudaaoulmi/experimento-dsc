
package com.atlassw.tools.eclipse.checkstyle.config;

import org.eclipse.osgi.util.NLS;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;

@ExceptionHandler
public privileged aspect ConfigHandle
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: Exception: CheckConfigurationWorkingCopy_setLocationHandle() 
                            || RetrowException_loadFromPersistenceHandle()
                            || RetrowException_migrateHandle()
                            || RetrowException_storeToPersistenceHandle();

    declare soft: CoreException: CheckConfigurationWorkingCopy_setModulesIterationHandle();

    declare soft: CheckstylePluginException: CheckstyleLogMessage_removeCheckConfigurationHandle()
                            || CheckConfigurationWorkingCopy_internalGetModules();

    declare soft: IOException: ConfigurationReaderHandle_startElementHandleHandle();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut CheckConfigurationWorkingCopy_setLocationHandle():
        execution (* CheckConfigurationWorkingCopy.setLocationHandle(..)) ;

    pointcut CheckConfigurationWorkingCopy_setModulesIterationHandle(): 
        call (* IResource.refreshLocal(..)) &&
        withincode (* CheckConfigurationWorkingCopy.internalSetModules(..)) ;


    pointcut CheckstyleLogMessage_removeCheckConfigurationHandle():
        call(* ProjectConfigurationFactory.isCheckConfigInUse(..)) &&
        withincode(* GlobalCheckConfigurationWorkingSet.removeCheckConfiguration(..)) ;

    pointcut ConfigurationReaderHandle_getAdditionalConfigDataHandler(): 
        execution (int ConfigurationReader.internalGetAdditionalConfigData(String,int)) ;

    pointcut ConfigurationReaderHandle_startElementHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.startElementHandle(..)) ;

    pointcut RetrowException_loadFromPersistenceHandle(): 
        execution (* CheckConfigurationFactory.internalLoadFromPersistence(..)) ;

    pointcut RetrowException_migrateHandle(): 
        execution (* CheckConfigurationFactory.internalMigrate(..)) ;

    pointcut RetrowException_storeToPersistenceHandle(): 
        execution (* GlobalCheckConfigurationWorkingSet.internalStoreToPersistence(..)) ;

    pointcut CheckConfigurationWorkingCopy_internalGetModules():
        execution(* CheckConfigurationWorkingCopy.internalGetModules(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    Object around(InputStream in, Object result) throws CheckstylePluginException:
                CheckConfigurationWorkingCopy_internalGetModules() &&
                args(in, result){
        try
        {
            result = proceed(in, result);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return result;
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

    // esses dois nao podem ser reusados pq o com o retorno boolean está
    // utilizando valores iniciais diferentes do valor default.
    boolean around(): CheckstyleLogMessage_removeCheckConfigurationHandle()
        {
        boolean result = true;
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

    int around(String tabWidthProp, int tabWidth): 
            ConfigurationReaderHandle_getAdditionalConfigDataHandler() &&
            args(tabWidthProp, tabWidth){
        try
        {
            tabWidth = proceed(tabWidthProp, tabWidth);
        }
        catch (Exception se)
        {
            // ignore
        }
        return tabWidth;
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

    void around(InputStream inStream) throws CheckstylePluginException: 
        RetrowException_loadFromPersistenceHandle() 
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

    void around(InputStream inStream, InputStream defaultConfigStream)
        throws CheckstylePluginException: 
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
}
