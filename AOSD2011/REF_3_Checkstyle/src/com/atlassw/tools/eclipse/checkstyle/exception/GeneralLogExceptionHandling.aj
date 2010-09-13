package com.atlassw.tools.eclipse.checkstyle.exception;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.team.core.TeamException;
import org.osgi.service.prefs.BackingStoreException;

import br.upe.dsc.reusable.exception.ILogObject;
import br.upe.dsc.reusable.exception.LogAbstractHandler;

import com.atlassw.tools.eclipse.checkstyle.builder.Auditor;
import com.atlassw.tools.eclipse.checkstyle.builder.ProjectClassLoader;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ConfigurationTypes;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.RemoteConfigurationType;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationConfigureDialog;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationWorkingSetEditor;
import com.atlassw.tools.eclipse.checkstyle.config.gui.RuleConfigurationEditDialog;
import com.atlassw.tools.eclipse.checkstyle.config.meta.MetadataFactory;
import com.atlassw.tools.eclipse.checkstyle.config.savefilter.SaveFilters;
import com.atlassw.tools.eclipse.checkstyle.preferences.PrefsInitializer;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.PluginFilters;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.CheckFileOnOpenPartListener;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.FilesInSyncFilter2;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.NonSrcDirsFilter;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.filters.PackageFilterEditor;
import com.atlassw.tools.eclipse.checkstyle.properties.FileSetEditDialog;
import com.atlassw.tools.eclipse.checkstyle.quickfixes.coding.StringLiteralEqualityQuickfix;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;


public privileged aspect GeneralLogExceptionHandling extends LogAbstractHandler {

    declare soft: Exception : ConfigurationType_internalStaticHandler() || 
    saveFilters_internalHandler()||  
    PluginFilters_internalHandler();

    declare soft: CoreException: 
    CheckFileOnOpenPartListener_partClosedHandler() ||
    CheckFileOnOpenPartListener_isFileAffectedHandler() ||
    NonSrcDirsFilter_getSourceDirPathsHandler() ||
    internalRunHandler() ||
    auditor_addErrorHandle() ||
    RemoteConfigurationType_storeCredentialsHandler() ||
    FileSetEditDialog_runHandler();

    declare soft: CheckstylePluginException: internalSelectionChanged_2Handler() || 
    RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler() || 
    metadataFactory_refreshHandler() ||
    CheckFileOnOpenPartListener_isFileAffectedHandler() ||
    CheckstyleLogMessage_refreshHandle();

    declare soft: BackingStoreException: internalCreateButtonBarHandler() || 
    internalWidgetSelectedHandler() || 
    PrefsInitializer_internalinitializeDefaultPreferencesHandler();

   

    declare soft: JavaModelException: NonSrcDirsFilter_getSourceDirPathsHandler() ||
    SourceFolderContentProvider_handleProjectHandler() || 
    SourceFolderContentProvider_handleContainerHandler() ||
    projectClassLoader_addToClassPathHandle();


    declare soft : InvocationTargetException : StringLiteralEqualityQuickfix_replaceNodeHandler();

    declare soft : IllegalAccessException : StringLiteralEqualityQuickfix_replaceNodeHandler() ;

    declare soft : NoSuchMethodException : StringLiteralEqualityQuickfix_replaceNodeHandler() ;

    declare soft : TeamException: FilesInSyncFilter2_acceptHandler();

    pointcut NonSrcDirsFilter_getSourceDirPathsHandler(): 
        execution(* NonSrcDirsFilter.getSourceDirPaths(..));
    
    pointcut SourceFolderContentProvider_handleProjectHandler(): 
        execution(* PackageFilterEditor.SourceFolderContentProvider.handleProject(..));

    pointcut SourceFolderContentProvider_handleContainerHandler(): 
        execution(* PackageFilterEditor.SourceFolderContentProvider.handleContainer(..));

    pointcut projectClassLoader_addToClassPathHandle(): 
        execution (* ProjectClassLoader.addToClassPath(..)) ;

    pointcut CheckFileOnOpenPartListener_partClosedHandler(): 
        execution(* CheckFileOnOpenPartListener.partClosed(..));
    
    pointcut CheckFileOnOpenPartListener_isFileAffectedHandler(): 
        execution(* CheckFileOnOpenPartListener.isFileAffected(..));
    
    pointcut internalRunHandler(): 
        call(* FileSetEditDialog.getFiles(..)) &&
        withincode(* FileSetEditDialog.internalRun(..));

    pointcut auditor_addErrorHandle(): 
        execution (* Auditor.CheckstyleAuditListener.addError(..)) ;
    
    pointcut RemoteConfigurationType_storeCredentialsHandler():
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.storeCredentials(..));

    pointcut FileSetEditDialog_runHandler(): 
        execution(* FileSetEditDialog.internalRun(..));
    
    pointcut RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler(): 
        execution(* RemoteConfigurationType.secInternalGetBytesFromURLConnection(..));
    pointcut internalSelectionChanged_2Handler():
        execution(* CheckConfigurationWorkingSetEditor.PageController.internalSelectionChanged(..));

    pointcut metadataFactory_refreshHandler() : 
        call (* MetadataFactory.doInitialization(..)) && withincode(* MetadataFactory.refresh(..));
   
//    pointcut CheckFileOnOpenPartListener_isFileAffectedHandler(): 
//        execution(* CheckFileOnOpenPartListener.isFileAffected(..));
    
    pointcut CheckstyleLogMessage_refreshHandle(): 
        execution (* CheckConfigurationFactory.refresh(..)) ;

    pointcut internalCreateButtonBarHandler():
        execution(* CheckConfigurationConfigureDialog.internalCreateButtonBar(..));
    pointcut internalWidgetSelectedHandler():
        (execution(* RuleConfigurationEditDialog.SelectionListenerImplementation.widgetSelected(..)) ) ||
        (execution(* RuleConfigurationEditDialog.internalFlush(..)));

    pointcut PrefsInitializer_internalinitializeDefaultPreferencesHandler() : 
        execution(* PrefsInitializer.initializeDefaultPreferences(..));

    pointcut FilesInSyncFilter2_acceptHandler(): 
        call(* FilesInSyncFilter2.internalAccent(..)) &&
        withincode(* FilesInSyncFilter2.accept(..));

    pointcut StringLiteralEqualityQuickfix_replaceNodeHandler():  
        execution(* StringLiteralEqualityQuickfix.ASTVisitorImplementation.replaceNode(..));
    pointcut ConfigurationType_internalStaticHandler():
        execution(* ConfigurationTypes.internalStatic(..));

    pointcut saveFilters_internalHandler() : 
        execution(* SaveFilters.internal(..));
    pointcut PluginFilters_internalHandler() :
        execution(* PluginFilters.internal(..));
    
    public pointcut checkedExceptionLog(): NonSrcDirsFilter_getSourceDirPathsHandler() ||
    SourceFolderContentProvider_handleProjectHandler() || 
    SourceFolderContentProvider_handleContainerHandler() ||
    projectClassLoader_addToClassPathHandle() ||
    CheckFileOnOpenPartListener_partClosedHandler() ||
    CheckFileOnOpenPartListener_isFileAffectedHandler() ||
   // NonSrcDirsFilter_getSourceDirPathsHandler() ||
    internalRunHandler() ||
    auditor_addErrorHandle() ||
    RemoteConfigurationType_storeCredentialsHandler() ||
    FileSetEditDialog_runHandler() ||
    RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler() || 
    internalSelectionChanged_2Handler() || 
    metadataFactory_refreshHandler() ||
    //CheckFileOnOpenPartListener_isFileAffectedHandler() || duplicado
    CheckstyleLogMessage_refreshHandle() ||
    internalCreateButtonBarHandler() || 
    internalWidgetSelectedHandler() || 
    PrefsInitializer_internalinitializeDefaultPreferencesHandler();
   
    
    public pointcut exceptionLog(): FilesInSyncFilter2_acceptHandler()
    || StringLiteralEqualityQuickfix_replaceNodeHandler()
    || ConfigurationType_internalStaticHandler()
    || saveFilters_internalHandler()
    || PluginFilters_internalHandler()
   ;
    
    public String getMessageText(int pointcutId){
        return null;
    }
    
    public ILogObject getLogObject(){
        return CheckstyleLog.getIntance();
    }
    
}
