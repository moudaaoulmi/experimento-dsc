
package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public privileged aspect ProjectconfigHandler
{
    
    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft: Exception : ProjectConfigurationFactory_internalEndElementHandler() ||
                              ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler();

    declare soft: CheckstylePluginException : ProjectConfigurationFactory_startElementHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut ProjectConfigurationFactory_startElementHandler(): execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.startElement(..));

    pointcut ProjectConfigurationFactory_internalEndElementHandler(): execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.internalEndElement(..));


    pointcut ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler(): 
        execution(* ProjectConfigurationWorkingCopy.internalStoreToPersistence(..));
    
    // ---------------------------
    // Advice's
    // ---------------------------
    
    void around() throws SAXException : 
        ProjectConfigurationFactory_startElementHandler() || ProjectConfigurationFactory_internalEndElementHandler(){
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            throw new SAXException(e);
        }
    }

    void around() throws CheckstylePluginException : 
        ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler(){  
        try{
            proceed();
        }catch(Exception e){
        CheckstylePluginException.rethrow(e, NLS.bind(
                ErrorMessages.errorWritingCheckConfigurations, e.getLocalizedMessage()));
        }
    }
}
