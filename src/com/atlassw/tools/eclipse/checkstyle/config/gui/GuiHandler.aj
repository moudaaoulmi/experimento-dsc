
package com.atlassw.tools.eclipse.checkstyle.config.gui;

import java.util.ArrayList;
import org.eclipse.osgi.util.NLS;

import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationWorkingSetEditor;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.config.gui.RuleConfigurationEditDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ComboViewer;


public privileged aspect GuiHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft: CheckstylePluginException: CheckConfigurationConfigureDialog_initializeHandler() ||
                                             CheckConfigurationConfigureDialog_ModuleHandler();

    declare soft: CheckstylePluginException:
                    CheckConfigurationPropertiesDialog_widgetSelectedHandler()|| 
                    CheckConfigurationPropertiesDialog_okPressedHandler() || 
                    CheckConfigurationPropertiesDialog_setUniqueNameHandler() ||
                    CheckConfigurationPropertiesDialog_createDialogAreaHandler() ||
                    CheckConfigurationWorkingSetEditor_internalCheckConfigHandler() ||
                    CheckConfigurationWorkingSetEditor_internalConfigureCheckConfigHandler() ||
                    CheckConfigurationFactory_exportCheckstyleCheckConfigHandler() ||
                    ResolvablePropertiesDialog_findPropertyItemsHandler() ||
                    RuleConfigurationEditDialog_okPressedHandler();// ||

    // RuleConfigurationEditDialog_internalOkPressedHandler();

    declare soft: Exception: CheckConfigurationPropertiesDialog_createConfigurationEditorHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------

    pointcut CheckConfigurationConfigureDialog_initializeHandler(): 
        call(* CheckConfigurationWorkingCopy.getModules(..)) &&
        withincode(* CheckConfigurationConfigureDialog.initialize(..));

    pointcut CheckConfigurationConfigureDialog_ModuleHandler():
        execution(* CheckConfigurationConfigureDialog.PageController.openModule(..)) ||
      (  call(RuleConfigurationEditDialog.new(..)) &&
         withincode(* CheckConfigurationConfigureDialog.PageController.newModule(..)) ) ||
      (  call(* CheckConfigurationWorkingCopy.setModules(..)) &&
         withincode(* CheckConfigurationConfigureDialog.okPressed(..)) );

    pointcut CheckConfigurationPropertiesDialog_createDialogAreaHandler(): 
        call(* CheckConfigurationWorkingCopy.setName(..)) &&
        within(CheckConfigurationPropertiesDialog) &&
        within(ISelectionChangedListener+);

    pointcut CheckConfigurationPropertiesDialog_widgetSelectedHandler():
        execution(* CheckConfigurationPropertiesDialog.getEditedWorkingCopyInternal(..));

    pointcut CheckConfigurationPropertiesDialog_okPressedHandler(): 
        execution(* CheckConfigurationPropertiesDialog.okPressed(..));

    pointcut CheckConfigurationPropertiesDialog_createConfigurationEditorHandler():
        execution(* CheckConfigurationPropertiesDialog.createConfigurationEditor(..));

    pointcut CheckConfigurationPropertiesDialog_setUniqueNameHandler():
        execution(* CheckConfigurationPropertiesDialog.internalSetUniqueName(..));

    pointcut CheckConfigurationWorkingSetEditor_internalCheckConfigHandler():
        execution(* CheckConfigurationWorkingSetEditor.addCheckConfig(..)) ||
        execution(* CheckConfigurationWorkingSetEditor.copyCheckConfig(..));

    pointcut CheckConfigurationWorkingSetEditor_internalConfigureCheckConfigHandler():
        execution (* CheckConfigurationWorkingSetEditor.internalConfigureCheckConfig(..));

    pointcut CheckConfigurationFactory_exportCheckstyleCheckConfigHandler():
        call(* CheckConfigurationFactory.exportConfiguration(..)) &&
        withincode(* CheckConfigurationWorkingSetEditor.exportCheckstyleCheckConfig(..));

    pointcut ResolvablePropertiesDialog_findPropertyItemsHandler():
        execution(* ResolvablePropertiesDialog.Controller.findPropertyItems(..));

    pointcut RuleConfigurationEditDialog_okPressedHandler():
        call(* ComboViewer.getSelection(..)) &&
        withincode(* RuleConfigurationEditDialog.okPressed(..));

    // TODO VERIFICAR COM ROMULO E DEPOIS DOCUMENTAR
    // pointcut RuleConfigurationEditDialog_internalOkPressedHandler():
    // execution(* RuleConfigurationEditDialog.internalOkPressed(..));

    // ---------------------------
    // Advice's
    // ---------------------------
    Object around(): CheckConfigurationConfigureDialog_initializeHandler(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckConfigurationConfigureDialog cCCD = (CheckConfigurationConfigureDialog) thisJoinPoint
                    .getThis();
            cCCD.mModules = new ArrayList();
            CheckstyleLog.errorDialog(cCCD.getShell(), e, true);
        }
        return result;
    }

    Object around(): CheckConfigurationConfigureDialog_ModuleHandler() || 
                     ResolvablePropertiesDialog_findPropertyItemsHandler(){
        Object result = null;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            TitleAreaDialog object = (TitleAreaDialog) thisJoinPoint.getThis();
            CheckstyleLog.errorDialog(object.getShell(), e, true);
        }
        return result;
    }

    void around(): CheckConfigurationWorkingSetEditor_internalCheckConfigHandler() || 
                   CheckConfigurationFactory_exportCheckstyleCheckConfigHandler() {
        try
        {
            proceed();
        }
        catch (CheckstylePluginException ex)
        {
            CheckConfigurationWorkingSetEditor cW = (CheckConfigurationWorkingSetEditor) thisJoinPoint
                    .getThis();
            CheckstyleLog.errorDialog(cW.getShell(), ex, true);
        }
    }

    void around(): CheckConfigurationPropertiesDialog_createConfigurationEditorHandler(){
        try
        {
            proceed();
        }
        catch (Exception ex)
        {
            CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                    .getThis();
            CheckstyleLog.errorDialog(cD.getShell(), ex, true);
        }
    }

    IStructuredSelection around(): RuleConfigurationEditDialog_okPressedHandler(){
        IStructuredSelection result = null;
        try
        {
            result = proceed();
        }
        catch (IllegalArgumentException e)
        {
            CheckstyleLog.log(e);
        }
        return result;
    }

    void around(): CheckConfigurationPropertiesDialog_createDialogAreaHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            // NOOP
        }
    }

    void around(): CheckConfigurationPropertiesDialog_widgetSelectedHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException ex)
        {
            CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                    .getThis();
            cD.setErrorMessage(ex.getLocalizedMessage());
        }
    }

    void around(): CheckConfigurationPropertiesDialog_okPressedHandler(){
        try
        {
            proceed();
        }
        catch (CheckstylePluginException e)
        {
            CheckstyleLog.log(e);
            CheckConfigurationPropertiesDialog cD = (CheckConfigurationPropertiesDialog) thisJoinPoint
                    .getThis();
            cD.setErrorMessage(e.getLocalizedMessage());
        }
    }

    void around(CheckConfigurationWorkingCopy config, String checkConfigName, String uniqueName,
            int counter):
                CheckConfigurationPropertiesDialog_setUniqueNameHandler() && 
                args(config, checkConfigName, uniqueName, counter){
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

    void around(CheckConfigurationWorkingCopy config): 
        CheckConfigurationWorkingSetEditor_internalConfigureCheckConfigHandler() && 
        args(config) {
        try
        {
            proceed(config);
        }
        catch (CheckstylePluginException e)
        {
            CheckConfigurationWorkingSetEditor cC = (CheckConfigurationWorkingSetEditor) thisJoinPoint
                    .getThis();
            CheckstyleLog.warningDialog(cC.getShell(), NLS.bind(
                    ErrorMessages.errorCannotResolveCheckLocation, config.getLocation(), config
                            .getName()), e);
        }
    }

    // void around(IConfigPropertyWidget widget, ConfigProperty property):
    // RuleConfigurationEditDialog_internalOkPressedHandler()
    // && args (widget, property){
    // try
    // {
    // proceed(widget, property);
    // }
    // catch (CheckstylePluginException e)
    // {
    // RuleConfigurationEditDialog rC = (RuleConfigurationEditDialog)
    // thisJoinPoint.getThis();
    // String message =
    // NLS.bind(Messages.RuleConfigurationEditDialog_msgInvalidPropertyValue,
    // property.getMetaData().getName());
    // rC.setErrorMessage(message);
    // //return;
    // }
    // }

}
