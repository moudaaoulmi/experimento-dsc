
package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

public aspect ConfigurationTypeHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    
    declare soft: IOException : getResolvedConfigurationFileURLHandler() ||
                                getCheckstyleConfigurationHandler() ||
                                internalGetAdditionPropertiesBundleBytesHandler();

    declare soft: Exception : internalStaticHandler();
    
    // ---------------------------
    // Pointcut's
    // ---------------------------
    
    pointcut getResolvedConfigurationFileURLHandler() : 
            execution (* ConfigurationType.getResolvedConfigurationFileURL(..)) &&
            within(ConfigurationType);

    pointcut getCheckstyleConfigurationHandler() : 
            execution (* ConfigurationType.getCheckstyleConfiguration(..)) &&
            within(ConfigurationType);

    pointcut internalGetAdditionPropertiesBundleBytesHandler():
            execution (* ConfigurationType.internalGetAdditionPropertiesBundleBytes(..)) &&
            within (ConfigurationType);

    pointcut internalGetBytesFromURLConnectionHandler():
        execution (* ConfigurationType.internalGetBytesFromURLConnection(..));

    pointcut internalStaticHandler():
        execution(* ConfigurationTypes.internalStatic(..));
    
    // ---------------------------
    // Advice's
    // ---------------------------
    
    after() throwing (Exception e) throws CheckstylePluginException:
        getResolvedConfigurationFileURLHandler()||
        getCheckstyleConfigurationHandler()
    {
        CheckstylePluginException.rethrow(e);
    }

    byte[] around(): internalGetAdditionPropertiesBundleBytesHandler(){
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

    void around(URLConnection connection, InputStream in, byte[] configurationFileData):internalGetBytesFromURLConnectionHandler() &&
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

    void around(): internalStaticHandler(){
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            CheckstyleLog.log(e);
        }
    }

}