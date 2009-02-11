//============================================================================
//
// Copyright (C) 2002-2007  David Schneider, Lars Ködderitzsch
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//============================================================================

package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckstyleConfigurationFile;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.PropertyResolver;

/**
 * Implementation of a check configuration that uses an exteral checkstyle
 * configuration file.
 * 
 * @author Lars Ködderitzsch
 */
public class RemoteConfigurationType extends ConfigurationType
{

    /** Key to access the information if the configuration should be cached. */
    public static final String KEY_CACHE_CONFIG = "cache-file"; //$NON-NLS-1$

    /** Key to access the path of the cached configuration file. */
    public static final String KEY_CACHE_FILE_LOCATION = "cache-file-location"; //$NON-NLS-1$

    /** Key to access the path of the cached property file. */
    public static final String KEY_CACHE_PROPS_FILE_LOCATION = "cache-props-file-location"; //$NON-NLS-1$

    /** Key to access the username. */
    public static final String KEY_USERNAME = "username"; //$NON-NLS-1$

    /** Key to access the password. */
    public static final String KEY_PASSWORD = "password"; //$NON-NLS-1$

    private static Set sFailedWith401URLs = new HashSet();

    /**
     * {@inheritDoc}
     */
    public CheckstyleConfigurationFile getCheckstyleConfiguration(
            ICheckConfiguration checkConfiguration) throws CheckstylePluginException
    {

        boolean useCacheFile = Boolean.valueOf(
                (String) checkConfiguration.getAdditionalData().get(KEY_CACHE_CONFIG))
                .booleanValue();

        CheckstyleConfigurationFile data = new CheckstyleConfigurationFile();

        synchronized(Authenticator.class)
        {

            String currentRedirects = System.getProperty("http.maxRedirects"); //$NON-NLS-1$

            Authenticator oldAuthenticator = RemoteConfigAuthenticator.getDefault();
            internalGetCheckstyleConfiguration(checkConfiguration, useCacheFile, data,
                    currentRedirects, oldAuthenticator);

        }
        return data;
    }

    private void internalGetCheckstyleConfiguration(ICheckConfiguration checkConfiguration,
            boolean useCacheFile, CheckstyleConfigurationFile data, String currentRedirects,
            Authenticator oldAuthenticator) throws CheckstylePluginException
    {

        // resolve the true configuration file URL
        data.setResolvedConfigFileURL(resolveLocation(checkConfiguration));

        Authenticator.setDefault(new RemoteConfigAuthenticator(data.getResolvedConfigFileURL()));

        boolean originalFileSuccess = false;
        byte[] configurationFileData = null;

        secInternalGetCheckstyleConfiguration(data, useCacheFile, originalFileSuccess,
                configurationFileData, checkConfiguration);

        data.setCheckConfigFileBytes(configurationFileData);

        // get the properties bundle
        byte[] additionalPropertiesBytes = null;
        if (originalFileSuccess)
        {
            additionalPropertiesBytes = getAdditionPropertiesBundleBytes(data
                    .getResolvedConfigFileURL());
        }
        else if (useCacheFile)
        {
            additionalPropertiesBytes = getBytesFromCacheBundleFile(checkConfiguration);
        }

        data.setAdditionalPropertyBundleBytes(additionalPropertiesBytes);

        // get the property resolver
        PropertyResolver resolver = getPropertyResolver(checkConfiguration, data);
        data.setPropertyResolver(resolver);

        // write to cache file
        if (originalFileSuccess && useCacheFile)
        {
            writeToCacheFile(checkConfiguration, configurationFileData, additionalPropertiesBytes);
        }

    }

    private void secInternalGetCheckstyleConfiguration(CheckstyleConfigurationFile data,
            boolean useCacheFile, boolean originalFileSuccess, byte[] configurationFileData,
            ICheckConfiguration checkConfiguration) throws IOException
    {
        System.setProperty("http.maxRedirects", "3"); //$NON-NLS-1$ //$NON-NLS-2$

        URLConnection connection = data.getResolvedConfigFileURL().openConnection();

        // get the configuration file data
        configurationFileData = getBytesFromURLConnection(connection);

        // get last modification timestamp
        data.setModificationStamp(connection.getLastModified());

        originalFileSuccess = true;

    }

    /**
     * {@inheritDoc}
     */
    protected URL resolveLocation(ICheckConfiguration checkConfiguration) throws IOException
    {
        return new URL(checkConfiguration.getLocation());
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCheckConfigRemoved(ICheckConfiguration checkConfiguration)
        throws CheckstylePluginException
    {
        super.notifyCheckConfigRemoved(checkConfiguration);

        // remove authentication info
        RemoteConfigAuthenticator.removeCachedAuthInfo(checkConfiguration
                .getResolvedConfigurationFileURL());

        boolean useCacheFile = Boolean.valueOf(
                (String) checkConfiguration.getAdditionalData().get(KEY_CACHE_CONFIG))
                .booleanValue();

        if (useCacheFile)
        {
            // remove the cached configuration file from the workspace metadata
            String cacheFileLocation = (String) checkConfiguration.getAdditionalData().get(
                    KEY_CACHE_FILE_LOCATION);

            IPath cacheFilePath = CheckstylePlugin.getDefault().getStateLocation();
            cacheFilePath = cacheFilePath.append(cacheFileLocation);
            File cacheFile = cacheFilePath.toFile();
            cacheFile.delete();
        }
    }

    /**
     * Method to get an input stream to the cached configuration file.
     * 
     * @param checkConfig the check configuration
     * @return the input stream
     * @throws IOException error getting the stream (file does not exist)
     */
    private byte[] getBytesFromCacheFile(ICheckConfiguration checkConfig) throws IOException
    {
        String cacheFileLocation = (String) checkConfig.getAdditionalData().get(
                KEY_CACHE_FILE_LOCATION);

        IPath cacheFilePath = CheckstylePlugin.getDefault().getStateLocation();
        cacheFilePath = cacheFilePath.append(cacheFileLocation);
        File cacheFile = cacheFilePath.toFile();

        URL configURL = cacheFile.toURL();
        URLConnection connection = configURL.openConnection();

        return getBytesFromURLConnection(connection);
    }

    public byte[] getGetBytesFromCacheFile(ICheckConfiguration checkConfig) throws IOException
    {
        return getBytesFromCacheFile(checkConfig);
    }

    /**
     * Method to get an input stream to the cached bundle file.
     * 
     * @param checkConfig the check configuration
     * @return the input stream
     * @throws IOException error getting the stream (file does not exist)
     */
    private byte[] getBytesFromCacheBundleFile(ICheckConfiguration checkConfig)
    {
        String cacheFileLocation = (String) checkConfig.getAdditionalData().get(
                KEY_CACHE_PROPS_FILE_LOCATION);

        // bug 1748626
        if (cacheFileLocation == null)
        {
            return null;
        }

        byte[] result = internalGetBytesFromCacheBundleFile(cacheFileLocation);
        return result;
    }

    private byte[] internalGetBytesFromCacheBundleFile(String cacheFileLocation)
    {

        IPath cacheFilePath = CheckstylePlugin.getDefault().getStateLocation();
        cacheFilePath = cacheFilePath.append(cacheFileLocation);
        File cacheFile = cacheFilePath.toFile();

        URL configURL = cacheFile.toURL();
        URLConnection connection = configURL.openConnection();

        return getBytesFromURLConnection(connection);

    }

    private void writeToCacheFile(ICheckConfiguration checkConfig, byte[] configFileBytes,
            byte[] bundleBytes)
    {
        String cacheFileLocation = (String) checkConfig.getAdditionalData().get(
                KEY_CACHE_FILE_LOCATION);

        IPath cacheFilePath = CheckstylePlugin.getDefault().getStateLocation();
        cacheFilePath = cacheFilePath.append(cacheFileLocation);
        File cacheFile = cacheFilePath.toFile();

        oneWriteToCacheFile(cacheFile, configFileBytes, checkConfig);

        if (bundleBytes != null)
        {

            String propsCacheFileLocation = (String) checkConfig.getAdditionalData().get(
                    KEY_CACHE_PROPS_FILE_LOCATION);

            IPath propsCacheFilePath = CheckstylePlugin.getDefault().getStateLocation();
            propsCacheFilePath = propsCacheFilePath.append(propsCacheFileLocation);
            File propsCacheFile = propsCacheFilePath.toFile();

            twoWriteToCacheFile(propsCacheFile, bundleBytes);
        }
    }

    private void oneWriteToCacheFile(File cacheFile, byte[] configFileBytes,
            ICheckConfiguration checkConfig)
    {
        FileUtils.writeByteArrayToFile(cacheFile, configFileBytes);
    }

    private void twoWriteToCacheFile(File propsCacheFile, byte[] bundleBytes)
    {
        FileUtils.writeByteArrayToFile(propsCacheFile, bundleBytes);
    }

    protected byte[] getBytesFromURLConnection(URLConnection connection) throws IOException
    {

        byte[] configurationFileData = null;
        InputStream in = null;
        internalGetBytesFromURLConnection(connection, configurationFileData, in);
        return configurationFileData;
    }

    private void internalGetBytesFromURLConnection(URLConnection connection,
            byte[] configurationFileData, InputStream in) throws IOException
    {
        if (connection instanceof HttpURLConnection)
        {

            if (!sFailedWith401URLs.contains(connection.getURL().toString()))
            {

                HttpURLConnection httpConn = (HttpURLConnection) connection;
                httpConn.setInstanceFollowRedirects(true);
                httpConn.connect();
                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    secInternalGetBytesFromURLConnection(connection);

                    // add to 401ed URLs
                    sFailedWith401URLs.add(connection.getURL().toString());
                    throw new IOException(ErrorMessages.RemoteConfigurationType_msgUnAuthorized);
                }
            }
            else
            {
                // don't retry since we just get another 401
                throw new IOException(ErrorMessages.RemoteConfigurationType_msgUnAuthorized);
            }
        }

        in = connection.getInputStream();
        configurationFileData = IOUtils.toByteArray(in);

    }

    private void secInternalGetBytesFromURLConnection(URLConnection connection)
    {
        RemoteConfigAuthenticator.removeCachedAuthInfo(connection.getURL());

    }

    /**
     * Support for http authentication.
     * 
     * @author Lars Ködderitzsch
     */
    public static class RemoteConfigAuthenticator extends Authenticator
    {

        /** The check configuration URL. */
        private URL mResolvedCheckConfigurationURL;

        /**
         * Creates the authenticator.
         * 
         * @param resolvedCheckConfigurationURL the check configuration URL
         */
        public RemoteConfigAuthenticator(URL resolvedCheckConfigurationURL)
        {
            mResolvedCheckConfigurationURL = resolvedCheckConfigurationURL;
        }

        /**
         * Hacked together a piece of code to get the current default
         * authenticator. Can't believe the API is that bad...
         * 
         * @return the current Authenticator
         */
        public static Authenticator getDefault()
        {
            Authenticator currentDefault = null;
            // Hack to get the current authenticator
            Field[] fields = Authenticator.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
            {
                if (Authenticator.class.equals(fields[i].getType()))
                {
                    fields[i].setAccessible(true);
                    internalGetDefault(currentDefault, i, fields);
                    break;
                }
            }
            return currentDefault;
        }
        public static void internalGetDefault( Authenticator currentDefault, int i, Field[] fields){
           //TODO REFATORAR CRISTIANE
            currentDefault = (Authenticator) fields[i].get(Authenticator.class);
        }

        /**
         * Stores the credentials to the key ring.
         * 
         * @param resolvedCheckConfigurationURL the url
         * @param userName the user name
         * @param password the password
         */
        public static void storeCredentials(URL resolvedCheckConfigurationURL, String userName,
                String password)
        {
            Map authInfo = new HashMap();

            authInfo.put(KEY_USERNAME, userName);
            authInfo.put(KEY_PASSWORD, password);

            // store authorization info to the internal key ring
            Platform.addAuthorizationInfo(resolvedCheckConfigurationURL, "", "", authInfo); //$NON-NLS-1$ //$NON-NLS-2$

            sFailedWith401URLs.remove(resolvedCheckConfigurationURL.toString());

        }

        public static PasswordAuthentication getPasswordAuthentication(
                URL resolvedCheckConfigurationURL)
        {

            PasswordAuthentication auth = null;

            Map authInfo = Platform.getAuthorizationInfo(resolvedCheckConfigurationURL, "", ""); //$NON-NLS-1$ //$NON-NLS-2$

            if (authInfo != null)
            {
                auth = new PasswordAuthentication((String) authInfo.get(KEY_USERNAME), authInfo
                        .get(KEY_PASSWORD) != null ? ((String) authInfo.get(KEY_PASSWORD))
                        .toCharArray() : new char[0]);
            }

            return auth;
        }

        /**
         * Removes the authentication info from the session cache.
         * 
         * @param resolvedCheckConfigurationURL the check configuration URL
         */
        public static void removeCachedAuthInfo(URL resolvedCheckConfigurationURL)
            throws CheckstylePluginException
        {

            sFailedWith401URLs.remove(resolvedCheckConfigurationURL.toString());

            if (resolvedCheckConfigurationURL != null)
            {
                Platform.flushAuthorizationInfo(resolvedCheckConfigurationURL, "", ""); //$NON-NLS-1$ //$NON-NLS-2$
            }

        }

        /**
         * {@inheritDoc}
         */
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return getPasswordAuthentication(mResolvedCheckConfigurationURL);
        }
    }
}