package com.atlassw.tools.eclipse.checkstyle.builder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.xml.sax.SAXException;
import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckstyleConfigurationFile;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Filter;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.IProjectConfiguration;

@ExceptionHandler
public privileged aspect BuilderHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: CoreException: buildProjectJob_runHandler() ||
                                 auditor_runAuditHandle();

    declare soft: CheckstylePluginException: checkstyleBuilder_buildHandler()|| 
                  checkstyleBuilder_handleBuildSelectionHandler();

    declare soft: BadLocationException: auditor_calculateMarkerOffsetHandle();

    declare soft: IOException: PackageNamesLoader_getPackageNames() ||
                  PackageNamesLoader_internalgetPackageNames2()||
                  auditor_runAuditHandle() ||
                  PackageNamesLoader_internalgetPackageNames();

    declare soft: ParserConfigurationException: PackageNamesLoader_internalgetPackageNames();

    declare soft: SAXException: PackageNamesLoader_internalgetPackageNames2() ||
                                PackageNamesLoader_internalgetPackageNames();

    declare soft: ClassNotFoundException: packageObjectFactory_createObjectHandle();

    declare soft: InstantiationException: packageObjectFactory_createObjectHandle();

    declare soft: IllegalAccessException: packageObjectFactory_createObjectHandle();

    declare soft: MalformedURLException: projectClassLoader_handlePathHandle();

    declare soft: CheckstyleException: packageObjectFactory_createModuleHandle()||  
                                       packageObjectFactory_doMakeObjectHandle()||  
                                       auditor_runAuditHandle();

    declare soft: CheckstylePluginException: runCheckstyleOnFilesJob_runInWorkspaceHandle();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    pointcut buildProjectJob_runHandler():
        execution (* BuildProjectJob.run(..)) ;

    pointcut checkstyleBuilder_buildHandler(): 
        execution(* CheckstyleBuilder.internalBuild(..)) ;

    pointcut checkstyleBuilder_handleBuildSelectionHandler(): 
        execution(* CheckstyleBuilder.handleBuildSelection(..));

    pointcut auditor_calculateMarkerOffsetHandle(): 
        execution (* Auditor.CheckstyleAuditListener.calculateMarkerOffset(..)) ;

    pointcut auditor_runAuditHandle(): 
        execution (* Auditor.internalRunAudit(..)) ;

    pointcut PackageNamesLoader_getPackageNames():
        execution(* PackageNamesLoader.getPackageNames(..));

    pointcut PackageNamesLoader_internalgetPackageNames2():
        execution(* PackageNamesLoader.internalgetPackageNames2(..));

    pointcut PackageNamesLoader_internalgetPackageNames():
        execution(* PackageNamesLoader.internalGetPackageNames(..));

    pointcut packageObjectFactory_createObjectHandle(): 
        execution (* PackageObjectFactory.createObject(..)) ;

    pointcut packageObjectFactory_createModuleHandle(): 
        execution (* PackageObjectFactory.createModule(..)) ;

    pointcut packageObjectFactory_doMakeObjectHandle(): 
        execution(* PackageObjectFactory.doMakeObject(..)) ;

    pointcut projectClassLoader_handlePathHandle(): 
        execution (* ProjectClassLoader.handlePath(..)) ;

    pointcut runCheckstyleOnFilesJob_runInWorkspaceHandle(): 
        execution (* RunCheckstyleOnFilesJob.runInWorkspace(..)) ;

    pointcut checkerFactory_internalCreateCheckerHandler(): 
        execution (* CheckerFactory.internalCreateChecker(..)) ;

    // ---------------------------
    // Advice's
    // ---------------------------
    PackageNamesLoader around() throws CheckstylePluginException: PackageNamesLoader_internalgetPackageNames(){
        PackageNamesLoader result = null;
        try
        {
            result = proceed();
        }
        catch (ParserConfigurationException e)
        {
            PackageNamesLoader pNL = (PackageNamesLoader) thisJoinPoint.getThis();
            CheckstylePluginException.rethrow(e, "unable to parse " + pNL.DEFAULT_PACKAGES); //$NON-NLS-1$
        }
        catch (SAXException e)
        {
            PackageNamesLoader pNL = (PackageNamesLoader) thisJoinPoint.getThis();
            CheckstylePluginException.rethrow(e, "unable to parse " + pNL.DEFAULT_PACKAGES + " - " //$NON-NLS-1$ //$NON-NLS-2$
                    + e.getMessage());
        }
        catch (IOException e)
        {
            PackageNamesLoader pNL = (PackageNamesLoader) thisJoinPoint.getThis();
            CheckstylePluginException.rethrow(e, "unable to read " + pNL.DEFAULT_PACKAGES); //$NON-NLS-1$
        }
        return result;
    }

    void around(PackageNamesLoader nameLoader, URL aPackageFile, InputStream iStream): PackageNamesLoader_internalgetPackageNames2()
        && args(nameLoader, aPackageFile, iStream){
        try
        {
            proceed(nameLoader, aPackageFile, iStream);
        }
        catch (SAXException e)
        {
            CheckstyleLog.log(e, "unable to parse " + aPackageFile.toExternalForm() //$NON-NLS-1$
                    + " - " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            CheckstyleLog.log(e, "unable to read " + aPackageFile.toExternalForm()); //$NON-NLS-1$
        }
        finally
        {
            IOUtils.closeQuietly(iStream);
        }
    }

    List around() throws CheckstylePluginException: PackageNamesLoader_getPackageNames() {
        List result = null;
        try
        {
            result = proceed();
        }
        catch (IOException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        return result;
    }

    void around(IProject project, IProgressMonitor monitor, Checker checker,
            AuditListener listener, Filter runtimeExceptionFilter, ClassLoader contextClassloader)
        throws CheckstylePluginException: auditor_runAuditHandle() &&
            args(project, monitor, checker, listener, runtimeExceptionFilter, contextClassloader)
        {
        try
        {
            proceed(project, monitor, checker, listener, runtimeExceptionFilter, contextClassloader);
        }
        finally
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
    }

    void around() throws CheckstylePluginException: auditor_runAuditHandle(){
        try
        {
            proceed();
        }
        catch (IOException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        catch (CoreException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        catch (CheckstyleException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }

    Object around(String aName) throws CheckstyleException:
        packageObjectFactory_createModuleHandle() && args(aName){
        try
        {
            return proceed(aName);
        }
        catch (CheckstyleException ex)
        {
            try
            {
                PackageObjectFactory pOF = (PackageObjectFactory) thisJoinPoint.getThis();
                return pOF.doMakeObject(aName + "Check");
            }
            catch (CheckstyleException ex2)
            {
                throw new CheckstyleException("Unable to instantiate " + aName, ex2);
            }
        }
    }

    IStatus around(IProgressMonitor monitor): buildProjectJob_runHandler() && 
        args(monitor){
        IStatus result = null;
        try
        {
            result = proceed(monitor);
        }
        catch (CoreException e)
        {
            result = e.getStatus();
        }
        finally
        {
            monitor.done();
        }
        return result;
    }

    IProjectConfiguration around() throws CoreException: 
        checkstyleBuilder_buildHandler(){
        IProjectConfiguration result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.ERROR, e
                    .getMessage() != null ? e.getMessage()
                    : ErrorMessages.CheckstyleBuilder_msgErrorUnknown, e);
            throw new CoreException(status);
        }
        return result;
    }

    void around(): auditor_calculateMarkerOffsetHandle() {
        try
        {
            proceed();
        }
        catch (BadLocationException e)
        {
            // seems to happen quite often so its no use to log since we
            // can't do anything about it
            // CheckstyleLog.log(e);
        }
    }

    void around(): projectClassLoader_handlePathHandle() {
        try
        {
            proceed();
        }
        catch (MalformedURLException mfe)
        {
            // log the exception although this should not happen
            CheckstyleLog.log(mfe, mfe.getLocalizedMessage());
        }
    }

    Object around(String aClassName) throws CheckstyleException: 
        packageObjectFactory_createObjectHandle() && args(aClassName) {
        Object result = null;
        try
        {
            result = proceed(aClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new CheckstyleException("Unable to find class for " + aClassName, e); //$NON-NLS-1$
        }
        catch (InstantiationException e)
        {
            // /CLOVER:OFF
            throw new CheckstyleException("Unable to instantiate " + aClassName, e); //$NON-NLS-1$
            // /CLOVER:ON
        }
        catch (IllegalAccessException e)
        {
            // /CLOVER:OFF
            throw new CheckstyleException("Unable to instantiate " + aClassName, e); //$NON-NLS-1$
            // /CLOVER:ON
        }
        return result;
    }

    Object around(String aName) throws CheckstyleException: 
        packageObjectFactory_doMakeObjectHandle() && args(aName){
        Object result = null;
        try
        {
            result = proceed(aName);
        }
        catch (CheckstyleException ex)
        {
            PackageObjectFactory pOF = (PackageObjectFactory) thisJoinPoint.getThis();
            for (int i = 0; i < pOF.mPackages.size(); i++)
            {
                final String packageName = (String) pOF.mPackages.get(i);
                final String className = packageName + aName;
                try
                {
                    return pOF.createObject(className);
                }
                catch (CheckstyleException e)
                {
                    ; // keep looking
                }
            }

            throw new CheckstyleException("Unable to instantiate " + aName); //$NON-NLS-1$
        }
        return result;
    }

    Object around() throws CoreException : runCheckstyleOnFilesJob_runInWorkspaceHandle() ||
              checkstyleBuilder_handleBuildSelectionHandler() {
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.ERROR, e
                    .getLocalizedMessage(), e);
            throw new CoreException(status);
        }
        return null;
    }

    InputStream around(CheckstyleConfigurationFile configFileData, InputStream in): 
        checkerFactory_internalCreateCheckerHandler() && args(configFileData,in) {
        try
        {
            in = proceed(configFileData, in);
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return in;
    }

}
