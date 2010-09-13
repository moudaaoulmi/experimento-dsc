package com.atlassw.tools.eclipse.checkstyle.exception;

import java.io.BufferedOutputStream;
import br.upe.dsc.reusable.exception.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.xml.sax.SAXException;

import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.builder.Auditor;
import com.atlassw.tools.eclipse.checkstyle.builder.CheckerFactory;
import com.atlassw.tools.eclipse.checkstyle.builder.CheckstyleBuilder;
import com.atlassw.tools.eclipse.checkstyle.builder.PackageNamesLoader;
import com.atlassw.tools.eclipse.checkstyle.builder.Auditor.CheckstyleAuditListener;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationTester;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.config.CheckstyleConfigurationFile;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigProperty;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigurationReader;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigurationWriter;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.config.Module;
import com.atlassw.tools.eclipse.checkstyle.config.ResolvableProperty;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ConfigurationType;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ExternalFileConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.InternalConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ProjectConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.RemoteConfigurationType;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ResourceBundlePropertyResolver;
import com.atlassw.tools.eclipse.checkstyle.config.gui.RuleConfigurationEditDialog;
import com.atlassw.tools.eclipse.checkstyle.config.meta.MetadataFactory;
import com.atlassw.tools.eclipse.checkstyle.config.migration.CheckConfigurationMigrator;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileMatchPattern;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileSet;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.IProjectConfiguration;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfiguration;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.AbstractFilter;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.util.CustomLibrariesClassLoader;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.PropertyResolver;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Filter;

public privileged aspect GeneralExceptionHandler extends EmptyBlockAbstractExceptionHandling
{

    private Map inStream = new HashMap();
    private Map byteOut = new HashMap();
    private Map outStream = new HashMap();
    private Map checker = new HashMap();
    private Map list = new HashMap();

    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: Exception : 
    ConfigurationType_getResolvedConfigurationFileURLHandler() ||
    ConfigurationType_getCheckstyleConfigurationHandler() ||
    FileMatchPattern_internalSetMatchPatternHandler() ||
    RetrowException_exportConfigurationHandle() ||
    RetrowException_endElementHandle();

    declare soft: CoreException: ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
    checkstyleBuilder_buildProjectsHandleHandle() ||
    ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
    auditor_runAuditHandle() ||
    RemoteConfigurationType_removeCachedAuthInfoHandler()||
    setModules2();

    declare soft: CheckstylePluginException: 
    checkConfigurationMigrator_endElementHandler() ||
    checkConfigurationMigrator_startElementHandler() ||
    ProjectConfigurationFactory_startElementHandler();

    declare soft: IOException: ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
    ExternalFileConfiguration_internalEnsureFileExistsHandler() ||
    checkConfigurationMigrator_ensureFileExistsHandler() ||
    checkConfigurationMigrator_migrateHandler() ||
    RetrowException_runHandle() ||
    ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
    PackageNamesLoader_getPackageNames() ||
    auditor_runAuditHandle() ||
    CustomLibrariesClassLoader_get() ||
    RetrowException_setModulesHandle() ||
    RemoteConfigurationType_internalGetCheckstyleConfigurationHandler() ||
    RetrowException_resolveEntityHandleHandle() ||
    metadataFactory_resolveEntityHandler();

    declare soft: SAXException : checkConfigurationMigrator_migrateHandler() ||
    RetrowException_runHandle() ||
    ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
    RetrowException_writeHandle();

    declare soft: ParserConfigurationException : checkConfigurationMigrator_migrateHandler() ||
    RetrowException_runHandle() ||
    ProjectConfigurationFactory_internalLoadFromPersistenceHandler();

    declare soft : CloneNotSupportedException : FileMatchPattern_cloneHandler() || 
    FileMatchPattern_cloneFileSetHandler() || 
    FileMatchPattern_cloneProjectHandler() || 
    FileMatchPattern_cloneWorkingCopyHandler() ||
    AbstractFilter_cloneHandler() ||
    cloneHandle();

    declare soft: CheckstyleException: auditor_runAuditHandle() ||
    RetrowException_getUnresolvedPropertiesIterationHandle();

    declare soft: UnknownHostException: RemoteConfigurationType_internalGetCheckstyleConfigurationHandler();

    declare soft: FileNotFoundException: RemoteConfigurationType_internalGetCheckstyleConfigurationHandler();

    declare soft: IllegalAccessException: RemoteConfigurationType_internalGetDefaultHandler();

    declare soft: TransformerConfigurationException: RetrowException_writeHandle();

    
    // ---------------------------
    // Pointcut's
    // ---------------------------
    public pointcut emptyBlockException(): setModules2() 
                                           || ResourceBundlePropertyResolver_resolveHandle() 
                                           || metadataFactory_getMetadataI18NBundleHandler();
    
    pointcut getInput():
        ((  call(* IFile.getContents(..)) &&
                withincode (* ProjectConfigurationFactory.internalLoadFromPersistence(..))) ||
                (  call (* ICheckConfiguration.getCheckstyleConfiguration(..)) &&
                        withincode(* CheckConfigurationFactory.internalExportConfiguration(..))) ||
                        (  call(* CheckstyleConfigurationFile.getCheckConfigFileStream(..)) &&
                                withincode(* CheckConfigurationTester.getUnresolvedPropertiesIteration(..)))  );

    pointcut getOutStream():
        ((  call(BufferedOutputStream.new(..)) &&
                withincode(* CheckConfigurationWorkingCopy.setModules(..))  ) ||
                (   call(BufferedOutputStream.new(..)) &&
                        withincode(* ProjectConfigurationEditor.internalEnsureFileExists(..))   ) ||
                        (   call(BufferedOutputStream.new(..)) &&
                                withincode(* ExternalFileConfigurationEditor.internalEnsureFileExists(..))  ) ||   
                                (   call(BufferedOutputStream.new(..)) &&
                                        withincode(* InternalConfigurationEditor.internalEnsureFileExists(..))   )  ||
                                        (   call(BufferedOutputStream.new(..)) &&
                                                withincode(* CheckConfigurationMigrator.OldConfigurationHandler.internalEnsureFileExists(..))   )   ||
                                                (   call(BufferedOutputStream.new(..)) &&
                                                        withincode(* CheckConfigurationFactory.internalExportConfiguration(..)))
        );

    pointcut getByteOut():
        call(ByteArrayOutputStream.new(..)) &&
        withincode(* CheckConfigurationWorkingCopy.setModules(..)); 

    pointcut getChecker():
        call(* CheckerFactory.createChecker(..)) &&
        withincode(* Auditor.internalRunAudit(..));

    pointcut getList():
        call(CheckstyleAuditListener.new(..)) &&
        withincode(* Auditor.internalRunAudit(..));

    pointcut setModules2():
        call(* IResource.refreshLocal(..)) && 
        withincode(* CheckConfigurationWorkingCopy.setModules(..));

    pointcut metadataFactory_resolveEntityHandler() : 
        execution(* MetadataFactory.MetaDataHandler.resolveEntity(..));

    pointcut RetrowException_resolveEntityHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.resolveEntity(..)) ;

    pointcut ProjectConfigurationFactory_startElementHandler(): 
        execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.startElement(..));

    pointcut RetrowException_runHandle(): 
        execution (* ConfigurationReader.read(..)) ;

    pointcut checkConfigurationMigrator_migrateHandler(): 
        execution(* CheckConfigurationMigrator.migrate(..));

    pointcut ProjectConfigurationFactory_internalLoadFromPersistenceHandler(): 
        execution(* ProjectConfigurationFactory.internalLoadFromPersistence(..));

    
   

    /*
     * A principio (codigo original) nem precisava do tratador, dessa forma que
     * estava afetando o metodo todo, ele mudava o comportamento, pois tava
     * tratando (super.start(context)) o que na verdade é para relançar, como
     * está no codigo original. pointcut CheckstylePlugin_startHandle():
     * execution( CheckstylePlugin.start(..));
     */
   
   

    pointcut FileMatchPattern_internalSetMatchPatternHandler() :
        execution (* FileMatchPattern.internalSetMatchPattern(..));

    pointcut ConfigurationType_getResolvedConfigurationFileURLHandler() : 
        execution (* ConfigurationType.getResolvedConfigurationFileURL(..)) &&
        within(ConfigurationType);

    pointcut ConfigurationType_getCheckstyleConfigurationHandler() : 
        execution (* ConfigurationType.getCheckstyleConfiguration(..)) &&
        within(ConfigurationType);

    pointcut ProjectConfigurationEditor_internalEnsureFileExistsHandler():
        execution(* ProjectConfigurationEditor.internalEnsureFileExists(..));

    pointcut ExternalFileConfiguration_internalEnsureFileExistsHandler():
        execution(* ExternalFileConfigurationEditor.internalEnsureFileExists(..)) ||
        execution(* InternalConfigurationEditor.internalEnsureFileExists(..));

    pointcut checkConfigurationMigrator_ensureFileExistsHandler() : 
        execution(* CheckConfigurationMigrator.OldConfigurationHandler.internalEnsureFileExists(..));

    pointcut checkstyleBuilder_buildProjectsHandleHandle(): 
        execution(* CheckstyleBuilder.buildProjectsHandle(..));

    pointcut FileMatchPattern_cloneHandler(): 
        execution(* FileMatchPattern.clone(..));

    pointcut FileMatchPattern_cloneFileSetHandler(): 
        execution(* FileSet.clone(..));

    pointcut FileMatchPattern_cloneProjectHandler(): 
        execution(* ProjectConfiguration.clone(..));

    pointcut FileMatchPattern_cloneWorkingCopyHandler(): 
        execution(* ProjectConfigurationWorkingCopy.clone(..));

    pointcut AbstractFilter_cloneHandler(): 
        execution(* AbstractFilter.clone(..));

    pointcut cloneHandle(): 
        execution (* CheckConfigurationWorkingCopy.clone(..)) || 
        execution (* Module.clone(..)) || 
        execution (* ConfigProperty.clone(..)) ||
        execution (* ResolvableProperty.clone(..)) ;

    pointcut auditor_runAuditHandle(): 
        execution (* Auditor.internalRunAudit(..)) ;

    pointcut PackageNamesLoader_getPackageNames():
        execution(* PackageNamesLoader.getPackageNames(..));

    pointcut CustomLibrariesClassLoader_get():
        execution(* CustomLibrariesClassLoader.get(..));

    pointcut RetrowException_setModulesHandle(): 
        execution (* CheckConfigurationWorkingCopy.setModules(..)) ;

    pointcut RetrowException_exportConfigurationHandle(): 
        execution (* CheckConfigurationFactory.internalExportConfiguration(..)) ;

    pointcut RemoteConfigurationType_internalGetCheckstyleConfigurationHandler():
        execution (* RemoteConfigurationType.internalGetCheckstyleConfiguration(..));

    pointcut RemoteConfigurationType_internalGetDefaultHandler() : 
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.getDefault(..));

    pointcut RuleConfigurationEditDialog_okPressedHandler():
        call(* intrenalOkPressesGetSelection(..)) &&
        withincode(* RuleConfigurationEditDialog.okPressed(..));

    pointcut RetrowException_getUnresolvedPropertiesIterationHandle(): 
        execution (* CheckConfigurationTester.getUnresolvedPropertiesIteration(..));
   
    pointcut RemoteConfigurationType_removeCachedAuthInfoHandler():
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.removeCachedAuthInfo(..));

    pointcut RetrowException_writeHandle(): 
        execution (* ConfigurationWriter.write(..)) ;

    pointcut RetrowException_endElementHandle(): 
        execution (* CheckConfigurationFactory.CheckConfigurationsFileHandler.endElement(..));

    pointcut ProjectConfigurationFactory_endElementHandler(): 
        execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.endElement(..));

    pointcut checkConfigurationMigrator_startElementHandler(): 
        execution(* CheckConfigurationMigrator.OldConfigurationHandler.startElement(..));

    pointcut checkConfigurationMigrator_endElementHandler() : 
        execution(* CheckConfigurationMigrator.OldConfigurationHandler.endElement(..));

    pointcut metadataFactory_getMetadataI18NBundleHandler() : 
        execution(* MetadataFactory.getMetadataI18NBundle(..));

    pointcut ResourceBundlePropertyResolver_resolveHandle(): 
        execution(* ResourceBundlePropertyResolver.resolve(..));
    // ---------------------------
    // Advice's
    // ---------------------------

    Object around(): getByteOut() {
        Object byteout = proceed();
        byteOut.put(Thread.currentThread().getName(), byteout);
        return byteout;
    }

    Object around (): getOutStream(){
        Object out = proceed();
        outStream.put(Thread.currentThread().getName(), out);
        return out;
    }

    Object around(): getInput(){
        Object input = proceed();
        inStream.put(Thread.currentThread().getName(), input);
        return input;
    }

    Object around(): getChecker(){
        Object check = proceed();
        checker.put(Thread.currentThread().getName(), check);
        return check;
    }

    Object around(): getList(){
        Object listener = proceed();
        list.put(Thread.currentThread().getName(), listener);
        return listener;
    }

//    Object around(): setModules2(){
//        try{
//            return proceed();
//        }catch(CoreException e){
//            // NOOP - just ignore
//        }
//        return null;
//    }
//    Object around(): ResourceBundlePropertyResolver_resolveHandle() ||
//    metadataFactory_getMetadataI18NBundleHandler(){
//        Object result = null;
//        try
//        {
//            result = proceed();
//        }
//        catch (MissingResourceException e)
//        {
//            // ignore
//        }
//        return result;
//    }

    CheckstyleConfigurationFile around(CheckstyleConfigurationFile data, String currentRedirects,
            Authenticator oldAuthenticator, ICheckConfiguration checkConfiguration,
            boolean useCacheFile) throws CheckstylePluginException: 
                RemoteConfigurationType_internalGetCheckstyleConfigurationHandler() &&
                args(data, currentRedirects, oldAuthenticator, checkConfiguration, useCacheFile){
        CheckstyleConfigurationFile result = null;
        try
        {
            result = proceed(data, currentRedirects, oldAuthenticator, checkConfiguration,
                    useCacheFile);
        }
        finally
        {
            Authenticator.setDefault(oldAuthenticator);

            if (currentRedirects != null)
            {
                System.setProperty("http.maxRedirects", currentRedirects); //$NON-NLS-1$
            }
            else
            {
                System.getProperties().remove("http.maxRedirects"); //$NON-NLS-1$
            }
        }
        return result;
    }

    void around() throws CheckstylePluginException: RetrowException_setModulesHandle(){
        try
        {
            proceed();
        }
        finally
        {
            IOUtils.closeQuietly((ByteArrayOutputStream)this.byteOut.get(Thread.currentThread().getName()));
            IOUtils.closeQuietly((OutputStream)this.outStream.get(Thread.currentThread().getName()));
        }
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
            Checker check = (Checker)this.checker.get(Thread.currentThread().getName());
            monitor.done();
            // Cleanup listener and filter
            if (check != null)
            {
                check.removeListener((AuditListener)this.list.get(Thread.currentThread().getName()));
                check.removeFilter(runtimeExceptionFilter);
            }
            // restore the original classloader
            Thread.currentThread().setContextClassLoader(contextClassloader);
        }
    }

    void around() throws CheckstylePluginException: 
        (   ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
                ExternalFileConfiguration_internalEnsureFileExistsHandler() ||
                checkConfigurationMigrator_ensureFileExistsHandler() ) {
        try
        {
            proceed();
        }
        finally
        {
            IOUtils.closeQuietly((OutputStream)this.outStream.get(Thread.currentThread().getName()));
        }
    }

    IProjectConfiguration around(IProject project,
            IProjectConfiguration configuration, IFile file, InputStream inStream) 
    throws CheckstylePluginException : 
        ProjectConfigurationFactory_internalLoadFromPersistenceHandler()
        && args(project, configuration,  file,  inStream){
        IProjectConfiguration result = configuration;
        try
        {
            result = proceed(project, configuration,  file,  inStream);
        }
        finally
        {
            IOUtils.closeQuietly((InputStream)this.inStream.get(Thread.currentThread().getName()));
        }
        return result;
    }

    Object around() throws CheckstylePluginException: checkstyleBuilder_buildProjectsHandleHandle()||
    ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
    ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
    auditor_runAuditHandle() ||
    RemoteConfigurationType_removeCachedAuthInfoHandler(){
        Object result = null;
        try {
            result = proceed();
        } catch (CoreException e) {
            CheckstylePluginException.rethrow(e);
        }
        return result;
    }

    Object around() throws CheckstylePluginException: 
        RetrowException_runHandle() ||
        ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
        checkConfigurationMigrator_migrateHandler() ||
        ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
        ExternalFileConfiguration_internalEnsureFileExistsHandler() ||
        checkConfigurationMigrator_ensureFileExistsHandler() ||
        auditor_runAuditHandle() ||
        PackageNamesLoader_getPackageNames() || 
        CustomLibrariesClassLoader_get() ||
        RetrowException_setModulesHandle() ||
        RemoteConfigurationType_internalGetCheckstyleConfigurationHandler()
        {
        Object result = null;
        try {
            result = proceed();
        } catch (IOException ioe) {
            CheckstylePluginException.rethrow(ioe);
        }
        return result;
        }

    CheckstyleConfigurationFile around() throws CheckstylePluginException: 
        RemoteConfigurationType_internalGetCheckstyleConfigurationHandler(){
        CheckstyleConfigurationFile result = null;
        try {
            result = proceed();
        } catch (UnknownHostException e) {
            CheckstylePluginException.rethrow(e, NLS.bind(
                    ErrorMessages.RemoteConfigurationType_errorUnknownHost, e.getMessage()));
        } catch (FileNotFoundException e) {
            CheckstylePluginException.rethrow(e, NLS.bind(
                    ErrorMessages.RemoteConfigurationType_errorFileNotFound, e.getMessage()));
        }
        return result;
    }
    
    void around() throws CheckstylePluginException: RetrowException_exportConfigurationHandle()
    {
        try
        {
            proceed();
        }
        finally
        {
            IOUtils.closeQuietly((InputStream)this.inStream.get(Thread.currentThread().getName()));
            IOUtils.closeQuietly((OutputStream)this.outStream.get(Thread.currentThread().getName()));
        }
    }

    Object around() throws CheckstylePluginException: 
        ConfigurationType_getResolvedConfigurationFileURLHandler()||
        ConfigurationType_getCheckstyleConfigurationHandler() ||
        RetrowException_exportConfigurationHandle() 
        {
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (Exception e)
        {
            CheckstylePluginException.rethrow(e);
        }
        return result;

        }

    Object around() : FileMatchPattern_cloneHandler() || 
    FileMatchPattern_cloneFileSetHandler() || 
    FileMatchPattern_cloneProjectHandler() || 
    FileMatchPattern_cloneWorkingCopyHandler() ||
    AbstractFilter_cloneHandler() ||
    cloneHandle(){
        try
        {
            return proceed();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(); // should never happen
        }
    }

    void around() throws CheckstylePluginException: RetrowException_writeHandle() {
        try
        {
            proceed();
        }
        catch (TransformerConfigurationException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }

    Object around() throws CheckstylePluginException: 
        RetrowException_runHandle() ||
        ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
        checkConfigurationMigrator_migrateHandler()
        {
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (ParserConfigurationException pe)
        {
            CheckstylePluginException.rethrow(pe);
        }
        return result;
        }

    Object around() throws CheckstylePluginException: 
        RetrowException_runHandle() ||
        ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
        checkConfigurationMigrator_migrateHandler() ||
        RetrowException_writeHandle(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (SAXException se)
        {
            Exception ex = se.getException() != null ? se.getException() : se;
            CheckstylePluginException.rethrow(ex);
        }
        return result;
    }

    Object around(Object obj): 
        (RuleConfigurationEditDialog_okPressedHandler() ||
                RemoteConfigurationType_internalGetDefaultHandler()) &&
                args(obj){
        try
        {
            obj = proceed(obj);
        }
        catch (IllegalArgumentException e)
        {
            CheckstyleLog.log(e);
        }
        return obj;
    }


    void around(CheckstyleConfigurationFile configFile, PropertyResolver resolver,
            ClassLoader contextClassloader, InputStream in) throws CheckstylePluginException:
                RetrowException_getUnresolvedPropertiesIterationHandle() &&
                args(configFile, resolver, contextClassloader, in){
        try
        {
            proceed(configFile, resolver, contextClassloader, in);
        }
        finally
        {
            IOUtils.closeQuietly((InputStream)this.inStream.get(Thread.currentThread().getName()));
            // restore the original classloader
            Thread.currentThread().setContextClassLoader(contextClassloader);
        }

    }

    void around() throws CheckstylePluginException:
        RetrowException_getUnresolvedPropertiesIterationHandle() ||
        auditor_runAuditHandle()
        {
        try
        {
            proceed();
        }
        catch (CheckstyleException e)
        {
            CheckstylePluginException.rethrow(e);
        }
        }

    void around() throws SAXException : RetrowException_endElementHandle() ||
    ProjectConfigurationFactory_endElementHandler() {
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            throw new SAXException(e);
        }
    }

    Object around() throws SAXException: 
        RetrowException_resolveEntityHandleHandle() ||
        metadataFactory_resolveEntityHandler(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (IOException e)
        {
            throw new SAXException("" + e, e);
        }
        return result;
    }

    void around() throws SAXException : checkConfigurationMigrator_startElementHandler() || 
    checkConfigurationMigrator_endElementHandler() ||
    ProjectConfigurationFactory_startElementHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            throw new SAXException(e);
        }
    }

    Authenticator around(): RemoteConfigurationType_internalGetDefaultHandler(){
        Authenticator currentDefault = null;
        try {
            currentDefault = proceed();
        }
        catch (IllegalArgumentException e) {
            //XXX LOG - não generalizar
            CheckstyleLog.log(e);
        }
        return currentDefault;
    }
}
