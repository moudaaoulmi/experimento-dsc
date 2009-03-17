package com.atlassw.tools.eclipse.checkstyle.config.gui;

import java.util.ArrayList;

import org.eclipse.osgi.util.NLS;
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
import com.atlassw.tools.eclipse.checkstyle.config.gui.RuleConfigurationEditDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;

public privileged aspect GuiHandler
{

    // ---------------------------
    // Declare soft's
    // ---------------------------

    declare soft: CheckstylePluginException: CheckConfigurationConfigureDialog_initializeHandler() ||
                                             CheckConfigurationConfigureDialog_ModuleHandler();

    declare soft: CheckstylePluginException:
        CheckConfigurationPropertiesDialog_widgetSelectedHandler()|| 
                    okPressedHandler() || 
                    setUniqueNameHandler() ||
                    CheckConfigurationPropertiesDialog_createDialogAreaHandler() ||
                    internalCheckConfigHandler() ||
                    internalConfigureCheckConfigHandler() ||
                    exportCheckstyleCheckConfigHandler() ||
                    findPropertyItemsHandler() ||
                    secInternalOkPressedHandler();

    declare soft: Exception: createConfigurationEditorHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------


    pointcut CheckConfigurationConfigureDialog_initializeHandler(): 
        call(* CheckConfigurationWorkingCopy.getModules(..)) &&
        withincode(* CheckConfigurationConfigureDialog.initialize(..));
    
    pointcut CheckConfigurationConfigureDialog_ModuleHandler():
        execution(* CheckConfigurationConfigureDialog.PageController.openModule(..)) 
      || (  call(RuleConfigurationEditDialog.new(..)) &&
            withincode(* CheckConfigurationConfigureDialog.PageController.newModule(..)) )
      || (  call(* CheckConfigurationWorkingCopy.setModules(..)) &&
            withincode(* CheckConfigurationConfigureDialog.okPressed(..))
          );

    pointcut CheckConfigurationPropertiesDialog_createDialogAreaHandler(): 
        call(* CheckConfigurationWorkingCopy.setName(..)) &&
        within(CheckConfigurationPropertiesDialog) &&
        within(ISelectionChangedListener+);

    pointcut CheckConfigurationPropertiesDialog_widgetSelectedHandler():
        execution(* CheckConfigurationPropertiesDialog.getEditedWorkingCopyInternal(..));
  //ver a partir daqui!!!!!!!!!!!
    pointcut okPressedHandler(): 
        execution(* CheckConfigurationPropertiesDialog.okPressed(..));

    pointcut createConfigurationEditorHandler():
        execution(* CheckConfigurationPropertiesDialog.createConfigurationEditor(..));

    pointcut setUniqueNameHandler():
        execution(* CheckConfigurationPropertiesDialog.internalSetUniqueName(..));

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

    pointcut internalOkPressedHandler():
        execution(* RuleConfigurationEditDialog.internalOkPressed(..));

    pointcut secInternalOkPressedHandler():
        execution(* RuleConfigurationEditDialog.secInternalOkPressed(..));
    

    
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
                   findPropertyItemsHandler(){
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

    void around(): internalCheckConfigHandler() ||  exportCheckstyleCheckConfigHandler() {
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

    void around():createConfigurationEditorHandler(){
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

    void around():okPressedHandler(){
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

    void around(CheckConfigurationWorkingCopy config): internalConfigureCheckConfigHandler() && 
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

    void around(IConfigPropertyWidget widget, ConfigProperty property):secInternalOkPressedHandler()
        && args (widget, property){
        try
        {
            proceed(widget, property);
        }
        catch (CheckstylePluginException e)
        {
            RuleConfigurationEditDialog rC = (RuleConfigurationEditDialog) thisJoinPoint.getThis();
            String message = NLS.bind(Messages.RuleConfigurationEditDialog_msgInvalidPropertyValue,
                    property.getMetaData().getName());
            rC.setErrorMessage(message);
            return;
        }
    }

}
