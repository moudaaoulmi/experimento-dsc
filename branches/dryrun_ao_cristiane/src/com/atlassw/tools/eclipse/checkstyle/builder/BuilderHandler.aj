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

public privileged aspect BuilderHandler{
    
    declare soft: CoreException: buildProjectJob_runHandler();
    pointcut buildProjectJob_runHandler(): execution (* BuildProjectJob.run(..)) ;
    
    IStatus around(): buildProjectJob_runHandler() {
        IStatus result = null;
        try{
           result = proceed();
        } catch (CoreException e) {
            result = e.getStatus();
        }
        return result;
    }
    
    declare soft: CheckstylePluginException: checkstyleBuilder_buildHandler();
    pointcut checkstyleBuilder_buildHandler(): execution(* CheckstyleBuilder.build(..)) ;
    
    IProject[] around() throws CoreException:  checkstyleBuilder_buildHandler() {
        IProject[] result = null;
        try{
            result = proceed();
        }catch(CheckstylePluginException e){
            Status status = new Status(IStatus.ERROR, 
                    CheckstylePlugin.PLUGIN_ID,
                    IStatus.ERROR,
                    e.getMessage() != null ? e.getMessage(): ErrorMessages.CheckstyleBuilder_msgErrorUnknown, e);
             throw new CoreException(status);
        }
        return result;
    }
    
    declare soft: CheckstylePluginException: checkstyleBuilder_handleBuildSelectionHandler();
    pointcut checkstyleBuilder_handleBuildSelectionHandler(): execution(* CheckstyleBuilder.handleBuildSelection(..));
    
    void around()throws CoreException: checkstyleBuilder_handleBuildSelectionHandler() {
       try{
           proceed();
       } catch (CheckstylePluginException e)
       {
           Status status = new Status(IStatus.ERROR, 
                   CheckstylePlugin.PLUGIN_ID, 
                   IStatus.ERROR, e.getLocalizedMessage(), e);
           throw new CoreException(status);
       }
    }

    declare soft: CoreException: auditor_addErrorHandle();
    pointcut auditor_addErrorHandle(): execution (* Auditor.CheckstyleAuditListener.addError(..)) ;
    
    void around(): auditor_addErrorHandle() {
        try{
            proceed();
        } catch (CoreException e) {
            checkstyleLogMessage(e);
        }
    }
    
    declare soft: BadLocationException: auditor_calculateMarkerOffsetHandle();
    pointcut auditor_calculateMarkerOffsetHandle(): 
        execution (* Auditor.CheckstyleAuditListener.calculateMarkerOffset(..)) ;
    
    void around(): auditor_calculateMarkerOffsetHandle() {
        try{
            proceed();
        } catch (BadLocationException e) {
         // seems to happen quite often so its no use to log since we
            // can't do anything about it
            // CheckstyleLog.log(e);
        }
    }
    
    declare soft: SAXException: packageNamesLoader_getPackageNameInteration3Handle();
    declare soft: IOException: packageNamesLoader_getPackageNameInteration3Handle();
    
    pointcut packageNamesLoader_getPackageNameInteration3Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration3(..)) ;
    
    void around(PackageNamesLoader nameLoader, URL aPackageFile): packageNamesLoader_getPackageNameInteration3Handle() 
            && args(nameLoader, aPackageFile) {
        try{
            proceed(nameLoader, aPackageFile);
        }catch (SAXException e) {
            checkstyleLogMessage(e, "unable to parse " + aPackageFile.toExternalForm() //$NON-NLS-1$
                    + " - " + e.getLocalizedMessage()); //$NON-NLS-1$
        }catch (IOException e) {
            checkstyleLogMessage(e, "unable to read " + aPackageFile.toExternalForm()); //$NON-NLS-1$
        }
    }
    
    declare soft: JavaModelException: projectClassLoader_addToClassPathHandle();
    pointcut projectClassLoader_addToClassPathHandle(): 
        execution (* ProjectClassLoader.addToClassPath(..)) ;
    
    void around(): projectClassLoader_addToClassPathHandle() {
        try{
            proceed();
        } catch (JavaModelException jme) {
            CheckstyleLog.log(jme);
        }
    }
    
    declare soft: MalformedURLException: projectClassLoader_handlePathHandle();
    pointcut projectClassLoader_handlePathHandle(): 
        execution (* ProjectClassLoader.handlePath(..)) ;
    
    void around(): projectClassLoader_handlePathHandle() {
        try{
            proceed();
        } catch (MalformedURLException mfe) { 
         // log the exception although this should not happen
            CheckstyleLog.log(mfe, mfe.getLocalizedMessage());
        }
    }
    
    
    private void checkstyleLogMessage(Exception e, String message) {
        CheckstyleLog.log(e,message);
    }
    
    private void checkstyleLogMessage(Exception e) {
        CheckstyleLog.log(e);
    }
    
    declare soft: ClassNotFoundException: packageObjectFactory_createObjectHandle();
    declare soft: InstantiationException: packageObjectFactory_createObjectHandle();
    declare soft: IllegalAccessException: packageObjectFactory_createObjectHandle();
    
    pointcut packageObjectFactory_createObjectHandle(): 
        execution (* PackageObjectFactory.createObject(..)) ;
    
    Object around(String aClassName)throws CheckstyleException: packageObjectFactory_createObjectHandle() && args(aClassName) {
        Object result = null;
        try{
            result = proceed(aClassName);
        } catch (ClassNotFoundException e) {
            throw new CheckstyleException("Unable to find class for " + aClassName, e); //$NON-NLS-1$
        }catch (InstantiationException e) {
            // /CLOVER:OFF
            throw new CheckstyleException("Unable to instantiate " + aClassName, e); //$NON-NLS-1$
         // /CLOVER:ON
        }catch (IllegalAccessException e) {
            // /CLOVER:OFF
            throw new CheckstyleException("Unable to instantiate " + aClassName, e); //$NON-NLS-1$
         // /CLOVER:ON
        }
        return result;
    }
 
    declare soft: CheckstyleException: packageObjectFactory_createModuleInternalHandle();
    
    pointcut packageObjectFactory_createModuleInternalHandle(): 
        execution (* PackageObjectFactory.createModuleInternal(..)) ;
    
    Object around(String aName)throws CheckstyleException: packageObjectFactory_createModuleInternalHandle() && args(aName) {
        Object result = null;
        try{
            result = proceed(aName);
        }catch(CheckstyleException e){
            throw new CheckstyleException("Unable to instantiate " + aName, e);//$NON-NLS-1$
        }
        return result;
    }
    
    declare soft: CheckstyleException: packageObjectFactory_createModuleHandle();
    
    pointcut packageObjectFactory_createModuleHandle(): 
        execution (* PackageObjectFactory.createModule(..)) ;
    
    Object around(String aName)throws CheckstyleException: packageObjectFactory_createModuleHandle() && args(aName) {
        Object result = null;
        try{
            result = proceed(aName);
        }catch(CheckstyleException e){
            PackageObjectFactory p = (PackageObjectFactory) thisJoinPoint.getThis();
            result = p.createModuleInternal(aName);
        }
        return result;
    }
    
    declare soft: CheckstyleException: packageObjectFactory_doMakeObjectHandle();
    
    pointcut packageObjectFactory_doMakeObjectHandle(): 
        execution (* PackageObjectFactory.doMakeObject(..)) ;
    
    Object around(String aName)throws CheckstyleException: packageObjectFactory_doMakeObjectHandle() && args(aName) {
        Object result = null;
        try{
            result = proceed(aName);
        }catch(CheckstyleException e){
            PackageObjectFactory p = (PackageObjectFactory) thisJoinPoint.getThis();
            result = p.doMakeObjectInternal(aName);
        }
        return result;
    }
    
    declare soft: CheckstyleException: packageObjectFactory_doMakeObjectInternal2Handle();
    
    pointcut packageObjectFactory_doMakeObjectInternal2Handle(): 
        execution (* PackageObjectFactory.doMakeObjectInternal2(..)) ;
    
    Object around(String className): packageObjectFactory_doMakeObjectInternal2Handle() && args(className) {
        Object result = null;
        try{
            result = proceed(className);
        }catch(CheckstyleException e){
           //Do nothing
        }
        return result;
    }
    
    declare soft: IOException: auditor_runAuditHandle();
    declare soft: CoreException: auditor_runAuditHandle();
    declare soft: CheckstyleException: auditor_runAuditHandle();
    declare soft: CoreException: checkstyleBuilder_buildProjectsHandleHandle();
    declare soft: IOException: packageNamesLoader_getPackageNameInteration1Handle();
    declare soft: ParserConfigurationException: packageNamesLoader_getPackageNamesHandle();
    declare soft: SAXException: packageNamesLoader_getPackageNamesHandle();
    declare soft: IOException: packageNamesLoader_getPackageNamesHandle();
    
    
    pointcut auditor_runAuditHandle(): 
        execution (* Auditor.runAudit(..)) ;
    pointcut checkstyleBuilder_buildProjectsHandleHandle(): execution(* CheckstyleBuilder.buildProjectsHandle(..));
    pointcut packageNamesLoader_getPackageNameInteration1Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration1(..)) ;
    pointcut packageNamesLoader_getPackageNamesHandle(): 
        execution (* PackageNamesLoader.getPackageNames(..)) ;
    
    void around()throws CheckstylePluginException: auditor_runAuditHandle() {
        try{
            proceed();
        } catch (IOException e) {
            this.retrowException(e);
        }catch (CoreException e) {
            this.retrowException(e);
        }catch (CheckstyleException e) {
            this.retrowException(e);
        }
    }
    
   
    void around() throws CheckstylePluginException: checkstyleBuilder_buildProjectsHandleHandle() {
       try{
           proceed();
       } catch (CoreException e)
       {
           this.retrowException(e);
       }
    }
    
    void around()throws CheckstylePluginException: packageNamesLoader_getPackageNameInteration1Handle() {
        try{
            proceed();
        }catch (IOException e) {
            retrowException(e); //$NON-NLS-1$
        }
    }   
   
    
    List around()throws CheckstylePluginException: packageNamesLoader_getPackageNamesHandle() {
        List result = null;
        try{
            result = proceed();
        } catch (ParserConfigurationException e) {
            retrowException(e, "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES); //$NON-NLS-1$
        }catch (SAXException e) {
            retrowException(e, "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }catch (IOException e) {
            retrowException(e, "unable to parse " + PackageNamesLoader.DEFAULT_PACKAGES); //$NON-NLS-1$
        }
        return result;
    }
    
    private void retrowException(Exception e) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e);
    }
    
    private void retrowException(Exception e, String message) throws CheckstylePluginException {
        CheckstylePluginException.rethrow(e, message);
    }
    
    declare soft: CheckstylePluginException: runCheckstyleOnFilesJob_runInWorkspaceHandle();
    pointcut runCheckstyleOnFilesJob_runInWorkspaceHandle(): 
        execution (* RunCheckstyleOnFilesJob.runInWorkspace(..)) ;
    
    IStatus around() throws CoreException: runCheckstyleOnFilesJob_runInWorkspaceHandle() {
        try{
            proceed();
        } catch (CheckstylePluginException e) { 
            Status status = new Status(IStatus.ERROR, CheckstylePlugin.PLUGIN_ID, IStatus.ERROR, e
                    .getLocalizedMessage(), e);
            throw new CoreException(status);
        }
        return null;
    }

}
