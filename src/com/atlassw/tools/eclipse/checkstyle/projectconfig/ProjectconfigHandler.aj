
package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralExceptionHandler;

public privileged aspect ProjectconfigHandler extends GeneralExceptionHandler
{
    
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft : CloneNotSupportedException : FileMatchPattern_cloneHandler() || FileMatchPattern_cloneFileSetHandler() || 
                                                FileMatchPattern_cloneProjectHandler() || FileMatchPattern_cloneWorkingCopyHandler();

    declare soft: Exception : PluginFilters_internalHandler() || ProjectConfigurationFactory_internalEndElementHandler() ||
                              ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler();

    declare soft: CheckstylePluginException : ProjectConfigurationFactory_startElementHandler();

    declare soft: CoreException : ProjectConfigurationFactory_internalLoadFromPersistenceHandler();

    declare soft: SAXException : ProjectConfigurationFactory_internalLoadFromPersistenceHandler();

    declare soft: ParserConfigurationException : ProjectConfigurationFactory_internalLoadFromPersistenceHandler();

    declare soft: IOException : ProjectConfigurationFactory_internalLoadFromPersistenceHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    /*pointcut FileMatchPattern_internalSetMatchPatternHandler() : execution (* FileMatchPattern.internalSetMatchPattern(..));*/

    pointcut FileMatchPattern_cloneHandler(): execution(* FileMatchPattern.clone(..));

    pointcut FileMatchPattern_cloneFileSetHandler(): execution(* FileSet.clone(..));

    pointcut FileMatchPattern_cloneProjectHandler(): execution(* ProjectConfiguration.clone(..));

    pointcut FileMatchPattern_cloneWorkingCopyHandler(): execution(* ProjectConfigurationWorkingCopy.clone(..));
    
    pointcut PluginFilters_internalHandler() : execution(* PluginFilters.internal(..));
    
    pointcut ProjectConfigurationFactory_startElementHandler(): execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.startElement(..));

    pointcut ProjectConfigurationFactory_internalEndElementHandler(): execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.internalEndElement(..));

    pointcut ProjectConfigurationFactory_internalLoadFromPersistenceHandler(): execution(* ProjectConfigurationFactory.internalLoadFromPersistence(..));

    pointcut ProjectConfigurationWorkingCopy_internalStoreToPersistenceHandler(): execution(* ProjectConfigurationWorkingCopy.internalStoreToPersistence(..));

    public pointcut exceptionPoints(): execution (* FileMatchPattern.internalSetMatchPattern(..));
    
    // ---------------------------
    // Advice's
    // ---------------------------
    
    //HERANÇA
    /*void around() throws CheckstylePluginException : 
        FileMatchPattern_internalSetMatchPatternHandler(){    
        try{
            proceed();
        }catch(Exception e){
            CheckstylePluginException.rethrow(e); // wrap the exception
        }
    }*/
    //HERANÇA
    
    Object around() : FileMatchPattern_cloneHandler() || FileMatchPattern_cloneFileSetHandler() || 
                      FileMatchPattern_cloneProjectHandler() || FileMatchPattern_cloneWorkingCopyHandler(){
        try{
            return proceed();
        }catch (CloneNotSupportedException e){
            throw new InternalError(); // should never happen
        }
    }
    
    void around() : PluginFilters_internalHandler(){
        try{
            proceed();
        }
        catch (Exception e){
            CheckstyleLog.log(e);
        }
    }
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

    IProjectConfiguration around(IProject project, IProjectConfiguration configuration, IFile file,
            InputStream inStream) throws CheckstylePluginException : ProjectConfigurationFactory_internalLoadFromPersistenceHandler() && args(project,
                    configuration, file, inStream){
        try
        {
            return proceed(project, configuration, file, inStream);
        }
        catch (CoreException ce)
        {
            CheckstylePluginException.rethrow(ce);
        }
        catch (SAXException se)
        {
            Exception ex = se.getException() != null ? se.getException() : se;
            CheckstylePluginException.rethrow(ex);
        }
        catch (ParserConfigurationException pe)
        {
            CheckstylePluginException.rethrow(pe);
        }
        catch (IOException ioe)
        {
            CheckstylePluginException.rethrow(ioe);
        }
        finally
        {
            IOUtils.closeQuietly(inStream);
        }
        return configuration;
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
