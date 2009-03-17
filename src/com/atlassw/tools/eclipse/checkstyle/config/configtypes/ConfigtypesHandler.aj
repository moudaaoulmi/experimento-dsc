package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;

import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.GlobalCheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.LocalCheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckstyleConfigurationFile;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;

import java.lang.IllegalArgumentException;

import java.net.MalformedURLException;
import java.net.Authenticator;
import java.net.UnknownHostException;

import java.util.MissingResourceException;
import java.net.Authenticator;

public privileged aspect ConfigtypesHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    
    declare soft: IOException : ConfigurationType_internalGetAdditionPropertiesBundleBytesHandler() ||
                                RemoteConfigurationType_internalGetCheckstyleConfigurationHandler() ||
                                RemoteConfigurationType_secInternalGetCheckstyleConfigurationHandler() ||
                                RemoteConfigurationType_internalGetBytesFromCacheBundleFileHandler() ||
                                RemoteConfigurationType_oneWriteToCacheFileHandler() ||
                                RemoteConfigurationType_twoWriteToCacheFile();

    declare soft: Exception : RemoteConfigurationType_internalGetBytesFromURLConnectionHandler();
    
    declare soft: CheckstylePluginException: ExternalFileConfiguration_internalGetEditedWorkingCopyHandler() ||
                                             ExternalFileConfiguration_isConfigurableHandler() ||
                                             ExternalFileConfiguration_internalResolveLocationHandler() ||
                                             InternalConfigurationEditor_widgetSelectedHandler() ||
                                             InternalConfigurationEditor_internalGetEditedWorkingCopyHandler2();

    declare soft: CheckstyleException: ExternalFileConfiguration_resolveDynamicLocationHandler();

    declare soft: CheckstylePluginException: ProjectConfigurationEditor_internalGetEditedWorkingCopyHandler() ||
                                             ProjectConfigurationType_internalIsConfigurableHandler() ||
                                             RemoteConfigurationEditor_internalCreateEditorControlHandler();

    declare soft: CoreException: RemoteConfigurationType_removeCachedAuthInfoHandler();

    declare soft: MalformedURLException: RemoteConfigurationEditor_getEditedWorkingCopyHandler();

    declare soft: UnknownHostException: RemoteConfigurationType_internalGetCheckstyleConfigurationHandler();

    declare soft: FileNotFoundException: RemoteConfigurationType_internalGetCheckstyleConfigurationHandler();

    declare soft: IllegalAccessException: RemoteConfigurationType_internalGetDefaultHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    
    pointcut ConfigurationType_internalGetAdditionPropertiesBundleBytesHandler():
            execution (* ConfigurationType.internalGetAdditionPropertiesBundleBytes(..)) &&
            within (ConfigurationType);

    pointcut ConfigurationType_internalGetBytesFromURLConnectionHandler():
        execution (* ConfigurationType.internalGetBytesFromURLConnection(..));
    
    pointcut ExternalFileConfiguration_internalGetEditedWorkingCopyHandler():
        execution(* ExternalFileConfigurationEditor.internalGetEditedWorkingCopy(..));

    pointcut ExternalFileConfiguration_resolveDynamicLocationHandler(): 
        execution(* ExternalFileConfigurationType.resolveDynamicLocation(..));

    pointcut ExternalFileConfiguration_isConfigurableHandler():
        execution(* ExternalFileConfigurationType.internalIsConfigurable(..));

    pointcut ExternalFileConfiguration_internalResolveLocationHandler():
        execution(* ExternalFileConfigurationType.internalResolveLocation(..));

    pointcut InternalConfigurationEditor_widgetSelectedHandler():
        execution(* InternalConfigurationEditor.internalWidgetSelected(..));

    pointcut InternalConfigurationEditor_internalGetEditedWorkingCopyHandler2():
        execution(* InternalConfigurationEditor.internalGetEditedWorkingCopy(..));

    pointcut ProjectConfigurationEditor_internalGetEditedWorkingCopyHandler():
        execution(* ProjectConfigurationEditor.internalGetEditedWorkingCopy(..));

    pointcut ProjectConfigurationEditor_secInternalEnsureFileExistsHandler():
        execution(* ProjectConfigurationEditor.secInternalEnsureFileExists(..));
   
    pointcut ProjectConfigurationType_internalIsConfigurableHandler():
        execution(* ProjectConfigurationType.internalIsConfigurable(..));
   
    pointcut RemoteConfigurationEditor_internalCreateEditorControlHandler():
        execution(* RemoteConfigurationEditor.internalCreateEditorControl(..));

    pointcut RemoteConfigurationEditor_getEditedWorkingCopyHandler():
        execution(* RemoteConfigurationEditor.getEditedWorkingCopy(..));

    pointcut RemoteConfigurationType_internalGetCheckstyleConfigurationHandler():
        execution(* RemoteConfigurationType.internalGetCheckstyleConfiguration(..));

    pointcut RemoteConfigurationType_secInternalGetCheckstyleConfigurationHandler():
        execution(* RemoteConfigurationType.secInternalGetCheckstyleConfiguration(..));

    pointcut RemoteConfigurationType_internalGetBytesFromCacheBundleFileHandler():
        execution(* RemoteConfigurationType.getBytesFromCacheBundleFile(..));

    pointcut RemoteConfigurationType_oneWriteToCacheFileHandler():
        execution(* RemoteConfigurationType.oneWriteToCacheFile(..));

    pointcut RemoteConfigurationType_twoWriteToCacheFile():
        execution(* RemoteConfigurationType.twoWriteToCacheFile(..));

    pointcut RemoteConfigurationType_removeCachedAuthInfoHandler():
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.removeCachedAuthInfo(..));

    pointcut RemoteConfigurationType_internalGetDefaultHandler() : 
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.getDefault(..));

    pointcut RemoteConfigurationType_internalGetBytesFromURLConnectionHandler():
        execution(* RemoteConfigurationType.internalGetBytesFromURLConnection(..));


    pointcut ResourceBundlePropertyResolver_resolveHandle(): 
        execution(* ResourceBundlePropertyResolver.resolve(..));
    

    // ---------------------------
    // Advice's
    // ---------------------------
    
    Object around() throws CheckstylePluginException: 
        RemoteConfigurationEditor_getEditedWorkingCopyHandler() ||
        ExternalFileConfiguration_resolveDynamicLocationHandler(){
        
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (MalformedURLException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        catch(CheckstyleException e){
            CheckstylePluginException.rethrow(e);   
        }
        return result;
    }
    

    void around() throws CheckstylePluginException: ExternalFileConfiguration_internalGetEditedWorkingCopyHandler()
    {
        ExternalFileConfigurationEditor eFC = (ExternalFileConfigurationEditor) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            String location = eFC.mLocation.getText();

            if (StringUtils.trimToNull(location) != null && eFC.ensureFileExists(location))
            {
                eFC.mWorkingCopy.setLocation(location);
            }
            else
            {
                throw e;
            }
        }
    }
    
    void around(String location) throws CheckstylePluginException: 
        InternalConfigurationEditor_internalGetEditedWorkingCopyHandler2() && args(location){

        InternalConfigurationEditor iCE = (InternalConfigurationEditor) thisJoinPoint.getThis();
        try
        {
            proceed(location);
        }
        catch (CheckstylePluginException e)
        {
            if (StringUtils.trimToNull(location) != null && iCE.ensureFileExists(location))
            {
                iCE.mWorkingCopy.setLocation(location);
            }
            else
            {
                throw e;
            }
        }
    }
    
    void around() throws CheckstylePluginException :
        ProjectConfigurationEditor_secInternalEnsureFileExistsHandler(){
        try
        {
            proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }
        
    void around(URLConnection connection, InputStream in, byte[] configurationFileData):
            ConfigurationType_internalGetBytesFromURLConnectionHandler() &&
            args(connection,in,configurationFileData)
            {
        try
        {
            proceed(connection, in, configurationFileData);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    void around() throws CheckstylePluginException : RemoteConfigurationType_removeCachedAuthInfoHandler(){
        try
        {
            proceed();

        }
        catch (CoreException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }

    void around(ICheckConfiguration checkConfiguration, boolean useCacheFile,
            CheckstyleConfigurationFile data, String currentRedirects,
            Authenticator oldAuthenticator) throws CheckstylePluginException: 
            RemoteConfigurationType_internalGetCheckstyleConfigurationHandler()
            && args(checkConfiguration, useCacheFile, data, currentRedirects, oldAuthenticator){
        try
        {
            proceed(checkConfiguration, useCacheFile, data, currentRedirects, oldAuthenticator);
        }
        catch (UnknownHostException e)
        {
            CheckstylePluginException.rethrow(e, NLS.bind(
                    ErrorMessages.RemoteConfigurationType_errorUnknownHost, e.getMessage()));
        }
        catch (FileNotFoundException e)
        {
            CheckstylePluginException.rethrow(e, NLS.bind(
                    ErrorMessages.RemoteConfigurationType_errorFileNotFound, e.getMessage()));
        }
        catch (IOException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        finally
        {
            Authenticator.setDefault(oldAuthenticator);

            if (currentRedirects != null)
            {
                System.setProperty("http.maxRedirects", currentRedirects); //$NON-NLS-1$
            }
            else
            {
                System.getProperties().remove("http.maxRedirects"); //$NON-NLS-1$
            }
        }
    }

    Object around(Object checkConfiguration, boolean isConfigurable): 
        (ProjectConfigurationType_internalIsConfigurableHandler() 
        || ExternalFileConfiguration_isConfigurableHandler()) && args(checkConfiguration, isConfigurable){
        Object result = null;
        try
        {
            result = proceed(checkConfiguration, isConfigurable);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
            isConfigurable = false;
        }
        return result;
    }
    
    void around(): InternalConfigurationEditor_widgetSelectedHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException ex)
        {
            InternalConfigurationEditor icE = (InternalConfigurationEditor) thisJoinPoint.getThis();
            icE.mDialog.setErrorMessage(ex.getLocalizedMessage());
        }
    }
    
    void around() throws CheckstylePluginException : 
        ProjectConfigurationEditor_internalGetEditedWorkingCopyHandler(){
        ProjectConfigurationEditor pCE = (ProjectConfigurationEditor) thisJoinPoint.getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            String location = pCE.mLocation.getText();

            if (StringUtils.trimToNull(location) == null)
            {
                throw e;
            }

            ICheckConfigurationWorkingSet ws = pCE.mCheckConfigDialog
                    .getCheckConfigurationWorkingSet();
            IPath tmp = new Path(location);
            boolean isFirstPartProject = ResourcesPlugin.getWorkspace().getRoot().getProject(
                    tmp.segment(0)).exists();

            if (ws instanceof LocalCheckConfigurationWorkingSet && !isFirstPartProject)
            {
                location = ((LocalCheckConfigurationWorkingSet) ws).getProject().getFullPath()
                        .append(location).toString();
                pCE.mLocation.setText(location);
            }
            else if (ws instanceof GlobalCheckConfigurationWorkingSet && !isFirstPartProject)
            {
                throw new CheckstylePluginException(NLS
                        .bind(Messages.ProjectConfigurationEditor_msgNoProjectInWorkspace, tmp
                                .segment(0)));
            }

            if (pCE.ensureFileExists(location))
            {
                pCE.mWorkingCopy.setLocation(pCE.mLocation.getText());
            }
            else
            {
                throw e;
            }
        }
    }
    
    
    void around() throws IOException: 
        ExternalFileConfiguration_internalResolveLocationHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
            throw new IOException(e.getMessage());
        }
    }

    void around(Shell shell): RemoteConfigurationEditor_internalCreateEditorControlHandler() && args(shell){
        try
        {
            proceed(shell);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(shell, e, true);
        }
    }
  

    void around(CheckstyleConfigurationFile data, boolean useCacheFile,
            boolean originalFileSuccess, byte[] configurationFileData,
            ICheckConfiguration checkConfiguration) throws IOException: 
            RemoteConfigurationType_secInternalGetCheckstyleConfigurationHandler() &&
            args (data, useCacheFile, originalFileSuccess, configurationFileData, checkConfiguration)
        {

        RemoteConfigurationType rCT = (RemoteConfigurationType) thisJoinPoint.getThis();
        try
        {
            proceed(data, useCacheFile, originalFileSuccess, configurationFileData,
                    checkConfiguration);
        }
        catch (IOException e)
        {
            if (useCacheFile)
            {
                configurationFileData = rCT.getGetBytesFromCacheFile(checkConfiguration);
            }
            else
            {
                throw e;
            }
        }
    }

    Object around(): RemoteConfigurationType_internalGetBytesFromCacheBundleFileHandler() ||
                     ConfigurationType_internalGetAdditionPropertiesBundleBytesHandler() ||
                     RemoteConfigurationType_twoWriteToCacheFile(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (IOException e)
        {
            // we won't load the bundle then
            // disabled logging bug #1647602
            // CheckstyleLog.log(ioe);
            // IF RemoteConfigurationType_twoWriteToCacheFile() THEN 
            // ignore this since there simply might be no properties file
        }
        return result;
    }
    
    void around(File cacheFile, byte[] configFileBytes, ICheckConfiguration checkConfig): 
        RemoteConfigurationType_oneWriteToCacheFileHandler() && args (cacheFile, configFileBytes, checkConfig){
        try
        {
            proceed(cacheFile, configFileBytes, checkConfig);
        }
        catch (IOException e)
        {
            CheckstyleLog.log(e, NLS.bind(
                    ErrorMessages.RemoteConfigurationType_msgRemoteCachingFailed, checkConfig
                            .getName(), checkConfig.getLocation()));
        }
    }


    void around(URLConnection connection, byte[] configurationFileData, InputStream in)
        throws IOException: RemoteConfigurationType_internalGetBytesFromURLConnectionHandler() &&
        args(connection, configurationFileData, in){
        try
        {
            proceed(connection, configurationFileData, in);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    Authenticator around(): RemoteConfigurationType_internalGetDefaultHandler(){
        Authenticator currentDefault = null;
        try
        {
            currentDefault = proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstyleLog.log(e);
        }
        catch (IllegalAccessException e)
        {
            CheckstyleLog.log(e);
        }
        return currentDefault;
    }

    String around():ResourceBundlePropertyResolver_resolveHandle(){
        String result = null;
        try
        {
            result = proceed();
        }
        catch (MissingResourceException e)
        {
            // ignore
        }
        return result;
    }
}