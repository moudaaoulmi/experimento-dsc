
package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import org.xml.sax.SAXException;
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

    declare soft: Exception : ProjectConfigurationFactory_endElementHandler() ||
                              ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler();

    declare soft: CheckstylePluginException : ProjectConfigurationFactory_startElementHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut ProjectConfigurationFactory_startElementHandler(): 
        execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.startElement(..));

    pointcut ProjectConfigurationFactory_endElementHandler(): 
        execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.endElement(..));

    pointcut ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler(): 
        execution(* ProjectConfigurationWorkingCopy.internalStoreToPersistence(..));

    // ---------------------------
    // Advice's
    // ---------------------------

    void around() throws SAXException : 
        ProjectConfigurationFactory_startElementHandler() || 
        ProjectConfigurationFactory_endElementHandler(){
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            throw new SAXException(e);
        }
    }

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
