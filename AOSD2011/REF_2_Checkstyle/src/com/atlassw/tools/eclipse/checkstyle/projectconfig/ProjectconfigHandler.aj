
package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.eclipse.osgi.util.NLS;
import org.apache.commons.io.IOUtils;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@ExceptionHandler
public privileged aspect ProjectconfigHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: Exception : ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler(): 
        execution(* ProjectConfigurationWorkingCopy.internalStoreToPersistence(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    void around(ProjectConfigurationWorkingCopy config, ByteArrayOutputStream pipeOut,
            InputStream pipeIn) throws CheckstylePluginException : 
                ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler()&& 
                args (config, pipeOut, pipeIn){
        try
        {
            proceed(config, pipeOut, pipeIn);
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e, NLS.bind(
                    ErrorMessages.errorWritingCheckConfigurations, e.getLocalizedMessage()));
        }
        finally
        {
            IOUtils.closeQuietly(pipeIn);
            IOUtils.closeQuietly(pipeOut);
        }
    }
}
