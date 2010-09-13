
package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import org.apache.commons.io.FileUtils;
import java.net.URLConnection;
import org.eclipse.core.resources.IFile;
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

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import java.net.URL;
import java.lang.IllegalArgumentException;

import java.net.MalformedURLException;

import java.util.HashMap;
import java.util.Map;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;

//@ExceptionHandler
public privileged aspect ConfigtypesExceptionHandler
{
    Map inputStream = new HashMap();
    
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: IOException : ConfigurationType_internalGetAdditionPropertiesBundleBytesHandler() ||
                                RemoteConfigurationType_internalGetBytesFromCacheBundleFileHandler() ||
                                RemoteConfigurationType_oneWriteToCacheFileHandler() ||
                                RemoteConfigurationType_twoWriteToCacheFile();

    declare soft: Exception : RemoteConfigurationType_internalGetBytesFromURLConnectionHandler();

    declare soft: CheckstylePluginException: ExternalFileConfiguration_getEditedWorkingCopyHandler() ||
                                             ExternalFileConfiguration_isConfigurableHandler() ||
                                             ExternalFileConfiguration_internalResolveLocationHandler() ||
                                             InternalConfigurationEditor_widgetSelectedHandler() ||
                                             InternalConfigurationEditor_internalGetEditedWorkingCopyHandler2();

    declare soft: CheckstyleException: ExternalFileConfiguration_resolveDynamicLocationHandler();

    declare soft: CheckstylePluginException: ProjectConfigurationEditor_internalGetEditedWorkingCopyHandler() ||
                                             ProjectConfigurationType_internalIsConfigurableHandler() ||
                                             RemoteConfigurationEditor_internalCreateEditorControlHandler();

    declare soft: MalformedURLException: RemoteConfigurationEditor_getEditedWorkingCopyHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut ConfigurationType_internalGetAdditionPropertiesBundleBytesHandler():
            execution (* ConfigurationType.getAdditionPropertiesBundleBytes(..)) &&
            within (ConfigurationType);

    pointcut ConfigurationType_internalGetBytesFromURLConnectionHandler():
        execution (* ConfigurationType.internalGetBytesFromURLConnection(..));

    pointcut ExternalFileConfiguration_getEditedWorkingCopyHandler():
        call (* CheckConfigurationWorkingCopy.setLocation(..)) &&
        withincode(* ExternalFileConfigurationEditor.getEditedWorkingCopy(..));

    pointcut ExternalFileConfiguration_resolveDynamicLocationHandler(): 
        execution(* ExternalFileConfigurationType.resolveDynamicLocation(..));

    pointcut ExternalFileConfiguration_isConfigurableHandler():
        execution(* ExternalFileConfigurationType.internalIsConfigurable(..));

    pointcut ExternalFileConfiguration_internalResolveLocationHandler():
        /*call(* ExternalFileConfigurationType.resolveDynamicLocation(..)) &&
        withincode(* ExternalFileConfigurationType.resolveLocation(..));*/
        execution(* ExternalFileConfigurationType.resolveLocation(..));

    pointcut InternalConfigurationEditor_widgetSelectedHandler():
        execution(* InternalConfigurationEditor.SelectionListenerImplementation.widgetSelected(..));

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

    pointcut RemoteConfigurationType_internalGetBytesFromCacheBundleFileHandler():
        execution(* RemoteConfigurationType.getBytesFromCacheBundleFile(..));

    pointcut RemoteConfigurationType_oneWriteToCacheFileHandler():
        execution(* RemoteConfigurationType.oneWriteToCacheFile(..));

    pointcut RemoteConfigurationType_twoWriteToCacheFile():
        call(* FileUtils.writeByteArrayToFile(..)) &&
        withincode(* RemoteConfigurationType.writeToCacheFile(..));

     pointcut RemoteConfigurationType_internalGetBytesFromURLConnectionHandler():
        execution(* RemoteConfigurationType.internalGetBytesFromURLConnection(..));

     pointcut RemoteConfigurationType_getInputStreamHandler() : 
         call(InputStream URLConnection.getInputStream()) && 
         (withincode(private byte[] RemoteConfigurationType.internalGetBytesFromURLConnection(..)));
     
    // ---------------------------
    // Advice's
    // ---------------------------
    String around(String location) throws CheckstylePluginException: 
            ExternalFileConfiguration_resolveDynamicLocationHandler() &&
            args(location){
        String result = location;
        try
        {
            result = proceed(location);
        }
        catch (CheckstyleException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        return result;
    }

    CheckConfigurationWorkingCopy around() throws CheckstylePluginException: 
        RemoteConfigurationEditor_getEditedWorkingCopyHandler(){
        CheckConfigurationWorkingCopy result = null;
        try
        {
            result = proceed();
        }
        catch (MalformedURLException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        return result;
    }

    void around() throws CheckstylePluginException: 
        ExternalFileConfiguration_getEditedWorkingCopyHandler()
    {
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            ExternalFileConfigurationEditor eFC = (ExternalFileConfigurationEditor) thisJoinPoint
                    .getThis();
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
        InternalConfigurationEditor_internalGetEditedWorkingCopyHandler2() && 
        args(location){
        try
        {
            proceed(location);
        }
        catch (CheckstylePluginException e)
        {
            InternalConfigurationEditor iCE = (InternalConfigurationEditor) thisJoinPoint.getThis();
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

    IFile around() throws CheckstylePluginException :
        ProjectConfigurationEditor_secInternalEnsureFileExistsHandler(){
        IFile result = null;
        try
        {
            result = proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        return result;
    }

    byte[] around(URLConnection connection, InputStream in, byte[] configurationFileData):
            (ConfigurationType_internalGetBytesFromURLConnectionHandler() ||
            RemoteConfigurationType_internalGetBytesFromURLConnectionHandler()) &&
            args(connection, in, configurationFileData)
            {
        byte[] result = null;
        try
        {
            result = proceed(connection, in, configurationFileData);
        }
        finally
        {
            IOUtils.closeQuietly((InputStream)inputStream.get(Thread.currentThread().getName()));
        }
        return result;
    }
    
    InputStream around(): RemoteConfigurationType_getInputStreamHandler(){
        InputStream in = proceed();
        inputStream.put(Thread.currentThread().getName(), in);
        return in;
    }

    Object around(Object checkConfiguration, boolean isConfigurable): 
        (ProjectConfigurationType_internalIsConfigurableHandler() ||
         ExternalFileConfiguration_isConfigurableHandler()) && 
         args(checkConfiguration, isConfigurable){
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
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {  
            ProjectConfigurationEditor pCE = (ProjectConfigurationEditor) thisJoinPoint.getThis();
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

    /* String */URL around() throws IOException: 
        ExternalFileConfiguration_internalResolveLocationHandler(){
        try
        {
            return proceed();
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
        RemoteConfigurationType_oneWriteToCacheFileHandler() && 
        args (cacheFile, configFileBytes, checkConfig){
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

}
