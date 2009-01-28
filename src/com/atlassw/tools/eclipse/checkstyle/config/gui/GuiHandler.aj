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
import org.eclipse.jface.dialogs.TitleAreaDialog;
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
                    findPropertyItemsHandler() ||
                    secInternalOkPressedHandler();

    declare soft: Exception: createConfigurationEditorHandler();
    
    declare soft: BackingStoreException: internalWidgetSelectedHandler();

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

    void around(): ModuleHandler() || findPropertyItemsHandler(){
        try {
            proceed();
        } catch (CheckstylePluginException e) {
            TitleAreaDialog object = (TitleAreaDialog) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(object.getShell(), e, true);
        }
    }
    
    void around(): internalCheckConfigHandler() ||  exportCheckstyleCheckConfigHandler() {
        try {
            proceed();
        } catch (CheckstylePluginException ex) {
            CheckConfigurationWorkingSetEditor cW = (CheckConfigurationWorkingSetEditor) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cW.getShell(), ex, true);
        }
    }
    
    void around():createConfigurationEditorHandler(){
        try {
            proceed();
        } catch (Exception ex) {
            CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(cD.getShell(), ex, true);
        }
    }

    void around(): internalCreateButtonBarHandler() || internalWidgetSelectedHandler() {
        try {
            proceed();
        } catch (BackingStoreException e1) {
            CheckstyleLog.log(e1);
        }
    }
    
    void around():internalOkPressedHandler(){
        try {
            proceed();
        } catch (IllegalArgumentException e) {
            CheckstyleLog.log(e);
        }
    }
    
    void around(): internalSelectionChanged_2Handler(){
        try {
            proceed();
        } catch (CheckstylePluginException e) {
            CheckstyleLog.log(e);
        }
    }
 
    void around(): internalSelectionChangedHandler(){
        try {
            proceed();
        } catch (CheckstylePluginException e)
        {
            // NOOP
        }
    }

    void around(): widgetSelectedHandler(){
        try {
            proceed();
        } catch (CheckstylePluginException ex) {
            CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint.getThis();
            cD.setErrorMessage(ex.getLocalizedMessage());
        }
    }

    void around():okPressedHandler(){
        try {
            proceed();
        } catch (CheckstylePluginException e) {
            CheckstyleLog.log(e);
            CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint.getThis();
            cD.setErrorMessage(e.getLocalizedMessage());
        }
    }


    void around(CheckConfigurationWorkingCopy config, String checkConfigName, String uniqueName,
            int counter):
            setUniqueNameHandler() && args(config, checkConfigName, uniqueName, counter){
        try {
            proceed(config, checkConfigName, uniqueName, counter);
        } catch (CheckstylePluginException e) {
            uniqueName = checkConfigName + " (" + counter + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            counter++;
        }
    }

    void around(CheckConfigurationWorkingCopy config): internalConfigureCheckConfigHandler() && 
        args(config) {
        try {
            proceed(config);
        } catch (CheckstylePluginException e) {
            CheckConfigurationWorkingSetEditor cC = (CheckConfigurationWorkingSetEditor) thisJoinPoint.getThis();
            CheckstyleLog.warningDialog(cC.getShell(), NLS.bind(
                    ErrorMessages.errorCannotResolveCheckLocation, config.getLocation(), config
                            .getName()), e);
        }
    }

    void around(IConfigPropertyWidget widget, ConfigProperty property):secInternalOkPressedHandler()
        && args (widget, property){
        try {
            proceed(widget, property);
        } catch (CheckstylePluginException e) {
            RuleConfigurationEditDialog rC = (RuleConfigurationEditDialog) thisJoinPoint.getThis();
            String message = NLS.bind(Messages.RuleConfigurationEditDialog_msgInvalidPropertyValue,
                    property.getMetaData().getName());
            rC.setErrorMessage(message);
            return;
        }
    }
    
}//GuiHandler
