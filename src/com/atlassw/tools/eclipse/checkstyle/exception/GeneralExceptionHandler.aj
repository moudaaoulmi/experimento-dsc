
package com.atlassw.tools.eclipse.checkstyle.exception;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.osgi.service.prefs.BackingStoreException;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.JavaModelException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ConfigurationTypes;
import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
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
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationTester;
import com.atlassw.tools.eclipse.checkstyle.builder.PackageNamesLoader;
import com.atlassw.tools.eclipse.checkstyle.util.CustomLibrariesClassLoader;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ExternalFileConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.InternalConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ProjectConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.migration.CheckConfigurationMigrator;
import java.io.InputStream;
import java.io.OutputStream;
import com.atlassw.tools.eclipse.checkstyle.builder.CheckstyleBuilder;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigurationReader;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.builder.ProjectClassLoader;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.PackageFilterEditor;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.NonSrcDirsFilter;
import com.atlassw.tools.eclipse.checkstyle.properties.FileSetEditDialog;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.CheckFileOnOpenPartListener;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.RemoteConfigurationType;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileMatchPattern;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.AbstractFilter;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.FileSet;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfiguration;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.ProjectConfigurationWorkingCopy;

public aspect GeneralExceptionHandler
{
    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft: Exception : ConfigurationType_internalStaticHandler() || 
                              saveFilters_internalHandler()||  
                              PluginFilters_internalHandler() ||
                              ConfigurationType_getResolvedConfigurationFileURLHandler() ||
                              ConfigurationType_getCheckstyleConfigurationHandler() ||
                              FileMatchPattern_internalSetMatchPatternHandler();

    declare soft: CoreException: auditor_runAuditHandle() ||
                                 ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
                                 checkstyleBuilder_buildProjectsHandleHandle() ||
                                 ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
                                 CheckFileOnOpenPartListener_partClosedHandler() ||
                                 CheckFileOnOpenPartListener_isFileAffectedHandler() ||
                                 NonSrcDirsFilter_getSourceDirPathsHandler() ||
                                 internalRunHandler() ||
                                 auditor_addErrorHandle() ||
                                 RemoteConfigurationType_storeCredentialsHandler();

    declare soft: CheckstylePluginException: internalSelectionChanged_2Handler() || 
                                             RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler() || 
                                             metadataFactory_refreshHandler() ||
                                             CheckFileOnOpenPartListener_isFileAffectedHandler();

    declare soft: BackingStoreException: internalCreateButtonBarHandler() || 
                                         internalWidgetSelectedHandler() || 
                                         PrefsInitializer_internalinitializeDefaultPreferencesHandler();

    declare soft: CheckstyleException: auditor_runAuditHandle();

    declare soft: IOException: auditor_runAuditHandle()||
                               packageNamesLoader_getPackageNameInteration1Handle() ||
                               buildHandler() ||
                               ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
                               ExternalFileConfiguration_internalEnsureFileExistsHandler() ||
                               checkConfigurationMigrator_ensureFileExistsHandler() ||
                               checkConfigurationMigrator_migrateHandler() ||
                               RetrowException_runHandle() ||
                               ProjectConfigurationFactory_internalLoadFromPersistenceHandler();;

    declare soft: SAXException : checkConfigurationMigrator_migrateHandler() ||
                                 RetrowException_runHandle() ||
                                 ProjectConfigurationFactory_internalLoadFromPersistenceHandler();

    declare soft: ParserConfigurationException : checkConfigurationMigrator_migrateHandler() ||
                                                 RetrowException_runHandle() ||
                                                 ProjectConfigurationFactory_internalLoadFromPersistenceHandler();

    declare soft: JavaModelException: NonSrcDirsFilter_getSourceDirPathsHandler() ||
                                      SourceFolderContentProvider_handleProjectHandler() || 
                                      SourceFolderContentProvider_handleContainerHandler() ||
                                      projectClassLoader_addToClassPathHandle();

    declare soft : CloneNotSupportedException : FileMatchPattern_cloneHandler() || FileMatchPattern_cloneFileSetHandler() || 
                   FileMatchPattern_cloneProjectHandler() || FileMatchPattern_cloneWorkingCopyHandler() ||
                   AbstractFilter_cloneHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
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

    pointcut buildHandler(): 
        execution(* CustomLibrariesClassLoader.get(..)) ;

    pointcut packageNamesLoader_getPackageNameInteration1Handle(): 
        execution (* PackageNamesLoader.getPackageNameInteration1(..)) ;

    pointcut auditor_runAuditHandle(): 
        execution (* Auditor.runAudit(..)) ;

    pointcut ConfigurationType_internalStaticHandler():
        execution(* ConfigurationTypes.internalStatic(..));

    /*
     * A principio (codigo original) nem precisava do tratador, dessa forma que
     * estava afetando o metodo todo, ele mudava o comportamento, pois tava
     * tratando (super.start(context)) o que na verdade é para relançar, como
     * está no codigo original. pointcut CheckstylePlugin_startHandle():
     * execution( CheckstylePlugin.start(..));
     */

    pointcut auditor_addErrorHandle(): execution (* Auditor.CheckstyleAuditListener.addError(..)) ;

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
        execution(* RuleConfigurationEditDialog.internalWidgetSelected(..));

    pointcut PrefsInitializer_internalinitializeDefaultPreferencesHandler() : 
        execution(* PrefsInitializer.internalinitializeDefaultPreferences(..));

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

    // ---------------------------
    // Advice's
    // ---------------------------
    Object around(): CheckFileOnOpenPartListener_partClosedHandler() ||
                     CheckFileOnOpenPartListener_isFileAffectedHandler() ||
                     NonSrcDirsFilter_getSourceDirPathsHandler() ||
                     internalRunHandler() ||
                     auditor_addErrorHandle() ||
                     RemoteConfigurationType_storeCredentialsHandler(){
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

    Object around() throws CheckstylePluginException: RetrowException_runHandle() ||
                                                       ProjectConfigurationFactory_internalLoadFromPersistenceHandler() ||
                                                       checkConfigurationMigrator_migrateHandler(){
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
        catch (ParserConfigurationException pe)
        {
            CheckstylePluginException.rethrow(pe);
        }
        catch (IOException ioe)
        {
            CheckstylePluginException.rethrow(ioe);
        }
        return result;
    }

    Object around() throws CheckstylePluginException: checkstyleBuilder_buildProjectsHandleHandle()||
                                                      auditor_runAuditHandle() ||
                                                      ProjectConfigurationEditor_internalEnsureFileExistsHandler() ||
                                                      ProjectConfigurationFactory_internalLoadFromPersistenceHandler(){
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

    IProjectConfiguration around(IProject project, IProjectConfiguration configuration, IFile file,
            InputStream inStream) throws CheckstylePluginException : 
                    ProjectConfigurationFactory_internalLoadFromPersistenceHandler() && 
                    args(project,configuration, file, inStream){

        IProjectConfiguration result = configuration;
        try
        {
            result = proceed(project, configuration, file, inStream);
        }
        finally
        {
            IOUtils.closeQuietly(inStream);
        }
        return result;
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
        catch (IOException ioe)
        {
            CheckstylePluginException.rethrow(ioe);
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }
    }

    Object around(): RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler() || 
                   internalSelectionChanged_2Handler() || 
                   metadataFactory_refreshHandler() ||
                   CheckFileOnOpenPartListener_isFileAffectedHandler(){
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
                   PrefsInitializer_internalinitializeDefaultPreferencesHandler() {
        try
        {
            proceed();
        }
        catch (BackingStoreException e1)
        {
            CheckstyleLog.log(e1);
        }
    }

    void around() throws CheckstylePluginException: auditor_runAuditHandle(){
        try
        {
            proceed();
        }
        catch (CheckstyleException e)
        {
            CheckstylePluginException.rethrow(e);
        }
    }

    // Esses com catch (Exception) só estão aqui sendo reusados pq os pointcuts
    // deles
    // passaram a não mais existir em outros aspectos.
    // se existissem, teria que acontecer uma precedencia entre os aspectos.
    void around(): ConfigurationType_internalStaticHandler() || 
                   /*CheckstylePlugin_startHandle() || */
                   saveFilters_internalHandler() ||
                   PluginFilters_internalHandler(){
        try
        {
            proceed();
        }
        catch (Exception e)
        {
            CheckstyleLog.log(e);
        }
    }

    Object around() throws CheckstylePluginException: ConfigurationType_getResolvedConfigurationFileURLHandler()||
                                                      ConfigurationType_getCheckstyleConfigurationHandler(){

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
                      AbstractFilter_cloneHandler(){
        try
        {
            return proceed();
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError(); // should never happen
        }
    }
}
