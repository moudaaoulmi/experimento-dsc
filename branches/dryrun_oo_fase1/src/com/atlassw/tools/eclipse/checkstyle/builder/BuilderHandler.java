package com.atlassw.tools.eclipse.checkstyle.builder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaModelException;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class BuilderHandler
{
    public IStatus buildProjectJob_runHandler(CoreException e)
    {
        IStatus status;
        status = e.getStatus();
        return status;
    }
    
    public void buildProjectJob_runHandler2(IProgressMonitor monitor)
    {
        monitor.done();
    }
    
    public void checkstyleBuilder_buildHandler(CheckstylePluginException e) throws CoreException
    {
        Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID,
                IStatus.ERROR, e.getMessage() != null ? e.getMessage()
                        : ErrorMessages.CheckstyleBuilder_msgErrorUnknown, e);
        throw new CoreException(status);
    }
    
    // Estes dois podem ser reusados, mas não sei que nomenclatura adotar...
    public void auditor_addErrorHandler(CoreException e)
    {
        CheckstyleLog.log(e);
    }
    
    public void projectClassLoader_addToClasspathHandler(JavaModelException jme)
    {
        CheckstyleLog.log(jme);
    }
    // ----------------------------------------------------------------------
    
    public void auditor_calculateMarkerOffset()
    {
        // seems to happen quite often so its no use to log since we
        // can't do anything about it
        // CheckstyleLog.log(e);
    }
    
    public void packageNamesLoader_getPackageNames(ParserConfigurationException e, String defaultPackages) throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e, "unable to parse " + defaultPackages); //$NON-NLS-1$
    }
    
    public void packageNamesLoader_getPackageNames2(SAXException e, String defaultPackages) throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e, "unable to parse " + defaultPackages + " - " //$NON-NLS-1$ //$NON-NLS-2$
                + e.getMessage());
    }  
    
    public void packageNamesLoader_getPackageNames3(IOException e, String defaultPackages) throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e, "unable to read " + defaultPackages); //$NON-NLS-1$
    }
    
    public void packageNamesLoader_getPackageNames4(URL aPackageFile, SAXException e)
    {
        CheckstyleLog.log(e, "unable to parse " + aPackageFile.toExternalForm() //$NON-NLS-1$
                + " - " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
    // ATENÇÃO: Pode agrupar packageNamesLoader_getPackageNames4 e packageNamesLoader_getPackageNames5
    public void packageNamesLoader_getPackageNames5(URL aPackageFile, IOException e)
    {
        CheckstyleLog.log(e, "unable to read " + aPackageFile.toExternalForm()); //$NON-NLS-1$
    }
    
    public void packageNamesLoader_getPackageNames6(InputStream iStream)
    {
        IOUtils.closeQuietly(iStream);
    }
    
    public void projectClassLoader_handlePathHandler(MalformedURLException mfe)
    {
        // log the exception although this should not happen
        CheckstyleLog.log(mfe, mfe.getLocalizedMessage());
    }
    
    public void packageObjectFactory_createObjectHandler(String aClassName, ClassNotFoundException e) throws CheckstyleException
    {
        throw new CheckstyleException("Unable to find class for " + aClassName, e); //$NON-NLS-1$
    }
   
    public void packageObjectFactory_createObjectHandler2(String aClassName, Exception e) throws CheckstyleException
    {
        // /CLOVER:OFF
        throw new CheckstyleException("Unable to instantiate " + aClassName, e); //$NON-NLS-1$
        // /CLOVER:ON
    }

    public void packageObjectFactory_createModuleHandlerHandler(String aName, CheckstyleException ex2) throws CheckstyleException
    {
        throw new CheckstyleException("Unable to instantiate " + aName, ex2); //$NON-NLS-1$
    }
    
    public void projectObjectFactory_doMakeObjectHandler()
    {
        ; // keep looking
    }
    
    public void builderHandlerRethrowException(Exception e) throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e);
    }
    
    public void runCheckstyleOnFilesJob_runInWorkspace(CheckstylePluginException e) throws CoreException
    {
        Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.ERROR, e.getLocalizedMessage(), e);
        throw new CoreException(status);
    }
    
    public void checkstyleBuilder_handleBuildSelection(CheckstylePluginException e) throws CoreException
    {
        Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.ERROR, e.getLocalizedMessage(), e);
        throw new CoreException(status);
    }
}
