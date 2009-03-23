
package com.atlassw.tools.eclipse.checkstyle.builder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.eclipse.core.runtime.IConfigurationElement;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckstyleConfigurationFile;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.io.InputStream;
import com.atlassw.tools.eclipse.checkstyle.config.CheckstyleConfigurationFile;

public privileged aspect BuilderHandler
{

    declare soft: CoreException: buildProjectJob_runHandler();

    declare soft: CheckstylePluginException: checkstyleBuilder_buildHandler()|| checkstyleBuilder_handleBuildSelectionHandler();

    declare soft: BadLocationException: auditor_calculateMarkerOffsetHandle();

    declare soft: SAXException: packageNamesLoader_getPackageNameInteration3Handle()||packageNamesLoader_getPackageNamesHandle();

    declare soft: IOException: packageNamesLoader_getPackageNameInteration3Handle()||
                               packageNamesLoader_getPackageNamesHandle();

    declare soft: ClassNotFoundException: packageObjectFactory_createObjectHandle();

    declare soft: InstantiationException: packageObjectFactory_createObjectHandle();

    declare soft: IllegalAccessException: packageObjectFactory_createObjectHandle();

    declare soft: MalformedURLException: projectClassLoader_handlePathHandle();

    declare soft: CheckstyleException: //packageObjectFactory_createModuleInternalHandle()||  
                                       packageObjectFactory_createModuleHandle()||  
                                       packageObjectFactory_doMakeObjectHandle()||  
                                       packageObjectFactory_internalDoMakeObjectHandle()||
                                       auditor_runAuditHandle();

    declare soft: ParserConfigurationException: packageNamesLoader_getPackageNamesHandle();

    declare soft: CheckstylePluginException: runCheckstyleOnFilesJob_runInWorkspaceHandle();

    // pointcuts

    pointcut buildProjectJob_runHandler():
        execution (* BuildProjectJob.run(..)) ;

    pointcut checkstyleBuilder_buildHandler(): 
        execution(* CheckstyleBuilder.build(..)) ;

    pointcut checkstyleBuilder_handleBuildSelectionHandler(): 
        execution(* CheckstyleBuilder.handleBuildSelection(..));

    pointcut auditor_calculateMarkerOffsetHandle(): 
        execution (* Auditor.CheckstyleAuditListener.calculateMarkerOffset(..)) ;

    pointcut packageNamesLoader_getPackageNameInteration3Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration3(..)) ;

    pointcut packageObjectFactory_createObjectHandle(): 
        execution (* PackageObjectFactory.createObject(..)) ;

    pointcut projectClassLoader_handlePathHandle(): 
        execution (* ProjectClassLoader.handlePath(..)) ;

    pointcut packageObjectFactory_createModuleHandle(): 
        execution (* PackageObjectFactory.createModule(..)) ;

    pointcut packageObjectFactory_doMakeObjectHandle(): 
        execution(* PackageObjectFactory.doMakeObject(..)) ;

    pointcut packageObjectFactory_internalDoMakeObjectHandle():
         call(* PackageObjectFactory.createObject(..)) &&
         withincode(* PackageObjectFactory.internalDoMakeObject(..)) ;

    pointcut auditor_runAuditHandle(): 
        execution (* Auditor.runAudit(..)) ;

    pointcut packageNamesLoader_getPackageNamesHandle(): 
        execution (* PackageNamesLoader.getPackageNames(..)) ;

    pointcut runCheckstyleOnFilesJob_runInWorkspaceHandle(): 
        execution (* RunCheckstyleOnFilesJob.runInWorkspace(..)) ;

    pointcut checkerFactory_internalCreateCheckerHandler(): 
        execution (* CheckerFactory.internalCreateChecker(..)) ;

    
    Object around(String aName) throws CheckstyleException:
        packageObjectFactory_createModuleHandle() && args(aName){
        try{
            return proceed(aName);
        }catch(CheckstyleException ex){
            try{
                PackageObjectFactory pOF = (PackageObjectFactory) thisJoinPoint.getThis();
                return pOF.doMakeObject(aName + "Check");
            }catch(CheckstyleException ex2){
                throw new CheckstyleException("Unable to instantiate " + aName , ex2);
            }
        }
    }
    
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
            CheckstyleLog.log(e, "unable to parse " + aPackageFile.toExternalForm() //$NON-NLS-1$
                    + " - " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            CheckstyleLog.log(e, "unable to read " + aPackageFile.toExternalForm()); //$NON-NLS-1$
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
        catch (CheckstyleException e)
        {
            PackageObjectFactory pOF = (PackageObjectFactory) thisJoinPoint.getThis();
            pOF.internalDoMakeObject(aName);
        }
        return result;
    }

    Object around() throws CheckstyleException: 
        packageObjectFactory_internalDoMakeObjectHandle() {
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstyleException e)
        {
            ; // Keep looking
        }
        return result;
    }

    List<Object> around() throws CheckstylePluginException: packageNamesLoader_getPackageNamesHandle() {
        List<Object> result = null;
        try
        {
            result = proceed();
        }
        catch (ParserConfigurationException e)
        {
            CheckstylePluginException.rethrow(e,
                    "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES); //$NON-NLS-1$
        }
        catch (SAXException e)
        {
            CheckstylePluginException.rethrow(e,
                    "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch (IOException e)
        {
            CheckstylePluginException.rethrow(e,
                    "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES); //$NON-NLS-1$
        }
        return result;
    }

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
