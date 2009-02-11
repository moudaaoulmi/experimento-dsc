package com.atlassw.tools.eclipse.checkstyle.exception;

import org.eclipse.core.runtime.CoreException;
import org.osgi.service.prefs.BackingStoreException;

import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ConfigurationTypes;
import com.atlassw.tools.eclipse.checkstyle.CheckstylePlugin;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.RemoteConfigurationType;
import com.atlassw.tools.eclipse.checkstyle.builder.Auditor;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationWorkingSetEditor;
import com.atlassw.tools.eclipse.checkstyle.config.meta.MetadataFactory;
import com.atlassw.tools.eclipse.checkstyle.config.savefilter.SaveFilters;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationConfigureDialog;
import com.atlassw.tools.eclipse.checkstyle.config.gui.RuleConfigurationEditDialog;
import com.atlassw.tools.eclipse.checkstyle.preferences.PrefsInitializer;
import com.atlassw.tools.eclipse.checkstyle.projectconfig.PluginFilters;

public aspect GeneralExceptionHandler{

    declare soft: Exception : ConfigurationType_internalStaticHandler() || saveFilters_internalHandler()||  PluginFilters_internalHandler();
    
    declare soft: CoreException: auditor_addErrorHandle() || RemoteConfigurationType_storeCredentialsHandler();
    
    declare soft: CheckstylePluginException: internalSelectionChanged_2Handler() || 
            RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler() || metadataFactory_refreshHandler();
    
    declare soft: BackingStoreException: internalCreateButtonBarHandler() || 
            internalWidgetSelectedHandler() || PrefsInitializer_internalinitializeDefaultPreferencesHandler();
    
    pointcut ConfigurationType_internalStaticHandler():
        execution(* ConfigurationTypes.internalStatic(..));
    
    pointcut CheckstylePlugin_startHandle(): execution(* CheckstylePlugin.start(..));
    
    pointcut auditor_addErrorHandle(): execution (* Auditor.CheckstyleAuditListener.addError(..)) ;
    
    pointcut RemoteConfigurationType_storeCredentialsHandler():
        execution(* RemoteConfigurationType.RemoteConfigAuthenticator.storeCredentials(..));
    
    pointcut internalSelectionChanged_2Handler():
        execution(* CheckConfigurationWorkingSetEditor.PageController.internalSelectionChanged(..));
    
    pointcut RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler(): 
        execution(* RemoteConfigurationType.secInternalGetBytesFromURLConnection(..));
    
    pointcut metadataFactory_refreshHandler() : call (* MetadataFactory.doInitialization(..)) && withincode(* MetadataFactory.refresh(..));
    
    pointcut saveFilters_internalHandler() : execution(* SaveFilters.internal(..));
    
    pointcut internalCreateButtonBarHandler():
        execution(* CheckConfigurationConfigureDialog.internalCreateButtonBar(..));
    
    pointcut internalWidgetSelectedHandler():
        execution(* RuleConfigurationEditDialog.internalWidgetSelected(..));
    
    pointcut PrefsInitializer_internalinitializeDefaultPreferencesHandler() : execution(* PrefsInitializer.internalinitializeDefaultPreferences(..));
    
    pointcut PluginFilters_internalHandler() : execution(* PluginFilters.internal(..));
    
    void around(): ConfigurationType_internalStaticHandler() || 
            CheckstylePlugin_startHandle() || saveFilters_internalHandler() ||
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
    
    void around(): auditor_addErrorHandle() || RemoteConfigurationType_storeCredentialsHandler() {
        try
        {
            proceed();
        }
        catch (CoreException e)
        {
            CheckstyleLog.log(e);
        }
    }
    
    void around(): RemoteConfigurationType_secInternalGetBytesFromURLConnectionHandler() || 
                internalSelectionChanged_2Handler() || metadataFactory_refreshHandler(){
        try {
            proceed();
        } catch (CheckstylePluginException e) {
            CheckstyleLog.log(e);
        }
    }
    
    void around(): internalCreateButtonBarHandler() || 
            internalWidgetSelectedHandler() || PrefsInitializer_internalinitializeDefaultPreferencesHandler() {
        try {
            proceed();
        } catch (BackingStoreException e1) {
            CheckstyleLog.log(e1);
        }
    }
}
