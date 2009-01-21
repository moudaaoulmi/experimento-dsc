
package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.MissingResourceException;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckstyleConfigurationFile;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public privileged aspect RemoteConfigurationTypeHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft: UnknownHostException: internalGetCheckstyleConfigurationHandler();

    declare soft: FileNotFoundException: internalGetCheckstyleConfigurationHandler();

    declare soft: IOException: internalGetCheckstyleConfigurationHandler() ||
                    secInternalGetCheckstyleConfigurationHandler() ||
                    internalGetBytesFromCacheBundleFileHandler() ||
                    oneWriteToCacheFileHandler() ||
                    twoWriteToCacheFile();

    declare soft: CheckstylePluginException: secInternalGetBytesFromURLConnectionHandler();

    declare soft: Exception: internalGetBytesFromURLConnectionHandler();

    declare soft: CoreException: storeCredentialsHandler() ||
                    removeCachedAuthInfoHandler();

    declare soft: IllegalAccessException: internalGetDefaultHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------

    pointcut internalGetCheckstyleConfigurationHandler():
        execution(* RemoteConfigurationType.internalGetCheckstyleConfiguration(..));

    pointcut secInternalGetCheckstyleConfigurationHandler():
        execution(* RemoteConfigurationType.secInternalGetCheckstyleConfiguration(..));

    pointcut internalGetBytesFromCacheBundleFileHandler():
        execution(* RemoteConfigurationType.internalGetBytesFromCacheBundleFile(..));

    pointcut oneWriteToCacheFileHandler():
        execution(* RemoteConfigurationType.oneWriteToCacheFile(..));

    pointcut twoWriteToCacheFile():
        execution(* RemoteConfigurationType.twoWriteToCacheFile(..));

    pointcut storeCredentialsHandler():
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.storeCredentials(..));

    pointcut removeCachedAuthInfoHandler():
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.removeCachedAuthInfo(..));

    pointcut internalGetDefaultHandler() : 
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.internalGetDefault(..));

    pointcut internalGetBytesFromURLConnectionHandler():
        execution(* RemoteConfigurationType.internalGetBytesFromURLConnection(..));

    pointcut secInternalGetBytesFromURLConnectionHandler(): 
        execution(* RemoteConfigurationType.secInternalGetBytesFromURLConnection(..));

    pointcut internalResolveHandle(): 
        execution(* ResourceBundlePropertyResolver.internalResolve(..));

    // ---------------------------
    // Advice's
    // ---------------------------

    void around(ICheckConfiguration checkConfiguration, boolean useCacheFile,
            CheckstyleConfigurationFile data, String currentRedirects,
            Authenticator oldAuthenticator) throws CheckstylePluginException: 
            internalGetCheckstyleConfigurationHandler()
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

    void around(CheckstyleConfigurationFile data, boolean useCacheFile,
            boolean originalFileSuccess, byte[] configurationFileData,
            ICheckConfiguration checkConfiguration) throws IOException: secInternalGetCheckstyleConfigurationHandler() &&
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

    byte[] around():internalGetBytesFromCacheBundleFileHandler(){
        byte[] result = null;
        try
        {
            result = proceed();
        }
        catch (IOException e)
        {
            // we won't load the bundle then
            // disabled logging bug #1647602
            // CheckstyleLog.log(ioe);
        }
        return result;
    }

    void around(File cacheFile, byte[] configFileBytes, ICheckConfiguration checkConfig): 
        oneWriteToCacheFileHandler() && args (cacheFile, configFileBytes, checkConfig){
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

    void around(): twoWriteToCacheFile(){
        try
        {
            proceed();
        }
        catch (IOException e)
        {
            // ignore this since there simply might be no properties file
        }
    }

    void around(): secInternalGetBytesFromURLConnectionHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
        }
    }

    void around(URLConnection connection, byte[] configurationFileData, InputStream in)
        throws IOException: internalGetBytesFromURLConnectionHandler() &&
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

    void around(): storeCredentialsHandler(){
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            CheckstyleLog.log(e);
        }
    }

    void around() throws CheckstylePluginException : removeCachedAuthInfoHandler(){
        try
        {
            proceed();

        }
        catch (CoreException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }

    void around(): internalGetDefaultHandler(){

        try
        {
            proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstyleLog.log(e);
        }
        catch (IllegalAccessException e)
        {
            CheckstyleLog.log(e);
        }

    }

    String around():internalResolveHandle(){
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
