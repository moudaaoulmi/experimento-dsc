/**
 * 
 */
package com.atlassw.tools.eclipse.checkstyle.config.gui;

import java.util.ArrayList;

import org.eclipse.osgi.util.NLS;
import org.osgi.service.prefs.BackingStoreException;
import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationWorkingSetEditor;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;

import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.ConfigProperty;
import com.atlassw.tools.eclipse.checkstyle.config.gui.widgets.IConfigPropertyWidget;


/**
 * @author julianasaraiva
 *
 */
public aspect GuiHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft: BackingStoreException: internalCreateButtonBarHandler();

    declare soft: CheckstylePluginException: internalInitializeHandler() ||
                    ModuleHandler();

    declare soft: CheckstylePluginException: internalSelectionChanged_2Handler() ||
                    widgetSelectedHandler()|| 
                    okPressedHandler() || 
                    setUniqueNameHandler() ||
                    internalSelectionChangedHandler() ||
                    internalCheckConfigHandler() ||
                    internalConfigureCheckConfigHandler() ||
                    exportCheckstyleCheckConfigHandler() ||
                    findPropertyItemsHandler();

    declare soft: Exception: createConfigurationEditorHandler();
    
    declare soft: BackingStoreException: internalWidgetSelectedHandler();

    declare soft: CheckstylePluginException: secInternalOkPressedHandler();
    
    // ---------------------------
    // Pointcut's
    // ---------------------------
    
    pointcut internalCreateButtonBarHandler():
        execution(* CheckConfigurationConfigureDialog.internalCreateButtonBar(..));

    pointcut internalInitializeHandler(): 
        execution(* CheckConfigurationConfigureDialog.internalInitialize(..));

    pointcut ModuleHandler():
        execution(* CheckConfigurationConfigureDialog.PageController.openModule(..)) ||
        execution(* CheckConfigurationConfigureDialog.PageController.internalNewModule(..)) ||
        execution(* CheckConfigurationConfigureDialog.internalOkPressed(..));

    pointcut internalSelectionChangedHandler(): 
        execution(* CheckConfigurationPropertiesDialog.internalSelectionChanged(..));

    pointcut widgetSelectedHandler():
        execution(* CheckConfigurationPropertiesDialog.getEditedWorkingCopyInternal(..));

    pointcut okPressedHandler(): execution(* CheckConfigurationPropertiesDialog.okPressed(..));

    pointcut createConfigurationEditorHandler():
        execution(* CheckConfigurationPropertiesDialog.createConfigurationEditor(..));

    pointcut setUniqueNameHandler():
        execution(* CheckConfigurationPropertiesDialog.internalSetUniqueName(..));
    
    pointcut internalSelectionChanged_2Handler():
        execution(private void CheckConfigurationWorkingSetEditor.PageController.internalSelectionChanged(..));

    pointcut internalCheckConfigHandler():
        execution(* CheckConfigurationWorkingSetEditor.internalAddCheckConfig(..)) ||
        execution(* CheckConfigurationWorkingSetEditor.internalCopyCheckConfig(..));

    pointcut internalConfigureCheckConfigHandler():
        execution (* CheckConfigurationWorkingSetEditor.internalConfigureCheckConfig(..));

    pointcut exportCheckstyleCheckConfigHandler():
        call(* CheckConfigurationFactory.exportConfiguration(..)) &&
        withincode(* CheckConfigurationWorkingSetEditor.exportCheckstyleCheckConfig(..));

    pointcut findPropertyItemsHandler():
        execution(* ResolvablePropertiesDialog.Controller.findPropertyItems(..));

    pointcut internalWidgetSelectedHandler():
        execution(* RuleConfigurationEditDialog.internalWidgetSelected(..));

    pointcut internalOkPressedHandler():
        execution(* RuleConfigurationEditDialog.internalOkPressed(..));

    pointcut secInternalOkPressedHandler():
        execution(* RuleConfigurationEditDialog.secInternalOkPressed(..));

    // ---------------------------
    // Advice's
    // ---------------------------

    void around(): ModuleHandler(){
        CheckConfigurationConfigureDialog cD = (CheckConfigurationConfigureDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(cD.getShell(), e, true);
        }
    }
    
    void around():createConfigurationEditorHandler(){
        CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (Exception ex)
        {
            CheckstyleLog.errorDialog(cD.getShell(), ex, true);
        }

    }

    void around(): internalCreateButtonBarHandler() || internalWidgetSelectedHandler() {
        try
        {
            proceed();
        }
        catch (BackingStoreException e1)
        {
            CheckstyleLog.log(e1);
        }
    }
    
    void around():internalOkPressedHandler(){
        try
        {
            proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstyleLog.log(e);
        }
    }
    
    void around(): internalSelectionChanged_2Handler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
        }
    }
 
    void around(): internalSelectionChangedHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            // NOOP
        }
    }

    void around(): widgetSelectedHandler(){
        CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException ex)
        {
            cD.setErrorMessage(ex.getLocalizedMessage());
        }
    }

    void around():okPressedHandler(){
        CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
            cD.setErrorMessage(e.getLocalizedMessage());
        }
    }

   

    void around(CheckConfigurationWorkingCopy config, String checkConfigName, String uniqueName,
            int counter):
            setUniqueNameHandler() && args(config, checkConfigName, uniqueName, counter){
        try
        {
            proceed(config, checkConfigName, uniqueName, counter);
        }
        catch (CheckstylePluginException e)
        {
            uniqueName = checkConfigName + " (" + counter + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            counter++;
        }
    }


    void around(): internalCheckConfigHandler(){
        CheckConfigurationWorkingSetEditor cW = (CheckConfigurationWorkingSetEditor) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException ex)
        {
            CheckstyleLog.errorDialog(cW.getShell(), ex, true);
        }
    }
    
    
    void around(): findPropertyItemsHandler(){
        ResolvablePropertiesDialog c = (ResolvablePropertiesDialog) thisJoinPoint.getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(c.getShell(), e, true);
        }
    }

    
    void around(CheckConfigurationWorkingCopy config): internalConfigureCheckConfigHandler() && 
        args(config){
        CheckConfigurationWorkingSetEditor cC = (CheckConfigurationWorkingSetEditor) thisJoinPoint
                .getThis();
        try
        {
            proceed(config);
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.warningDialog(cC.getShell(), NLS.bind(
                    ErrorMessages.errorCannotResolveCheckLocation, config.getLocation(), config
                            .getName()), e);
        }
    }


    
    void around(): exportCheckstyleCheckConfigHandler(){
        CheckConfigurationWorkingSetEditor cW = (CheckConfigurationWorkingSetEditor) thisJoinPoint
                .getThis();
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.errorDialog(cW.getShell(), ErrorMessages.msgErrorFailedExportConfig, e,
                    true);
        }
    }
    

    void around(IConfigPropertyWidget widget, ConfigProperty property):secInternalOkPressedHandler()
        && args (widget, property){
        RuleConfigurationEditDialog rC = (RuleConfigurationEditDialog) thisJoinPoint.getThis();
        try
        {
            proceed(widget, property);
        }
        catch (CheckstylePluginException e)
        {
            String message = NLS.bind(Messages.RuleConfigurationEditDialog_msgInvalidPropertyValue,
                    property.getMetaData().getName());
            rC.setErrorMessage(message);
            return;
        }
    }
}//GuiHandler
