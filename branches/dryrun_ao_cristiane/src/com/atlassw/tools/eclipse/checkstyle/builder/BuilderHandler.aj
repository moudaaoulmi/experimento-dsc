
package com.atlassw.tools.eclipse.checkstyle.builder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public privileged aspect BuilderHandler
{

    declare soft: CoreException: buildProjectJob_runHandler() ||auditor_runAuditHandle()||checkstyleBuilder_buildProjectsHandleHandle();

    declare soft: CheckstylePluginException: checkstyleBuilder_buildHandler()|| checkstyleBuilder_handleBuildSelectionHandler();

    declare soft: BadLocationException: auditor_calculateMarkerOffsetHandle();

    declare soft: SAXException: packageNamesLoader_getPackageNameInteration3Handle()||packageNamesLoader_getPackageNamesHandle();

    declare soft: IOException: packageNamesLoader_getPackageNameInteration3Handle()||auditor_runAuditHandle()||packageNamesLoader_getPackageNameInteration1Handle()||packageNamesLoader_getPackageNamesHandle();

    declare soft: JavaModelException: projectClassLoader_addToClassPathHandle();

    declare soft: ClassNotFoundException: packageObjectFactory_createObjectHandle();

    declare soft: InstantiationException: packageObjectFactory_createObjectHandle();

    declare soft: IllegalAccessException: packageObjectFactory_createObjectHandle();

    declare soft: MalformedURLException: projectClassLoader_handlePathHandle();

    declare soft: CheckstyleException: packageObjectFactory_createModuleInternalHandle()||   packageObjectFactory_createModuleHandle()||   packageObjectFactory_doMakeObjectHandle()||  packageObjectFactory_doMakeObjectInternal2Handle()||auditor_runAuditHandle();

    declare soft: ParserConfigurationException: packageNamesLoader_getPackageNamesHandle();

    declare soft: CheckstylePluginException: runCheckstyleOnFilesJob_runInWorkspaceHandle();

    
    // pointcuts

    pointcut buildProjectJob_runHandler(): execution (* BuildProjectJob.run(..)) ;

    pointcut checkstyleBuilder_buildHandler(): execution(* CheckstyleBuilder.build(..)) ;

    pointcut checkstyleBuilder_handleBuildSelectionHandler(): execution(* CheckstyleBuilder.handleBuildSelection(..));

    pointcut auditor_calculateMarkerOffsetHandle(): 
        execution (* Auditor.CheckstyleAuditListener.calculateMarkerOffset(..)) ;

    pointcut packageNamesLoader_getPackageNameInteration3Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration3(..)) ;

    pointcut projectClassLoader_addToClassPathHandle(): 
        execution (* ProjectClassLoader.addToClassPath(..)) ;

    pointcut packageObjectFactory_createObjectHandle(): 
        execution (* PackageObjectFactory.createObject(..)) ;

    pointcut projectClassLoader_handlePathHandle(): 
        execution (* ProjectClassLoader.handlePath(..)) ;

    pointcut packageObjectFactory_createModuleInternalHandle(): 
        execution (* PackageObjectFactory.createModuleInternal(..)) ;

    pointcut packageObjectFactory_createModuleHandle(): 
        execution (* PackageObjectFactory.createModule(..)) ;

    pointcut packageObjectFactory_doMakeObjectHandle(): 
        execution (* PackageObjectFactory.doMakeObject(..)) ;

    pointcut packageObjectFactory_doMakeObjectInternal2Handle(): 
        execution (* PackageObjectFactory.doMakeObjectInternal2(..)) ;

    pointcut auditor_runAuditHandle(): 
        execution (* Auditor.runAudit(..)) ;

    pointcut checkstyleBuilder_buildProjectsHandleHandle(): execution(* CheckstyleBuilder.buildProjectsHandle(..));

    pointcut packageNamesLoader_getPackageNameInteration1Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration1(..)) ;

    pointcut packageNamesLoader_getPackageNamesHandle(): 
        execution (* PackageNamesLoader.getPackageNames(..)) ;

    pointcut runCheckstyleOnFilesJob_runInWorkspaceHandle(): 
        execution (* RunCheckstyleOnFilesJob.runInWorkspace(..)) ;
    
    IStatus around(): buildProjectJob_runHandler() {
        IStatus result = null;
        try
        {
            result = proceed();
        }
        catch (CoreException e)
        {
            result = e.getStatus();
        }
        return result;
    }

    IProject[] around() throws CoreException:  checkstyleBuilder_buildHandler() {
        IProject[] result = null;
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

    void around(PackageNamesLoader nameLoader, URL aPackageFile): packageNamesLoader_getPackageNameInteration3Handle() 
            && args(nameLoader, aPackageFile) {
        try
        {
            proceed(nameLoader, aPackageFile);
        }
        catch (SAXException e)
        {
            checkstyleLogMessage(e, "unable to parse " + aPackageFile.toExternalForm() //$NON-NLS-1$
                    + " - " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            checkstyleLogMessage(e, "unable to read " + aPackageFile.toExternalForm()); //$NON-NLS-1$
        }
    }

    void around(): projectClassLoader_addToClassPathHandle() {
        try
        {
            proceed();
        }
        catch (JavaModelException jme)
        {
            CheckstyleLog.log(jme);
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

    private void checkstyleLogMessage(Exception e, String message)
    {
        CheckstyleLog.log(e, message);
    }

    private void checkstyleLogMessage(Exception e)
    {
        CheckstyleLog.log(e);
    }

    Object around(String aClassName) throws CheckstyleException: packageObjectFactory_createObjectHandle() && args(aClassName) {
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

    Object around(String aName) throws CheckstyleException: packageObjectFactory_createModuleInternalHandle() && args(aName) {
        Object result = null;
        try
        {
            result = proceed(aName);
        }
        catch (CheckstyleException e)
        {
            throw new CheckstyleException("Unable to instantiate " + aName, e);//$NON-NLS-1$
        }
        return result;
    }

    // reusado
    Object around(String aName) throws CheckstyleException: (packageObjectFactory_doMakeObjectHandle()||packageObjectFactory_createModuleHandle()) && args(aName) {
        Object result = null;
        try
        {
            result = proceed(aName);
        }
        catch (CheckstyleException e)
        {
            PackageObjectFactory p = (PackageObjectFactory) thisJoinPoint.getThis();
            result = p.doMakeObjectInternal(aName);
        }
        return result;
    }

    Object around(String className): packageObjectFactory_doMakeObjectInternal2Handle() && args(className) {
        Object result = null;
        try
        {
            result = proceed(className);
        }
        catch (CheckstyleException e)
        {
            // Do nothing
        }
        return result;
    }

    // inicio reuso

    void around() throws CheckstylePluginException: packageNamesLoader_getPackageNameInteration1Handle()||auditor_runAuditHandle() {
        try
        {
            proceed();
        }
        catch (IOException e)
        {
            retrowException(e); //$NON-NLS-1$
        }
    }

    void around() throws CheckstylePluginException: checkstyleBuilder_buildProjectsHandleHandle()||auditor_runAuditHandle() {
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            this.retrowException(e);
        }
    }

    void around() throws CheckstylePluginException: auditor_runAuditHandle() {
        try
        {
            proceed();
        }
        catch (CheckstyleException e)
        {
            this.retrowException(e);
        }
    }

    // fim reuso

    List<Object> around() throws CheckstylePluginException: packageNamesLoader_getPackageNamesHandle() {
        List<Object> result = null;
        try
        {
            result = proceed();
        }
        catch (ParserConfigurationException e)
        {
            retrowException(e, "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES); //$NON-NLS-1$
        }
        catch (SAXException e)
        {
            retrowException(e,
                    "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch (IOException e)
        {
            retrowException(e, "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES); //$NON-NLS-1$
        }
        return result;
    }

    private void retrowException(Exception e) throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e);
    }

    private void retrowException(Exception e, String message) throws CheckstylePluginException
    {
        CheckstylePluginException.rethrow(e, message);
    }

    // reusado

    Object around() throws CoreException : runCheckstyleOnFilesJob_runInWorkspaceHandle() ||checkstyleBuilder_handleBuildSelectionHandler() {
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

}
