
package com.atlassw.tools.eclipse.checkstyle.builder;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class BuilderHandler extends GeneralException
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

    public void auditor_calculateMarkerOffset()
    {
    // seems to happen quite often so its no use to log since we
    // can't do anything about it
    // CheckstyleLog.log(e);
    }

    public void packageNamesLoader_getPackageNames6(InputStream iStream)
    {
        IOUtils.closeQuietly(iStream);
    }

    public void projectObjectFactory_doMakeObjectHandler()
    {
        ; // keep looking
    }

    public void builderHandlerRethrowException(Exception e) throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e);
    }

    public void packageNamesLoader_getPackageNames(Exception e, String msg, String defaultPackages)
        throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e, msg + defaultPackages);
    }

    public void rethrowCheckstylePluginException(Exception e, String msg)
        throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e, msg);
    }

    public void builderHandlerRethrowCoreException(CheckstylePluginException e, String msg)
        throws CoreException
    {
        Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.ERROR, 
                msg, e); 
        throw new CoreException(status);
    }

    public void throwCheckstyleException(String msg, String aClassName, Exception e)
        throws CheckstyleException
    {
        throw new CheckstyleException("Unable to find class for " + aClassName, e);
    }
}
