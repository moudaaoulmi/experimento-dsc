
package com.atlassw.tools.eclipse.checkstyle.config.gui;

import java.util.ArrayList;
import br.upe.dsc.reusable.exception.*;
import org.eclipse.osgi.util.NLS;

import com.atlassw.tools.eclipse.checkstyle.config.gui.CheckConfigurationWorkingSetEditor;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.ErrorMessages;
import com.atlassw.tools.eclipse.checkstyle.config.gui.RuleConfigurationEditDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;

@ExceptionHandler
public privileged aspect GuiExceptionHandler extends EmptyBlockAbstractExceptionHandling
{

    // ---------------------------
    // Declare soft's
    // ---------------------------
    declare soft: CheckstylePluginException: CheckConfigurationConfigureDialog_initializeHandler() ||
                                             CheckConfigurationConfigureDialog_ModuleHandler();

    declare soft: CheckstylePluginException:
                    CheckConfigurationPropertiesDialog_widgetSelectedHandler()|| 
                    CheckConfigurationPropertiesDialog_okPressedHandler() || 
                    CheckConfigurationPropertiesDialog_createDialogAreaHandler() ||
                    CheckConfigurationWorkingSetEditor_internalCheckConfigHandler() ||
                    CheckConfigurationWorkingSetEditor_internalConfigureCheckConfigHandler() ||
                    CheckConfigurationFactory_exportCheckstyleCheckConfigHandler() ||
                    ResolvablePropertiesDialog_findPropertyItemsHandler();
                  

    declare soft: Exception: CheckConfigurationPropertiesDialog_createConfigurationEditorHandler();

    // ---------------------------
    // Pointcut's
    // ---------------------------
    public pointcut emptyBlockException(): CheckConfigurationPropertiesDialog_createDialogAreaHandler();
    
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
        execution(* CheckConfigurationPropertiesDialog.ISelectionChangedListenerImplementation.selectionChanged(..));
    
    pointcut CheckConfigurationPropertiesDialog_widgetSelectedHandler():
        execution(* CheckConfigurationPropertiesDialog.getEditedWorkingCopyInternal(..));

    pointcut CheckConfigurationPropertiesDialog_okPressedHandler(): 
        execution(* CheckConfigurationPropertiesDialog.okPressed(..));

    pointcut CheckConfigurationPropertiesDialog_createConfigurationEditorHandler():
        execution(* CheckConfigurationPropertiesDialog.createConfigurationEditor(..));

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

    // ---------------------------
    // Advice's
    // ---------------------------
    Object around(): CheckConfigurationConfigureDialog_initializeHandler(){
        CheckConfigurationConfigureDialog cCCD = (CheckConfigurationConfigureDialog) thisJoinPoint.getThis();
        Object result = cCCD.mModules;
        try
        {
            result = proceed();
        }
        catch (CheckstylePluginException e)
        {
            result = new ArrayList();
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

//    void around(): CheckConfigurationPropertiesDialog_createDialogAreaHandler(){
//        try
//        {
//            proceed();
//        }
//        catch (CheckstylePluginException e)
//        {
//            // NOOP
//        }
//    }

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
}