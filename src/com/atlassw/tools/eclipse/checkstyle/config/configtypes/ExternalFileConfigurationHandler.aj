
package com.atlassw.tools.eclipse.checkstyle.config.configtypes;

import java.io.IOException;
import java.io.File;
import org.apache.commons.io.IOUtils;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.io.OutputStream;
import org.apache.commons.lang.StringUtils;

public privileged aspect ExternalFileConfigurationHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    
    declare soft: IOException: internalEnsureFileExistsHandler();

    declare soft: CheckstylePluginException: internalGetEditedWorkingCopyHandler() ||
                    isConfigurableHandler() ||
                    internalResolveLocationHandler() ||
                    widgetSelectedHandler() ||
                    internalGetEditedWorkingCopyHandler();

    declare soft: CheckstyleException: resolveDynamicLocationHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    
    pointcut internalEnsureFileExistsHandler():
        execution(* ExternalFileConfigurationEditor.internalEnsureFileExists(..)) ||
        execution(* InternalConfigurationEditor.internalEnsureFileExists(..));

    pointcut internalGetEditedWorkingCopyHandler():
        execution(* ExternalFileConfigurationEditor.internalGetEditedWorkingCopy(..));

    pointcut resolveDynamicLocationHandler(): 
        execution(* ExternalFileConfigurationType.internalResolveDynamicLocation(..));

    pointcut isConfigurableHandler():
        execution(* ExternalFileConfigurationType.internalIsConfigurable(..));

    pointcut internalResolveLocationHandler():
        execution(* ExternalFileConfigurationType.internalResolveLocation(..));

    pointcut widgetSelectedHandler():
        execution(* InternalConfigurationEditor.internalWidgetSelected(..));

    pointcut internalGetEditedWorkingCopyHandler2():
        execution(* InternalConfigurationEditor.internalGetEditedWorkingCopy(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    
    void around(File file, OutputStream out) throws CheckstylePluginException: 
        internalEnsureFileExistsHandler() && args(file, out){
        try
        {
            proceed(file, out);
        }
        catch (IOException ioe)
        {
            CheckstylePluginException.rethrow(ioe);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    void around() throws CheckstylePluginException: internalGetEditedWorkingCopyHandler()
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

    void around(String location) throws CheckstylePluginException: internalGetEditedWorkingCopyHandler2() && args(location){

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

    String around() throws CheckstylePluginException: resolveDynamicLocationHandler(){
        String result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstyleException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        return result;
    }

    String around(String location, boolean isConfigurable): 
        isConfigurableHandler() && args(location, isConfigurable){
        String result = null;
        try
        {
            result = proceed(location, isConfigurable);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
            isConfigurable = false;
        }
        return result;
    }

    void around() throws IOException: 
        internalResolveLocationHandler(){
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

    void around(): widgetSelectedHandler(){
        InternalConfigurationEditor icE = (InternalConfigurationEditor) thisJoinPoint.getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException ex)
        {
            icE.mDialog.setErrorMessage(ex.getLocalizedMessage());
        }
    }

}
