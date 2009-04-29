
package com.atlassw.tools.eclipse.checkstyle.exception;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.service.prefs.BackingStoreException;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.JavaModelException;
import com.puppycrawl.tools.checkstyle.PropertyResolver;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Filter;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ConfigurationTypes;
import com.atlassw.tools.eclipse.checkstyle.builder.Auditor;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationWorkingSetEditor;
import com.atlassw.tools.eclipse.checkstyle.config.meta.MetadataFactory;
import com.atlassw.tools.eclipse.checkstyle.config.savefilter.SaveFilters;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationConfigureDialog;
import com.atlassw.tools.eclipse.checkstyle.config.gui.RuleConfigurationEditDialog;
import com.atlassw.tools.eclipse.checkstyle.preferences.PrefsInitializer;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.IProjectConfiguration;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.PluginFilters;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ConfigurationType;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileMatchPattern;
import com.atlassw.tools.eclipse.checkstyle.builder.PackageNamesLoader;
import com.atlassw.tools.eclipse.checkstyle.util.CustomLibrariesClassLoader;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ExternalFileConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.InternalConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ProjectConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.migration.CheckConfigurationMigrator;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationTester;

import java.net.Authenticator;

import com.atlassw.tools.eclipse.checkstyle.builder.CheckstyleBuilder;
import com.atlassw.tools.eclipse.checkstyle.config.CheckstyleConfigurationFile;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigurationReader;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.builder.ProjectClassLoader;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.PackageFilterEditor;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.NonSrcDirsFilter;
import com.atlassw.tools.eclipse.checkstyle.properties.FileSetEditDialog;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.CheckFileOnOpenPartListener;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.RemoteConfigurationType;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.AbstractFilter;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileSet;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfiguration;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationWorkingCopy;
import java.net.UnknownHostException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.SelectionListener;
import org.osgi.service.prefs.Preferences;
import com.atlassw.tools.eclipse.checkstyle.exception.ExceptionHandler;

import com.atlassw.tools.eclipse.checkstyle.config.Module;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigProperty;
import com.atlassw.tools.eclipse.checkstyle.config.ResolvableProperty;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigurationWriter;

public privileged aspect GeneralExceptionHandler
{
    
    private Map inStream = new HashMap();
    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: Exception : ConfigurationType_internalStaticHandler() || 
                              saveFilters_internalHandler()||  
                              PluginFilters_internalHandler() ||
                              ConfigurationType_getResolvedConfigurationFileURLHandler() ||
                              ConfigurationType_getCheckstyleConfigurationHandler() ||
                              FileMatchPattern_internalSetMatchPatternHandler() ||
                              RetrowException_exportConfigurationHandle() ||
                              RetrowException_endElementHandle();

    declare soft: CoreException: ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
                                 checkstyleBuilder_buildProjectsHandleHandle() ||
                                 ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
                                 CheckFileOnOpenPartListener_partClosedHandler() ||
                                 CheckFileOnOpenPartListener_isFileAffectedHandler() ||
                                 NonSrcDirsFilter_getSourceDirPathsHandler() ||
                                 internalRunHandler() ||
                                 auditor_addErrorHandle() ||
                                 RemoteConfigurationType_storeCredentialsHandler() ||
                                 auditor_runAuditHandle() ||
                                 FileSetEditDialog_runHandler() ||
                                 RemoteConfigurationType_removeCachedAuthInfoHandler();

    declare soft: CheckstylePluginException: internalSelectionChanged_2Handler() || 
                                             RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler() || 
                                             metadataFactory_refreshHandler() ||
                                             CheckFileOnOpenPartListener_isFileAffectedHandler() ||
                                             CheckstyleLogMessage_refreshHandle() ||
                                             checkConfigurationMigrator_endElementHandler() ||
                                             checkConfigurationMigrator_startElementHandler() ||
                                             ProjectConfigurationFactory_startElementHandler();
    
    declare soft: BackingStoreException: internalCreateButtonBarHandler() || 
                                         internalWidgetSelectedHandler() || 
                                         PrefsInitializer_internalinitializeDefaultPreferencesHandler();

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

    declare soft: JavaModelException: NonSrcDirsFilter_getSourceDirPathsHandler() ||
                                      SourceFolderContentProvider_handleProjectHandler() || 
                                      SourceFolderContentProvider_handleContainerHandler() ||
                                      projectClassLoader_addToClassPathHandle();

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
    pointcut getInput():
        call(* IFile.getContents(..)) &&
        withincode (* ProjectConfigurationFactory.internalLoadFromPersistence(..));
    
    pointcut metadataFactory_resolveEntityHandler() : 
        execution(* MetadataFactory.MetaDataHandler.resolveEntity(..));
    
    pointcut RetrowException_resolveEntityHandleHandle(): 
        execution (* ConfigurationReader.ConfigurationHandler.resolveEntity(..)) ;
    
    pointcut ProjectConfigurationFactory_startElementHandler(): 
        execution(* ProjectConfigurationFactory.ProjectConfigFileHandler.startElement(..));

    pointcut internalRunHandler(): 
        call(* FileSetEditDialog.getFiles(..)) &&
        withincode(* FileSetEditDialog.internalRun(..));

    pointcut CheckFileOnOpenPartListener_partClosedHandler(): 
        execution(* CheckFileOnOpenPartListener.partClosed(..));

    pointcut CheckFileOnOpenPartListener_isFileAffectedHandler(): 
        execution(* CheckFileOnOpenPartListener.isFileAffected(..));

    pointcut projectClassLoader_addToClassPathHandle(): 
        execution (* ProjectClassLoader.addToClassPath(..)) ;

    pointcut SourceFolderContentProvider_handleContainerHandler(): 
        execution(* PackageFilterEditor.SourceFolderContentProvider.handleContainer(..));

    pointcut NonSrcDirsFilter_getSourceDirPathsHandler(): 
        execution(* NonSrcDirsFilter.getSourceDirPaths(..));

    pointcut SourceFolderContentProvider_handleProjectHandler(): 
        execution(* PackageFilterEditor.SourceFolderContentProvider.handleProject(..));

    pointcut RetrowException_runHandle(): 
        execution (* ConfigurationReader.read(..)) ;

    pointcut checkConfigurationMigrator_migrateHandler(): 
        execution(* CheckConfigurationMigrator.migrate(..));

    pointcut ProjectConfigurationFactory_internalLoadFromPersistenceHandler(): 
        execution(* ProjectConfigurationFactory.internalLoadFromPersistence(..));

    pointcut ConfigurationType_internalStaticHandler():
        execution(* ConfigurationTypes.internalStatic(..));

    /*
     * A principio (codigo original) nem precisava do tratador, dessa forma que
     * estava afetando o metodo todo, ele mudava o comportamento, pois tava
     * tratando (super.start(context)) o que na verdade é para relançar, como
     * está no codigo original. pointcut CheckstylePlugin_startHandle():
     * execution( CheckstylePlugin.start(..));
     */
    pointcut auditor_addErrorHandle(): 
        execution (* Auditor.CheckstyleAuditListener.addError(..)) ;

    pointcut RemoteConfigurationType_storeCredentialsHandler():
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.storeCredentials(..));

    pointcut internalSelectionChanged_2Handler():
        execution(* CheckConfigurationWorkingSetEditor.PageController.internalSelectionChanged(..));

    pointcut RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler(): 
        execution(* RemoteConfigurationType.secInternalGetBytesFromURLConnection(..));

    pointcut metadataFactory_refreshHandler() : 
        call (* MetadataFactory.doInitialization(..)) && withincode(* MetadataFactory.refresh(..));

    pointcut saveFilters_internalHandler() : 
        execution(* SaveFilters.internal(..));

    pointcut internalCreateButtonBarHandler():
        execution(* CheckConfigurationConfigureDialog.internalCreateButtonBar(..));

    pointcut internalWidgetSelectedHandler():
        (execution(* RuleConfigurationEditDialog.SelectionListenerImplementation.widgetSelected(..)) ) ||
        (execution(* RuleConfigurationEditDialog.internalFlush(..)));

    pointcut PrefsInitializer_internalinitializeDefaultPreferencesHandler() : 
        execution(* PrefsInitializer.initializeDefaultPreferences(..));

    pointcut PluginFilters_internalHandler() :
        execution(* PluginFilters.internal(..));

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
        execution (* CheckConfigurationWorkingCopy.internalSetModules(..)) ;

    pointcut RetrowException_exportConfigurationHandle(): 
        execution (* CheckConfigurationFactory.internalExportConfiguration(..)) ;

    pointcut FileSetEditDialog_runHandler(): 
        execution(* FileSetEditDialog.internalRun(..));

    pointcut RemoteConfigurationType_internalGetCheckstyleConfigurationHandler():
        execution (* RemoteConfigurationType.internalGetCheckstyleConfiguration(..));

    pointcut RemoteConfigurationType_internalGetDefaultHandler() : 
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.getDefault(..));

    pointcut RuleConfigurationEditDialog_okPressedHandler():
        call(* intrenalOkPressesGetSelection(..)) &&
        withincode(* RuleConfigurationEditDialog.okPressed(..));

    pointcut RetrowException_getUnresolvedPropertiesIterationHandle(): 
        execution (* CheckConfigurationTester.getUnresolvedPropertiesIteration(..));

    pointcut CheckstyleLogMessage_refreshHandle(): 
        execution (* CheckConfigurationFactory.refresh(..)) ;

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

    Object around(): getInput(){
        Object input = proceed();
        inStream.put(Thread.currentThread().getName(), input);
        return input;
    }
    
    Object around(): ResourceBundlePropertyResolver_resolveHandle() ||
                     metadataFactory_getMetadataI18NBundleHandler(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (MissingResourceException e)
        {
            // ignore
        }
        return result;
    }
    
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

    void around(Object modules, OutputStream out, ByteArrayOutputStream byteOut)
        throws CheckstylePluginException: RetrowException_setModulesHandle() &&
    args(modules, out, byteOut){
        try
        {
            proceed(modules, out, byteOut);
        }
        finally
        {
            IOUtils.closeQuietly(byteOut);
            IOUtils.closeQuietly(out);
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

    void around(Object file, OutputStream out) throws CheckstylePluginException: 
        (   ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
            ExternalFileConfiguration_internalEnsureFileExistsHandler() ||
            checkConfigurationMigrator_ensureFileExistsHandler() ) && 
        args(file, out){

        try
        {
            proceed(file, out);
        }
        finally
        {
            IOUtils.closeQuietly(out);
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

    Object around(): CheckFileOnOpenPartListener_partClosedHandler() ||
                     CheckFileOnOpenPartListener_isFileAffectedHandler() ||
                     NonSrcDirsFilter_getSourceDirPathsHandler() ||
                     internalRunHandler() ||
                     auditor_addErrorHandle() ||
                     RemoteConfigurationType_storeCredentialsHandler() ||
                     FileSetEditDialog_runHandler(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CoreException e)
        {
            CheckstyleLog.log(e);
        }
        return result;
    }

    Object around() : NonSrcDirsFilter_getSourceDirPathsHandler() ||
                      SourceFolderContentProvider_handleProjectHandler() || 
                      SourceFolderContentProvider_handleContainerHandler() ||
                      projectClassLoader_addToClassPathHandle(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (JavaModelException e)
        {
            CheckstyleLog.log(e);
        }
        return result;
    }

    Object around() throws CheckstylePluginException: checkstyleBuilder_buildProjectsHandleHandle()||
                                                      ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
                                                      ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
                                                      auditor_runAuditHandle() ||
                                                      RemoteConfigurationType_removeCachedAuthInfoHandler(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CoreException e)
        {
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
        try
        {
            result = proceed();
        }
        catch (IOException ioe)
        {
            CheckstylePluginException.rethrow(ioe);
        }
        return result;
    }

    CheckstyleConfigurationFile around() throws CheckstylePluginException: 
        RemoteConfigurationType_internalGetCheckstyleConfigurationHandler(){
        CheckstyleConfigurationFile result = null;
        try
        {
            result = proceed();
        }
        catch (UnknownHostException e)
        {
            CheckstylePluginException.rethrow(e, NLS.bind(
                    ErrorMessages.RemoteConfigurationType_errorUnknownHost, e.getMessage()));
        }
        catch (FileNotFoundException e)
        {
            CheckstylePluginException.rethrow(e, NLS.bind(
                    ErrorMessages.RemoteConfigurationType_errorFileNotFound, e.getMessage()));
        }
        return result;
    }

    Object around(): RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler() || 
                   internalSelectionChanged_2Handler() || 
                   metadataFactory_refreshHandler() ||
                   CheckFileOnOpenPartListener_isFileAffectedHandler() ||
                   CheckstyleLogMessage_refreshHandle()
    {
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
        }
        return result;
    }

    void around(): internalCreateButtonBarHandler() || 
                   internalWidgetSelectedHandler() || 
                   PrefsInitializer_internalinitializeDefaultPreferencesHandler() 
    {
        try
        {
            proceed();
        }
        catch (BackingStoreException e1)
        {
            CheckstyleLog.log(e1);
        }
    }

    // Esses com catch (Exception) só estão aqui sendo reusados pq os pointcuts
    // deles passaram a não mais existir em outros aspectos.
    // se existissem, teria que acontecer uma precedencia entre os aspectos.
    void around(): ConfigurationType_internalStaticHandler() || 
                   saveFilters_internalHandler() ||
                   PluginFilters_internalHandler()
    {
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            CheckstyleLog.log(e);
        }
    }

    void around(ICheckConfiguration config, File file, InputStream in, OutputStream out)
        throws CheckstylePluginException: RetrowException_exportConfigurationHandle()
        && args(config, file, in, out)
    {
        try
        {
            proceed(config, file, in, out);
        }
        finally
        {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
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

    Authenticator around(): RemoteConfigurationType_internalGetDefaultHandler(){
        Authenticator currentDefault = null;
        try
        {
            currentDefault = proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstyleLog.log(e);
        }
        return currentDefault;
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
            IOUtils.closeQuietly(in);
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
}
