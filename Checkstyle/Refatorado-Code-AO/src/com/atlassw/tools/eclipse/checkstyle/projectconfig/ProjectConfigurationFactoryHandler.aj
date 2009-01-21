package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import org.xml.sax.SAXException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import java.io.InputStream;

public privileged aspect ProjectConfigurationFactoryHandler
{
    declare soft: CheckstylePluginException : startElementHandler();
    declare soft: Exception : internalEndElementHandler();
    declare soft: CoreException : internalLoadFromPersistenceHandler();
    declare soft: SAXException : internalLoadFromPersistenceHandler();
    declare soft: ParserConfigurationException : internalLoadFromPersistenceHandler();
    declare soft: IOException : internalLoadFromPersistenceHandler();
    
    pointcut startElementHandler(): execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.startElement(..));
    pointcut internalEndElementHandler(): execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.internalEndElement(..));
    pointcut internalLoadFromPersistenceHandler(): execution(* ProjectConfigurationFactory.internalLoadFromPersistence(..));
    
    after() throwing(Exception e) throws SAXException : 
        startElementHandler() || internalEndElementHandler(){    
        throw new SAXException(e);
    }
    
    IProjectConfiguration around(IProject project,
            IProjectConfiguration configuration, IFile file, InputStream inStream) 
            throws CheckstylePluginException : internalLoadFromPersistenceHandler() && args(project,
                    configuration, file, inStream){
        try{
            return proceed(project,
                    configuration, file, inStream);
        }catch (CoreException ce){
            CheckstylePluginException.rethrow(ce);
        }catch (SAXException se){
            Exception ex = se.getException() != null ? se.getException() : se;
            CheckstylePluginException.rethrow(ex);
        }catch (ParserConfigurationException pe){
            CheckstylePluginException.rethrow(pe);
        }catch (IOException ioe){
            CheckstylePluginException.rethrow(ioe);
        }finally{
            IOUtils.closeQuietly(inStream);
        }
        return configuration;
    }
}
