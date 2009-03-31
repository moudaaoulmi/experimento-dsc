
package com.atlassw.tools.eclipse.checkstyle.builder;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.exception.GeneralException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Filter;

@ExceptionHandler
public class BuilderHandler extends GeneralException
{
    public IStatus buildProjectJob_runHandler(CoreException e, IStatus status)
    {
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

    public void auditor_runAudit(IProgressMonitor monitor, Checker checker, AuditListener listener,
            Filter runtimeExceptionFilter, ClassLoader contextClassloader)
    {
        monitor.done();

        // Cleanup listener and filter
        if (checker != null)
        {
            checker.removeListener(listener);
            checker.removeFilter(runtimeExceptionFilter);
        }

        // restore the original classloader
        Thread.currentThread().setContextClassLoader(contextClassloader);

    }

    public void projectObjectFactory_doMakeObjectHandler()
    {
        ; // keep looking
    }

    public void builderHandlerRethrowCoreException(CheckstylePluginException e, String msg)
        throws CoreException
    {
        Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.ERROR, msg, e);
        throw new CoreException(status);
    }

    public void throwCheckstyleException(String msg, String aClassName, Exception e)
        throws CheckstyleException
    {
        throw new CheckstyleException("Unable to find class for " + aClassName, e);
    }
    
    public void buildProjectJob_createCheckerHandler(InputStream in){
        IOUtils.closeQuietly(in);
    }
}
